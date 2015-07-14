package com.v3ld1n.items.ratchet;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.v3ld1n.PlayerData;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.EntityUtil;
import com.v3ld1n.util.LocationUtil;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.PlayerAnimation;
import com.v3ld1n.util.ProjectileBuilder;
import com.v3ld1n.util.RepeatableRunnable;

public class RatchetBow extends V3LD1NItem {
    private final Color fireworkColor = Color.ORANGE;

    public RatchetBow() {
        super("ratchets-bow");
    }

    // Launches projectiles
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack itemInHand = player.getItemInHand();

        if (!this.equalsItem(itemInHand) && isRightClick(event.getAction())) {
            return;
        }

        RatchetBowType fireball = RatchetBowType.FIREBALL;
        PlayerData setting = PlayerData.RATCHETS_BOW;
        RatchetBowType type;
        try {
            type = RatchetBowType.valueOf(setting.getString(uuid));
        } catch (Exception e) {
            type = fireball;
        }

        Class<? extends Projectile> projectile = type.getProjectile();
        if (projectile == Arrow.class) {
            return;
        }
        event.setCancelled(true);

        ProjectileBuilder builder = new ProjectileBuilder(projectile)
            .setLaunchSound(this.getSoundSetting(type.getSound()))
            .setSpeed(1.5);
        if (type == fireball) {
            builder.setLaunchParticle(this.getParticleSetting("launch-particle"));
        }

        Projectile launched = builder.launch(player);
        if (launched.getType() == EntityType.WITHER_SKULL && type == RatchetBowType.BLUE_WITHER_SKULL) {
            ((WitherSkull) launched).setCharged(true);
        }
    }

    // Arrows
    @EventHandler
    public void onShoot(final EntityShootBowEvent event) {
        if (!(event.getProjectile() instanceof Projectile)) {
            return;
        }
        Projectile projectile = (Projectile) event.getProjectile();
        if (!projectileIsValid(projectile, EntityType.ARROW)) {
            return;
        }

        Player player = (Player) event.getEntity();
        UUID uuid = player.getUniqueId();

        PlayerData setting = PlayerData.RATCHETS_BOW;
        RatchetBowType type;
        try {
            type = RatchetBowType.valueOf(setting.getString(uuid));
        } catch (Exception e) {
            return;
        }

        if (type == RatchetBowType.TRIPLE_ARROWS) {
            double direction = this.getDoubleSetting("triple-arrows-direction");
            EntityUtil.randomDirection(projectile, direction);
            for (int i = 0; i < 2; i++) {
                Projectile arrow = new ProjectileBuilder(Arrow.class)
                    .setRandomDirection(direction)
                    .setSpeed(event.getForce() * 4)
                    .launch(player);
                    ItemMeta meta = player.getItemInHand().getItemMeta();
                    if (meta.hasEnchant(Enchantment.ARROW_FIRE)) {
                        EntityUtil.infiniteFire(arrow);
                    }
            }
        } else if (type == RatchetBowType.FIREWORK_ARROW) {
            EntityUtil.infiniteFire(event.getProjectile());
        }
    }

    // Sets the damage from projectile jumping
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Projectile)) {
            return;
        }
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        Projectile projectile = (Projectile) event.getDamager();
        if (!projectileIsValid(projectile, EntityType.FIREBALL, EntityType.WITHER_SKULL)) {
            return;
        }
        Player damagedPlayer = (Player) event.getEntity();
        Player shooter = (Player) projectile.getShooter();
        boolean damagedPlayerIsShooter = damagedPlayer.getUniqueId().equals(shooter.getUniqueId());
        if (damagedPlayerIsShooter) {
            event.setDamage(this.getDoubleSetting("fireball-jump-damage"));
        }
    }

    // Projectile hit
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectileIsValid(projectile, EntityType.FIREBALL)) {
            this.displayParticles(projectile.getLocation());
        }
        projectileJump(projectile);
        teleport(projectile);
        fireworkArrows(projectile);
    }

    /**
     * Makes entities near the projectile jump
     * @param projectile the projectile
     */
    private void projectileJump(Projectile projectile) {
        if (!projectileIsValid(projectile, EntityType.FIREBALL, EntityType.WITHER_SKULL)) {
            return;
        }
        Player shooter = (Player) projectile.getShooter();
        Fireball fireball = (Fireball) projectile;
        double radius = this.getDoubleSetting("fireball-jump-radius");
        for (Entity entity : fireball.getNearbyEntities(radius, radius, radius)) {
            if (!(entity instanceof LivingEntity) || entity.getType() == EntityType.ARMOR_STAND) {
                continue;
            }
            EntityUtil.projectileJump((LivingEntity) entity, fireball);
            if (entity.equals(shooter)) {
                Particle.displayList(entity.getLocation(), this.getStringListSetting("jump-particles"));
            }
        }
    }

    /**
     * Teleports players to a secret area when hitting a location with a fireball
     * @param projectile the projectile
     */
    private void teleport(Projectile projectile) {
        if (!projectileIsValid(projectile, EntityType.FIREBALL)) {
            return;
        }
        final Player shooter = (Player) projectile.getShooter();
        final Location location = projectile.getLocation();
        Location hitLocationMin = this.getLocationSetting("teleport-hit-location-min");
        Location hitLocationMax = this.getLocationSetting("teleport-hit-location-max");
        if (!LocationUtil.isInArea(location, hitLocationMin, hitLocationMax)) {
            return;
        }
        displayTeleportParticles(shooter.getLocation());
        RepeatableRunnable effects = new RepeatableRunnable(Bukkit.getScheduler(), V3LD1N.getPlugin(), 6, 4, 2) {
            @Override
            public void onRun() {
                Particle.displayList(location, getStringListSetting("teleport-hit-particles"));
                if (getBooleanSetting("teleport-lightning")) {
                    location.getWorld().strikeLightning(location);
                }
            }
        };
        effects.run();
        if (this.getBooleanSetting("teleport-lightning")) {
            Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable(){
                @Override
                public void run() {
                    location.getWorld().strikeLightning(location);
                }
            }, 24);
        }
        final Location teleportLocation = this.getLocationSetting("teleport-location");
        Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
            @Override
            public void run() {
                shooter.teleport(teleportLocation);
                PlayerAnimation.BED_LEAVE.playTo(shooter);
                displayTeleportParticles(teleportLocation);
            }
        }, this.getIntSetting("teleport-delay"));
    }

    /**
     * Displays a circle particle effect when teleporting
     * @param location the location to display the particles at
     */
    private void displayTeleportParticles(final Location location) {
        RepeatableRunnable particleTask = new RepeatableRunnable(Bukkit.getScheduler(), V3LD1N.getPlugin(), 2, 3, 10) {
            @Override
            public void onRun() {
                location.getWorld().playEffect(location, Effect.ENDER_SIGNAL, 0);
                location.setY(location.getY() + 0.2);
            }
        };
        particleTask.run();
    }

    /**
     * Makes the projectile explode and create a firework effect
     * @param projectile the projectile
     */
    private void fireworkArrows(Projectile projectile) {
        if (!projectileIsValid(projectile, EntityType.ARROW)) {
            return;
        }
        Player shooter = (Player) projectile.getShooter();
        if (PlayerData.RATCHETS_BOW.getString(shooter.getUniqueId()) == null) {
            return;
        }
        UUID uuid = shooter.getUniqueId();
        String setting = PlayerData.RATCHETS_BOW.getString(uuid);
        RatchetBowType type = RatchetBowType.valueOf(setting);
        if (type != RatchetBowType.FIREWORK_ARROW) {
            return;
        }
        Type fireworkType;
        try {
            fireworkType = Type.valueOf(PlayerData.FIREWORK_ARROWS.getString(uuid));
        } catch (Exception e) {
            fireworkType = Type.BALL;
        }
        Color fadeColor = Color.fromRGB(216, 205, 17);
        FireworkEffect effect = FireworkEffect.builder()
                .with(fireworkType)
                .withColor(fireworkColor)
                .withFade(fadeColor)
                .withFlicker()
                .withTrail()
                .build();
        EntityUtil.detonateFireworkProjectile(projectile, effect, projectile.getLocation());
    }
}
package com.v3ld1n.items.ratchet;

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
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionData;

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
        if (!entityIsHoldingItem(player)) return;
        if (!isRightClick(event.getAction())) return;

        RatchetBowType type = getBowType(player);
        Class<? extends Projectile> projectile = type.getProjectile();
        if (projectile == Arrow.class) return;

        // Launches the projectile
        event.setCancelled(true);
        ProjectileBuilder builder = new ProjectileBuilder(projectile)
            .setLaunchSounds(settings.getSounds(type.getSound()))
            .setSpeed(1.5);
        if (type == RatchetBowType.FIREBALL) {
            builder.setLaunchParticles(settings.getParticles("launch-particles"));
        }

        Projectile launched = builder.launch(player);
        // Sets wither skull to charged
        if (launched.getType() == EntityType.WITHER_SKULL && type == RatchetBowType.BLUE_WITHER_SKULL) {
            ((WitherSkull) launched).setCharged(true);
        }
    }

    // Arrows
    @EventHandler
    public void onShoot(final EntityShootBowEvent event) {
        if (!(event.getProjectile() instanceof Projectile)) return;
        Projectile projectile = (Projectile) event.getProjectile();
        if (!projectileIsValid(projectile, EntityType.ARROW, EntityType.SPECTRAL_ARROW, EntityType.TIPPED_ARROW)) return;

        Player player = (Player) event.getEntity();

        // Gets the player's projectile setting
        PlayerData setting = PlayerData.RATCHETS_BOW;
        RatchetBowType type;
        try {
            type = RatchetBowType.valueOf(setting.getString(player));
        } catch (Exception e) {
            return;
        }

        // Arrow types
        if (type == RatchetBowType.TRIPLE_ARROWS) {
            double direction = settings.getDouble("triple-arrows-direction");
            EntityUtil.randomDirection(projectile, direction);
            for (int i = 0; i < 2; i++) {
                Projectile arrow = new ProjectileBuilder(projectile.getClass())
                    .setRandomDirection(direction)
                    .setSpeed(event.getForce() * 4)
                    .launch(player);
                ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
                if (meta.hasEnchant(Enchantment.ARROW_FIRE)) {
                    EntityUtil.infiniteFire(arrow);
                }
                if (projectile.getType() == EntityType.TIPPED_ARROW) {
                	TippedArrow tippedArrow = (TippedArrow) projectile;
                	PotionData potion = tippedArrow.getBasePotionData();
                	((TippedArrow) arrow).setBasePotionData(potion);
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
            event.setDamage(settings.getDouble("fireball-jump-damage"));
        }
    }

    // Projectile hit / fireball particles
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectileIsValid(projectile, EntityType.FIREBALL)) {
            displayParticles(projectile.getLocation());
        }
        projectileJump(projectile);
        teleport(projectile);
        fireworkArrows(projectile);
    }

    // Gets the player's projectile setting, or uses a fireball if it is not set
    private RatchetBowType getBowType(Player player) {
        RatchetBowType fireball = RatchetBowType.FIREBALL;
        PlayerData setting = PlayerData.RATCHETS_BOW;
        RatchetBowType type;
        try {
            type = RatchetBowType.valueOf(setting.getString(player));
        } catch (Exception e) {
            type = fireball;
        }
        return type;
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
        double radius = settings.getDouble("fireball-jump-radius");
        for (Entity entity : fireball.getNearbyEntities(radius, radius, radius)) {
            if (!(entity instanceof LivingEntity) || entity.getType() == EntityType.ARMOR_STAND) {
                continue;
            }
            EntityUtil.projectileJump((LivingEntity) entity, fireball);
            if (entity.equals(shooter)) {
                Particle.displayList(settings.getParticles("jump-particles"), entity.getLocation());
            }
        }
    }

    /**
     * Teleports players to a secret area when hitting a location with a fireball
     * @param projectile the projectile
     */
    private void teleport(Projectile projectile) {
        if (!projectileIsValid(projectile, EntityType.FIREBALL)) return;
        final Player shooter = (Player) projectile.getShooter();
        final Location location = projectile.getLocation();
        Location hitLocationMin = settings.getLocation("teleport-hit-location-min");
        Location hitLocationMax = settings.getLocation("teleport-hit-location-max");
        if (!LocationUtil.isInArea(location, hitLocationMin, hitLocationMax)) return;

        RepeatableRunnable effects = new RepeatableRunnable() {
            @Override
            public void onRun() {
                Particle.displayList(settings.getParticles("teleport-hit-particles"), location);
                if (settings.getBoolean("teleport-lightning")) {
                    location.getWorld().strikeLightning(location);
                }
            }
        };
        effects.start(6, 4, 2);

        if (settings.getBoolean("teleport-lightning")) {
            Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable(){
                @Override
                public void run() {
                    location.getWorld().strikeLightning(location);
                }
            }, 24);
        }

        final Location teleportLocation = settings.getLocation("teleport-location");
        Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
            @Override
            public void run() {
                shooter.teleport(teleportLocation);
                PlayerAnimation.BED_LEAVE.playToPlayer(shooter);
                displayTeleportParticles(teleportLocation);
            }
        }, settings.getInt("teleport-delay"));
    }

    /**
     * Displays a circle particle effect when teleporting
     * @param location the location to display the particles at
     */
    private void displayTeleportParticles(final Location location) {
        RepeatableRunnable particleTask = new RepeatableRunnable() {
            @Override
            public void onRun() {
                location.getWorld().playEffect(location, Effect.ENDER_SIGNAL, 0);
                location.setY(location.getY() + 0.2);
            }
        };
        particleTask.start(2, 3, 10);
    }

    /**
     * Makes the projectile explode and create a firework effect
     * @param projectile the projectile
     */
    private void fireworkArrows(Projectile projectile) {
        if (!projectileIsValid(projectile, EntityType.ARROW)) return;
        Player shooter = (Player) projectile.getShooter();
        if (PlayerData.RATCHETS_BOW.getString(shooter) == null) return;

        String setting = PlayerData.RATCHETS_BOW.getString(shooter);
        RatchetBowType bowType = RatchetBowType.valueOf(setting);
        if (bowType != RatchetBowType.FIREWORK_ARROW) return;

        Type fireworkType;
        try {
            fireworkType = Type.valueOf(PlayerData.FIREWORK_ARROWS.getString(shooter));
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
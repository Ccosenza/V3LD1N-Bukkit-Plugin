package com.v3ld1n.items.ratchet;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
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
    private final Color fwColor = Color.ORANGE;

    public RatchetBow() {
        super("ratchets-bow");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        ItemStack hand = p.getItemInHand();
        if (!(this.equalsItem(hand) && useActions.contains(event.getAction()))) {
            return;
        }

        RatchetBowType type;
        RatchetBowType fb = RatchetBowType.FIREBALL;
        PlayerData cfg = PlayerData.RATCHETS_BOW;
        type = cfg.getString(uuid) == null ? fb : RatchetBowType.valueOf(cfg.getString(uuid));
        if (type.getProjectile() == Arrow.class) {
            return;
        }
        event.setCancelled(true);

        ProjectileBuilder b = new ProjectileBuilder(type.getProjectile())
            .setLaunchSound(this.getSoundSetting(type.getSound()))
            .setSpeed(1.5);
        if (type == fb) {
            b.setLaunchParticle(this.getParticleSetting("launch-particle"));
        }

        Projectile launched = b.launch(p);
        if (launched.getType() == EntityType.WITHER_SKULL && type == RatchetBowType.BLUE_WITHER_SKULL) {
            ((WitherSkull) launched).setCharged(true);
        }
    }

    @EventHandler
    public void onShoot(final EntityShootBowEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }
        final Player p = (Player) event.getEntity();
        if (!this.equalsItem(p.getItemInHand())) {
            return;
        }
        if (PlayerData.RATCHETS_BOW.getString(p.getUniqueId()) == null) {
            return;
        }
        String setting = PlayerData.RATCHETS_BOW.getString(p.getUniqueId()).toUpperCase();
        if (RatchetBowType.valueOf(setting) == RatchetBowType.TRIPLE_ARROWS) {
            Entity pr = event.getProjectile();
            double direction = this.getDoubleSetting("triple-arrows-direction");
            EntityUtil.randomDirection(pr, direction);
            for (int i = 0; i < 2; i++) {
                new ProjectileBuilder(Arrow.class)
                    .setRandomDirection(direction)
                    .setSpeed(event.getForce() * 4)
                    .launch(p);
            }
        } else if (RatchetBowType.valueOf(setting) == RatchetBowType.FIREWORK_ARROW) {
            event.getProjectile().setFireTicks(Integer.MAX_VALUE);
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
        Projectile pr = (Projectile) event.getDamager();
        if (!isValid(pr, EntityType.FIREBALL, EntityType.WITHER_SKULL)) {
            return;
        }
        Player p = (Player) event.getEntity();
        Player shooter = (Player) pr.getShooter();
        boolean pIsShooter = p.getUniqueId().equals(shooter.getUniqueId());
        if (pIsShooter) {
            event.setDamage(this.getDoubleSetting("fireball-jump-damage"));
        }
    }

    // Projectile hit
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile pr = event.getEntity();
        if (isValid(pr, EntityType.FIREBALL)) {
            this.displayParticles(pr.getLocation());
        }
        projectileJump(pr);
        teleport(pr);
        fireworkArrows(pr);
    }

    /**
     * Makes entities near the projectile jump
     * @param pr the projectile
     */
    private void projectileJump(Projectile pr) {
        if (!isValid(pr, EntityType.FIREBALL, EntityType.WITHER_SKULL)) {
            return;
        }
        Player shooter = (Player) pr.getShooter();
        Fireball fb = (Fireball) pr;
        double radius = this.getDoubleSetting("fireball-jump-radius");
        for (Entity e : fb.getNearbyEntities(radius, radius, radius)) {
            if (!(e instanceof LivingEntity) || e.getType() == EntityType.ARMOR_STAND) {
                continue;
            }
            EntityUtil.projectileJump((LivingEntity) e, fb);
            if (e.equals(shooter)) {
                Particle.displayList(e.getLocation(), this.getStringListSetting("jump-particles"));
            }
        }
    }

    /**
     * Teleports players to a secret area when hitting a location with a fireball
     * @param pr the projectile
     */
    private void teleport(Projectile pr) {
        if (!isValid(pr, EntityType.FIREBALL)) {
            return;
        }
        final Player shooter = (Player) pr.getShooter();
        final Location location = pr.getLocation();
        Location hitLocMin = this.getLocationSetting("teleport-hit-location-min");
        Location hitLocMax = this.getLocationSetting("teleport-hit-location-max");
        if (!LocationUtil.isInArea(location, hitLocMin, hitLocMax)) {
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
        final Location tele = this.getLocationSetting("teleport-location");
        Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
            @Override
            public void run() {
                shooter.teleport(tele);
                PlayerAnimation.BED_LEAVE.playTo(shooter);
                displayTeleportParticles(tele);
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
     * @param pr the projectile
     */
    private void fireworkArrows(Projectile pr) {
        if (!isValid(pr, EntityType.ARROW)) {
            return;
        }
        Player shooter = (Player) pr.getShooter();
        if (PlayerData.RATCHETS_BOW.getString(shooter.getUniqueId()) == null) {
            return;
        }
        UUID uuid = shooter.getUniqueId();
        String setting = PlayerData.RATCHETS_BOW.getString(uuid).toUpperCase();
        RatchetBowType type = RatchetBowType.valueOf(setting);
        if (type != RatchetBowType.FIREWORK_ARROW) {
            return;
        }
        Type fwType = Type.BALL;
        if (PlayerData.FIREWORK_ARROWS.getString(uuid) != null) {
            fwType = Type.valueOf(PlayerData.FIREWORK_ARROWS.getString(uuid));
        }
        Color fade = Color.fromRGB(216, 205, 17);
        FireworkEffect effect = FireworkEffect.builder()
                .with(fwType)
                .withColor(fwColor)
                .withFade(fade)
                .withFlicker()
                .withTrail()
                .build();
        EntityUtil.detonateFireworkProjectile(pr, effect, pr.getLocation());
    }

    /**
     * Checks if the projectile is a specific type, its shooter is a player, and the shooter is holding Ratchet's Bow
     * @param pr the projectile
     * @param types the list of valid types
     * @return whether the projectile is valid
     */
    private boolean isValid(Projectile pr, EntityType... types) {
        List<EntityType> typeList = Arrays.asList(types);
        boolean typeValid = typeList.contains(pr.getType());

        boolean shooterIsPlayer = pr.getShooter() != null && pr.getShooter() instanceof Player;

        Player shooter = (Player) pr.getShooter();
        boolean holdingItem = this.equalsItem(shooter.getItemInHand());

        return typeValid && shooterIsPlayer && holdingItem;
    }
}
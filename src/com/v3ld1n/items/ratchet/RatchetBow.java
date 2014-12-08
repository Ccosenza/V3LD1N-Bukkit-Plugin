package com.v3ld1n.items.ratchet;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.v3ld1n.PlayerData;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.EntityUtil;
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
        if (this.equalsItem(p.getItemInHand()) && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (PlayerData.RATCHETS_BOW.getString(p.getUniqueId()) != null) {
                String projectile = PlayerData.RATCHETS_BOW.getString(p.getUniqueId());
                if (!projectile.equalsIgnoreCase("arrow") && !projectile.equalsIgnoreCase("triplearrows") && !projectile.equalsIgnoreCase("fireworkarrow")) {
                    event.setCancelled(true);
                    switch (projectile.toLowerCase()) {
                        case "snowball":
                            new ProjectileBuilder()
                                .withType(Snowball.class)
                                .withLaunchSound(this.getStringSetting("snowball-sound"))
                                .launch(p, 1.5);
                            break;
                        case "enderpearl":
                            new ProjectileBuilder()
                            .withType(EnderPearl.class)
                            .withLaunchSound(this.getStringSetting("ender-pearl-sound"))
                            .withRandomDirection(this.getDoubleSetting("ender-pearl-direction"))
                            .launch(p, 1.5);
                            break;
                        case "egg":
                            new ProjectileBuilder()
                            .withType(Egg.class)
                            .withLaunchSound(this.getStringSetting("egg-sound"))
                            .launch(p, 1.5);
                            break;
                        case "witherskull":
                            new ProjectileBuilder()
                            .withType(WitherSkull.class)
                            .withLaunchSound(this.getStringSetting("wither-skull-sound"))
                            .launch(p, 1.5);
                            break;
                        case "bluewitherskull":
                            WitherSkull blueSkull = (WitherSkull) new ProjectileBuilder()
                                .withType(WitherSkull.class)
                                .withLaunchSound(this.getStringSetting("blue-wither-skull-sound"))
                                .launch(p, 1.5);
                            blueSkull.setCharged(true);
                            break;
                        default:
                            break;
                    }
                }
            } else {
                event.setCancelled(true);
                new ProjectileBuilder()
                    .withType(Fireball.class)
                    .withLaunchSound(this.getStringSetting("fireball-sound"))
                    .withLaunchParticle(this.getStringSetting("launch-particle"))
                    .launch(p, 1.5);
            }
        }
    }

    @EventHandler
    public void onShoot(final EntityShootBowEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            final Player p = (Player) event.getEntity();
            if (this.equalsItem(p.getItemInHand())) {
                if (PlayerData.RATCHETS_BOW.getString(p.getUniqueId()) != null) {
                    String option = PlayerData.RATCHETS_BOW.getString(p.getUniqueId());
                    if (option.equals("triplearrows")) {
                        Entity pr = event.getProjectile();
                        double direction = this.getDoubleSetting("triple-arrows-direction");
                        EntityUtil.randomDirection(pr, direction);
                        new ProjectileBuilder()
                            .withType(Arrow.class)
                            .withRandomDirection(direction)
                            .launch(p, event.getForce() * 4);
                        new ProjectileBuilder()
                            .withType(Arrow.class)
                            .withRandomDirection(direction)
                            .launch(p, event.getForce() * 4);
                    } else if (option.equals("fireworkarrow")) {
                        event.getProjectile().setFireTicks(Integer.MAX_VALUE);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Fireball) {
            Player p = (Player) event.getEntity();
            Fireball fireball = (Fireball) event.getDamager();
            if (fireball.getShooter() instanceof Player) {
                Player shooter = (Player) fireball.getShooter();
                if (p.getUniqueId().equals(shooter.getUniqueId()) && this.equalsItem(p.getItemInHand())) {
                    event.setDamage(this.getDoubleSetting("fireball-jump-damage"));
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile pr = event.getEntity();
        if (pr instanceof Fireball) {
            Fireball fireball = (Fireball) pr;
            if (fireball.getShooter() != null && fireball.getShooter() instanceof Player) {
                Player shooter = (Player) fireball.getShooter();
                double radius = this.getDoubleSetting("fireball-jump-radius");
                double playerSpeed = this.getDoubleSetting("player-jump-speed");
                double mobSpeed = this.getDoubleSetting("mob-jump-speed");
                for (Entity e : fireball.getNearbyEntities(radius, radius, radius)) {
                    if (e instanceof LivingEntity) {
                        if (this.equalsItem(shooter.getItemInHand())) {
                            if (e.getType() == EntityType.PLAYER) {
                                Player p = (Player) e;
                                EntityUtil.projectileJump(p, fireball, playerSpeed, playerSpeed, playerSpeed);
                                if (p.getName().equals(shooter.getName())) {
                                    Particle.fromString(this.getStringSetting("jump-particle")).display(p.getLocation());
                                }
                            } else {
                                EntityUtil.projectileJump((LivingEntity) e, fireball, mobSpeed, mobSpeed, mobSpeed);
                            }
                        }
                    }
                }
            }
        }
        if (pr instanceof Fireball && !(pr instanceof WitherSkull) && pr.getShooter() instanceof Player) {
            final Player shooter = (Player) pr.getShooter();
            if (this.equalsItem(shooter.getItemInHand())) {
                final Location location = pr.getLocation();
                Particle.fromString(this.getStringSetting("particle")).display(pr.getLocation());
                Location hitLocMin = this.getLocationSetting("teleport-hit-location-min");
                Location hitLocMax = this.getLocationSetting("teleport-hit-location-max");
                if (location.getWorld().getName().equals(hitLocMin.getWorld().getName())
                        && location.getX() >= hitLocMin.getX()
                        && location.getY() >= hitLocMin.getY()
                        && location.getZ() >= hitLocMin.getZ()
                        && location.getX() <= hitLocMax.getX()
                        && location.getY() <= hitLocMax.getY()
                        && location.getZ() <= hitLocMax.getZ()) {
                    displayTeleportParticles(shooter.getLocation());
                    RepeatableRunnable teleportTask = new RepeatableRunnable(Bukkit.getScheduler(), V3LD1N.getPlugin(), 6, 4, 2) {
                        @Override
                        public void onRun() {
                            Particle.fromString(getStringSetting("teleport-hit-particle")).display(location);
                            location.getWorld().strikeLightning(location);
                        }
                    };
                    teleportTask.run();
                    Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable(){
                        @Override
                        public void run() {
                            location.getWorld().strikeLightning(location);
                        }
                    }, 24);
                    final Location teleLoc = this.getLocationSetting("teleport-location");
                    Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            shooter.teleport(teleLoc);
                            PlayerAnimation.BED_LEAVE.playToPlayer(shooter);
                            displayTeleportParticles(teleLoc);
                        }
                    }, this.getIntSetting("teleport-delay"));
                }
            }
        } else if (pr.getType() == EntityType.ARROW && pr.getShooter() instanceof Player) {
            Player shooter = (Player) pr.getShooter();
            if (this.equalsItem(shooter.getItemInHand())) {
                if (PlayerData.RATCHETS_BOW.getString(shooter.getUniqueId()) != null) {
                    switch (PlayerData.RATCHETS_BOW.getString(shooter.getUniqueId())) {
                    case "fireworkarrow":
                        Type fwType = Type.BALL;
                        if (PlayerData.FIREWORK_ARROWS.getString(shooter.getUniqueId()) != null) {
                            fwType = Type.valueOf(PlayerData.FIREWORK_ARROWS.getString(shooter.getUniqueId()));
                        }
                        Color fadeColor = Color.fromRGB(216, 205, 17);
                        EntityUtil.detonateFireworkProjectile(pr, pr.getLocation(), fwType, fwColor, fadeColor);
                        break;
                    default:
                        break;
                    }
                }
            }
        }
    }

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
}
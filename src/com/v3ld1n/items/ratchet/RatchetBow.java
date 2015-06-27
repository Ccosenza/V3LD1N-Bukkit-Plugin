package com.v3ld1n.items.ratchet;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.PlayerData;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.EntityUtil;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.PlayerAnimation;
import com.v3ld1n.util.ProjectileBuilder;
import com.v3ld1n.util.RepeatableRunnable;

public class RatchetBow extends V3LD1NItem {
    private final Color color = Color.ORANGE;

    public RatchetBow() {
        super("ratchets-bow");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack hand = p.getItemInHand();
        if (this.equalsItem(hand) && useActions.contains(event.getAction())) {
            if (PlayerData.RATCHETS_BOW.getString(p.getUniqueId()) != null) {
                String projectileString = PlayerData.RATCHETS_BOW.getString(p.getUniqueId());
                RatchetBowType pr = RatchetBowType.valueOf(projectileString);
                if (pr != RatchetBowType.ARROW && pr != RatchetBowType.TRIPLE_ARROWS && pr != RatchetBowType.FIREWORK_ARROW) {
                    event.setCancelled(true);
                    switch (pr) {
                        case SNOWBALL:
                            new ProjectileBuilder()
                                .withType(Snowball.class)
                                .withLaunchSound(this.getSoundSetting("snowball-sound"))
                                .launch(p, 1.5);
                            break;
                        case ENDER_PEARL:
                            new ProjectileBuilder()
                            .withType(EnderPearl.class)
                            .withLaunchSound(this.getSoundSetting("ender-pearl-sound"))
                            .withRandomDirection(this.getDoubleSetting("ender-pearl-direction"))
                            .launch(p, 1.5);
                            break;
                        case EGG:
                            new ProjectileBuilder()
                            .withType(Egg.class)
                            .withLaunchSound(this.getSoundSetting("egg-sound"))
                            .launch(p, 1.5);
                            break;
                        case WITHER_SKULL:
                            new ProjectileBuilder()
                            .withType(WitherSkull.class)
                            .withLaunchSound(this.getSoundSetting("wither-skull-sound"))
                            .launch(p, 1.5);
                            break;
                        case BLUE_WITHER_SKULL:
                            WitherSkull blueSkull = (WitherSkull) new ProjectileBuilder()
                                .withType(WitherSkull.class)
                                .withLaunchSound(this.getSoundSetting("blue-wither-skull-sound"))
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
                    .withLaunchSound(this.getSoundSetting("fireball-sound"))
                    .withLaunchParticle(this.getParticleSetting("launch-particle"))
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
                    if (RatchetBowType.fromString(option) == RatchetBowType.TRIPLE_ARROWS) {
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
                    } else if (RatchetBowType.fromString(option) == RatchetBowType.FIREWORK_ARROW) {
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
                for (Entity e : fireball.getNearbyEntities(radius, radius, radius)) {
                    if (e instanceof LivingEntity && e.getType() != EntityType.ARMOR_STAND) {
                        if (this.equalsItem(shooter.getItemInHand())) {
                            EntityUtil.projectileJump((LivingEntity) e, fireball);
                            if (e.getType() == EntityType.PLAYER) {
                                Player p = (Player) e;
                                if (p.getName().equals(shooter.getName())) {
                                    Particle.displayList(p.getLocation(), this.getStringListSetting("jump-particles"));
                                }
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
                this.displayParticles(pr.getLocation());
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
                            Particle.displayList(location, getStringListSetting("teleport-hit-particles"));
                            if (getBooleanSetting("teleport-lightning")) {
                                location.getWorld().strikeLightning(location);
                            }
                        }
                    };
                    teleportTask.run();
                    if (this.getBooleanSetting("teleport-lightning")) {
                        Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable(){
                            @Override
                            public void run() {
                                location.getWorld().strikeLightning(location);
                            }
                        }, 24);
                    }
                    final Location teleLoc = this.getLocationSetting("teleport-location");
                    Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            shooter.teleport(teleLoc);
                            PlayerAnimation.BED_LEAVE.playTo(shooter);
                            displayTeleportParticles(teleLoc);
                        }
                    }, this.getIntSetting("teleport-delay"));
                }
            }
        } else if (pr.getType() == EntityType.ARROW && pr.getShooter() instanceof Player) {
            Player shooter = (Player) pr.getShooter();
            if (this.equalsItem(shooter.getItemInHand())) {
                if (PlayerData.RATCHETS_BOW.getString(shooter.getUniqueId()) != null) {
                    UUID uuid = shooter.getUniqueId();
                    RatchetBowType projectile = RatchetBowType.fromString(PlayerData.RATCHETS_BOW.getString(uuid));
                    switch (projectile) {
                    case FIREWORK_ARROW:
                        Type type = Type.BALL;
                        if (PlayerData.FIREWORK_ARROWS.getString(shooter.getUniqueId()) != null) {
                            type = Type.valueOf(PlayerData.FIREWORK_ARROWS.getString(shooter.getUniqueId()));
                        }
                        Color fade = Color.fromRGB(216, 205, 17);
                        FireworkEffect effect = FireworkEffect.builder()
                                .with(type)
                                .withColor(color)
                                .withFade(fade)
                                .withFlicker()
                                .withTrail()
                                .build();
                        EntityUtil.detonateFireworkProjectile(pr, effect, pr.getLocation());
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
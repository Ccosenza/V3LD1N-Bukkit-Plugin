package com.v3ld1n.items.ratchet;

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
    private final Color color = Color.ORANGE;

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
        if (event.getEntityType() == EntityType.PLAYER) {
            final Player p = (Player) event.getEntity();
            if (this.equalsItem(p.getItemInHand())) {
                if (PlayerData.RATCHETS_BOW.getString(p.getUniqueId()) != null) {
                    String option = PlayerData.RATCHETS_BOW.getString(p.getUniqueId());
                    if (RatchetBowType.valueOf(option.toUpperCase()) == RatchetBowType.TRIPLE_ARROWS) {
                        Entity pr = event.getProjectile();
                        double direction = this.getDoubleSetting("triple-arrows-direction");
                        EntityUtil.randomDirection(pr, direction);
                        for (int i = 0; i < 2; i++) {
                            new ProjectileBuilder(Arrow.class)
                                .setRandomDirection(direction)
                                .setSpeed(event.getForce() * 4)
                                .launch(p);
                        }
                    } else if (RatchetBowType.valueOf(option.toUpperCase()) == RatchetBowType.FIREWORK_ARROW) {
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
                if (LocationUtil.isInArea(location, hitLocMin, hitLocMax)) {
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
                    RatchetBowType projectile = RatchetBowType.valueOf(PlayerData.RATCHETS_BOW.getString(uuid).toUpperCase());
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
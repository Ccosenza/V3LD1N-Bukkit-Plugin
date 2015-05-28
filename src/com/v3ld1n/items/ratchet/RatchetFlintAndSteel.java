package com.v3ld1n.items.ratchet;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.v3ld1n.V3LD1N;
import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.EntityUtil;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.ProjectileBuilder;
import com.v3ld1n.util.RepeatableRunnable;
import com.v3ld1n.util.Sound;

public class RatchetFlintAndSteel extends V3LD1NItem {
    public RatchetFlintAndSteel() {
        super("ratchets-flint-and-steel");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Action a = event.getAction();
        if (useActionsLeft.contains(a)) {
            if (this.equalsItem(p.getItemInHand())) {
                event.setCancelled(true);
                final Projectile pr = new ProjectileBuilder()
                    .withType(Fireball.class)
                    .withLaunchSound(Sound.fromString(this.getStringSetting("sound")))
                    .withLaunchParticle(this.getParticleSetting("launch-particle"))
                    .launch(p, 0.8);
                RepeatableRunnable trailTask = new RepeatableRunnable(Bukkit.getScheduler(), V3LD1N.getPlugin(), 0, this.getIntSetting("trail-ticks"), this.getIntSetting("trail-times")) {
                    @Override
                    public void onRun() {
                        if (pr != null && !pr.isDead()) {
                            List<String> setting = getStringListSetting("trail-particles");
                            List<Particle> trails = Particle.fromList(setting);
                            for (Particle trail : trails) {
                                trail.setSpeed(trail.getSpeed() - (random.nextFloat() / 10));
                                trail.display(pr.getLocation());
                            }
                        }
                    }
                };
                trailTask.run();
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile pr = event.getEntity();
        if (pr.getType() == EntityType.FIREBALL && pr.getShooter() instanceof Player) {
            Player shooter = (Player) pr.getShooter();
            if (this.equalsItem(shooter.getItemInHand())) {
                this.displayParticles(pr.getLocation());
                EntityUtil.detonateLightningProjectile(pr, pr.getLocation(), true);
            }
        }
    }
}
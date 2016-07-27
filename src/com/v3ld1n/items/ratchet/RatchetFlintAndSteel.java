package com.v3ld1n.items.ratchet;

import java.util.List;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.EntityUtil;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.ProjectileBuilder;
import com.v3ld1n.util.RepeatableRunnable;

public class RatchetFlintAndSteel extends V3LD1NItem {
    public RatchetFlintAndSteel() {
        super("ratchets-flint-and-steel");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!entityIsHoldingItem(player)) return;
        if (!isLeftClick(event.getAction())) return;

        event.setCancelled(true);
        Projectile projectile = new ProjectileBuilder(Fireball.class)
            .setLaunchSounds(sounds)
            .setLaunchParticles(settings.getParticles("launch-particles"))
            .setSpeed(0.8)
            .launch(player);
        displayTrail(projectile);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (!projectileIsValid(projectile, EntityType.FIREBALL)) return;

        displayParticles(projectile.getLocation());
        EntityUtil.detonateLightningProjectile(projectile, projectile.getLocation(), true);
    }

    private void displayTrail(final Projectile projectile) {
        int ticks = settings.getInt("trail-ticks");
        int times = settings.getInt("trail-times");
        RepeatableRunnable trail = new RepeatableRunnable() {
            @Override
            public void onRun() {
                if (projectile == null || projectile.isDead()) return;

                List<String> particleSetting = settings.getStringList("trail-particles");
                List<Particle> particles = Particle.fromList(particleSetting);

                // Adds a random speed to the particles
                for (Particle particle : particles) {
                    particle.setSpeed(particle.getSpeed() - (random.nextFloat() / 10));
                    particle.display(projectile.getLocation());
                }
            }
        };
        trail.start(0, ticks, times);
    }
}
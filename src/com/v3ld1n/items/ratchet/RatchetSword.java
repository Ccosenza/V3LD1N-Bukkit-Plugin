package com.v3ld1n.items.ratchet;

import java.util.ArrayList;
import java.util.List;

import com.v3ld1n.util.WorldUtil;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.EntityUtil;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.PlayerAnimation;

public class RatchetSword extends V3LD1NItem {
    public RatchetSword() {
        super("ratchets-sword");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!entityIsHoldingItem(player)) return;
        if (!isRightClick(event.getAction())) return;

        push(player, entitiesNear(player));
    }

    private List<Entity> entitiesNear(Player p) {
        List<Entity> entities = new ArrayList<>();
        List<Entity> nearbyEntities = p.getNearbyEntities(5, 5, 5);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Monster || entity instanceof Projectile) {
                entities.add(entity);
            }
        }
        return entities;
    }

    private void push(Player player, List<Entity> entities) {
        boolean pushingEnemy = false;
        for (Entity entity : entities) {
            pushingEnemy = entity instanceof Monster || entity instanceof Projectile;

            Location effectLocation;
            if (entity instanceof Monster) {
                effectLocation = ((Monster) entity).getEyeLocation();
            } else {
                effectLocation = entity.getLocation();
            }

            boolean isMonster = entity instanceof Monster;
            effectLocation = isMonster ? ((Monster) entity).getEyeLocation() : entity.getLocation();

            double pushSpeed = settings.getDouble("push-speed");
            double pushUpSpeed = settings.getDouble("push-up-speed");
            EntityUtil.pushToward(entity, player.getLocation(), new Vector(pushSpeed, 0, pushSpeed), true);

            if (!(entity instanceof Projectile)) {
                entity.setVelocity(entity.getVelocity().add(new Vector(0, pushUpSpeed, 0)));
            }

            Particle.displayList(settings.getParticles("mob-particles"), effectLocation);
            playSounds(effectLocation);
            if (pushingEnemy) {
                PlayerAnimation.SWING_ARM.play(player);
                particle(player.getLocation(), entities.size());
            }
        }
    }

    private void particle(Location loc, int count) {
        Block below = loc.getBlock().getRelative(BlockFace.DOWN);
        if (below.getType() != Material.AIR) {
            double radius = settings.getDouble("player-particle-radius");
            double multiplier = settings.getInt("player-particle-count-multiplier");
            for (Particle particle : this.particles) {
                WorldUtil.spawnParticleCircle(particle, loc, radius, (int) (count * multiplier));
            }
        }
    }
}
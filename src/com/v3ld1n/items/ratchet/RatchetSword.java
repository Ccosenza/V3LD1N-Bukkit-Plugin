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
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.EntityUtil;
import com.v3ld1n.util.Particle;

@SuppressWarnings("deprecation")
public class RatchetSword extends V3LD1NItem {
    public RatchetSword() {
        super("ratchets-sword");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Action a = event.getAction();
        if (useActions.contains(a)) {
            if (this.equalsItem(p.getItemInHand())) {
                push(p, entitiesNear(p));
            }
        }
    }

    private List<Entity> entitiesNear(Player p) {
        List<Entity> entities = new ArrayList<>();
        List<Entity> nearby = p.getNearbyEntities(5, 5, 5);
        for (Entity entity : nearby) {
            if (entity instanceof Monster || entity instanceof Projectile) {
                entities.add(entity);
            }
        }
        return entities;
    }

    private void push(Player p, List<Entity> es) {
        boolean entitiesContainsEnemy = false;
        for (Entity entity : es) {
            entitiesContainsEnemy = entity instanceof Monster || entity instanceof Projectile;
            Location location;
            if (entity instanceof Monster) {
                location = ((Monster) entity).getEyeLocation();
            } else {
                location = entity.getLocation();
            }
            boolean isMonster = entity instanceof Monster;
            location = isMonster ? ((Monster) entity).getEyeLocation() : entity.getLocation();
            double pushSpeed = this.getDoubleSetting("push-speed");
            double pushUpSpeed = this.getDoubleSetting("push-up-speed");
            EntityUtil.pushToward(entity, p.getLocation(), new Vector(pushSpeed, 0, pushSpeed), true);
            if (!(entity instanceof Projectile)) {
                entity.setVelocity(entity.getVelocity().add(new Vector(0, pushUpSpeed, 0)));
            }
            Particle.displayList(location, this.getStringListSetting("mob-particles"));
            this.getSoundSetting("sound").play(location);
            if (entitiesContainsEnemy) {
                particle(p.getLocation(), es.size());
            }
        }
    }

    private void particle(Location loc, int count) {
        Block below = loc.getBlock().getRelative(BlockFace.DOWN);
        if (below.getType() != Material.AIR) {
            Particle particle = Particle.builder()
                    .setName("iconcrack_" + below.getTypeId() + "_" + below.getData())
                    .setSpeed((float) this.getDoubleSetting("player-particle-speed"))
                    .build();
            double radius = this.getDoubleSetting("player-particle-radius");
            double multiplier = this.getIntSetting("player-particle-count-multiplier");
            WorldUtil.spawnParticleCircle(particle, loc, radius, (int) (count * multiplier));
        }
    }
}
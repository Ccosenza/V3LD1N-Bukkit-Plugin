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
import com.v3ld1n.util.SoundUtil;

@SuppressWarnings("deprecation")
public class RatchetSword extends V3LD1NItem {
    public RatchetSword() {
        super("ratchets-sword");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Action a = event.getAction();
        boolean entitiesContainsEnemy = false;
        if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
            if (this.equalsItem(p.getItemInHand())) {
                List<Entity> entities = p.getNearbyEntities(5,5,5);
                List<Entity> pushEntities = new ArrayList<>();
                for (Entity entity : entities) {
                    if (entity instanceof Monster || entity instanceof Projectile) {
                        pushEntities.add(entity);
                        entitiesContainsEnemy = true;
                        Location location;
                        if (entity instanceof Monster) {
                            location = ((Monster) entity).getEyeLocation();
                        } else {
                            location = entity.getLocation();
                        }
                        double pushSpeed = this.getDoubleSetting("push-speed");
                        double pushUpSpeed = this.getDoubleSetting("push-up-speed");
                        EntityUtil.pushToward(entity, p.getLocation(), pushSpeed, 0, pushSpeed);
                        if (!(entity instanceof Projectile)) {
                            entity.setVelocity(entity.getVelocity().add(new Vector(0, pushUpSpeed, 0)));
                        }
                        Particle.fromString(this.getStringSetting("mob-particle")).display(location);
                        SoundUtil.playSoundString(this.getStringSetting("sound"), location);
                    }
                }
                if (entitiesContainsEnemy) {
                    Block below = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
                    if (below.getType() != Material.AIR) {
                        Particle particle = Particle.builder()
                                .setName("iconcrack_" + below.getTypeId() + "_" + below.getData())
                                .setSpeed((float) this.getDoubleSetting("player-particle-speed"))
                                .build();
                        WorldUtil.spawnParticleCircle(particle, p.getLocation(), this.getDoubleSetting("player-particle-radius"), pushEntities.size() * this.getIntSetting("player-particle-count-multiplier"));
                    }
                }
            }
        }
    }
}
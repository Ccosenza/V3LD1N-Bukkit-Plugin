package com.v3ld1n.items.ratchet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.v3ld1n.V3LD1N;
import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.PlayerAnimation;
import com.v3ld1n.util.RepeatableRunnable;

public class RatchetFirework extends V3LD1NItem {
    public RatchetFirework() {
        super("ratchets-firework-rocket");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        Action a = event.getAction();
        if (useActions.contains(a)) {
            if (this.equalsItem(p.getItemInHand())) {
                event.setCancelled(true);
                if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                    PlayerAnimation.SWING_ARM.play(p, 25);
                    this.getSoundSetting("sound").play(p.getLocation());
                    RepeatableRunnable fwTask = new RepeatableRunnable(Bukkit.getScheduler(), V3LD1N.getPlugin(), 0, 2, 50) {
                        @Override
                        public void onRun() {
                            if (!p.isDead()) {
                                Vector divide = getVectorSetting("divide-velocity");
                                p.setVelocity(p.getLocation().getDirection().divide(divide));
                                p.setFallDistance(0);
                            }
                        }
                    };
                    RepeatableRunnable fwParticleTask = new RepeatableRunnable(Bukkit.getScheduler(), V3LD1N.getPlugin(), 0, 1, 110) {
                        @Override
                        public void onRun() {
                            if (!p.isDead()) {
                                Location loc = p.getLocation();
                                Material type = loc.getBlock().getType();
                                if (type == Material.WATER || type == Material.STATIONARY_WATER) {
                                    Particle.displayList(loc, getStringListSetting("water-particles"));
                                } else if (type == Material.LAVA || type == Material.STATIONARY_LAVA) {
                                    Particle.displayList(loc, getStringListSetting("lava-particles"));
                                } else {
                                    displayParticles(loc);
                                }
                            }
                        }
                    };
                    fwTask.run();
                    fwParticleTask.run();
                }
            }
        }
    }
}
package com.v3ld1n.items.ratchet;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.v3ld1n.V3LD1N;
import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.PlayerAnimation;
import com.v3ld1n.util.RepeatableRunnable;
import com.v3ld1n.util.Sound;

public class RatchetFirework extends V3LD1NItem {
    public RatchetFirework() {
        super("ratchets-firework-rocket");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        Action a = event.getAction();
        if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
            if (this.equalsItem(p.getItemInHand())) {
                event.setCancelled(true);
                if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                    PlayerAnimation.SWING_ARM.play(p, 25);
                    Sound.fromString(this.getStringSetting("sound")).play(p.getLocation());
                    RepeatableRunnable fireworkTask = new RepeatableRunnable(Bukkit.getScheduler(), V3LD1N.getPlugin(), 0, 2, 50) {
                        @Override
                        public void onRun() {
                            if (!p.isDead()) {
                                p.setVelocity(p.getLocation().getDirection().divide(getVectorSetting("divide-velocity")));
                                p.setFallDistance(0);
                            }
                        }
                    };
                    RepeatableRunnable fireworkParticleTask = new RepeatableRunnable(Bukkit.getScheduler(), V3LD1N.getPlugin(), 0, 1, 110) {
                        @Override
                        public void onRun() {
                            if (!p.isDead()) {
                                if (p.getLocation().getBlock().getType() == Material.WATER || p.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
                                    getParticleSetting("water-particle").display(p.getLocation());
                                } else if (p.getLocation().getBlock().getType() == Material.LAVA || p.getLocation().getBlock().getType() == Material.STATIONARY_LAVA) {
                                    getParticleSetting("lava-particle").display(p.getLocation());
                                }
                                else {
                                    getParticleSetting("particle").display(p.getLocation());
                                }
                            }
                        }
                    };
                    fireworkTask.run();
                    fireworkParticleTask.run();
                }
            }
        }
    }
}
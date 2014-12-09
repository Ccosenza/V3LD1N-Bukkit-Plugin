package com.v3ld1n.items.ratchet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.v3ld1n.V3LD1N;
import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.EntityUtil;
import com.v3ld1n.util.RepeatableRunnable;
import com.v3ld1n.util.WorldUtil;

public class RatchetFishingRod extends V3LD1NItem {
    public RatchetFishingRod() {
        super("ratchets-fishing-rod");
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        Action a = event.getAction();
        if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
            if (this.equalsItem(p.getItemInHand())) {
                double radius = this.getDoubleSetting("radius");
                for (final Entity e : p.getNearbyEntities(radius, radius, radius)) {
                    if (e.getType() == EntityType.FISHING_HOOK && ((Player) ((FishHook) e).getShooter()).getUniqueId().equals(p.getUniqueId())) {
                        for (Block block : WorldUtil.getNearbyBlocks(e.getLocation(), 1)) {
                            if (block.getType().isSolid()) {
                                final double distance = p.getLocation().distance(e.getLocation());
                                RepeatableRunnable grappleTask = new RepeatableRunnable(Bukkit.getScheduler(), V3LD1N.getPlugin(), 0, this.getIntSetting("ticks"), (long) (distance * this.getDoubleSetting("distance-multiplier"))) {
                                    @Override
                                    public void onRun() {
                                        Location pLoc = p.getLocation();
                                        Location eLoc = e.getLocation();
                                        double speedX = getDoubleSetting("speed-x");
                                        double speedY = getDoubleSetting("speed-y");
                                        double speedZ = getDoubleSetting("speed-z");
                                        if (distance < getDoubleSetting("close-distance") && pLoc.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                                            speedX /= getDoubleSetting("close-divide-speed");
                                            speedY /= getDoubleSetting("close-divide-speed");
                                            speedZ /= getDoubleSetting("close-divide-speed");
                                        }
                                        if (pLoc.getY() > eLoc.getY()) {
                                            speedY *= ((pLoc.getY() - eLoc.getY()) / 5);
                                        }
                                        if (pLoc.getBlockY() == eLoc.getBlockY()) {
                                            speedY = 0;
                                            p.setVelocity(p.getVelocity().add(new Vector(0, 0.1, 0)));
                                        }
                                        EntityUtil.pushToward(p, eLoc, speedX, speedY, speedZ);
                                        p.setFallDistance(0);
                                        getParticleSetting("particle").display(p.getLocation());
                                    }
                                };
                                grappleTask.run();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
package com.v3ld1n.items.ratchet;

import java.util.List;

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
import org.bukkit.event.entity.ProjectileLaunchEvent;
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
        if (useActions.contains(a) && this.equalsItem(p.getItemInHand())) {
            double r = this.getDoubleSetting("radius");
            for (final Entity e : p.getNearbyEntities(r, r, r)) {
                if (e.getType() == EntityType.FISHING_HOOK) {
                    FishHook hook = (FishHook) e;
                    Player shooter = (Player) hook.getShooter();
                    if (shooter.getUniqueId().equals(p.getUniqueId())) {
                        List<Block> near = WorldUtil.getNearbyBlocks(hook.getLocation(), 1);
                        for (Block block : near) {
                            if (block.getType().isSolid()) {
                                grapple(p, hook);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntityType() == EntityType.FISHING_HOOK) {
            final FishHook hook = (FishHook) event.getEntity();
            if (hook.getShooter() instanceof Player) {
                Player p = (Player) hook.getShooter();
                if (this.equalsItem(p.getItemInHand())) {
                    double speedMultiplier = this.getDoubleSetting("speed-multiplier");
                    hook.setVelocity(hook.getVelocity().multiply(speedMultiplier));
                }
            }
        }
    }

    public void grapple(final Player p, final FishHook hook) {
        final double distance = p.getLocation().distance(hook.getLocation());
        double multiplier = this.getDoubleSetting("distance-multiplier");
        long ticks = this.getIntSetting("ticks");
        long times = (long) (distance * multiplier);
        RepeatableRunnable task = new RepeatableRunnable(Bukkit.getScheduler(), V3LD1N.getPlugin(), 0, ticks, times) {
            @Override
            public void onRun() {
                Location pLoc = p.getLocation();
                Location eLoc = hook.getLocation();
                double speedX = getDoubleSetting("speed-x");
                double speedY = getDoubleSetting("speed-y");
                double speedZ = getDoubleSetting("speed-z");
                Block pBlock = pLoc.getBlock();
                Block down = pBlock.getRelative(BlockFace.DOWN);
                Material downType = down.getType();
                if (distance < getDoubleSetting("close-distance") && downType != Material.AIR) {
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
                EntityUtil.pushToward(p, eLoc, new Vector(speedX, speedY, speedZ), false);
                p.setFallDistance(0);
                displayParticles(p.getLocation());
            }
        };
        task.run();
    }
}
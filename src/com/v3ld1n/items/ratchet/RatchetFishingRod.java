package com.v3ld1n.items.ratchet;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

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
        Player player = event.getPlayer();
        if (!isRightClick(event.getAction())) return;

        double radius = settings.getDouble("radius");
        List<Entity> nearbyEntities = player.getNearbyEntities(radius, radius, radius);
        for (Entity entity : nearbyEntities) {
            if (!projectileIsValid(entity, EntityType.FISHING_HOOK)) continue;

            FishHook hook = (FishHook) entity;
            Player shooter = (Player) hook.getShooter();
            if (!player.equals(shooter)) return;

            List<Block> nearbyBlocks = WorldUtil.getNearbyBlocks(hook.getLocation(), 1);
            for (Block block : nearbyBlocks) {
                if (block.getType().isSolid()) {
                    grapple(player, hook);
                }
                break;
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!projectileIsValid(event.getEntity(), EntityType.FISHING_HOOK)) return;

        final FishHook hook = (FishHook) event.getEntity();
        double speedMultiplier = settings.getDouble("speed-multiplier");
        hook.setVelocity(hook.getVelocity().multiply(speedMultiplier));
    }

    /**
     * Pulls the player toward the hook
     * @param player the player
     * @param hook the fishing rod hook
     */
    public void grapple(final Player player, final FishHook hook) {
        final double distance = player.getLocation().distance(hook.getLocation());
        double multiplier = settings.getDouble("distance-multiplier");
        long ticks = settings.getInt("ticks");
        long times = (long) (distance * multiplier);

        RepeatableRunnable task = new RepeatableRunnable() {
            @Override
            public void onRun() {
                Location playerLocation = player.getLocation();
                Location hookLocation = hook.getLocation();
                double speedX = settings.getDouble("speed-x");
                double speedY = settings.getDouble("speed-y");
                double speedZ = settings.getDouble("speed-z");
                Block playerBlock = playerLocation.getBlock();
                Block blockBelow = playerBlock.getRelative(BlockFace.DOWN);
                if (distance < settings.getDouble("near-distance") && blockBelow.getType() != Material.AIR) {
                    double speedDivisor = settings.getDouble("near-speed-divisor");
                    speedX /= speedDivisor;
                    speedY /= speedDivisor;
                    speedZ /= speedDivisor;
                }

                // Increases the Y speed if the player is below the hook
                int playerY = playerLocation.getBlockY();
                int hookY = hookLocation.getBlockY();
                if (playerY > hookY) {
                    speedY *= ((playerLocation.getY() - hookLocation.getY()) / 5);
                }

                EntityUtil.pushToward(player, hookLocation, new Vector(speedX, speedY, speedZ), false);
                player.setFallDistance(0);
                displayParticles(player.getLocation());
            }
        };
        task.start(0, ticks, times);
    }
}
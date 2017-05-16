package com.v3ld1n.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class LocationUtil {
    private LocationUtil() {
    }

    /**
     * Returns whether a location is in an area
     * @param loc the location
     * @param start the area starting location
     * @param end the area end location
     * @return whether the location is in the area
     */
    public static boolean isInArea(Location loc, Location start, Location end) {
        if (!loc.getWorld().getName().equals(start.getWorld().getName())) {
            return false;
        }
        boolean inX = loc.getX() >= start.getX() && loc.getX() <= end.getX();
        boolean inY = loc.getY() >= start.getY() && loc.getY() <= end.getY();
        boolean inZ = loc.getZ() >= start.getZ() && loc.getZ() <= end.getZ();
        return inX && inY && inZ;
    }

    /**
     * Returns the nearest entity to another entity
     * @param entity the entity to find an entity near
     * @return the nearest entity
     */
    public static Entity getNearestEntity(Entity entity) {
        Entity nearestEntity = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Entity e : entity.getWorld().getEntities()) {
            if (entity != e) {
                double distance = entity.getLocation().distance(e.getLocation());
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestEntity = e;
                }
            }
        }
        return nearestEntity;
    }

    /**
     * Returns the nearest block entity to an entity
     * @param entity the entity to find a block entity near
     * @return the nearest block entity
     */
    public static BlockState getNearestBlockEntity(Entity entity) {
        BlockState nearestBlockEntity = null;
        double nearestDistance = Double.MAX_VALUE;
        for (BlockState be : WorldUtil.getBlockEntities(entity.getWorld())) {
            double distance = entity.getLocation().distance(be.getLocation());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestBlockEntity = be;
            }
        }
        return nearestBlockEntity;
    }

    /**
     * Returns the nearest player to another player
     * @param player the player to find a player near
     * @return the nearest player
     */
    public static Player getNearestPlayer(Player player) {
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Player p : player.getWorld().getPlayers()) {
            if (player.getUniqueId() != p.getUniqueId()) {
                double distance = player.getLocation().distance(p.getLocation());
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestPlayer = p;
                }
            }
        }
        return nearestPlayer;
    }

    /**
     * Returns a list of players in a radius around a location
     * @param location the location
     * @param radius the radius to find players in
     * @return the players in the radius
     */
    public static List<Player> getNearbyPlayers(Location location, double radius) {
        List<Player> players = new ArrayList<>();
        for (Player player : location.getWorld().getPlayers()) {
            if (player.getLocation().distance(location) <= radius) {
                players.add(player);
            }
        }
        return players;
    }

    /**
     * Returns a list of blocks near a location
     * @param location the location
     * @param radius the radius to find blocks in
     * @return the blocks in the radius
     */
    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for (int x = location.getBlockX() - radius ; x <= location.getBlockX() + radius ; x++) {
            for (int y = location.getBlockY() - radius ; y <= location.getBlockY() + radius ; y++) {
                for (int z = location.getBlockZ() - radius ; z <= location.getBlockZ() + radius ; z++) {
                    Block block = location.getWorld().getBlockAt(x, y, z);
                    if (!block.isEmpty()) {
                        blocks.add(block);
                    }
                }
            }
        }
        return blocks;
    }
}
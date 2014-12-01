package com.v3ld1n.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldUtil {
    private WorldUtil() {
    }

    /**
     * Returns the nearest player to another player
     * @param player the player to find a player near
     * @return the nearest player to the player
     */
    public static Player getNearestPlayer(Player player) {
        Double nearestDistance = null;
        Player nearestPlayer = null;
        for (Player p : player.getWorld().getPlayers()) {
            if ((nearestDistance == null || p.getLocation().distance(player.getLocation()) <= nearestDistance) && !p.getName().equals(player.getName())) {
                nearestDistance = p.getLocation().distance(player.getLocation());
                nearestPlayer = p;
            } else {
                nearestPlayer = null;
            }
        }
        return nearestPlayer;
    }

    /**
     * Returns the nearest player to a location
     * @param location the location
     * @return the nearest player to the location
     */
    public static Player getNearestPlayer(Location location) {
        Double nearestDistance = null;
        Player nearestPlayer = null;
        for (Player p : location.getWorld().getPlayers()) {
            if (nearestDistance == null || p.getLocation().distance(location) <= nearestDistance) {
                nearestDistance = p.getLocation().distance(location);
                nearestPlayer = p;
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
     * Spawns a circle of particles
     * @param particle the particle to spawn
     * @param location the location to spawn the particles around
     * @param distance the distance from the location to spawn the particles
     * @param count the amount of particles to spawn
     */
    public static void spawnParticleCircle(Particle particle, Location location, double distance, int count) {
        for (int i = 0; i < count; i++) {
            double angle, x, z;
            angle = 2 * Math.PI * i / count;
            x = Math.cos(angle) * distance;
            z = Math.sin(angle) * distance;
            Location location1 = location.add(x, 0, z);
            Location location2 = location.add(x, 0, z);
            Location location3 = location.add(x, 0, z);
            particle.display(location);
            location1.subtract(x, 0, z);
            location2.subtract(x, 0, z);
            location3.subtract(x, 0, z);
        }
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
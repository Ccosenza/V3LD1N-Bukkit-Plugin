package com.v3ld1n.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class WorldUtil {
    private WorldUtil() {
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
     * Returns a list of entities in a radius around a location
     * @param location the location
     * @param radius the radius to find entities in
     * @return the entities in the radius
     */
    public static List<Entity> getNearbyEntities(Location location, double radius) {
        List<Entity> entities = new ArrayList<>();
        for (Entity entity : location.getWorld().getEntities()) {
            if (entity.getLocation().distance(location) <= radius) {
                entities.add(entity);
            }
        }
        return entities;
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

    /**
     * Returns a list of block entities in a world
     * @param world the world
     * @return a list of block entities
     */
    public static List<BlockState> getBlockEntities(World world) {
        List<BlockState> blockEntities = new ArrayList<>();
        for (Chunk chunk : world.getLoadedChunks()) {
            for (BlockState blockState : chunk.getTileEntities()) {
                blockEntities.add(blockState);
            }
        }
        return blockEntities;
    }

    /**
     * Returns a list of all loaded entities on the server
     * @return a list of entities
     */
    public static List<Entity> getAllEntities() {
        List<Entity> entities = new ArrayList<>();
        for (World world : Bukkit.getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                entities.add(entity);
            }
        }
        return entities;
    }
}
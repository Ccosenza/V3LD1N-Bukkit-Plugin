package com.v3ld1n.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

public final class WorldUtil {
    private WorldUtil() {
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
package com.v3ld1n.tasks;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.v3ld1n.util.Particle;
import com.v3ld1n.util.SoundUtil;
import com.v3ld1n.util.WorldUtil;

public class TeleportTask extends Task {
    private final double radius;
    private final Location teleportTo;
    private final List<Particle> particles;
    private final Location particleLocation;
    private final List<Particle> idleParticles;
    private final Location idleParticleLocation;
    private final List<String> sounds;

    public TeleportTask(String name, long ticks, String runMode, Location location, double radius, Location teleportTo, List<Particle> particles, double particleY, List<Particle> idleParticles, double idleParticleY, List<String> sounds) {
        super(name, ticks, runMode, location);
        this.radius = radius;
        this.teleportTo = teleportTo;
        this.particles = particles;
        this.particleLocation = location;
        this.particleLocation.add(new Vector(0, particleY, 0));
        this.idleParticles = idleParticles;
        this.idleParticleLocation = location;
        this.idleParticleLocation.add(new Vector(0, idleParticleY, 0));
        this.sounds = sounds;
    }

    @Override
    public void run() {
        for (Particle particle : idleParticles) {
            particle.display(idleParticleLocation);
        }
        List<Player> players = WorldUtil.getNearbyPlayers(location, radius);
        for (Player player : players) {
            teleport(player);
        }
    }

    private void teleport(Player player) {
        player.teleport(teleportTo);
        for (Particle particle : particles) {
            particle.display(particleLocation);
        }
        for (String sound : sounds) {
            SoundUtil.playSoundString(sound, location);
        }
    }

    public double getRadius() {
        return radius;
    }

    public Location getTeleportLocation() {
        return teleportTo;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public Location getParticleLocation() {
        return particleLocation;
    }

    public List<Particle> getIdleParticles() {
        return idleParticles;
    }

    public Location getIdleParticleLocation() {
        return idleParticleLocation;
    }

    public List<String> getSounds() {
        return sounds;
    }
}
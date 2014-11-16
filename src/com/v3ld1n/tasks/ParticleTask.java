package com.v3ld1n.tasks;

import java.util.List;

import org.bukkit.Location;

import com.v3ld1n.util.Particle;

public class ParticleTask extends Task {
    private final List<Particle> particles;

    public ParticleTask(String name, long ticks, String runMode, Location location, List<Particle> particles) {
        super(name, ticks, runMode, location);
        this.particles = particles;
    }

    @Override
    public void run() {
        Particle particle = particles.get(random.nextInt(particles.size()));
        particle.display(location);
    }

    public List<Particle> getParticles() {
        return particles;
    }
}
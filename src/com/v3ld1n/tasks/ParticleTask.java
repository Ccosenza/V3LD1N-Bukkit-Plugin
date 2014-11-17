package com.v3ld1n.tasks;

import java.util.ArrayList;
import java.util.List;

import com.v3ld1n.Config;
import com.v3ld1n.util.Particle;

public class ParticleTask extends Task {
    public ParticleTask(String name) {
        super(name, Config.TASKS_PARTICLE);
    }

    @Override
    public void run() {
        List<Particle> particles = new ArrayList<>();
        for (String particleString : this.getStringListSetting("particles")) {
            particles.add(Particle.fromString(particleString));
        }
        Particle particle = particles.get(random.nextInt(particles.size()));
        particle.display(this.getLocationSetting("location"));
    }
}
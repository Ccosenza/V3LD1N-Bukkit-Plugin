package com.v3ld1n.items.ratchet;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.util.Particle;

public enum RatchetBowType {
    FIREBALL(ConfigSetting.PARTICLE_RATCHETSBOW_FIREBALL.getString()),
    ARROW(ConfigSetting.PARTICLE_RATCHETSBOW_ARROW.getString()),
    TRIPLE_ARROWS(ConfigSetting.PARTICLE_RATCHETSBOW_TRIPLE_ARROWS.getString()),
    FIREWORK_ARROW(ConfigSetting.PARTICLE_RATCHETSBOW_FIREWORK.getString()),
    SNOWBALL(ConfigSetting.PARTICLE_RATCHETSBOW_SNOWBALL.getString()),
    ENDER_PEARL(ConfigSetting.PARTICLE_RATCHETSBOW_ENDER_PEARL.getString()),
    EGG(ConfigSetting.PARTICLE_RATCHETSBOW_EGG.getString()),
    WITHER_SKULL(ConfigSetting.PARTICLE_RATCHETSBOW_SKULL.getString()),
    BLUE_WITHER_SKULL(ConfigSetting.PARTICLE_RATCHETSBOW_BLUE_SKULL.getString());

    private final Particle particle;

    private RatchetBowType(String particle) {
        this.particle = Particle.fromString(particle);
    }

    public Particle getParticle() {
        return particle;
    }
}
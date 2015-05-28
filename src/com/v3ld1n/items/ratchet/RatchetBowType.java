package com.v3ld1n.items.ratchet;

import java.util.Arrays;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.util.Particle;

public enum RatchetBowType {
    FIREBALL(ConfigSetting.PARTICLE_RATCHETSBOW_FIREBALL),
    ARROW(ConfigSetting.PARTICLE_RATCHETSBOW_ARROW),
    TRIPLE_ARROWS(ConfigSetting.PARTICLE_RATCHETSBOW_TRIPLE_ARROWS),
    FIREWORK_ARROW(ConfigSetting.PARTICLE_RATCHETSBOW_FIREWORK),
    SNOWBALL(ConfigSetting.PARTICLE_RATCHETSBOW_SNOWBALL),
    ENDER_PEARL(ConfigSetting.PARTICLE_RATCHETSBOW_ENDER_PEARL),
    EGG(ConfigSetting.PARTICLE_RATCHETSBOW_EGG),
    WITHER_SKULL(ConfigSetting.PARTICLE_RATCHETSBOW_SKULL),
    BLUE_WITHER_SKULL(ConfigSetting.PARTICLE_RATCHETSBOW_BLUE_SKULL);

    private final Particle particle;

    private RatchetBowType(ConfigSetting particle) {
        this.particle = Particle.fromString(particle.getString());
    }

    public Particle getParticle() {
        return particle;
    }

    public static RatchetBowType fromString(String string) {
        boolean contains = false;
        for (RatchetBowType type : Arrays.asList(values())) {
            if (type.name().equalsIgnoreCase(string)) {
                contains = true;
            }
        }
        if (contains) {
            RatchetBowType type = RatchetBowType.valueOf(string);
            return type;
        }
        return null;
    }
}
package com.v3ld1n.items.ratchet;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.WitherSkull;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.util.Particle;

public enum RatchetBowType {
    FIREBALL(Fireball.class, ConfigSetting.PARTICLE_RATCHETSBOW_FIREBALL, "fireball-sounds"),
    ARROW(Arrow.class, ConfigSetting.PARTICLE_RATCHETSBOW_ARROW, null),
    TRIPLE_ARROWS(Arrow.class, ConfigSetting.PARTICLE_RATCHETSBOW_TRIPLE_ARROWS, null),
    FIREWORK_ARROW(Arrow.class, ConfigSetting.PARTICLE_RATCHETSBOW_FIREWORK, null),
    SNOWBALL(Snowball.class, ConfigSetting.PARTICLE_RATCHETSBOW_SNOWBALL, "snowball-sounds"),
    ENDER_PEARL(EnderPearl.class, ConfigSetting.PARTICLE_RATCHETSBOW_ENDER_PEARL, "ender-pearl-sounds"),
    EGG(Egg.class, ConfigSetting.PARTICLE_RATCHETSBOW_EGG, "egg-sounds"),
    WITHER_SKULL(WitherSkull.class, ConfigSetting.PARTICLE_RATCHETSBOW_SKULL, "wither-skull-sounds"),
    BLUE_WITHER_SKULL(WitherSkull.class, ConfigSetting.PARTICLE_RATCHETSBOW_BLUE_SKULL, "blue-wither-skull-sounds");

    private final Class<? extends Projectile> projectile;
    private final Particle particle;
    private final String sound;

    private RatchetBowType(Class<? extends Projectile> projectile, ConfigSetting particle, String sound) {
        this.projectile = projectile;
        this.particle = Particle.fromString(particle.getString());
        this.sound = sound;
    }

    public Class<? extends Projectile> getProjectile() {
        return projectile;
    }

    public Particle getParticle() {
        return particle;
    }

    public String getSound() {
        return sound;
    }
}
package com.v3ld1n.util;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

public class ProjectileBuilder {
    Class<? extends Projectile> projectile;
    float soundVolume, soundPitch;
    Sound launchSound;
    String soundName, soundString;
    Particle launchParticle;
    String particleString;
    double randomDirection;

    /**
     * Set the type of the projectile
     * @param type the projectile class
     * @return this object
     */
    public ProjectileBuilder withType(Class<? extends Projectile> type) {
        this.projectile = type;
        return this;
    }

    /**
     * Add a firing sound
     * @param sound the sound
     * @param pitch the pitch of the sound
     * @param volume the volume of the sound
     * @return this object
     */
    public ProjectileBuilder withLaunchSound(Sound sound, float pitch, float volume) {
        this.launchSound = sound;
        this.soundVolume = volume;
        this.soundPitch = pitch;
        return this;
    }

    /**
     * Add a firing sound
     * @param sound the sound name
     * @param pitch the pitch of the sound
     * @param volume the volume of the sound
     * @return this object
     */
    public ProjectileBuilder withLaunchSound(String sound, float pitch, float volume) {
        this.soundName = sound;
        this.soundPitch = pitch;
        this.soundVolume = volume;
        return this;
    }

    /**
     * Add a firing sound
     * @param sound the sound string
     * @return this object
     */
    public ProjectileBuilder withLaunchSound(String sound) {
        this.soundString = sound;
        return this;
    }

    /**
     * Add a firing particle
     * @param particle the particle
     * @return this object
     */
    public ProjectileBuilder withLaunchParticle(Particle particle) {
        this.launchParticle = particle;
        return this;
    }

    /**
     * Add a firing particle
     * @param particle the particle
     * @return this object
     */
    public ProjectileBuilder withLaunchParticle(String particle) {
        this.particleString = particle;
        return this;
    }

    public ProjectileBuilder withRandomDirection(double distance) {
        this.randomDirection = distance;
        return this;
    }

    /**
     * Launch the projectile at a location
     * @param location the location to launch the projectile at
     * @param direction the direction of the projectile
     * @return the projectile
     */
    public Projectile launch(Location location, Vector direction) {
        Projectile pr = location.getWorld().spawn(location, projectile);
        pr.setVelocity(direction);
        if (soundName != null) {
            SoundUtil.playSound(soundName, location, soundVolume, soundPitch);
        } else if (launchSound != null) {
            location.getWorld().playSound(location, launchSound, soundVolume, soundPitch);
        } else if (soundString != null) {
            SoundUtil.playSoundString(soundString, location);
        }
        return pr;
    }

    /**
     * Make an entity launch the projectile
     * @param shooter the entity shooting the projectile
     * @param speed the speed of the projectile
     * @return the projectile
     */
    public Projectile launch(LivingEntity shooter, double speed) {
        Projectile pr = shooter.launchProjectile(projectile);
        pr.setVelocity(shooter.getLocation().getDirection().multiply(speed));
        if (soundName != null) {
            SoundUtil.playSound(soundName, shooter.getEyeLocation(), soundVolume, soundPitch);
        } else if (launchSound != null) {
            shooter.getWorld().playSound(shooter.getEyeLocation(), launchSound, soundVolume, soundPitch);
        } else if (soundString != null) {
            SoundUtil.playSoundString(soundString, shooter.getEyeLocation());
        }
        if (launchParticle != null) {
            launchParticle.display(pr.getLocation());
        } else if (particleString != null) {
            Particle.fromString(particleString).display(pr.getLocation());
        }
        if (randomDirection > 0) {
            EntityUtil.randomDirection(pr, randomDirection);
        }
        return pr;
    }
}
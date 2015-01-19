package com.v3ld1n.util;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

public class ProjectileBuilder {
    Class<? extends Projectile> projectile;
    Particle launchParticle;
    Sound launchSound;
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
     * Add a firing particle
     * @param particle the particle
     * @return this object
     */
    public ProjectileBuilder withLaunchParticle(Particle particle) {
        this.launchParticle = particle;
        return this;
    }

    /**
     * Add a firing sound
     * @param sound the sound
     * @return this object
     */
    public ProjectileBuilder withLaunchSound(Sound sound) {
        this.launchSound = sound;
        return this;
    }

    /**
     * Make the projectile move in a random direction
     * @param distance the distance from the starting direction
     * @return this object
     */
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
        if (launchSound != null) {
            launchSound.play(location);
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
        if (launchParticle != null) {
            launchParticle.display(pr.getLocation());
        }
        if (launchSound != null) {
            launchSound.play(shooter.getEyeLocation());
        }
        if (randomDirection > 0) {
            EntityUtil.randomDirection(pr, randomDirection);
        }
        return pr;
    }
}
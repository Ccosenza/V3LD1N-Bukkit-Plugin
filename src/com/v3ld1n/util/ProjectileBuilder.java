package com.v3ld1n.util;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;

public class ProjectileBuilder {
    private Class<? extends Projectile> projectile;
    private double speed = 1;
    private List<Particle> launchParticles;
    private List<Sound> launchSounds;
    private double randomDirection;

    public ProjectileBuilder(Class<? extends Projectile> type) {
        this.projectile = type;
    }

    /**
     * Set the type of the projectile
     * @param type the projectile class
     * @return this object
     */
    public ProjectileBuilder setType(Class<? extends Projectile> type) {
        this.projectile = type;
        return this;
    }

    public ProjectileBuilder setSpeed(double speed) {
        this.speed = speed;
        return this;
    }

    /**
     * Add a firing particle
     * @param particle the particle
     * @return this object
     */
    public ProjectileBuilder setLaunchParticles(List<Particle> particles) {
        this.launchParticles = particles;
        return this;
    }

    /**
     * Add a firing sound
     * @param sound the sound
     * @return this object
     */
    public ProjectileBuilder setLaunchSounds(List<Sound> sounds) {
        this.launchSounds = sounds;
        return this;
    }

    /**
     * Make the projectile move in a random direction
     * @param distance the distance from the starting direction
     * @return this object
     */
    public ProjectileBuilder setRandomDirection(double distance) {
        this.randomDirection = distance;
        return this;
    }

    /**
     * Make an entity launch the projectile
     * @param shooter the entity shooting the projectile
     * @param speed the speed of the projectile
     * @return the projectile
     */
    public Projectile launch(LivingEntity shooter) {
        Projectile pr = shooter.launchProjectile(projectile);
        pr.setVelocity(shooter.getLocation().getDirection().multiply(speed));
        if (launchParticles != null) {
            Particle.displayList(launchParticles, pr.getLocation());
        }
        if (launchSounds != null) {
            Sound.playList(launchSounds, shooter.getEyeLocation());
        }
        if (randomDirection > 0) {
            EntityUtil.randomDirection(pr, randomDirection);
        }
        return pr;
    }
}
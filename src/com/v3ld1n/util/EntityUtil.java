package com.v3ld1n.util;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.V3LD1N;

public final class EntityUtil {
    private static final Random random = new Random();

    private EntityUtil() {
    }
    
    /**
     * Pushes an entity towards a location
     * @param entity the entity to push
     * @param to the location to push the entity towards
     * @param speed the speed to push the entity
     * @param fromEyeLocation whether the entity is pushed from its eye location instead of its location
     */
    public static void pushAway(Entity entity, Location to, Vector speed, boolean fromEyeLocation) {
        boolean fromEye = entity instanceof LivingEntity && fromEyeLocation;
        Location loc = fromEye ? entity.getLocation() : ((LivingEntity) entity).getEyeLocation();
        Vector direction = loc.toVector().subtract(to.toVector()).normalize();
        direction = direction.multiply(speed);
        entity.setVelocity(direction);
        if (entity instanceof Fireball) {
            ((Fireball) entity).setDirection(direction);
        }
    }

    /**
     * Pushes an entity away from a location
     * @param entity the entity to push
     * @param from the location to push the entity from
     * @param speed the speed to push the entity
     * @param fromEyeLocation whether the entity is pushed from its eye location instead of its location
     */
    public static void pushToward(Entity entity, Location from, Vector speed, boolean fromEyeLocation) {
        Vector nSpeed = speed.multiply(-1);
        pushAway(entity, from, nSpeed, fromEyeLocation);
    }

    /**
     * Randomly changes an entity's direction
     * @param entity the entity to move
     * @param distance the maximum distance from the starting location
     */
    public static void randomDirection(Entity entity, double distance) {
        Vector direction = entity.getLocation().getDirection();
        Vector newDirection;
        direction.add(randomVector(distance));
        newDirection = direction.subtract(randomVector(distance));
        entity.getLocation().setDirection(newDirection);
        Vector velocity = entity.getVelocity();
        Vector newVelocity;
        velocity.add(randomVector(distance));
        newVelocity = velocity.subtract(randomVector(distance));
        entity.setVelocity(newVelocity);
    }

    /**
     * Returns a random vector
     * @param multiplier the multiplier
     * @return the vector
     */
    public static Vector randomVector(double multiplier) {
        Vector rv = new Vector(random.nextDouble(), random.nextDouble(), random.nextDouble());
        rv = rv.multiply(multiplier);
        return rv;
    }
    
    /**
     * Add health to a living entity
     * @param entity the entity to heal
     * @param health the amount of health to heal
     */
    public static void heal(LivingEntity entity, double health) {
        if (entity.getHealth() + health > entity.getMaxHealth()) {
            entity.setHealth(entity.getMaxHealth());
        } else {
            entity.setHealth(entity.getHealth() + health);
        }
    }
    
    /**
     * Launches a firework rocket that explodes 1 tick later
     * @param effect the firework effect
     * @param location the location to launch the rocket at
     * @param delay the number of ticks before detonating the rocket
     */
    public static void displayFireworkEffect(FireworkEffect effect, Location location, long delay) {
        final Firework fw = location.getWorld().spawn(location, Firework.class);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.addEffects(effect);
        fw.setFireworkMeta(fwm);
        Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
            @Override
            public void run() {
                fw.detonate();
            }
        }, delay);
    }

    /**
     * Strikes lightning at a location and destroys the projectile
     * @param projectile the projectile to destroy
     * @param location the location of the lightning strike
     * @param damage whether the lightning should do damage
     */
    public static void detonateLightningProjectile(Projectile projectile, Location location, boolean damage) {
        if (damage) {
            projectile.getWorld().strikeLightning(location);
        } else {
            projectile.getWorld().strikeLightningEffect(location);
        }
        projectile.remove();
    }

    /**
     * Deletes the projectile and spawns an instantly detonating firework rocket at its location
     * @param projectile the projectile
     * @param effect the firework effect
     * @param location the location to spawn the firework
     */
    public static void detonateFireworkProjectile(Projectile projectile, FireworkEffect effect, Location location) {
        displayFireworkEffect(effect, location, 1);
        projectile.remove();
    }

    /**
     * Adds to an entity's velocity
     * @param entity the entity
     * @param velocity the velocity to add
     */
    public static void addVelocity(Entity entity, Vector velocity) {
        Vector newVelocity = entity.getVelocity().add(velocity);
        entity.setVelocity(newVelocity);
    }

    /**
     * Makes an entity "jump" away from a projectile
     * @param entity the jumping entity
     * @param projectile the projectile
     */
    public static void projectileJump(LivingEntity entity, Projectile projectile) {
        double speed = ConfigSetting.PROJECTILE_JUMP_SPEED.getDouble();
        boolean isPlayer = entity.getType() == EntityType.PLAYER;
        boolean sneaking = isPlayer ? ((Player) entity).isSneaking() : false;
        if (sneaking) {
            speed *= 1.25;
            Location loc = entity.getLocation();
            BlockFace down = BlockFace.DOWN;
            Block b = loc.getBlock();
            Block bBelow = b.getRelative(down);
            Block bTwoBelow = bBelow.getRelative(down);
            if (!b.getType().isSolid() && !bBelow.getType().isSolid() && !bTwoBelow.getType().isSolid()) {
                entity.setVelocity(new Vector(0, speed, 0));
                return;
            }
        }
        pushAway(entity, projectile.getLocation(), new Vector(speed, speed, speed), false);
    }
}
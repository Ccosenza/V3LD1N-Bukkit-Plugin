package com.v3ld1n.util;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
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
     * @param speedX the x velocity
     * @param speedY the y velocity
     * @param speedZ the z velocity
     */
    public static void pushToward(Entity entity, Location to, double speedX, double speedY, double speedZ, boolean fromEyeLocation) {
        Location loc = entity.getLocation();
        if (entity instanceof LivingEntity && fromEyeLocation) {
            loc = ((LivingEntity) entity).getEyeLocation();
        }
        Vector direction = loc.toVector().subtract(to.toVector()).normalize();
        direction.setX(direction.getX() * -(speedX))
                .setY(direction.getY() * -(speedY))
                .setZ(direction.getZ() * -(speedZ));
        entity.setVelocity(direction);
        if (entity instanceof Fireball) {
            ((Fireball) entity).setDirection(direction);
        }
    }

    /**
     * Randomly changes an entity's direction
     * @param entity the entity to move
     * @param distance the maximum distance from the starting location
     */
    public static void randomDirection(Entity entity, double distance) {
        Vector direction = entity.getLocation().getDirection();
        Vector newDirection;
        direction.add(new Vector(random.nextDouble() * distance, random.nextDouble() * distance, random.nextDouble() * distance));
        newDirection = direction.subtract(new Vector(random.nextDouble() * distance, random.nextDouble() * distance, random.nextDouble() * distance));
        entity.getLocation().setDirection(newDirection);
        Vector velocity = entity.getVelocity();
        Vector newVelocity;
        velocity.add(new Vector(random.nextDouble() * distance, random.nextDouble() * distance, random.nextDouble() * distance));
        newVelocity = velocity.subtract(new Vector(random.nextDouble() * distance, random.nextDouble() * distance, random.nextDouble() * distance));
        entity.setVelocity(newVelocity);
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
     * Strikes lightning at a location and destroys the projectile
     * @param projectile the projectile to destroy
     * @param location the location of the lightning strike
     */
    public static void detonateLightningProjectile(Projectile projectile, Location location) {
        projectile.getWorld().strikeLightning(location);
        projectile.remove();
    }

    /**
     * Deletes the projectile and spawns an instantly detonating firework rocket at its location
     * @param projectile the projectile
     * @param location the location to spawn the firework
     * @param type the firework type
     * @param color the firework color
     * @param fade the firework fade color
     */
    public static void detonateFireworkProjectile(Projectile projectile, FireworkEffect effect, Location location) {
        displayFireworkEffect(effect, location, 1);
        projectile.remove();
    }

    /**
     * Makes a player "jump" away from a projectile
     * @param entity the jumping player
     * @param projectile the projectile
     * @param speedX the X-axis speed
     * @param speedY the Y-axis speed
     * @param speedZ the Z-axis speed
     */
    public static void projectileJump(LivingEntity entity, Projectile projectile, double speedX, double speedY, double speedZ) {
        boolean sneaking = false;
        if (entity.getType() == EntityType.PLAYER && ((Player) entity).isSneaking()) {
            sneaking = true;
        }
        if (sneaking) {
            double xz = ConfigSetting.PROJECTILE_JUMP_X_Z.getDouble();
            double y = ConfigSetting.PROJECTILE_JUMP_Y.getDouble();
            BlockFace down = BlockFace.DOWN;
            Location loc = entity.getLocation();
            Material block = loc.getBlock().getType();
            Material blockBelow = loc.getBlock().getRelative(down).getType();
            Material blockTwoBelow = loc.getBlock().getRelative(down).getRelative(down).getType();
            Material air = Material.AIR;
            if (block == air && blockBelow != air) {
                pushToward(entity, projectile.getLocation(), -speedX * xz, -speedY * y, -speedZ * xz, true);
            } else if (block == air && blockBelow == air && blockTwoBelow != air) {
                pushToward(entity, projectile.getLocation(), -speedX * xz, -speedY * y, -speedZ * xz, true);
            } else if (block == air && blockBelow == air && blockTwoBelow == air) {
                entity.setVelocity(new Vector(0, speedY * y, 0));
            } else if (block != air) {
                pushToward(entity, projectile.getLocation(), -speedX * xz, -speedY * y, -speedZ * xz, true);
            }
        } else {
            pushToward(entity, projectile.getLocation(), -speedX, -speedY, -speedZ, true);
        }
    }
}
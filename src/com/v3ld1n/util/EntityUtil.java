package com.v3ld1n.util;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import com.v3ld1n.V3LD1N;

public class EntityUtil {
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
    public static void pushToward(Entity entity, Location to, double speedX, double speedY, double speedZ) {
        Location entityLocation;
        if (entity instanceof LivingEntity) {
            entityLocation = ((LivingEntity) entity).getEyeLocation();
        } else {
            entityLocation = entity.getLocation();
        }
        Vector direction = entityLocation.toVector().subtract(to.toVector()).normalize();
        direction.setX(direction.getX()*-(speedX))
                .setY(direction.getY()*-(speedY))
                .setZ(direction.getZ()*-(speedZ));
        entity.setVelocity(direction);
        if (entity instanceof Fireball) {
            ((Fireball) entity).setDirection(direction);
        }
    }

    /**
     * Randomly changes an entity's direction
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
     * Pushes an entity towards a location
     * @param entity the entity to push
     * @param to the location to push the entity towards
     * @param speed the velocity
     */
    public static void pushToward(Entity entity, Location to, Vector speed) {
        Location entityLocation;
        if (entity instanceof LivingEntity) {
            entityLocation = ((LivingEntity) entity).getEyeLocation();
        } else {
            entityLocation = entity.getLocation();
        }
        Vector direction = entityLocation.toVector().subtract(to.toVector()).normalize();
        direction.setX(direction.getX()*-(speed.getX()))
                .setY(direction.getY()*-(speed.getY()))
                .setZ(direction.getZ()*-(speed.getZ()));
        entity.setVelocity(direction);
        if (entity instanceof Fireball) {
            ((Fireball) entity).setDirection(direction);
        }
    }
    
    /**
     * Add health to a living entity
     * @param entity the entity to heal
     * @param health the amount of health to heal
     */
    public static void healEntity(LivingEntity entity, double health) {
        if (entity.getHealth() + health > entity.getMaxHealth()) {
            entity.setHealth(entity.getMaxHealth());
        } else {
            entity.setHealth(entity.getHealth() + health);
        }
    }
    
    /**
     * Launches a firework rocket that explodes 1 tick later
     * @param location the location to launch the rocket at
     * @param type the type of explosion
     * @param color the color of the explosion
     * @param fade the fade color of the explosion
     */
    public static void displayFireworkEffect(Location location, Type type, Color color, Color fade) {
        final Firework fw = location.getWorld().spawn(location, Firework.class);
        FireworkMeta fwm = fw.getFireworkMeta();
        FireworkEffect fwe = FireworkEffect.builder()
                .with(type)
                .withColor(color)
                .withFade(fade)
                .withTrail()
                .withFlicker()
                .build();
        fwm.addEffects(fwe);
        fw.setFireworkMeta(fwm);
        Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
            @Override
            public void run() {
                fw.detonate();
            }
        }, 1L);
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
    public static void detonateFireworkProjectile(Projectile projectile, Location location, Type type, Color color, Color fade) {
        displayFireworkEffect(location, type, color, fade);
        projectile.remove();
    }

    /**
     * Makes a projectile home toward monsters
     * @param projectile the projectile
     * @param ticks the interval in ticks
     */
    public static void homingProjectileTask(final Projectile projectile, long ticks) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(V3LD1N.getPlugin(), new Runnable() {
            @Override
            public void run() {
                Entity e = projectile.getNearbyEntities(25, 25, 25).get(0);
                Vector direction = projectile.getLocation().toVector().subtract(e.getLocation().toVector()).normalize();
                direction.setX(direction.getX()*-(1.0))
                        .setY(direction.getY()*-(1.0))
                        .setZ(direction.getZ()*-(1.0));
                projectile.setVelocity(direction);
            }
        }, ticks, ticks);
    }

    /**
     * Makes a player "jump" away from a projectile
     * @param player the jumping player
     * @param projectile the projectile
     */
    public static void projectileJump(Player player, Projectile projectile, double speedX, double speedY, double speedZ) {
        if (player.isSneaking()) {
            Location loc = player.getLocation();
            Location belowLoc = player.getLocation();
            Location twoBelowLoc = player.getLocation();
            belowLoc.setY(loc.getY() - 1);
            twoBelowLoc.setY(loc.getY() - 2);
            Material locBlock = loc.getBlock().getType();
            Material belowLocBlock = belowLoc.getBlock().getType();
            Material twoBelowLocBlock = twoBelowLoc.getBlock().getType();
            if (locBlock == Material.AIR && belowLocBlock != Material.AIR) {
                pushToward(player, projectile.getLocation(), -speedX * 1.5, -speedY * 1.1, -speedZ * 1.5);
            } else if (locBlock == Material.AIR && belowLocBlock == Material.AIR && twoBelowLocBlock != Material.AIR) {
                pushToward(player, projectile.getLocation(), -speedX * 1.5, -speedY * 1.1, -speedZ * 1.5);
            } else if (locBlock == Material.AIR && belowLocBlock == Material.AIR && twoBelowLocBlock == Material.AIR) {
                player.setVelocity(new Vector(0, speedY * 1.4, 0));
            } else if (locBlock != Material.AIR) {
                pushToward(player, projectile.getLocation(), -speedX * 1.5, -speedY * 1.5, -speedZ * 1.5);
            }
        } else {
            pushToward(player, projectile.getLocation(), -speedX, -speedY, -speedZ);
        }
    }

    /**
     * Makes an entity "jump" away from a projectile
     * @param entity the jumping entity
     * @param projectile the projectile
     */
    public static void projectileJump(LivingEntity entity, Projectile projectile, double speedX, double speedY, double speedZ) {
        Location loc = entity.getLocation();
        Location belowLoc = entity.getLocation();
        Location twoBelowLoc = entity.getLocation();
        belowLoc.setY(loc.getY() - 1);
        twoBelowLoc.setY(loc.getY() - 2);
        Material locBlock = loc.getBlock().getType();
        Material belowLocBlock = belowLoc.getBlock().getType();
        Material twoBelowLocBlock = twoBelowLoc.getBlock().getType();
        entity.setFallDistance(0);
        if (locBlock == Material.AIR && belowLocBlock != Material.AIR) {
            pushToward(entity, projectile.getLocation(), -speedX, -speedY, -speedZ);
        } else if (locBlock == Material.AIR && belowLocBlock == Material.AIR && twoBelowLocBlock != Material.AIR) {
            pushToward(entity, projectile.getLocation(), -speedX, -speedY, -speedZ);
        } else if (locBlock == Material.AIR && belowLocBlock == Material.AIR && twoBelowLocBlock == Material.AIR) {
            entity.setVelocity(new Vector(0, speedY * 1.1, 0));
        } else if (locBlock != Material.AIR) {
            pushToward(entity, projectile.getLocation(), -speedX, -speedY * 1.2, -speedZ);
        }
    }
}
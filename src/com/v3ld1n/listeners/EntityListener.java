package com.v3ld1n.listeners;

import com.v3ld1n.ConfigSetting;

import org.bukkit.entity.Creature;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.v3ld1n.util.PlayerUtil;

public class EntityListener implements Listener {
    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        boolean isPlayer = event.getDamager().getType() == EntityType.PLAYER;

        // Protects item frames from non-player attacks
        if (event.getCause() != DamageCause.ENTITY_ATTACK) {
            if (entity.getType() == EntityType.ITEM_FRAME && isPlayer) {
                event.setCancelled(true);
            }
        }

        // Protects Ender Crystals in WorldGuard regions
        if (entity instanceof EnderCrystal && isPlayer) {
            if (!PlayerUtil.canBuild((Player) event.getDamager(), entity.getLocation())) {
                event.setCancelled(true);
            }
        }

        // Protects players from firework explosions
        if (entity.getType() == EntityType.PLAYER && event.getDamager().getType() == EntityType.FIREWORK) {
            if (ConfigSetting.DISABLE_FIREWORK_DAMAGE.getBoolean()) {
                event.setCancelled(true);
            }
        }
    }

    // Protects non-living entities from lightning
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() == DamageCause.LIGHTNING) {
            if (!(event.getEntity() instanceof Creature)) {
                event.setCancelled(true);
            }
        }
    }

    // Disables mob spawning
    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        Entity creature = event.getEntity();
        String worldName = creature.getWorld().getName();

        if (ConfigSetting.CANCEL_SPAWN_WORLDS.getList().contains(worldName)) {
            if (creature.getType() != EntityType.ARMOR_STAND) {
                event.setCancelled(true);
            }
        }
    }

    // Removes projectiles on hit
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity projectile = event.getEntity();
        String worldName = projectile.getWorld().getName();

        if (ConfigSetting.REMOVE_ARROWS_WORLDS.getList().contains(worldName)) {
            projectile.remove();
        }
    }

    // Disables item dropping
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        String worldName = entity.getWorld().getName();

        if (ConfigSetting.CANCEL_DROP_WORLDS.getList().contains(worldName)) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
    }
}
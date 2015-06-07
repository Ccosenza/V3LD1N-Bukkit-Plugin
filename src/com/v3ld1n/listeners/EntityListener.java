package com.v3ld1n.listeners;

import com.v3ld1n.ConfigSetting;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EnderCrystal;
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

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.v3ld1n.V3LD1N;

public class EntityListener implements Listener {
    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getCause() != DamageCause.ENTITY_ATTACK) {
            if (event.getEntity().getType() == EntityType.ITEM_FRAME) {
                if (!(event.getDamager() instanceof Player)) {
                    event.setCancelled(true);
                }
            }
        }
        if (event.getEntity() instanceof EnderCrystal && event.getDamager() instanceof Player) {
            if (V3LD1N.getWorldGuard() != null) {
                WorldGuardPlugin wg = V3LD1N.getWorldGuard();
                if (wg.isEnabled()) {
                    if (!wg.canBuild((Player) event.getDamager(), event.getEntity().getLocation())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() == DamageCause.LIGHTNING) {
            if (!(event.getEntity() instanceof Creature)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        if (ConfigSetting.CANCEL_SPAWN_WORLDS.getList().contains(event.getEntity().getWorld().getName())) {
            if (event.getEntityType() != EntityType.ARMOR_STAND) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (ConfigSetting.REMOVE_ARROW_WORLDS.getList().contains(event.getEntity().getWorld().getName())) {
            if (event.getEntity() != null) {
                event.getEntity().remove();
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (ConfigSetting.CANCEL_DROP_WORLDS.getList().contains(event.getEntity().getWorld().getName())) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
    }
}
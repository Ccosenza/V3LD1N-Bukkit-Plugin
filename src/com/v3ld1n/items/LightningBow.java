package com.v3ld1n.items;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.v3ld1n.util.EntityUtil;

public class LightningBow extends V3LD1NItem {
    public LightningBow() {
        super("lightning-bow");
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (!projectileIsValid(projectile, EntityType.ARROW)) return;

        strikeLightning(projectile);
    }

    /**
     * Creates lightning where the projectile hits
     * @param projectile the projectile
     */
    private void strikeLightning(Projectile projectile) {
        boolean damage = settings.getBoolean("lightning-damage");
        Location location = projectile.getLocation();
        EntityUtil.detonateLightningProjectile(projectile, location, damage);
    }
}
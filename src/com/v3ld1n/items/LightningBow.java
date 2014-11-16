package com.v3ld1n.items;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
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
        Projectile pr = event.getEntity();
        if (pr.getType() == EntityType.ARROW && pr.getShooter() instanceof Player) {
            Player shooter = (Player) pr.getShooter();
            if (this.equalsItem(shooter.getItemInHand())) {
                EntityUtil.detonateLightningProjectile(pr, pr.getLocation(), this.getBooleanSetting("lightning-damage"));
            }
        }
    }
}
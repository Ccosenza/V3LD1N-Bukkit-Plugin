package com.v3ld1n.items.ratchet;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.Particle;

public class RatchetBoots extends V3LD1NItem {
    public RatchetBoots() {
        super("ratchets-boots");
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Player p = (Player) event.getEntity();
            if (this.equalsItem(p.getInventory().getBoots())) {
                if (event.getCause() == DamageCause.FALL) {
                    double fallDamage = event.getDamage();
                    event.setCancelled(true);
                    this.displayParticles(p.getLocation());
                    damage(p, fallDamage);
                }
            }
        }
    }

    private void damage(Player p, double damage) {
        double r = this.getDoubleSetting("radius");
        for (Entity entity : p.getNearbyEntities(r, r, r)) {
            if (entity instanceof Monster) {
                Monster monster = (Monster) entity;
                monster.damage(damage * this.getDoubleSetting("damage-multiplier"));
                Particle.displayList(monster.getEyeLocation(), this.getStringListSetting("damage-particles"));
            }
        }
    }
}
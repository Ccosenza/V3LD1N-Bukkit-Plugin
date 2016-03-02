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
        if (event.getEntityType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        if (!equalsItem(player.getInventory().getBoots())) return;
        if (event.getCause() != DamageCause.FALL) return;

        event.setCancelled(true);
        displayParticles(player.getLocation());
        double fallDamage = event.getDamage();
        damageNearbyEntities(player, fallDamage);
    }

    /**
     * Damages entities near the player
     * @param player the player
     * @param damage the base damage
     */
    private void damageNearbyEntities(Player player, double damage) {
        double radius = settings.getDouble("radius");
        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (!(entity instanceof Monster)) continue;

            Monster monster = (Monster) entity;
            monster.damage(damage * settings.getDouble("damage-multiplier"));
            Particle.displayList(settings.getParticles("damage-particles"), monster.getEyeLocation());
        }
    }
}
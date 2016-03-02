package com.v3ld1n.items.ratchet;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityCombustEvent;

import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.Particle;

public class RatchetChestplate extends V3LD1NItem {
    public RatchetChestplate() {
        super("ratchets-chestplate");
    }

    @EventHandler
    public void onCombust(EntityCombustEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        if (!equalsItem(player.getInventory().getChestplate())) return;

        event.setCancelled(true);
        Location chestplateLocation = player.getEyeLocation();
        chestplateLocation.setY(chestplateLocation.getY() - 0.3);

        List<Particle> randomParticles = particles;

        // Adds a random speed to the particles
        for (Particle particle : randomParticles) {
            particle.setSpeed(particle.getSpeed() + random.nextFloat());
            particle.display(chestplateLocation);
        }
    }
}
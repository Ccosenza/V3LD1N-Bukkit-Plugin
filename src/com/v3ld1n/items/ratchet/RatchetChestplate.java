package com.v3ld1n.items.ratchet;

import java.util.List;

import org.bukkit.Location;
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
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            Location loc = p.getEyeLocation();
            loc.setY(loc.getY() - 0.3);
            if (this.equalsItem(p.getInventory().getChestplate())) {
                event.setCancelled(true);
                List<String> setting = this.getStringListSetting("particles");
                List<Particle> particles = Particle.fromList(setting);
                for (Particle particle : particles) {
                    particle.setSpeed(particle.getSpeed() + random.nextFloat());
                    particle.display(loc);
                }
            }
        }
    }
}
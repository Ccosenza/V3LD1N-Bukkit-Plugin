package com.v3ld1n.items;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.v3ld1n.util.Particle;
import com.v3ld1n.util.Sound;

public class FlightFeather extends V3LD1NItem {
    public FlightFeather() {
        super("flight-feather");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        Action a = event.getAction();
        if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
            if (this.equalsItem(p.getItemInHand())) {
                event.setCancelled(true);
                p.setVelocity(p.getLocation().getDirection().divide(new Vector(2, 2, 2)).add(this.getVectorSetting("velocity")));
                p.setFallDistance(0);
                Particle.fromString(this.getStringSetting("particle")).display(p.getLocation());
                Sound.fromString(this.getStringSetting("sound")).play(p.getLocation());
            }
        }
    }
}
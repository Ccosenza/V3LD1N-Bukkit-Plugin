package com.v3ld1n.items;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class FlightFeather extends V3LD1NItem {
    public FlightFeather() {
        super("flight-feather");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        Action a = event.getAction();
        if (useActions.contains(a)) {
            if (this.equalsItem(p.getItemInHand())) {
                event.setCancelled(true);
                use(p);
            }
        }
    }

    private void use(Player p) {
        Vector setting = this.getVectorSetting("velocity");
        Vector velocity = p.getLocation().getDirection().divide(new Vector(2, 2, 2)).add(setting);
        p.setVelocity(velocity);
        p.setFallDistance(0);
        Location loc = p.getLocation();
        this.displayParticles(loc);
        this.getSoundSetting("sound").play(p.getLocation());
    }
}
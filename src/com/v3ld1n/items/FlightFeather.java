package com.v3ld1n.items;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class FlightFeather extends V3LD1NItem {
    public FlightFeather() {
        super("flight-feather");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!entityIsHoldingItem(player)) return;
        if (!isRightClick(event.getAction(), event.getHand())) return;

        event.setCancelled(true);
        use(player);
    }

    /**
     * Pushes the player forward
     * @param player the player
     */
    private void use(Player player) {
        Location playerLocation = player.getLocation();
        Vector setting = settings.getVector("velocity");
        Vector playerDirection = playerLocation.getDirection();
        Vector velocity = playerDirection.divide(new Vector(2, 2, 2)).add(setting);

        player.setVelocity(velocity);
        player.setFallDistance(0);
        displayParticles(playerLocation);
        playSounds(playerLocation);
    }
}
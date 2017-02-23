package com.v3ld1n.items.ratchet;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import com.v3ld1n.items.V3LD1NItem;

public class RatchetTotem extends V3LD1NItem {
    public RatchetTotem() {
        super("ratchets-totem");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!entityIsHoldingItem(player)) return;
        if (!isRightClick(event.getAction(), event.getHand())) return;

        push(player);
    }

    private void push(Player player) {
        player.setVelocity(player.getVelocity().setY(settings.getDouble("speed")));
        displayParticles(player.getLocation());
        playSounds(player.getLocation());
    }
}
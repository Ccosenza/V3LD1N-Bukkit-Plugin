package com.v3ld1n.items.ratchet;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.v3ld1n.items.V3LD1NItem;

public class RatchetHelmet extends V3LD1NItem {
    public RatchetHelmet() {
        super("ratchets-helmet");
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        HumanEntity p = event.getEntity();
        Player player = (Player) p;
        if (this.equalsItem(p.getInventory().getHelmet())) {
            if (event.getFoodLevel() < player.getFoodLevel()) {
                event.setCancelled(true);
            }
        }
    }
}
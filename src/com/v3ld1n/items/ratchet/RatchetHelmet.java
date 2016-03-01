package com.v3ld1n.items.ratchet;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.v3ld1n.items.V3LD1NItem;

public class RatchetHelmet extends V3LD1NItem {
    public RatchetHelmet() {
        super("ratchets-helmet");
    }

    // Prevents hunger loss
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        if (!equalsItem(player.getInventory().getHelmet())) return;

        boolean hungerIsIncreasing = event.getFoodLevel() >= player.getFoodLevel();
        if (hungerIsIncreasing) return;

        event.setCancelled(true);
    }

    // Multiplies health regeneration
    @EventHandler
    public void onHealthRegen(EntityRegainHealthEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        if (!equalsItem(player.getInventory().getHelmet())) return;

        double amount = event.getAmount();
        double multiplier = settings.getDouble("health-multiplier");
        event.setAmount(amount * multiplier);
    }
}
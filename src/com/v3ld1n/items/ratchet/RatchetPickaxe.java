package com.v3ld1n.items.ratchet;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.ItemUtil;

public class RatchetPickaxe extends V3LD1NItem {
    public RatchetPickaxe() {
        super("ratchets-pickaxe");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!entityIsHoldingItem(player)) return;
        if (!isRightClick(event.getAction())) return;

        smeltItems(player);
    }

    private void smeltItems(Player player) {
        double radius = settings.getDouble("radius");

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity.getType() != EntityType.DROPPED_ITEM) return;

            Item item = (Item) entity;
            ItemStack itemStack = item.getItemStack();

            ItemStack smeltedItem = ItemUtil.smelt(itemStack);
            item.setItemStack(smeltedItem);

            if (itemStack.equals(smeltedItem)) continue;
            this.displayParticles(item.getLocation());
            this.playSounds(item.getLocation());
        }
    }
}
package com.v3ld1n.items.ratchet;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import com.v3ld1n.items.V3LD1NItem;

public class RatchetPickaxe extends V3LD1NItem {
    public RatchetPickaxe() {
        super("ratchets-pickaxe");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (this.equalsItem(event.getPlayer().getItemInHand())) {
            this.getParticleSetting("particle").display(event.getBlock().getLocation());
        }
    }
}
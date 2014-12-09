package com.v3ld1n.items.ratchet;

import org.bukkit.Location;
import org.bukkit.block.Block;
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
            Block block = event.getBlock();
            Location loc = new Location(block.getWorld(), block.getLocation().getX() + 0.5, block.getLocation().getY() + 0.5, block.getLocation().getZ() + 0.5);
            this.getParticleSetting("particle").display(loc);
        }
    }
}
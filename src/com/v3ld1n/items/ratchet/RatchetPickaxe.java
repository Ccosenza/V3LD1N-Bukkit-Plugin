package com.v3ld1n.items.ratchet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import com.v3ld1n.V3LD1N;
import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.ItemUtil;
import com.v3ld1n.util.WorldUtil;

public class RatchetPickaxe extends V3LD1NItem {
    public RatchetPickaxe() {
        super("ratchets-pickaxe");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (this.equalsItem(event.getPlayer().getItemInHand())) {
            Block block = event.getBlock();
            Location bl = block.getLocation();
            double x = bl.getX() + 0.5;
            double y = bl.getY() + 0.5;
            double z = bl.getZ() + 0.5;
            final Location loc = new Location(block.getWorld(), x, y, z);
            this.displayParticles(loc);
            Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    for (Entity entity : WorldUtil.getNearbyEntities(loc, getDoubleSetting("radius"))) {
                        if (entity.getType() == EntityType.DROPPED_ITEM) {
                            Item item = (Item) entity;
                            ItemUtil.smelt(item.getItemStack());
                        }
                    }
                }
            }, 1);
        }
    }
}
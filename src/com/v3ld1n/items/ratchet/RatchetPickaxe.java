package com.v3ld1n.items.ratchet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.V3LD1N;
import com.v3ld1n.items.V3LD1NItem;
import com.v3ld1n.util.ItemUtil;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.WorldUtil;

public class RatchetPickaxe extends V3LD1NItem {
    public RatchetPickaxe() {
        super("ratchets-pickaxe");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (this.equalsItem(event.getPlayer().getItemInHand())) {
            Block block = event.getBlock();
            final Location loc = new Location(block.getWorld(), block.getLocation().getX() + 0.5, block.getLocation().getY() + 0.5, block.getLocation().getZ() + 0.5);
            for (String particle : this.getStringListSetting("particles")) {
                Particle.fromString(particle).display(loc);
            }
            Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    for (Entity entity : WorldUtil.getNearbyEntities(loc, 0.5)) {
                        if (entity.getType() == EntityType.DROPPED_ITEM) {
                            Item item = (Item) entity;
                            ItemStack smeltedStack = ItemUtil.smelt(item.getItemStack());
                            item.remove();
                            loc.getWorld().dropItemNaturally(loc, smeltedStack);
                        }
                    }
                }
            }, 1);
        }
    }
}
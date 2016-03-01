package com.v3ld1n.items.ratchet;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
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
        Player player = event.getPlayer();
        if (!entityIsHoldingItem(player)) return;

        Block block = event.getBlock();
        smeltDrops(block);
    }

    private void smeltDrops(Block block) {
        Location blockLocation = block.getLocation();
        double x = blockLocation.getX() + 0.5;
        double y = blockLocation.getY() + 0.5;
        double z = blockLocation.getZ() + 0.5;
        final Location blockCenter = new Location(block.getWorld(), x, y, z);

        this.displayParticles(blockCenter);

        Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
            @Override
            public void run() {
                double radius = settings.getDouble("radius");
                List<Entity> nearbyEntities = WorldUtil.getNearbyEntities(blockCenter, radius);
                for (Entity entity : nearbyEntities) {
                    if (entity.getType() != EntityType.DROPPED_ITEM) return;
                    Item item = (Item) entity;
                    ItemUtil.smelt(item.getItemStack());
                }
            }
        }, 1);
    }
}
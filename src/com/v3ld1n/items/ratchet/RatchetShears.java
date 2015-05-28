package com.v3ld1n.items.ratchet;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;

import com.v3ld1n.V3LD1N;
import com.v3ld1n.items.V3LD1NItem;

public class RatchetShears extends V3LD1NItem {
    public RatchetShears() {
        super("ratchets-shears");
    }

    @EventHandler
    public void onShear(PlayerShearEntityEvent event) {
        Player p = event.getPlayer();
        if (event.getEntity().getType() == EntityType.SHEEP) {
            Sheep sheep = (Sheep) event.getEntity();
            if (this.equalsItem(p.getItemInHand())) {
                shear(sheep);
            }
        }
    }

    private void shear(final Sheep sheep) {
        DyeColor color = sheep.getColor();
        Wool wool = new Wool(color);
        ItemStack drops = wool.toItemStack(this.getIntSetting("drop-count"));
        sheep.getWorld().dropItemNaturally(sheep.getLocation(), drops);
        this.displayParticles(sheep.getEyeLocation());
        Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
            @Override
            public void run() {
                sheep.setSheared(false);
            }
        }, 1200L);
    }
}
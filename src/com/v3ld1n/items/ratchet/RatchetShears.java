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
            final Sheep sheep = (Sheep) event.getEntity();
            DyeColor sheepcolor = sheep.getColor();
            if (this.equalsItem(p.getItemInHand())) {
                Wool wool = new Wool(sheepcolor);
                ItemStack drops = wool.toItemStack(this.getIntSetting("drop-count"));
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), drops);
                this.getParticleSetting("particle").display(sheep.getEyeLocation());
                Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable(){
                    @Override
                    public void run() {
                        sheep.setSheared(false);
                    }
                }, 1200L);
            }
        }
    }
}
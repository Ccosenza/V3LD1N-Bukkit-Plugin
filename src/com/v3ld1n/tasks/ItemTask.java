package com.v3ld1n.tasks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.Config;
import com.v3ld1n.util.ConfigUtil;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.WorldUtil;

public class ItemTask extends Task {
    public ItemTask(String name) {
        super(name, Config.TASKS_ITEM);
    }

    @Override
    public void run() {
        Location location = this.getLocationSetting("location");
        String runMode = this.getStringSetting("runmode");
        double radius = this.getDoubleSetting("radius");
        List<ItemStack> giveItems = new ArrayList<>();

        List<ItemStack> items = new ArrayList<>();
        for (String itemString : this.getStringListSetting("items")) {
            items.add(ConfigUtil.itemFromString(itemString));
        }

        List<Particle> particles = new ArrayList<>();
        for (String particleString : this.getStringListSetting("particles")) {
            particles.add(Particle.fromString(particleString));
        }

        if (runMode.equalsIgnoreCase("random")) {
            giveItems.add(items.get(random.nextInt(items.size())));
        } else if (runMode.equalsIgnoreCase("all")) {
            giveItems = items;
        }
        ItemStack[] giveItemsArray = giveItems.toArray(new ItemStack[giveItems.size()]);
        for (Player p : WorldUtil.getNearbyPlayers(location, radius)) {
            p.getInventory().addItem(giveItemsArray);
            for (Particle particle : particles) {
                particle.display(location);
            }
        }
    }
}
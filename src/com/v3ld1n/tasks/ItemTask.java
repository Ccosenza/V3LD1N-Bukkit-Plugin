package com.v3ld1n.tasks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.util.Particle;
import com.v3ld1n.util.WorldUtil;

public class ItemTask extends Task {
    final List<ItemStack> items;
    final double radius;
    final List<Particle> particles;

    public ItemTask(String name, long ticks, String runMode, Location location, List<ItemStack> items, double radius, List<Particle> particles) {
        super(name, ticks, runMode, location);
        this.items = items;
        this.radius = radius;
        this.particles = particles;
    }

    @Override
    public void run() {
        List<ItemStack> giveItems = new ArrayList<>();
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

    public List<ItemStack> getItems() {
        return items;
    }

    public double getRadius() {
        return radius;
    }
}
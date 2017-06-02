package com.v3ld1n.tasks;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

import com.v3ld1n.Config;
import com.v3ld1n.util.LocationUtil;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.Sound;

public class AdvancementTask extends Task {
    public AdvancementTask(String name) {
        super(name, Config.TASKS_ADVANCEMENT);
    }

    @Override
    public void run() {
        Location locationMin = this.getLocationSetting("location-min");
        Location locationMax = this.getLocationSetting("location-max");
        String advancement = this.getStringSetting("advancement");

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            Location playerLocation = player.getLocation();
            if (LocationUtil.isInArea(player.getLocation(), locationMin, locationMax)) {
                grantAdvancement(player, advancement);

                for (String particle : this.getStringListSetting("particles")) {
                    Particle.fromString(particle).display(playerLocation);
                }
                for (String sound : this.getStringListSetting("sounds")) {
                    Sound.fromString(sound).play(playerLocation);
                }
            }
        }
    }
    
    public static void grantAdvancement(Player player, String advancementId) {
        Iterator<Advancement> iter = Bukkit.getServer().advancementIterator();
        while (iter.hasNext()) {
            Advancement advancement = iter.next();
            if (advancement.getKey().getKey().equalsIgnoreCase(advancementId)) {
                AdvancementProgress progress = player.getAdvancementProgress(advancement);
                progress.awardCriteria("impossible");
            }
        }
    }
}
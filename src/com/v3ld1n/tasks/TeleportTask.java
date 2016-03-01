package com.v3ld1n.tasks;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.v3ld1n.Config;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.PlayerAnimation;
import com.v3ld1n.util.Sound;
import com.v3ld1n.util.WorldUtil;

public class TeleportTask extends Task {
    public TeleportTask(String name) {
        super(name, Config.TASKS_TELEPORT);
    }

    @Override
    public void run() {
        Location location = this.getLocationSetting("location");
        double radius = this.getDoubleSetting("radius");
        Location teleportLoc = this.getLocationSetting("teleport-location");
        Location particleLocation = null;
        boolean playAnimation = false;

        if (this.getSetting("particle-location") != null) {
            particleLocation = this.getLocationSetting("particle-location");
        } else {
            particleLocation = location;
        }

        if (this.getBooleanSetting("animation")) {
            playAnimation = true;
        }

        List<Player> players = WorldUtil.getNearbyPlayers(location, radius);
        for (Player player : players) {
            player.teleport(teleportLoc);
            if (playAnimation) {
                PlayerAnimation.BED_LEAVE.playToPlayer(player);
            }
            for (String particle : this.getStringListSetting("particles")) {
                Particle.fromString(particle).display(particleLocation);
            }
            for (String particle : this.getStringListSetting("teleport-location-particles")) {
                Particle.fromString(particle).display(teleportLoc);
            }
            for (String sound : this.getStringListSetting("sounds")) {
                Sound.fromString(sound).play(location);
            }
            for (String sound : this.getStringListSetting("teleport-location-sounds")) {
                Sound.fromString(sound).play(teleportLoc);
            }
        }
    }
}
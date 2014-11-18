package com.v3ld1n.tasks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.v3ld1n.Config;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.PlayerAnimation;
import com.v3ld1n.util.SoundUtil;
import com.v3ld1n.util.WorldUtil;

public class TeleportTask extends Task {
    public TeleportTask(String name) {
        super(name, Config.TASKS_TELEPORT);
    }

    @Override
    public void run() {
        Location location = this.getLocationSetting("location");
        double radius = this.getDoubleSetting("radius");
        Location teleportTo = this.getLocationSetting("teleport-location");
        Location particleLocation = null;
        boolean playAnimation = false;

        if (this.getLocationSetting("particle-location") != null) {
            particleLocation = this.getLocationSetting("particle-location");
        } else {
            particleLocation = location;
        }

        if (this.getBooleanSetting("animation")) {
            playAnimation = true;
        }

        List<Particle> particles = new ArrayList<>();
        for (String particleString : this.getStringListSetting("particles")) {
            particles.add(Particle.fromString(particleString));
        }

        List<Player> players = WorldUtil.getNearbyPlayers(location, radius);
        for (Player player : players) {
            player.teleport(teleportTo);
            if (playAnimation) {
                PlayerAnimation.BED_LEAVE.playToPlayer(player);
            }
            for (String particle : this.getStringListSetting("particles")) {
                Particle.fromString(particle).display(particleLocation);
            }
            for (String sound : this.getStringListSetting("sounds")) {
                SoundUtil.playSoundString(sound, location);
            }
        }
    }
}
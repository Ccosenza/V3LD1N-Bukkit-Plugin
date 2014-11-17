package com.v3ld1n.tasks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.v3ld1n.Config;
import com.v3ld1n.util.Particle;
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
        Location particleLocation = this.getLocationSetting("particle-location");

        List<Particle> particles = new ArrayList<>();
        for (String particleString : this.getStringListSetting("particles")) {
            particles.add(Particle.fromString(particleString));
        }

        List<Player> players = WorldUtil.getNearbyPlayers(location, radius);
        for (Player player : players) {
            player.teleport(teleportTo);
            for (String particle : this.getStringListSetting("particles")) {
                Particle.fromString(particle).display(particleLocation);
            }
            for (String sound : this.getStringListSetting("sounds")) {
                SoundUtil.playSoundString(sound, location);
            }
        }
    }
}
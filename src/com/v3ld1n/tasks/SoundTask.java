package com.v3ld1n.tasks;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.v3ld1n.util.BlockUtil;
import com.v3ld1n.util.SoundUtil;

public class SoundTask extends Task {
    private String currentSound;
    private final List<String> sounds;
    private final int distance;
    private final Location signLoc;
    private final int signLine;
    private final ChatColor signColor;

    public SoundTask(String name, long ticks, String runMode, Location location, List<String> sounds, int distance, Location signLocation, int signLine, ChatColor signColor) {
        super(name, ticks, runMode, location);
        this.sounds = sounds;
        this.distance = distance;
        this.signLoc = signLocation;
        this.signLine = signLine;
        this.signColor = signColor;
    }

    @Override
    public void run() {
        List<String> soundList = sounds;
        if (soundList.contains(currentSound)) {
            soundList.remove(currentSound);
        }
        currentSound = sounds.get(random.nextInt(sounds.size()));
        String[] split = currentSound.split("\\|");
        String[] idSplit = split[0].split("\\.");
        String soundName = idSplit[idSplit.length - 1];
        BlockUtil.editSign(signLoc.getBlock(), signLine, signColor + soundName);
        for (Player p : location.getWorld().getPlayers()) {
            if (distance > -1) {
                SoundUtil.playSoundString(currentSound, location);
            } else {
                if (p.getLocation().distance(location) <= distance) {
                    SoundUtil.playSoundString(currentSound, location);
                }
            }
        }
    }

    public List<String> getSounds() {
        return sounds;
    }

    public int getDistance() {
        return distance;
    }

    public Location getSignLocation() {
        return signLoc;
    }

    public int getSignLine() {
        return signLine;
    }

    public ChatColor getSignColor() {
        return signColor;
    }
}
package com.v3ld1n.tasks;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.v3ld1n.Config;
import com.v3ld1n.util.BlockUtil;
import com.v3ld1n.util.SoundUtil;

public class SoundTask extends Task {
    private String currentSound;

    public SoundTask(String name) {
        super(name, Config.TASKS_SOUND);
    }

    @Override
    public void run() {
        Location location = this.getLocationSetting("location");
        double distance = this.getDoubleSetting("distance");
        Location signLoc = this.getLocationSetting("sign-location");
        int signLine = this.getIntSetting("sign-line");
        ChatColor signColor = ChatColor.valueOf(this.getStringSetting("sign-color"));

        List<String> soundList = this.getStringListSetting("sounds");
        if (soundList.contains(currentSound)) {
            soundList.remove(currentSound);
        }

        currentSound = soundList.get(random.nextInt(soundList.size()));
        String soundName = nameOf(currentSound);
        BlockUtil.editSign(signLoc.getBlock(), signLine, signColor + soundName);
        for (Player p : location.getWorld().getPlayers()) {
            if (distance < 0) {
                SoundUtil.playSoundString(currentSound, location);
            } else {
                if (p.getLocation().distance(location) <= distance) {
                    SoundUtil.playSoundString(currentSound, location);
                }
            }
        }
    }

    public String getCurrentSoundName() {
        return nameOf(currentSound);
    }

    private String nameOf(String sound) {
        String[] split = sound.split("\\|");
        String[] idSplit = split[0].split("\\.");
        String soundName = idSplit[idSplit.length - 1];
        return soundName;
    }
}
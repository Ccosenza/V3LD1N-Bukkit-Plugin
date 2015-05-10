package com.v3ld1n.tasks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.v3ld1n.Config;
import com.v3ld1n.util.BlockUtil;
import com.v3ld1n.util.ConfigUtil;
import com.v3ld1n.util.Sound;
import com.v3ld1n.util.TimeUtil;

public class SoundTask extends Task {
    private String currentSound;
    private long nextTime;
    private List<String> signs;

    public SoundTask(String name) {
        super(name, Config.TASKS_SOUND);
        signs = this.getStringListSetting("signs");
        nextTime = TimeUtil.getTime() + TimeUtil.ticksToMillis(this.getLongSetting("ticks"));
    }

    @Override
    public void run() {
        Location location = this.getLocationSetting("location");
        double distance = this.getDoubleSetting("distance");
        signs = this.getStringListSetting("signs");
        int signLine = this.getIntSetting("sign-line");
        ChatColor signColor = ChatColor.valueOf(this.getStringSetting("sign-color"));
        nextTime = TimeUtil.getTime() + TimeUtil.ticksToMillis(this.getLongSetting("ticks"));

        List<String> soundList = this.getStringListSetting("sounds");
        if (soundList.size() > 1 && soundList.contains(currentSound)) {    
            soundList.remove(currentSound);
        }

        currentSound = soundList.get(random.nextInt(soundList.size()));
        String soundName = nameOf(currentSound);
        for (String sign : signs) {
            BlockUtil.editSign(ConfigUtil.locationFromString(sign).getBlock(), signLine, signColor + soundName);
        }
        for (Player p : location.getWorld().getPlayers()) {
            if (distance < 0) {
                Sound.fromString(currentSound).play(location);
            } else {
                if (p.getLocation().distance(location) <= distance) {
                    Sound.fromString(currentSound).play(location, p);
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

    public List<Sign> getSigns() {
        List<Sign> s = new ArrayList<>();
        for (String sign : signs) {
            Block block = ConfigUtil.locationFromString(sign).getBlock();
            if (block.getState() != null && block.getState() instanceof Sign) {
                s.add((Sign) block.getState());
            }
        }
        return s;
    }

    public long getNextTime() {
        return nextTime;
    }
}
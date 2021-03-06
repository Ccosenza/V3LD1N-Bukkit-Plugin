package com.v3ld1n.tasks;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;

import com.v3ld1n.Config;
import com.v3ld1n.util.ConfigUtil;

public abstract class Task {
    private final String name;
    private final Config config;
    protected Random random = new Random();

    public Task(String name, Config config) {
        this.name = name;
        this.config = config;
    }

    abstract void run();

    public String getName() {
        return name;
    }

    public Config getConfig() {
        return config;
    }

    public Object getSetting(String settingName) {
        return config.getConfig().get(name + "." + settingName);
    }

    public String getStringSetting(String settingName) {
        return config.getConfig().getString(name + "." + settingName);
    }

    public int getIntSetting(String settingName) {
        return config.getConfig().getInt(name + "." + settingName);
    }

    public long getLongSetting(String settingName) {
        return config.getConfig().getLong(name + "." + settingName);
    }

    public double getDoubleSetting(String settingName) {
        return config.getConfig().getDouble(name + "." + settingName);
    }

    public boolean getBooleanSetting(String settingName) {
        return config.getConfig().getBoolean(name + "." + settingName);
    }

    public List<String> getStringListSetting(String settingName) {
        return config.getConfig().getStringList(name + "." + settingName);
    }

    public Location getLocationSetting(String settingName) {
        String setting = config.getConfig().getString(name + "." + settingName);
        return ConfigUtil.locationFromString(setting);
    }

    @Override
    public String toString() {
        return name + "(" + this.getIntSetting("ticks") + " ticks)";
    }
}
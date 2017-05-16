package com.v3ld1n;

import org.bukkit.configuration.file.FileConfiguration;

import com.v3ld1n.util.ConfigAccessor;

public enum Config {
    CHANGELOG("changelog.yml"),
    CONFIG("config.yml"),
    FAQ("faq.yml"),
    ITEMS("items.yml"),
    MESSAGES("messages.yml"),
    MOTD("motd.yml"),
    PARTICLES("particles.yml"),
    PLAYER_DATA("player-data.yml"),
    SIGNS("signs.yml"),
    TASKS_ITEM("tasks-item.yml"),
    TASKS_PARTICLE("tasks-particle.yml"),
    TASKS_SOUND("tasks-sound.yml"),
    TASKS_TELEPORT("tasks-teleport.yml"),
    WARPS("warps.yml"),
    WORLD_OPTIONS("world-options.yml");

    private final String fileName;

    private Config(String fileName) {
        this.fileName = fileName;
    }

    ConfigAccessor getAccessor() {
        for (ConfigAccessor config : V3LD1N.getConfigs()) {
            if (config.getFileName().equals(fileName)) {
                return config;
            }
        }
        return null;
    }

    public FileConfiguration getConfig() {
        return this.getAccessor().getConfig();
    }

    public void saveConfig() {
        this.getAccessor().saveConfig();
    }

    public String getFileName() {
        return fileName;
    }
}
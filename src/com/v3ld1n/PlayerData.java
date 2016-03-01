package com.v3ld1n;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public enum PlayerData {
    AUTO_RESOURCE_PACK("auto-resource-pack"),
    TRAILS("trails"),
    RATCHETS_BOW("ratchets-bow"),
    FIREWORK_ARROWS("firework-arrows");

    private final String configName;

    private PlayerData(String configName) {
        this.configName = configName;
    }

    public Object get(Player player) {
        return Config.PLAYER_DATA.getConfig().get(configName + "." + player.getUniqueId());
    }

    public String getString(Player player) {
        return Config.PLAYER_DATA.getConfig().getString(configName + "." + player.getUniqueId());
    }

    public int getInt(Player player) {
        return Config.PLAYER_DATA.getConfig().getInt(configName + "." + player.getUniqueId());
    }

    public double getDouble(Player player) {
        return Config.PLAYER_DATA.getConfig().getDouble(configName + "." + player.getUniqueId());
    }

    public boolean getBoolean(Player player) {
        return Config.PLAYER_DATA.getConfig().getBoolean(configName + "." + player.getUniqueId());
    }

    public List<?> getList(Player player) {
        return Config.PLAYER_DATA.getConfig().getList(configName + "." + player.getUniqueId());
    }

    public Vector getVector(Player player) {
        return Config.PLAYER_DATA.getConfig().getVector(configName + "." + player.getUniqueId());
    }

    public void set(Player player, Object value) {
        V3LD1N.getConfig("player-data.yml").getConfig().set(configName + "." + player.getUniqueId(), value);
        V3LD1N.getConfig("player-data.yml").saveConfig();
    }
}
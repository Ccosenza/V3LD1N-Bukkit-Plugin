package com.v3ld1n;

import org.bukkit.entity.Player;

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

    public void set(Player player, Object value) {
        V3LD1N.getConfig("player-data.yml").getConfig().set(configName + "." + player.getUniqueId(), value);
        V3LD1N.getConfig("player-data.yml").saveConfig();
    }
}
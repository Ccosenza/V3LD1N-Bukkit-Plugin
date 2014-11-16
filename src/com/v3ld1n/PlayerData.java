package com.v3ld1n;

import java.util.List;
import java.util.UUID;

import org.bukkit.util.Vector;

public enum PlayerData {
    AUTO_RESOURCE_PACK("auto-resource-pack"),
    TRAILS("trails"),
    RATCHETS_BOW("ratchets-bow"),
    FIREWORK_ARROWS("firework-arrows");

    private final String name;

    private PlayerData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Object get(UUID uuid) {
        return Config.PLAYER_DATA.getConfig().get(name + "." + uuid);
    }

    public String getString(UUID uuid) {
        return Config.PLAYER_DATA.getConfig().getString(name + "." + uuid);
    }

    public int getInt(UUID uuid) {
        return Config.PLAYER_DATA.getConfig().getInt(name + "." + uuid);
    }

    public double getDouble(UUID uuid) {
        return Config.PLAYER_DATA.getConfig().getDouble(name + "." + uuid);
    }

    public boolean getBoolean(UUID uuid) {
        return Config.PLAYER_DATA.getConfig().getBoolean(name + "." + uuid);
    }

    public List getList(UUID uuid) {
        return Config.PLAYER_DATA.getConfig().getList(name + "." + uuid);
    }

    public Vector getVector(UUID uuid) {
        return Config.PLAYER_DATA.getConfig().getVector(name + "." + uuid);
    }

    public void set(UUID uuid, Object value) {
        V3LD1N.getConfig("player-data.yml").getConfig().set(name + "." + uuid, value);
        V3LD1N.getConfig("player-data.yml").saveConfig();
    }
}
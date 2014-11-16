package com.v3ld1n.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.v3ld1n.PlayerData;

public class PlayerUtil {
    private PlayerUtil() {
    }

    public static Player getOnlinePlayer(String name) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Returns whether a player has a trail
     * @param player the player
     * @return whether the player has a trail
     */
    public static boolean hasTrail(Player player) {
        return PlayerData.TRAILS.get(player.getUniqueId()) != null;
    }
}
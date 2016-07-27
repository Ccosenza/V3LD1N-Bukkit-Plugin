package com.v3ld1n.util;

import net.minecraft.server.v1_10_R1.PacketPlayOutGameStateChange;

import org.bukkit.entity.Player;

public enum GameState {
    INVALID_BED(0),
    END_RAIN(1),
    START_RAIN(2),
    CHANGE_GAMEMODE(3),
    LEAVE_END(4),
    DEMO(5),
    ARROW_HIT(6),
    DARKNESS(7),
    SKY_FADE(8),
    ELDER_GUARDIAN(10);

    private final int id;

    private GameState(int id) {
        this.id = id;
    }

    /**
     * Returns the ID of the game state
     * @return the game state ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Plays the game state to a player
     * @param player the player
     * @param value the data value for the game state
     */
    public void play(Player player, float value) {
        PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(id, value);
        PlayerUtil.sendPacket(packet, player);
    }
}
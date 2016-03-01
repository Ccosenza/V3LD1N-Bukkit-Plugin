package com.v3ld1n.util;

import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public enum PlayerAnimation {
    SWING_ARM(0),
    DAMAGE(1),
    BED_LEAVE(2),
    CRIT(4),
    MAGIC_CRIT(5);

    private final int id;

    private PlayerAnimation(int id) {
        this.id = id;
    }

    /**
     * Returns the ID of the animation
     * @return the animation ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Plays the animation to all players in a radius around the player
     * @param player the player to play the animation on
     * @param radius the radius to show the animation in
     */
    public void play(Player player) {
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation(((CraftPlayer) player).getHandle(), id);
        PlayerUtil.sendPacket(packet, player);
        for (Player p : player.getWorld().getPlayers()) {
            PlayerUtil.sendPacket(packet, p);
        }
    }

    /**
     * Plays the animation to a player
     * @param player the player
     */
    public void playToPlayer(Player player) {
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation(((CraftPlayer) player).getHandle(), id);
        PlayerUtil.sendPacket(packet, player);
    }
}
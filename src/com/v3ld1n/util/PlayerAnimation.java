package com.v3ld1n.util;

import net.minecraft.server.v1_8_R2.PacketPlayOutAnimation;

import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
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
    public void play(Player player, double radius) {
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation(((CraftPlayer) player).getHandle(), id);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        for (Entity e : player.getNearbyEntities(radius, radius, radius)) {
            if (e != null && e instanceof Player) {
                Player p = (Player) e;
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    /**
     * Plays the animation to a player
     * @param player the player
     */
    public void playTo(Player player) {
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation(((CraftPlayer) player).getHandle(), id);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
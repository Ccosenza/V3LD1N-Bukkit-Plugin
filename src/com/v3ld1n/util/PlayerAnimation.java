package com.v3ld1n.util;

import net.minecraft.server.v1_7_R4.PacketPlayOutAnimation;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public enum PlayerAnimation {
    SWING_ARM(0),
    DAMAGE(1),
    BED_LEAVE(2),
    FOOD_EAT(3),
    CRIT(4),
    MAGIC_CRIT(5),
    SNEAK(104),
    UNSNEAK(105);

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
    public void playToPlayer(Player player) {
        PacketPlayOutAnimation packet = new PacketPlayOutAnimation(((CraftPlayer) player).getHandle(), id);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
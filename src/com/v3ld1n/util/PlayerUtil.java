package com.v3ld1n.util;

import java.lang.reflect.Field;
import java.util.Random;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.EnumTitleAction;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R1.PacketPlayOutTitle;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.v3ld1n.PlayerData;

public final class PlayerUtil {
    private PlayerUtil() {
    }

    /**
     * Returns a player from a name
     * @param name the player's name
     * @return the player
     */
    public static Player getOnlinePlayer(String name) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Returns a player from a name
     * @param name the player's name
     * @return the player
     */
    public static OfflinePlayer getOfflinePlayer(String name) {
        for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers()) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Returns a random online player
     * @return a random online player
     */
    public static Player getRandomPlayer() {
        if (!Bukkit.getServer().getOnlinePlayers().isEmpty()) {
            int random = new Random().nextInt(Bukkit.getOnlinePlayers().size());
            return (Player) Bukkit.getServer().getOnlinePlayers().toArray()[random];
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

    /**
     * Displays a title to a player
     * @param player the player to display the title to
     * @param title the json title text
     * @param fadeIn ticks to fade in
     * @param stay ticks to stay
     * @param fadeOut ticks to fade out
     */
    public static void displayTitle(Player player, String title, int fadeIn, int stay, int fadeOut) {
        IChatBaseComponent json = ChatSerializer.a(title);
        PacketPlayOutTitle packet = new PacketPlayOutTitle(EnumTitleAction.TITLE, json, fadeIn, stay, fadeOut);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    /**
     * Displays a subtitle to a player
     * @param player the player to display the subtitle to
     * @param subtitle the json subtitle text
     * @param fadeIn ticks to fade in
     * @param stay ticks to stay
     * @param fadeOut ticks to fade out
     * @param emptyTitle whether to display an empty title (used to display only the subtitle)
     */
    public static void displaySubtitle(Player player, String subtitle, int fadeIn, int stay, int fadeOut, boolean emptyTitle) {
        IChatBaseComponent json = ChatSerializer.a(subtitle);
        if (emptyTitle) {
            displayTitle(player, "{text:\"\"}", fadeIn, stay, fadeOut);
        }
        PacketPlayOutTitle packet = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, json, fadeIn, stay, fadeOut);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    /**
     * Sets the player list header and footer for a player
     * @param player the player
     * @param jsonHeader the json header text
     * @param jsonFooter the json footer text
     */
    public static void sendPlayerListHeaderFooter(Player player, String jsonHeader, String jsonFooter) {
        IChatBaseComponent header = ChatSerializer.a(jsonHeader);
        IChatBaseComponent footer = ChatSerializer.a(jsonFooter);
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, header);
            headerField.setAccessible(!headerField.isAccessible());
            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, footer);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception e) {
        }
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    /**
     * Returns a player's ping
     * @param p the player
     * @return the player's ping
     */
    public static int getPing(Player p) {
        return ((CraftPlayer) p).getHandle().ping;
    }
}
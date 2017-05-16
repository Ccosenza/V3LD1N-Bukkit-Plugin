package com.v3ld1n.util;

import java.lang.reflect.Field;
import java.util.Random;

import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.v3ld1n.PlayerData;
import com.v3ld1n.V3LD1N;

public final class PlayerUtil {
    private PlayerUtil() {
    }

    /**
     * Returns an online player from a name
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
     * Returns an offline player from a name
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
        return PlayerData.TRAILS.get(player) != null;
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
            e.printStackTrace();
        }
        sendPacket(packet, player);
    }

    /**
     * Returns a player's ping
     * @param p the player
     * @return the player's ping
     */
    public static int getPing(Player p) {
        if (((CraftPlayer) p).getHandle() != null) {
            return ((CraftPlayer) p).getHandle().ping;
        }
        return 0;
    }

    /**
     * Returns an account's UUID
     * @param username the player's username
     * @param dashes whether to add dashes to the UUID
     * @return the UUID
     */
    public static String getUuid(String username, boolean dashes) {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + username;
        JsonElement element = StringUtil.readJsonFromUrl(url);
        if (element != null) {
            String uuid = element.getAsJsonObject().get("id").toString().replaceAll("\"", "");
            if (dashes) {
                return StringUtil.dashUuid(uuid);
            }
            return uuid;
        }
        return null;
    }

    /**
     * Returns the number of ticks a player has been on the server
     * @param player the player
     * @return the number of ticks
     */
    public static int getTicksPlayed(Player player) {
        return player.getStatistic(Statistic.PLAY_ONE_TICK);
    }

    /**
     * Returns the number of seconds a player has been on the server
     * @param player the player
     * @return the number of seconds
     */
    public static int getSecondsPlayed(Player player) {
        return getTicksPlayed(player) / 20;
    }

    /**
     * Returns the number of minutes a player has been on the server
     * @param player the player
     * @return the number of minutes
     */
    public static int getMinutesPlayed(Player player) {
        return getSecondsPlayed(player) / 60;
    }

    /**
     * Returns the number of hours a player has been on the server
     * @param player the player
     * @return the number of hours
     */
    public static int getHoursPlayed(Player player) {
        return getMinutesPlayed(player) / 60;
    }

    /**
     * Returns the number of days a player has been on the server
     * @param player the player
     * @return the number of days
     */
    public static int getDaysPlayed(Player player) {
        return getHoursPlayed(player) / 24;
    }

    /**
     * Returns the number of weeks a player has been on the server
     * @param player the player
     * @return the number of weeks
     */
    public static int getWeeksPlayed(Player player) {
        return getDaysPlayed(player) / 7;
    }

    /**
     * Sends a packet to a player
     * @param packet the packet
     * @param player the player
     */
    public static void sendPacket(Packet<?> packet, Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    /**
     * Returns whether a player can build in a WorldGuard region
     * @param player the player
     * @param location the location to check if the player can build at
     * @return whether the player can build
     */
    public static boolean canBuild(Player player, Location location) {
        if (V3LD1N.getWorldGuard() == null) return true;

        WorldGuardPlugin wg = V3LD1N.getWorldGuard();
        return wg.canBuild(player, location);
    }

    /**
     * Takes an item from a player, or removes the item stack if there is only one item
     * @param player the player to take the item from
     * @param item the item to take from
     * @param takeAmount the amount of items to take
     */
    public static void takeItem(Player player, ItemStack item, int takeAmount) {
        if (item.getAmount() > takeAmount) {
            item.setAmount(item.getAmount() - takeAmount);
        } else {
            player.getInventory().remove(item);
        }
    }
}
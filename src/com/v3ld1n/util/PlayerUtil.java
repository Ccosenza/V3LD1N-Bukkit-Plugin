package com.v3ld1n.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Random;

import net.minecraft.server.v1_8_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.Packet;
import net.minecraft.server.v1_8_R2.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R2.PacketPlayOutTitle.EnumTitleAction;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.google.gson.JsonElement;
import com.v3ld1n.PlayerData;

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
        sendPacket(packet, player);
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
        sendPacket(packet, player);
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
     * Returns a map of player variables
     * @param player the player
     * @return the player info
     */
    public static HashMap<String, Object> getInfo(Player player) {
        Player p = player.getPlayer();
        HashMap<String, Object> info = new HashMap<>();
        info.put("UUID", p.getUniqueId());
        info.put("Display Name", p.getDisplayName());
        info.put("Online", p.isOnline());
        info.put("Ping", getPing(p));
        info.put("Location", StringUtil.fromLocation(p.getLocation()));
        info.put("Biome", StringUtil.fromEnum(p.getLocation().getBlock().getBiome(), true));
        info.put("Standing on", StringUtil.fromEnum(p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType(), true));
        info.put("Health", p.getHealth());
        info.put("Max Health", p.getMaxHealth());
        info.put("Hunger", p.getFoodLevel());
        info.put("Experience", p.getExp());
        info.put("Op", p.isOp());
        info.put("Banned", p.isBanned());
        info.put("IP Address", p.getAddress());
        info.put("Game Mode", StringUtil.fromEnum(p.getGameMode(), true));
        info.put("Flying", p.isFlying());
        return info;
    }

    /**
     * Returns an account's UUID
     * @param playerName the username
     * @param dashes whether to add dashes to the UUID
     * @return the UUID
     */
    public static String getUuid(String username, boolean dashes) {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + username + "?at=1422921600";
        JsonElement element = StringUtil.readJsonFromUrl(url);
        if (element != null) {
            String uuid = element.getAsJsonObject().get("id").toString().replaceAll("\"", "");
            if (dashes) {
                return StringUtil.dashUUID(uuid);
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
}
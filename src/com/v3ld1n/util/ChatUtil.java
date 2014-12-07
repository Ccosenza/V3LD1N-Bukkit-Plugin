package com.v3ld1n.util;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.v3ld1n.Config;

public class ChatUtil {
    private ChatUtil() {
    }

    /**
     * Sends a message
     * @param to CommandSender to send the message to
     * @param message the message
     * @param type the message type
     */
    public static void sendMessage(CommandSender to, String message, int type) {
        if (to instanceof Player) {
            String jsonMessage = "{\"text\":\"" + message.replaceAll("\"", "\\\\\"") + "\"}";
            IChatBaseComponent chat = ChatSerializer.a(jsonMessage);
            PacketPlayOutChat packet = new PacketPlayOutChat(chat, (byte) type);
            ((CraftPlayer) to).getHandle().playerConnection.sendPacket(packet);
        } else {
            to.sendMessage(message);
        }
    }

    /**
     * Sends a JSON message
     * @param to CommandSender to send the message to
     * @param message the message
     * @param type the message type
     */
    public static void sendJsonMessage(CommandSender to, String message, int type) {
        if (to instanceof Player) {
            IChatBaseComponent chat = ChatSerializer.a(message);
            PacketPlayOutChat packet = new PacketPlayOutChat(chat, (byte) type);
            ((CraftPlayer) to).getHandle().playerConnection.sendPacket(packet);
        } else {
            to.sendMessage(message);
        }
    }

    /**
     * Sends the MOTD to a player
     * @param p the player to send the message to
     */
    public static void displayMotd(Player p) {
        for (String jsonText : Config.MOTD.getConfig().getStringList("lines")) {
            ChatUtil.sendJsonMessage(p, jsonText, 0);
        }
    }
}
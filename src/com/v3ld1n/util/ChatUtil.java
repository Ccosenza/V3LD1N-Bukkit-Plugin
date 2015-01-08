package com.v3ld1n.util;

import java.util.List;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.v3ld1n.Config;
import com.v3ld1n.Message;

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
    public static void sendMotd(Player p) {
        for (String jsonText : Config.MOTD.getConfig().getStringList("lines")) {
            ChatUtil.sendJsonMessage(p, StringUtil.formatText(jsonText), 0);
        }
    }

    public static void sendUnreadReports() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.hasPermission("v3ld1n.report.read")) {
                sendUnreadReports(p);
            }
        }
    }

    public static void sendUnreadReports(Player p) {
        p.sendMessage(String.format(Message.REPORT_UNREAD.toString(), ConfigUtil.getUnreadReports(p.getUniqueId())));
    }

    public static void sendList(CommandSender sender, String title, List<?> items) {
        StringBuilder sb = new StringBuilder();
        for (Object object : items) {
            sb.append(String.format(Message.LIST_ITEM.toString(), object.toString()));
        }
        String message = title + sb.toString().substring(0, sb.toString().length() - 2);
        sender.sendMessage(message);
    }
}
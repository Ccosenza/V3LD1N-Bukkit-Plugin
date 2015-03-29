package com.v3ld1n.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_8_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.PacketPlayOutChat;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.v3ld1n.Config;
import com.v3ld1n.Message;

public final class ChatUtil {
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

    /**
     * Sends the number of unread reports to all players
     */
    public static void sendUnreadReports() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.hasPermission("v3ld1n.report.read")) {
                sendUnreadReports(p);
            }
        }
    }

    /**
     * Sends the number of unread reports to  a player
     * @param p the player to send the message to
     */
    public static void sendUnreadReports(Player p) {
        String message = "{text:\"" + String.format(Message.REPORT_UNREAD.toString(), ConfigUtil.getUnreadReports(p.getUniqueId())) + "\","
                + "clickEvent:{action:run_command,value:\"/report list\"}}";
        ChatUtil.sendJsonMessage(p, message, 0);
    }

    /**
     * Sends a list in chat messages
     * @param sender the CommandSender to send the message to
     * @param title the list title
     * @param items the list items
     * @param type the list type
     */
    public static void sendList(CommandSender sender, String title, List<?> items, ListType type) {
        List<String> strings = new ArrayList<>();
        for (Object item : items) {
            strings.add(item.toString());
        }
        switch (type) {
        case SHORT:
            StringBuilder sb = new StringBuilder();
            for (String item : strings) {
                sb.append(String.format(Message.SHORT_LIST_ITEM.toString(), item));
            }
            String message = title + sb.toString().substring(0, sb.toString().length() - 2);
            sender.sendMessage(message);
            break;
        case LONG:
            sender.sendMessage(title);
            for (String item : strings) {
                sender.sendMessage(StringUtil.formatText(String.format(Message.LONG_LIST_ITEM.toString(), item)));
            }
            break;
        case SIDEBAR:
            if (sender instanceof Player) {
                SidebarMessage sbm = new SidebarMessage(title);
                sbm.setLines(strings);
                sbm.display((Player) sender, 200);
            }
            break;
        default:
            throw new IllegalArgumentException();
        }
    }
}
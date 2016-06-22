package com.v3ld1n.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
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
    public static void sendMessage(CommandSender to, String message, MessageType type) {
        if (to instanceof Player) {
            String jsonMessage = "{\"text\":\"" + message.replaceAll("\"", "\\\\\"") + "\"}";
            IChatBaseComponent chat = ChatSerializer.a(jsonMessage);
            PacketPlayOutChat packet = new PacketPlayOutChat(chat, type.getId());
            PlayerUtil.sendPacket(packet, (Player) to);
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
    public static void sendJsonMessage(CommandSender to, String message, MessageType type) {
        if (to instanceof Player) {
            IChatBaseComponent chat = ChatSerializer.a(message);
            PacketPlayOutChat packet = new PacketPlayOutChat(chat, type.getId());
            PlayerUtil.sendPacket(packet, (Player) to);
        } else {
            to.sendMessage(message);
        }
    }

    /**
     * Sends the MOTD to a player
     * @param player the player to send the message to
     */
    public static void sendMotd(Player player) {
        String listName = "old-players";
        if (!player.hasPlayedBefore()) {
            listName = "new-players";
        }
        for (String jsonText : Config.MOTD.getConfig().getStringList(listName)) {
            String message = StringUtil.formatText(jsonText);
            message = StringUtil.replacePlayerVariables(message, player);
            ChatUtil.sendJsonMessage(player, message, MessageType.CHAT);
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
     * Sends the number of unread reports to a player
     * @param player the player to send the message to
     */
    public static void sendUnreadReports(Player player) {
        int unread = ConfigUtil.getUnreadReports(player.getUniqueId());
        String message = "{text:\"" + String.format(Message.REPORT_UNREAD.toString(), unread) + "\","
                + "clickEvent:{action:run_command,value:\"/report list\"}}";
        ChatUtil.sendJsonMessage(player, message, MessageType.CHAT);
    }

    /**
     * Returns a section of a list
     * @param list the list
     * @param page the page
     * @param pageSize the size of the pages
     * @return the objects on that page
     */
    public static <T> List<T> getPage(List<T> list, int pageNumber, int pageSize) {
        List<List<T>> pages = new ArrayList<>();
        final int ls = list.size();
        for (int i = 0; i < ls; i += pageSize) {
            pages.add(new ArrayList<>(list.subList(i, Math.min(ls, i + pageSize))));
        }
        List<T> page = new ArrayList<>();
        if (pages.size() > 0) {
            page = pageNumber > pages.size() || pageNumber <= 0 ? pages.get(0) : pages.get(pageNumber - 1);
        }
        return page;
    }

    /**
     * Returns the number of pages in a list
     * @param list the list
     * @param pageSize the size of the pages
     * @return the number of pages
     */
    public static <T> int getNumberOfPages(List<T> list, int pageSize) {
        List<List<T>> pages = new ArrayList<>();
        final int ls = list.size();
        for (int i = 0; i < ls; i += pageSize) {
            pages.add(new ArrayList<>(list.subList(i, Math.min(ls, i + pageSize))));
        }
        return pages.size();
    }

    /**
     * Sends a list to a user
     * @param user the CommandSender to send the message to
     * @param title the list title
     * @param items the list items
     * @param type the list type
     */
    public static void sendList(CommandSender user, String title, List<?> items, ListType type) {
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
            user.sendMessage(message);
            break;
        case LONG:
            user.sendMessage(title);
            for (String item : strings) {
                Message.LONG_LIST_ITEM.sendF(user, StringUtil.formatText(item));
            }
            break;
        case SIDEBAR:
            if (user instanceof Player) {
                SidebarMessage sbm = new SidebarMessage(title);
                sbm.setLines(strings);
                sbm.display((Player) user, 200);
            }
            break;
        default:
            throw new IllegalArgumentException();
        }
    }
}
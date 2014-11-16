package com.v3ld1n.util;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

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
            if (((CraftPlayer) to).getHandle().playerConnection.networkManager.getVersion() >= 47) {
                IChatBaseComponent chat = ChatSerializer.a("{\"text\":\"" + message + "\"}");
                PacketPlayOutChat packet = new PacketPlayOutChat(chat, (byte) type);
                ((CraftPlayer) to).getHandle().playerConnection.sendPacket(packet);
            } else {
                to.sendMessage(Message.CHAT_OUTDATED.toString());
            }
        } else {
            to.sendMessage(message);
        }
    }

    /**
     * Sends the MOTD to a player
     * @param p the player to send the message to
     */
    public static void displayMotd(Player p) {
        p.sendMessage("MOTD");
    }
    
    /**
     * Sends an information message to a player
     * @param p the player to send the message to
     */
    public static void displayInfo(Player p) {
        p.sendMessage("Info Message");
    }
}
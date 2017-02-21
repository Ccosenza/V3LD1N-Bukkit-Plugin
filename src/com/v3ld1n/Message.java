package com.v3ld1n;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.MessageType;
import com.v3ld1n.util.StringUtil;

public class Message {
    private String name;
    private String message;

    public Message(String name, String message) {
        this.name = name;
        this.message = StringUtil.formatText(message);
    }

    @Override
    public String toString() {
        return message;
    }

    /**
     * Returns the message's name in messages.yml
     * @return the message's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sends the message to a user
     * @param user the user
     */
    public void send(CommandSender user) {
        user.sendMessage(this.toString());
    }

    /**
     * Formats the message, then sends it to a user
     * @param user the user
     * @param format the strings to format the message with
     */
    public void sendF(CommandSender user, Object... format) {
        user.sendMessage(String.format(this.toString(), format));
    }

    /**
     * Sends the message to a user as a type 2 message (above action bar)
     * @param user the user
     */
    public void aSend(CommandSender user) {
        ChatUtil.sendMessage(user, this.toString(), MessageType.ACTION_BAR);
    }

    /**
     * Formats the message, then sends it to a user as a type 2 message (above action bar)
     * @param user the user
     * @param format the strings to format the message with
     */
    public void aSendF(CommandSender user, Object... format) {
        ChatUtil.sendMessage(user, String.format(this.toString(), format), MessageType.ACTION_BAR);
    }

    /**
     * Logs the message
     * @param level the log level
     */
    public void log(Level level) {
        Bukkit.getLogger().log(level, this.toString());
    }

    /**
     * Formats the message, then logs it
     * @param level the log level
     * @param format the strings to format the message with
     */
    public void logF(Level level, Object... format) {
        Bukkit.getLogger().log(level, String.format(this.toString(), format));
    }

    /**
     * Returns a specified message
     * @param name the message's name in messages.yml
     * @return the message
     */
    public static Message get(String name) {
        for (Message message : V3LD1N.getMessages()) {
            if (message.getName().equalsIgnoreCase(name)) {
                return message;
            }
        }
        return new Message(name, name);
    }
}
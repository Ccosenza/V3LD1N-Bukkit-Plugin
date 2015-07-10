package com.v3ld1n.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ListType;
import com.v3ld1n.util.MessageType;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class SendMessageCommand extends V3LD1NCommand {
    public SendMessageCommand() {
        this.addUsage("all <type> <message ...>", "Broadcast a chat message to all players");
        this.addUsage("<player> <type> <message ...>", "Send a message to a player");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.sendmessage")) {
            if (args.length >= 3) {
                String message = StringUtil.fromArray(args, 2);
                message = StringUtil.formatText(message);
                message = message.replaceAll("[\"\\\\]", "");
                MessageType type;
                try {
                    type = MessageType.valueOf(args[1].toUpperCase());
                } catch (Exception e) {
                    Message.SENDMESSAGE_INVALID_TYPE.send(sender);
                    sendTypeList(sender);
                    return true;
                }
                String arg = args[0];
                if (arg.equalsIgnoreCase("all") || arg.equalsIgnoreCase("a")) {
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        ChatUtil.sendMessage(p, message, type);
                    }
                } else {
                    if (PlayerUtil.getOnlinePlayer(arg) != null) {
                        ChatUtil.sendMessage(PlayerUtil.getOnlinePlayer(arg), message, type);
                    } else {
                        sendInvalidPlayerMessage(sender);
                    }
                }
                return true;
            }
            this.sendUsage(sender);
            return true;
        }
        sendPermissionMessage(sender);
        return true;
    }

    @Override
    public void sendUsage(CommandSender user) {
        super.sendUsage(user);
        sendTypeList(user);
    }

    private void sendTypeList(CommandSender user) {
        List<MessageType> types = Arrays.asList(MessageType.values());
        ChatUtil.sendList(user, Message.SENDMESSAGE_LIST_TITLE.toString(), types, ListType.SHORT);
    }
}
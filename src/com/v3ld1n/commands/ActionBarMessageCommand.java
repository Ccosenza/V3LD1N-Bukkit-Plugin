package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.MessageType;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class ActionBarMessageCommand extends V3LD1NCommand {
    public ActionBarMessageCommand() {
        this.addUsage("all <message ...>", "Broadcast a message to all players");
        this.addUsage("<player> <message ...>", "Send a message to a player");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.actionbarmessage")) {
            if (args.length >= 2) {
                String message = StringUtil.fromArray(args, 1);
                message = StringUtil.formatText(message);
                message = message.replaceAll("[\"\\\\]", "");
                if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("a")) {
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        ChatUtil.sendMessage(p, message, MessageType.ACTION_BAR);
                    }
                } else {
                    if (PlayerUtil.getOnlinePlayer(args[0]) != null) {
                        ChatUtil.sendMessage(PlayerUtil.getOnlinePlayer(args[0]), message, MessageType.ACTION_BAR);
                    } else {
                        sendInvalidPlayerMessage(sender);
                    }
                }
                return true;
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sendPermissionMessage(sender);
        return true;
    }
}
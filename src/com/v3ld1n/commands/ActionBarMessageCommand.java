package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
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
                        ChatUtil.sendMessage(p, message, 2);
                    }
                } else {
                    if (PlayerUtil.getOnlinePlayer(args[0]) != null) {
                        ChatUtil.sendMessage(PlayerUtil.getOnlinePlayer(args[0]), message, 2);
                    } else {
                        sender.sendMessage(Message.COMMAND_INVALID_PLAYER.toString());
                    }
                }
                return true;
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}
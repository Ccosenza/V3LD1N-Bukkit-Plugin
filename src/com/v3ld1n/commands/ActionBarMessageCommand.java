package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class ActionBarMessageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.actionbarmessage")) {
            if (args.length > 0) {
                String message = StringUtil.formatText(args[0].replaceAll("_", " "));
                if (args.length == 1) {
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        ChatUtil.sendMessage(p, message, 2);
                    }
                    return true;
                } else if (args.length == 2) {
                    if (PlayerUtil.getOnlinePlayer(args[1]) != null) {
                        ChatUtil.sendMessage(PlayerUtil.getOnlinePlayer(args[1]), message, 2);
                    } else {
                        sender.sendMessage(Message.COMMAND_INVALID_PLAYER.toString());
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}
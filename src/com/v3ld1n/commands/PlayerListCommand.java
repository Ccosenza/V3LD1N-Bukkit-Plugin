package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ConfigUtil;

public class PlayerListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.playerlist")) {
            if (args.length == 3) {
                String header = args[1].replaceAll("__", " ");
                String footer = args[2].replaceAll("__", " ");
                if (args[0].equalsIgnoreCase("set")) {
                    ConfigUtil.setPlayerListHeaderFooter(header, footer);
                } else {
                    return false;
                }
                ChatUtil.sendMessage(sender, String.format(Message.PLAYERLIST_SET.toString(), header, footer), 2);
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reset")) {
                    ConfigUtil.setPlayerListHeaderFooter("{text:\"\"}", "{text:\"\"}");
                    ChatUtil.sendMessage(sender, Message.PLAYERLIST_RESET.toString(), 2);
                    return true;
                }
            }
            return false;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}
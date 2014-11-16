package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ConfigUtil;
import com.v3ld1n.util.StringUtil;

public class PlayerListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.playerlist")) {
            if (args.length >= 2) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                String text = sb.toString();
                text = StringUtil.formatText(text.substring(0, text.length() - 1));
                if (args[0].equalsIgnoreCase("header")) {
                    ConfigUtil.setPlayerListHeader(text);
                } else if (args[0].equalsIgnoreCase("footer")) {
                    ConfigUtil.setPlayerListFooter(text);
                } else {
                    return false;
                }
                ChatUtil.sendMessage(sender, String.format(Message.PLAYERLIST_SET.toString(), text), 2);
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reset")) {
                    ConfigUtil.setPlayerListHeader(null);
                    ConfigUtil.setPlayerListFooter(null);
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
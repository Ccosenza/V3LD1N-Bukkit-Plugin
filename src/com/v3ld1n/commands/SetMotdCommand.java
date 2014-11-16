package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.StringUtil;

public class SetMotdCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.setmotd")) {
            if (args.length > 0) {
                StringBuilder sb = new StringBuilder();
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                String motd = sb.toString();
                motd = StringUtil.formatText(motd.substring(0, motd.length() - 1));
                ConfigSetting.SERVER_LIST_MOTD.setValue(motd);
                ChatUtil.sendMessage(sender, String.format(Message.SETMOTD_SET.toString(), motd), 2);
                return true;
            }
            return false;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}
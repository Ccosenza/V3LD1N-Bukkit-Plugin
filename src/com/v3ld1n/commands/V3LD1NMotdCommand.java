package com.v3ld1n.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.StringUtil;

public class V3LD1NMotdCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.v3ld1nmotd")) {
            if (args.length > 1) {
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                String motd = sb.toString();
                motd = StringUtil.formatText(motd.substring(0, motd.length() - 1));
                List<String> motds = ConfigSetting.SERVER_LIST_MOTD.getStringList();
                if (args[0].equalsIgnoreCase("add")) {
                    motds.add(motd);
                    ConfigSetting.SERVER_LIST_MOTD.setValue(motds);
                    ChatUtil.sendMessage(sender, String.format(Message.V3LD1NMOTD_ADD.toString(), motd), 2);
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (motds.contains(motd)) {
                        motds.remove(motd);
                        ChatUtil.sendMessage(sender, String.format(Message.V3LD1NMOTD_REMOVE.toString(), motd), 2);
                        return true;
                    }
                }
                return false;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    sender.sendMessage(Message.V3LD1NMOTD_LIST.toString());
                    for (String motd : ConfigSetting.SERVER_LIST_MOTD.getStringList()) {
                        sender.sendMessage(String.format(Message.V3LD1NMOTD_LIST_ITEM.toString(), motd));
                    }
                    return true;
                }
            }
            return false;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}
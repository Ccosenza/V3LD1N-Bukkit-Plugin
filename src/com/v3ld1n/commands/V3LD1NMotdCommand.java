package com.v3ld1n.commands;

import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.StringUtil;

public class V3LD1NMotdCommand extends V3LD1NCommand {
    public V3LD1NMotdCommand() {
        this.addUsage("add <text ...>", "Add an MOTD to the list");
        this.addUsage("remove <text ...>", "Remove an MOTD from the list");
        this.addUsage("list", "Send the MOTD list");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.v3ld1nmotd")) {
            if (args.length > 1) {
                String motd;
                if (args.length > 2) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    motd = sb.toString();
                    motd = StringUtil.formatText(motd.substring(0, motd.length() - 1));
                } else {
                    motd = args[1];
                }
                List<String> motds = ConfigSetting.SERVER_LIST_MOTD.getStringList();
                if (args[0].equalsIgnoreCase("add")) {
                    motds.add(motd);
                    ConfigSetting.SERVER_LIST_MOTD.setValue(motds);
                    ChatUtil.sendMessage(sender, String.format(Message.V3LD1NMOTD_ADD.toString(), motd), 2);
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    Iterator<String> iterator = motds.iterator();
                    while (iterator.hasNext()) {
                        if (iterator.next().equals(motd)) {
                            iterator.remove();
                        }
                    }
                    ConfigSetting.SERVER_LIST_MOTD.setValue(motds);
                    ChatUtil.sendMessage(sender, String.format(Message.V3LD1NMOTD_REMOVE.toString(), motd), 2);
                    return true;
                }
                this.sendUsage(sender, label, command.getDescription());
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    sender.sendMessage(Message.V3LD1NMOTD_LIST.toString());
                    for (String motd : ConfigSetting.SERVER_LIST_MOTD.getStringList()) {
                        sender.sendMessage(String.format(Message.V3LD1NMOTD_LIST_ITEM.toString(), motd));
                    }
                    return true;
                }
            }
            this.sendUsage(sender, label, command.getDescription());
            return true;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}
package com.v3ld1n.commands;

import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ListType;
import com.v3ld1n.util.StringUtil;

public class V3LD1NMotdCommand extends V3LD1NCommand {
    public V3LD1NMotdCommand() {
        this.addUsage("add <text ...>", "Add an MOTD to the list");
        this.addUsage("remove <text ...>", "Remove an MOTD from the list");
        this.addUsage("list", "Send the MOTD list");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.owner")) {
            List<String> motds = ConfigSetting.SERVER_LIST_MOTD.getStringList();
            if (args.length > 1) {
                String motd;
                motd = StringUtil.fromArray(args, 1);
                if (args[0].equalsIgnoreCase("add")) {
                    motds.add(motd);
                    ConfigSetting.SERVER_LIST_MOTD.setValue(motds);
                    Message.V3LD1NMOTD_ADD.aSendF(sender, motd);
                    return true;
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (args[1].equalsIgnoreCase("all")) {
                        motds.clear();
                    } else {
                        if (motds.contains(motd)) {
                            Iterator<String> iterator = motds.iterator();
                            while (iterator.hasNext()) {
                                if (iterator.next().equals(motd)) {
                                    iterator.remove();
                                }
                            }
                        } else {
                            Message.V3LD1NMOTD_INVALID.sendF(sender, motd);
                            return true;
                        }
                    }
                    ConfigSetting.SERVER_LIST_MOTD.setValue(motds);
                    Message.V3LD1NMOTD_REMOVE.aSendF(sender, motd);
                    return true;
                }
                this.sendUsage(sender);
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    String title = Message.V3LD1NMOTD_LIST_TITLE.toString();
                    ChatUtil.sendList(sender, title, motds, ListType.LONG);
                    return true;
                }
            }
            this.sendUsage(sender);
            return true;
        }
        sendPermissionMessage(sender);
        return true;
    }
}
package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ConfigUtil;

public class PlayerListCommand extends V3LD1NCommand {
    public PlayerListCommand() {
        this.addUsage("set <header> <footer>", "Set the header and footer");
        this.addUsage("reset", "Reset the header and footer");
        this.addUsage("ping", "Toggle ping display");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("set")) {
                    String header = args[1].replaceAll("__", " ");
                    String footer = args[2].replaceAll("__", " ");
                    ConfigUtil.setPlayerListHeaderFooter(header, footer);
                    ChatUtil.sendMessage(sender, String.format(Message.PLAYERLIST_SET.toString(), header, footer), 2);
                } else {
                    this.sendUsage(sender, label, command);
                }
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reset")) {
                    ConfigUtil.setPlayerListHeaderFooter("{text:\"\"}", "{text:\"\"}");
                    ChatUtil.sendMessage(sender, Message.PLAYERLIST_RESET.toString(), 2);
                    return true;
                } else if (args[0].equalsIgnoreCase("ping")) {
                    Message message = null;
                    if (ConfigSetting.PLAYER_LIST_PING_ENABLED.getBoolean()) {
                        message = Message.PLAYERLIST_DISABLE_PING;
                    } else {
                        message = Message.PLAYERLIST_ENABLE_PING;
                    }
                    ConfigUtil.toggleSetting(ConfigSetting.PLAYER_LIST_PING_ENABLED);
                    sender.sendMessage(message.toString());
                    return true;
                }
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}
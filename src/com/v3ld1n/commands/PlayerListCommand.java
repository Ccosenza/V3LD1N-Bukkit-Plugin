package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.util.ConfigUtil;
import com.v3ld1n.util.StringUtil;

public class PlayerListCommand extends V3LD1NCommand {
    private String usagePingTime = "ping time <ticks>";

    public PlayerListCommand() {
        this.addUsage("set <header> <footer>", "Set the header and footer");
        this.addUsage("reset", "Reset the header and footer");
        this.addUsage("ping", "Display ping display information");
        this.addUsage("ping toggle", "Toggle ping display");
        this.addUsage(usagePingTime, "Set ping display update frequency");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.owner")) {
            int l = args.length;
            if (l >= 1) {
                if (args[0].equalsIgnoreCase("set") && l == 3) {
                    String header = StringUtil.formatText(args[1].replaceAll("_", " "));
                    String footer = StringUtil.formatText(args[2].replaceAll("_", " "));
                    ConfigUtil.setPlayerListHeaderFooter(header, footer);
                    Message.get("playerlist-set").aSendF(sender, header, footer);
                    return true;
                } else if (args[0].equalsIgnoreCase("reset") && l == 1) {
                    ConfigUtil.setPlayerListHeaderFooter("{text:\"\"}", "{text:\"\"}");
                    Message.get("playerlist-reset").aSend(sender);
                    return true;
                } else if (args[0].equalsIgnoreCase("ping") && (l <= 3)) {
                    if (l == 1) {
                        int ticks = ConfigSetting.PLAYER_LIST_PING_TICKS.getInt();
                        double seconds = ((double) ticks) / 20;
                        Message.get("playerlist-ping-display").sendF(sender, ConfigSetting.PLAYER_LIST_PING_ENABLED.getBoolean());
                        Message.get("playerlist-ping-time").sendF(sender, String.format("%.2f", seconds), ticks);
                        return true;
                    } else if (l == 2) {
                        if (args[1].equalsIgnoreCase("toggle")) {
                            boolean pingEnabled = ConfigSetting.PLAYER_LIST_PING_ENABLED.getBoolean();
                            Message message = pingEnabled ? Message.get("playerlist-ping-disable") : Message.get("playerlist-ping-enable");
                            ConfigSetting.PLAYER_LIST_PING_ENABLED.toggle();
                            message.aSend(sender);
                            return true;
                        }
                    } else if (l == 3) {
                        if (args[1].equalsIgnoreCase("time")) {
                            int arg;
                            try {
                                arg = Integer.parseInt(args[2]);
                            } catch (IllegalArgumentException e) {
                                this.sendArgumentUsage(sender, usagePingTime);
                                return true;
                            }
                            ConfigSetting.PLAYER_LIST_PING_TICKS.setValue(arg);
                            Message.get("playerlist-ping-time-set").aSendF(sender, arg);
                            return true;
                        }
                    }
                }
            }
            this.sendUsage(sender);
            return true;
        }
        sendPermissionMessage(sender);
        return true;
    }
}
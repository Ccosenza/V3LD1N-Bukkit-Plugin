package com.v3ld1n.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.SidebarMessage;
import com.v3ld1n.util.StringUtil;

public class SidebarMessageCommand extends V3LD1NCommand {
    private static final int DEFAULT_TIME = 100;

    public SidebarMessageCommand() {
        this.addUsage("<time> <title> <line1> [more lines] ...", "Broadcast a message to all players");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.sidebarmessage")) {
            if (args.length >= 3) {
                int time;
                String timeArg = args[0];
                try {
                    if (timeArg.charAt(timeArg.length() - 1) == 's') {
                        time = Integer.parseInt(timeArg.substring(0, args[0].length() - 1)) * 20;
                    } else {
                        time = Integer.parseInt(timeArg);
                    }
                } catch (Exception IllegalArgumentException) {
                    sender.sendMessage(String.format(Message.SIDEBARMESSAGE_INVALID_TIME.toString(), DEFAULT_TIME, DEFAULT_TIME / 20));
                    time = DEFAULT_TIME;
                }
                SidebarMessage sbm = new SidebarMessage(args[1]);
                for (String arg : args) {
                    if (Arrays.asList(args).indexOf(arg) > 1) {
                        sbm.addLine(arg);
                    }
                }
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    sbm.display(p, time);
                }
                String message = String.format(Message.SIDEBARMESSAGE_DISPLAY.toString(), StringUtil.formatText(sbm.getTitle().replaceAll("_", " ")));
                ChatUtil.sendMessage(sender, message, 2);
                return true;
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}
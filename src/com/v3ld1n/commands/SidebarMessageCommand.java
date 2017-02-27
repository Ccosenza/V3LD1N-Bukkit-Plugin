package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.SidebarMessage;
import com.v3ld1n.util.StringUtil;

public class SidebarMessageCommand extends V3LD1NCommand {
    private static final int DEFAULT_TIME = 100;

    public SidebarMessageCommand() {
        this.addUsage("<time> <title> <line1> [more lines] ...", "Broadcast a message to all players");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendPermissionMessage(sender, "v3ld1n.sidebarmessage")) return true;

        if (args.length < 3) {
            this.sendUsage(sender);
            return true;
        }

        display(args, sender);
        return true;
    }

    private int getTicks(String timeArg, CommandSender user) {
        int ticks;
        try {
            if (timeArg.charAt(timeArg.length() - 1) != 's') {
                ticks = Integer.parseInt(timeArg);
            }
            ticks = Integer.parseInt(timeArg.substring(0, timeArg.length() - 1)) * 20;
        } catch (Exception IllegalArgumentException) {
            Message.get("sidebarmessage-invalid-time").sendF(user, DEFAULT_TIME, DEFAULT_TIME / 20);
            ticks = DEFAULT_TIME;
        }
        return ticks;
    }

    private void display(String[] args, CommandSender user) {
        int time = getTicks(args[0], user);
        SidebarMessage sidebarMessage = new SidebarMessage(args[1]);
        for (int i = 2; i < args.length; i++) {
            sidebarMessage.addLine(args[i]);
        }
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            sidebarMessage.display(p, time);
        }
        String titleWithSpaces = StringUtil.formatText(sidebarMessage.getTitle().replaceAll("_", " "));
        Message.get("sidebarmessage-display").aSendF(user, titleWithSpaces);
    }
}
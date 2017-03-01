package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.SidebarMessage;
import com.v3ld1n.util.StringUtil;

public class SidebarMessageCommand extends V3LD1NCommand {
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

        if (!StringUtil.isInteger(args[0])) {
            Message.get("sidebarmessage-invalid-time").send(sender);
            return true;
        }
        int ticks = Integer.parseInt(args[0]);

        display(args, ticks, sender);
        return true;
    }

    // Displays the sidebar to all players
    private void display(String[] args, int ticks, CommandSender user) {
        SidebarMessage sidebarMessage = new SidebarMessage(args[1]);
        for (int i = 2; i < args.length; i++) {
            sidebarMessage.addLine(args[i]);
        }
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            sidebarMessage.display(p, ticks);
        }
        String titleWithSpaces = StringUtil.formatText(sidebarMessage.getTitle().replaceAll("_", " "));
        Message.get("sidebarmessage-display").sendF(user, titleWithSpaces, ticks, (double) ticks / 20);
    }
}
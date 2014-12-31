package com.v3ld1n.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;

public class ReportCommand extends V3LD1NCommand {
    String usageReport = "<player> <reason ...>";

    public ReportCommand() {
        this.addUsage(usageReport, "Report a player to the server admins");
        this.addUsage("read <report number>", "Read a report");
        this.addUsage("list", "List reports");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String playerName = p.getName();
            UUID playerUuid = p.getUniqueId();
            if (args.length >= 2) {
                if (args.length == 2 && sender.isOp()) {
                }
                String title = args[0];
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                String reason = sb.toString();
                reason = reason.substring(0, reason.length() - 1);
                Report report = new Report(title, playerName, playerUuid, reason);
                V3LD1N.addReport(report);
                ChatUtil.sendUnreadReports();
                ChatUtil.sendMessage(sender, Message.REPORT_SEND.toString(), 2);
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                }
            }
            if (sender.hasPermission("v3ld1n.report.read")) {
                this.sendUsage(sender, label, command);
            } else {
                this.sendArgumentUsage(sender, label, command, usageReport);
            }
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}
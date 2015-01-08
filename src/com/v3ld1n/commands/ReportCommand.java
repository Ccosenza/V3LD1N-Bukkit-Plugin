package com.v3ld1n.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Config;
import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.StringUtil;

public class ReportCommand extends V3LD1NCommand {
    String usageReport = "<player> <reason ...>";
    String usageRead = ("read <report number>");
    String usageReadBy = ("readby <report number>");
    String usageDelete = ("delete <report number>");

    public ReportCommand() {
        this.addUsage(usageReport, "Report a player to the server admins");
        this.addUsage("list", "List reports");
        this.addUsage(usageRead, "Read a report");
        this.addUsage(usageReadBy, "List players who read a report");
        this.addUsage(usageDelete, "Delete a report");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String playerName = p.getName();
            UUID playerUuid = p.getUniqueId();
            String border = "{text:\"" + Message.REPORT_LIST_BORDER + "\","
                    + "color:dark_red}";
            String top = "{text:\"" + Message.REPORT_LIST_TOP + "\","
                    + "color:red}";
            if (args.length >= 2) {
                if (args.length == 2) {
                    if (sender.hasPermission("v3ld1n.report.read") && args[0].equalsIgnoreCase("read")) {
                        int arg;
                        try {
                            arg = Integer.parseInt(args[1]);
                        } catch (IllegalArgumentException e) {
                            this.sendArgumentUsage(sender, label, command, usageRead);
                            return true;
                        }
                        if (arg <= V3LD1N.getReports().size() && arg > 0) {
                            Report report = V3LD1N.getReports().get(arg - 1);
                            String title = "{text:\"" + Message.REPORT_READ_TITLE + "\","
                                    + "color:gold,"
                                    + "extra:["
                                    + "{text:\"" + report.getTitle() + "\","
                                    + "color:aqua}]}";
                            String senderName = "{text:\"" + Message.REPORT_READ_SENDER + "\","
                                    + "color:yellow,"
                                    + "extra:["
                                    + "{text:\"" + report.getSenderName() + "\","
                                    + "color:green}]}";
                            String reason = "{text:\"" + Message.REPORT_READ_REASON + "\","
                                    + "color:gold,"
                                    + "extra:["
                                    + "{text:\"" + report.getReason() + "\","
                                    + "color:aqua}]}";
                            String back = "{text:\"" + Message.REPORT_READ_BACK + "\","
                                    + "color:" + ConfigSetting.REPORT_READ_BACK_COLOR.getString() + ","
                                    + "clickEvent:{"
                                    + "action:\"run_command\","
                                    + "value:\"/" + label + " list\"}}";
                            ChatUtil.sendJsonMessage(p, border, 0);
                            ChatUtil.sendJsonMessage(p, top, 0);
                            ChatUtil.sendJsonMessage(p, border, 0);
                            ChatUtil.sendJsonMessage(p, title, 0);
                            ChatUtil.sendJsonMessage(p, senderName, 0);
                            ChatUtil.sendJsonMessage(p, reason, 0);
                            ChatUtil.sendJsonMessage(p, back, 0);
                            if (!report.isReadBy(playerUuid)) {
                                report.setReadBy(playerUuid);
                            }
                            return true;
                        }
                        p.sendMessage(Message.REPORT_INVALID.toString());
                        return true;
                    } else if (args[0].equalsIgnoreCase("delete")) {
                        if (sender.isOp()) {
                            int arg;
                            try {
                                arg = Integer.parseInt(args[1]);
                            } catch (IllegalArgumentException e) {
                                this.sendArgumentUsage(sender, label, command, usageDelete);
                                return true;
                            }
                            if (arg <= V3LD1N.getReports().size() && arg > 0) {
                                Report report = V3LD1N.getReports().get(arg - 1);
                                V3LD1N.getReports().remove(report);
                                Config.REPORTS.getConfig().set("reports." + report.getTitle(), null);
                                Config.REPORTS.saveConfig();
                                ChatUtil.sendMessage(sender, String.format(Message.REPORT_DELETE.toString(), report.getTitle()), 2);
                                return true;
                            }
                            p.sendMessage(Message.REPORT_INVALID.toString());
                            return true;
                        }
                        sender.sendMessage(Message.REPORT_DELETE_NO_PERMISSION.toString());
                        return true;
                    } else if (args[0].equalsIgnoreCase("readby")) {
                        if (sender.isOp()) {
                            int arg;
                            try {
                                arg = Integer.parseInt(args[1]);
                            } catch (IllegalArgumentException e) {
                                this.sendArgumentUsage(sender, label, command, usageReadBy);
                                return true;
                            }
                            if (arg <= V3LD1N.getReports().size() && arg > 0) {
                                Report report = V3LD1N.getReports().get(arg - 1);
                                sender.sendMessage(String.format(Message.REPORT_READBY_LIST.toString(), report.getTitle()));
                                if (!report.getReadPlayers().isEmpty()) {
                                    for (UUID uuid : report.getReadPlayers()) {
                                        String offlineName = Bukkit.getServer().getOfflinePlayer(uuid).getName();
                                        sender.sendMessage(StringUtil.formatText(String.format(Message.REPORT_READBY_LIST_ITEM.toString(), offlineName)));
                                    }
                                } else {
                                    sender.sendMessage(Message.NONE.toString());
                                }
                                return true;
                            }
                            p.sendMessage(Message.REPORT_INVALID.toString());
                            return true;
                        }
                        sender.sendMessage(Message.REPORT_READBY_NO_PERMISSION.toString());
                        return true;
                    }
                }
                String title = args[0];
                String reason = StringUtil.fromArray(args, 1);
                int sameTitles = 0;
                for (Report report : V3LD1N.getReports()) {
                    if (report.getTitle().equalsIgnoreCase(title) || report.getTitle().startsWith(title + "(")) {
                        ++sameTitles;
                    }
                }
                if (sameTitles > 0) {
                    title = title + "(" + sameTitles + ")";
                }
                title = title.replaceAll("[\"\\\\]", "");
                if (title.isEmpty()) {
                    title = Message.REPORT_NO_TITLE.toString();
                }
                reason = reason.replaceAll("[\"\\\\]", "");
                if (reason.isEmpty()) {
                    title = Message.REPORT_NO_REASON.toString();
                }
                Report report = new Report(title, playerName, playerUuid, reason);
                V3LD1N.addReport(report);
                ChatUtil.sendUnreadReports();
                ChatUtil.sendMessage(sender, String.format(Message.REPORT_SEND.toString(), title), 2);
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    if (sender.hasPermission("v3ld1n.report.read")) {
                        ChatUtil.sendJsonMessage(p, border, 0);
                        ChatUtil.sendJsonMessage(p, top, 0);
                        ChatUtil.sendJsonMessage(p, border, 0);
                        if (V3LD1N.getReports().isEmpty()) {
                            sender.sendMessage(Message.REPORT_LIST_EMPTY.toString());
                        } else {
                            for (Report report : V3LD1N.getReports()) {
                                String titleColor = ConfigSetting.REPORTS_LIST_UNREAD_COLOR.getString();
                                if (report.isReadBy(playerUuid)) {
                                    titleColor = ConfigSetting.REPORTS_LIST_READ_COLOR.getString();
                                }
                                ChatUtil.sendJsonMessage(p,
                                "{text:\"" + (V3LD1N.getReports().indexOf(report) + 1) + ". \","
                                + "color:" + "gold" + ","
                                + "extra:["
                                + "{text:\"" + report.getTitle() + "\","
                                + "color:" + titleColor + ","
                                + "clickEvent:{"
                                + "action:\"run_command\","
                                + "value:\"/" + label + " read " + (V3LD1N.getReports().indexOf(report) + 1) + "\"}}]}"
                                , 0);
                            }
                            ChatUtil.sendJsonMessage(p,
                                    "{text:\"" + Message.REPORT_LIST_HELP + "\","
                                    + "color:green}", 0);
                        }
                        return true;
                    }
                    sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
                    return true;
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
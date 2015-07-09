package com.v3ld1n.commands;

import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Config;
import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ListType;
import com.v3ld1n.util.MessageType;
import com.v3ld1n.util.StringUtil;
import com.v3ld1n.util.TimeUtil;

public class ReportCommand extends V3LD1NCommand {
    private String usageReport = "<player> <reason ...>";
    private String usageRead = ("read <report number>");
    private String usageReadBy = ("readby <report number>");
    private String usageDelete = ("delete <report number>");

    public ReportCommand() {
        this.addUsage(usageReport, "Report a player to the server admins");
        this.addUsage("list", "List reports", "v3ld1n.report.read");
        this.addUsage(usageRead, "Read a report", "v3ld1n.report.read");
        this.addUsage(usageReadBy, "List players who read a report", "v3ld1n.owner");
        this.addUsage(usageDelete, "Delete a report", "v3ld1n.owner");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String playerName = p.getName();
            UUID playerUuid = p.getUniqueId();
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
                            long rTime = report.getTime();
                            String time = "{text:\"" + Message.REPORT_READ_TIME + "\","
                                    + "color:yellow,"
                                    + "extra:["
                                    + "{text:\"" + TimeUtil.formatDate(rTime) + " at " + TimeUtil.formatTime(rTime) + "\","
                                    + "color:green}]}";
                            String back = "{text:\"" + Message.REPORT_READ_BACK + "\","
                                    + "color:" + ConfigSetting.REPORT_READ_BACK_COLOR.getString() + ","
                                    + "clickEvent:{"
                                    + "action:\"run_command\","
                                    + "value:\"/" + label + " list\"}}";
                            Message.REPORT_BORDER_TOP.send(p);
                            ChatUtil.sendJsonMessage(p, title, MessageType.CHAT);
                            ChatUtil.sendJsonMessage(p, senderName, MessageType.CHAT);
                            ChatUtil.sendJsonMessage(p, reason, MessageType.CHAT);
                            ChatUtil.sendJsonMessage(p, time, MessageType.CHAT);
                            ChatUtil.sendJsonMessage(p, back, MessageType.CHAT);
                            Message.REPORT_BORDER_BOTTOM.send(p);
                            if (!report.isReadBy(playerUuid)) {
                                report.setReadBy(playerUuid);
                            }
                            return true;
                        }
                        Message.REPORT_INVALID.send(p);
                        return true;
                    } else if (args[0].equalsIgnoreCase("delete")) {
                        if (sender.hasPermission("v3ld1n.owner")) {
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
                                Message.REPORT_DELETE.aSendF(sender, report.getTitle());
                                return true;
                            }
                            Message.REPORT_INVALID.send(p);
                            return true;
                        }
                        Message.REPORT_DELETE_NO_PERMISSION.send(sender);
                        return true;
                    } else if (args[0].equalsIgnoreCase("readby")) {
                        if (sender.hasPermission("v3ld1n.owner")) {
                            int arg;
                            try {
                                arg = Integer.parseInt(args[1]);
                            } catch (IllegalArgumentException e) {
                                this.sendArgumentUsage(sender, label, command, usageReadBy);
                                return true;
                            }
                            if (arg <= V3LD1N.getReports().size() && arg > 0) {
                                Report report = V3LD1N.getReports().get(arg - 1);
                                Message.REPORT_READBY_LIST_TITLE.sendF(sender, report.getTitle());
                                if (!report.getReadPlayers().isEmpty()) {
                                    String title = Message.REPORT_READBY_LIST_TITLE.toString();
                                    List<UUID> readBy = report.getReadPlayers();
                                    ChatUtil.sendList(sender, title, readBy, ListType.LONG);
                                } else {
                                    Message.NONE.send(sender);
                                }
                                return true;
                            }
                            Message.REPORT_INVALID.send(p);
                            return true;
                        }
                        Message.REPORT_READBY_NO_PERMISSION.send(sender);
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
                Report report = new Report(title, playerName, playerUuid, reason, TimeUtil.getTime());
                V3LD1N.addReport(report);
                ChatUtil.sendUnreadReports();
                Message.REPORT_SEND.aSendF(p, title);
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("list")) {
                    if (sender.hasPermission("v3ld1n.report.read")) {
                        Message.REPORT_BORDER_TOP.send(p);
                        if (V3LD1N.getReports().isEmpty()) {
                            Message.REPORT_LIST_EMPTY.send(p);
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
                                + "value:\"/" + label + " read " + (V3LD1N.getReports().indexOf(report) + 1) + "\"}}]}",
                                MessageType.CHAT);
                            }
                            Message.REPORT_LIST_HELP.send(p);
                        }
                        return true;
                    }
                    sendPermissionMessage(sender);
                    return true;
                }
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sendPlayerMessage(sender);
        return true;
    }
}
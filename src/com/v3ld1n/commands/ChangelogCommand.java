package com.v3ld1n.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.MessageType;
import com.v3ld1n.util.StringUtil;
import com.v3ld1n.util.TimeUtil;

public class ChangelogCommand extends V3LD1NCommand {
    private final int PAGE_SIZE = 7;

    public ChangelogCommand() {
        this.addUsage("", "Display the last " + PAGE_SIZE + " changelogs");
        this.addUsage("<page>", "Display older changelogs");
        this.addUsage("log <change>", "Log a change", "v3ld1n.owner");
        this.addUsage("link <url>", "Add a clickable link to today's changelog", "v3ld1n.owner");
        this.addUsage("link remove", "Remove the link from today's changelog", "v3ld1n.owner");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sendPlayerMessage(sender);
            return true;
        }
        Player player = (Player) sender;
        int page;

        if (args.length == 0) {
            // Display the first page of the changelog
            page = 1;
            displayChangelog(player, page);
            return true;
        } else if (args.length == 1 && StringUtil.isInteger(args[0])) {
            // Display a page of the changelog
            page = StringUtil.toInteger(args[0], 1);
            displayChangelog(player, page);
            return true;
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("log")) {
            // Add a change to the changelog
            String newChange = StringUtil.fromArray(args, 1);
            logChange(player, newChange);
            return true;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("link")) {
            if (player.hasPermission("v3ld1n.owner")) {
                Message message;
                String link = args[1].replaceAll("[\"\\\\]", "");

                // Remove the link from today's changelog
                if (args[1].equalsIgnoreCase("remove")) {
                    link = "";
                    message = Message.CHANGELOG_LINK_REMOVE;
                } else {
                    message = Message.CHANGELOG_LINK_SET;
                }

                // Add a link to today's changelog
                if (ChangelogDay.today() != null) {
                    ChangelogDay.today().setLink(link);
                    message.sendF(player, link);
                } else {
                    Message.CHANGELOG_LINK_ERROR.send(player);
                }
                return true;
            }
        }
        this.sendUsage(player);
        return true;
    }

    private void logChange(Player player, String newChange) {
        if (player.hasPermission("v3ld1n.owner")) {
            String uuid = player.getUniqueId().toString();
            String replaced = newChange.replaceAll("[\"\\\\]", "");
            Change change = new Change(TimeUtil.getTime(), uuid, replaced);
            V3LD1N.addChange(change, ChangelogDay.todayDate());
            displayChangelog(player, 1);
            Message.CHANGELOG_LOG.send(player);
        } else {
            Message.CHANGELOG_NO_PERMISSION.send(player);
        }
    }

    private void displayChangelog(Player p, int page) {
        List<ChangelogDay> days = new ArrayList<>(V3LD1N.getChangelogDays());
        Collections.reverse(days);
        Message.CHANGELOG_BORDER_TOP.sendF(p, page, ChatUtil.getNumberOfPages(days, PAGE_SIZE));
        List<ChangelogDay> daysOnPage = ChatUtil.getPage(days, page, PAGE_SIZE);

        for (ChangelogDay day : daysOnPage) {
            List<Change> changesOnDay = day.getChanges();
            SimpleDateFormat dateFormat = ChangelogDay.getDateFormat();
            try {
                Date date = dateFormat.parse(day.getDate());
                String formattedDate = TimeUtil.format(date.getTime(), "MMMM d, yyyy");
                String message = "{\"text\":\"[" + formattedDate + "]\","
                        + "\"color\":\"gold\","
                        + "\"hoverEvent\":{"
                        + "\"action\":\"show_text\","
                        + "\"value\":\"%s\"},"
                        + "\"clickEvent\":{"
                        + "\"action\":\"open_url\","
                        + "\"value\":\"%s\"}}";

                StringBuilder builder = new StringBuilder();
                builder.append(String.format(Message.CHANGELOG_HOVER_TOP.toString(), formattedDate) + "\n");

                for (Change change : changesOnDay) {
                    String changeTime = TimeUtil.formatTime(change.getTime());
                    String listItemFormat = Message.CHANGELOG_LIST_ITEM.toString();
                    listItemFormat = listItemFormat.replaceAll("%newline%", "\n");
                    builder.append(String.format(listItemFormat, changeTime, change.getChange()));
                }

                String builderString = builder.toString();
                builderString = builderString.substring(0, builderString.length() - 1);
                message = String.format(message, builderString, day.getLink());
                ChatUtil.sendJsonMessage(p, message, MessageType.CHAT);
            } catch (Exception e) {
                Message.CHANGELOG_ERROR.send(p);
                e.printStackTrace();
            }
        }
        Message.CHANGELOG_HELP.send(p);
        Message.CHANGELOG_BORDER_BOTTOM.send(p);
    }
}
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
import com.v3ld1n.util.StringUtil;
import com.v3ld1n.util.TimeUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

public class ChangelogCommand extends V3LD1NCommand {
    private final int PAGE_SIZE = 7;

    public ChangelogCommand() {
        this.addUsage("[page]", "Display the list of changelogs");
        this.addUsage("log <change>", "Log a change", "v3ld1n.owner");
        this.addUsage("link <url>", "Add a clickable link to today's changelog", "v3ld1n.owner");
        this.addUsage("link remove", "Remove the link from today's changelog", "v3ld1n.owner");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendNotPlayerMessage(sender)) return true;
        Player player = (Player) sender;

        int page;
        if (args.length == 0) {
            // Display the first page of the changelog
            page = 1;
            displayChangelog(player, page);
        } else if (args.length == 1 && StringUtil.isInteger(args[0])) {
            // Display a page of the changelog
            page = StringUtil.toInteger(args[0], 1);
            displayChangelog(player, page);
        } else if (args.length >= 2 && args[0].equalsIgnoreCase("log")) {
            // Add a change to the changelog
            String newChange = StringUtil.fromArray(args, 1);
            logChange(player, newChange);
        } else if (args.length == 2 && args[0].equalsIgnoreCase("link")) {
            if (!player.hasPermission("v3ld1n.owner")) {
                Message.get("changelog-permission").send(player);;
                return true;
            }
            Message message;
            String link = args[1].replaceAll("[\"\\\\]", "");

            // Remove the link from today's changelog
            if (args[1].equalsIgnoreCase("remove")) {
                link = "";
                message = Message.get("changelog-link-remove");
            } else {
                message = Message.get("changelog-link-set");
            }

            // Add a link to today's changelog
            if (ChangelogDay.today() != null) {
                ChangelogDay.today().setLink(link);
                message.sendF(player, link);
            } else {
                Message.get("changelog-link-error").send(player);
            }
        } else {
            this.sendUsage(player);
        }
        return true;
    }

    // Adds a change to today's changelog
    private void logChange(Player player, String newChange) {
        if (player.hasPermission("v3ld1n.owner")) {
            String uuid = player.getUniqueId().toString();
            String replaced = newChange.replaceAll("[\"\\\\]", "");
            Change change = new Change(TimeUtil.getTime(), uuid, replaced);
            V3LD1N.addChange(change, ChangelogDay.todayDate());
            displayChangelog(player, 1);
            Message.get("changelog-added").send(player);
        } else {
            Message.get("changelog-permission").send(player);
        }
    }

    // Displays a page of the changelog to the player
    private void displayChangelog(Player player, int page) {
        List<ChangelogDay> days = new ArrayList<>(V3LD1N.getChangelogDays());
        Collections.reverse(days);
        Message.get("changelog-border-top").sendF(player, page, ChatUtil.getNumberOfPages(days, PAGE_SIZE));

        List<ChangelogDay> daysOnPage = ChatUtil.getPage(days, page, PAGE_SIZE);
        for (ChangelogDay day : daysOnPage) {
            List<Change> changesOnDay = day.getChanges();
            SimpleDateFormat dateFormat = ChangelogDay.getDateFormat();
            try {
                Date date = dateFormat.parse(day.getDate());
                String formattedDate = TimeUtil.format(date.getTime(), "MMMM d, yyyy");

                StringBuilder builder = new StringBuilder();
                builder.append(String.format(Message.get("changelog-hover-top").toString(), formattedDate) + "\n");

                for (Change change : changesOnDay) {
                    String changeTime = TimeUtil.formatTime(change.getTime());
                    String listItemFormat = Message.get("changelog-list-item").toString();
                    listItemFormat = listItemFormat.replaceAll("%newline%", "\n");
                    builder.append(String.format(listItemFormat, changeTime, change.getChange()));
                }

                String builderString = builder.toString();
                builderString = builderString.substring(0, builderString.length() - 1);

                TextComponent message = new TextComponent("[" + formattedDate + "]");
                message.setColor(ChatColor.GOLD);
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(builderString).create()));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, day.getLink()));
                player.spigot().sendMessage(message);
            } catch (Exception e) {
                Message.get("changelog-display-error").send(player);
                e.printStackTrace();
            }
        }
        Message.get("changelog-help").send(player);
        Message.get("changelog-border-bottom").send(player);
    }
}
package com.v3ld1n.commands;

import java.text.SimpleDateFormat;
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

public class ChangelogCommand extends V3LD1NCommand {
    private final int PAGE_SIZE = 7;

    public ChangelogCommand() {
        this.addUsage("", "Display the last " + PAGE_SIZE + " changelogs");
        this.addUsage("<page>", "Display older changelogs");
        this.addUsage("log <change>", "Log a change", "v3ld1n.owner");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                displayChangelog(p, 1);
                return true;
            } else if (args.length == 1 && StringUtil.isInteger(args[0])) {
                displayChangelog(p, Integer.parseInt(args[0]));
                return true;
            } else if (args.length >= 2 && args[0].equalsIgnoreCase("log")) {
                if (p.hasPermission("v3ld1n.owner")) {
                    String changed = StringUtil.fromArray(args, 1);
                    changed = changed.replaceAll("[\"\\\\]", "");
                    Change change = new Change(TimeUtil.getTime(), p.getUniqueId().toString(), changed);
                    V3LD1N.addChange(change, ChangelogDay.today());
                    displayChangelog(p, 1);
                    Message.CHANGELOG_LOG.send(p);
                } else {
                    Message.CHANGELOG_NO_PERMISSION.send(p);
                }
                return true;
            }
            this.sendUsage(p, label, command);
            return true;
        }
        sendPlayerMessage(sender);
        return true;
    }

    private void displayChangelog(Player p, int page) {
        List<ChangelogDay> clds = V3LD1N.getChangelogDays();
        Collections.reverse(clds);
        Message.CHANGELOG_BORDER_TOP.sendF(p, page, ChatUtil.getNumberOfPages(clds, PAGE_SIZE));
        List<ChangelogDay> pg = ChatUtil.getPage(clds, page, PAGE_SIZE);
        for (ChangelogDay cld : pg) {
            List<Change> c = cld.getChanges();
            SimpleDateFormat df = ChangelogDay.getDateFormat();
            try {
                Date date = df.parse(cld.getDay());
                String format = TimeUtil.format(date.getTime(), "MMMM dd, yyyy");
                String message = "{\"text\":\"[" + format + "]\","
                        + "\"color\":\"gold\","
                        + "\"hoverEvent\":{"
                        + "\"action\":\"show_text\","
                        + "\"value\":\"%s\"}}";
                StringBuilder sb = new StringBuilder();
                sb.append(String.format(Message.CHANGELOG_HOVER_TOP.toString(), format) + "\n");
                for (Change change : c) {
                    String time = TimeUtil.formatTime(change.getTime());
                    sb.append(String.format(Message.CHANGELOG_LIST_ITEM.toString().replaceAll("%newline%", "\n"), time, change.getChange()));
                }
                String sbs = sb.toString();
                sbs = sbs.substring(0, sbs.length() - 1);
                message = String.format(message, sbs);
                ChatUtil.sendJsonMessage(p, message, 0);
            } catch (Exception e) {
                Message.CHANGELOG_ERROR.send(p);
                e.printStackTrace();
            }
        }
        Message.CHANGELOG_BORDER_BOTTOM.send(p);
    }
}
package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.RepeatableRunnable;
import com.v3ld1n.util.TimeUtil;

public class TimePlayedCommand extends V3LD1NCommand {
    private final String PREFIX = ConfigSetting.SCOREBOARD_PREFIX.getString();
    private static final int SECONDS = 15;

    public TimePlayedCommand() {
        this.addUsage("", "View your time played");
        this.addUsage("<player>", "View a player's time played");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final Player p;
            if (args.length == 0) {
                p = player;
            } else if (args.length == 1) {
                if (PlayerUtil.getOnlinePlayer(args[0]) != null) {
                    p = PlayerUtil.getOnlinePlayer(args[0]);
                } else {
                    player.sendMessage(Message.COMMAND_INVALID_PLAYER.toString());
                    return true;
                }
            } else {
                this.sendUsage(sender, label, command);
                return true;
            }
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            final Scoreboard board = manager.getNewScoreboard();
            String name = PREFIX + "timeplayed";
            if (name.length() > 16) {
                name = name.substring(0, 16);
            }
            final Objective objective = board.registerNewObjective(name, "dummy");
            objective.setDisplayName(ChatColor.GOLD + "Total Times (" + ChatColor.AQUA + p.getName() + ChatColor.GOLD + ")");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            RepeatableRunnable updateSidebarTask = new RepeatableRunnable(Bukkit.getScheduler(), V3LD1N.getPlugin(), 5, 5, SECONDS * 4) {
                @Override
                public void onRun() {
                    updateTime(objective, p, player);
                }
            };
            updateSidebarTask.run();
            player.setScoreboard(board);
            Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    for (Objective obj : board.getObjectives()) {
                        if (obj.getName().startsWith(PREFIX)) {
                            obj.unregister();
                        }
                    }
                }
            }, SECONDS * 20);
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }

    private void updateTime(Objective objective, Player player, Player to) {
        int ticks = PlayerUtil.getTicksPlayed(player);
        int seconds = PlayerUtil.getSecondsPlayed(player);
        int minutes = PlayerUtil.getMinutesPlayed(player);
        int hours = PlayerUtil.getHoursPlayed(player);
        int days = PlayerUtil.getDaysPlayed(player);
        int weeks = PlayerUtil.getWeeksPlayed(player);
        ChatUtil.sendMessage(to, Message.TIMEPLAYED_TIME.toString() + TimeUtil.fromSeconds(seconds), 2);
        objective.getScore("Ticks").setScore(ticks);
        if (seconds > 0) {
            objective.getScore("Seconds").setScore(seconds);
        }
        if (minutes > 0) {
            objective.getScore("Minutes").setScore(minutes);
        }
        if (hours > 0) {
            objective.getScore("Hours").setScore(hours);
        }
        if (days > 0) {
            objective.getScore("Days").setScore(days);
        }
        if (weeks > 0) {
            objective.getScore("Weeks").setScore(weeks);
        }
    }
}
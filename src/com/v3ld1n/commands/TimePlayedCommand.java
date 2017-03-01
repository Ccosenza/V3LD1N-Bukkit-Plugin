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
        if (sendNotPlayerMessage(sender)) return true;
        final Player player = (Player) sender;

        final Player playerViewing;
        if (args.length == 0) {
            // No player argument, command user is player
            playerViewing = player;
        } else if (args.length == 1 && PlayerUtil.getOnlinePlayer(args[0]) != null) {
            // Player is first argument
            playerViewing = PlayerUtil.getOnlinePlayer(args[0]);
        } else {
            this.sendUsage(sender);
            return true;
        }

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard scoreboard = manager.getNewScoreboard();
        final Objective objective = createObjective(playerViewing, scoreboard);

        displaySidebar(player, scoreboard, objective);

        RepeatableRunnable updateTask = new RepeatableRunnable() {
            @Override
            public void onRun() {
                updateTime(objective, playerViewing, player);
            }
        };
        updateTask.start(5, 5, SECONDS * 4);
        return true;
    }

    // Creates the objective and sets the name and display slot
    private Objective createObjective(Player playerViewing, Scoreboard scoreboard) {
        String name = PREFIX + "timeplayed";
        if (name.length() > 16) {
            name = name.substring(0, 16);
        }
        Objective objective = scoreboard.registerNewObjective(name, "dummy");
        String displayName = String.format(Message.get("timeplayed-title").toString(), playerViewing.getName());
        if (ChatColor.stripColor(displayName).length() > 16) {
            displayName = displayName.substring(0, 20);
        }
        objective.setDisplayName(displayName);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        return objective;
    }

    // Displays the sidebar to the player
    private void displaySidebar(Player player, Scoreboard scoreboard, Objective objective) {
        player.setScoreboard(scoreboard);
        Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
            @Override
            public void run() {
                objective.unregister();
            }
        }, SECONDS * 20);
    }

    // Sends the message to the player, and updates times on the scoreboard
    private void updateTime(Objective objective, Player player, Player to) {
        int ticks = PlayerUtil.getTicksPlayed(player);
        int seconds = PlayerUtil.getSecondsPlayed(player);
        int minutes = PlayerUtil.getMinutesPlayed(player);
        int hours = PlayerUtil.getHoursPlayed(player);
        int days = PlayerUtil.getDaysPlayed(player);
        int weeks = PlayerUtil.getWeeksPlayed(player);
        String time = TimeUtil.fromSeconds(seconds);
        Message.get("timeplayed-time").aSendF(to, time);
        objective.getScore(Message.get("timeplayed-ticks").toString()).setScore(ticks);
        if (seconds > 0) {
            objective.getScore(Message.get("timeplayed-seconds").toString()).setScore(seconds);
        }
        if (minutes > 0) {
            objective.getScore(Message.get("timeplayed-minutes").toString()).setScore(minutes);
        }
        if (hours > 0) {
            objective.getScore(Message.get("timeplayed-hours").toString()).setScore(hours);
        }
        if (days > 0) {
            objective.getScore(Message.get("timeplayed-days").toString()).setScore(days);
        }
        if (weeks > 0) {
            objective.getScore(Message.get("timeplayed-weeks").toString()).setScore(weeks);
        }
    }
}
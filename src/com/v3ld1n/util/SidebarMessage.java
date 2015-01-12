package com.v3ld1n.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.V3LD1N;

public class SidebarMessage {
    final int TITLE_CHARACTER_LIMIT = 32;
    final int LINE_CHARACTER_LIMIT = 40;

    String title;
    List<String> lines = new ArrayList<>();
    final String PREFIX = ConfigSetting.SCOREBOARD_PREFIX.getString();

    public SidebarMessage(String title, String... lines) {
        this.title = title;
        if (title.length() > TITLE_CHARACTER_LIMIT) {
            this.title = title.substring(0, TITLE_CHARACTER_LIMIT);
        }
        for (String line : lines) {
            if (line.length() > LINE_CHARACTER_LIMIT) {
                line = line.substring(0, LINE_CHARACTER_LIMIT);
            }
            this.lines.add(line);
        }
    }

    public SidebarMessage(String title) {
        this.title = title;
        if (title.length() > TITLE_CHARACTER_LIMIT) {
            this.title = title.substring(0, TITLE_CHARACTER_LIMIT);
        }
    }

    public String getTitle() {
        return this.title;
    }

    public List<String> getLines() {
        return this.lines;
    }

    public String getLine(int line) {
        return this.lines.get(line);
    }

    public void setTitle(String title) {
        this.title = title;
        if (title.length() > TITLE_CHARACTER_LIMIT) {
            this.title = title.substring(0, TITLE_CHARACTER_LIMIT);
        }
    }

    public void setLines(List<String> lines) {
        for (String line : lines) {
            if (line.length() > LINE_CHARACTER_LIMIT) {
                String substring = line.substring(0, LINE_CHARACTER_LIMIT);
                lines.set(lines.indexOf(line), substring);
            }
        }
        this.lines = lines;
    }

    public void setLine(int line, String text) {
        String newText = text;
        if (text.length() > LINE_CHARACTER_LIMIT) {
            newText = text.substring(0, LINE_CHARACTER_LIMIT);
        }
        this.lines.set(line, newText);
    }

    public void addLine(String text) {
        String newText = text;
        if (text.length() > LINE_CHARACTER_LIMIT) {
            newText = text.substring(0, LINE_CHARACTER_LIMIT);
        }
        this.lines.add(newText);
    }

    public void display(Player player, long ticks) {
        int scoreValue = lines.size();
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        String name = PREFIX + title;
        if (name.length() > 16) {
            name = name.substring(0, 16);
        }
        String displayName = StringUtil.formatText(title.replaceAll("_", " "));
        Objective objective = board.registerNewObjective(name, "dummy");
        objective.setDisplayName(displayName);
        for (String line : lines) {
            line = StringUtil.formatText(line.replaceAll("_", " "));
            Score score = objective.getScore(line);
            score.setScore(scoreValue);
            scoreValue--;
        }
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
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
        }, ticks);
    }
}
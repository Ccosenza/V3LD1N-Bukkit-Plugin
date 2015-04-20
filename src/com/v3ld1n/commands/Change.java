package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Change {
    private long time;
    private String player;
    private String change;

    public Change(long time, String player, String change) {
        this.time = time;
        this.player = player;
        this.change = change;
    }

    public long getTime() {
        return time;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(player);
    }

    public String getChange() {
        return change;
    }
}
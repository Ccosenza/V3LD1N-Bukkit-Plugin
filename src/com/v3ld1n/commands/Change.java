package com.v3ld1n.commands;

public class Change {
    private long time;
    private String player;
    private String change;
    private ChangelogDay day;

    public Change(long time, String player, String change) {
        this.time = time;
        this.player = player;
        this.change = change;
    }

    public long getTime() {
        return time;
    }

    public String getPlayer() {
        return player;
    }

    public String getChange() {
        return change;
    }

    public ChangelogDay getDay() {
        return day;
    }

    public void setDay(ChangelogDay day) {
        this.day = day;
    }
}
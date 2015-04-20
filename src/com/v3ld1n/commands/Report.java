package com.v3ld1n.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Report {
    private String title;
    private String senderName;
    private UUID senderUuid;
    private String reason;
    private long time;
    private List<UUID> read;

    public Report(String title, String senderName, UUID senderUuid, String reason, long time) {
        this.title = title;
        this.senderName = senderName;
        this.senderUuid = senderUuid;
        this.reason = reason;
        this.time = time;
        this.read = new ArrayList<>();
    }

    public Report(String title, String senderName, UUID senderUuid, String reason, long time, List<UUID> read) {
        this(title, senderName, senderUuid, reason, time);
        this.read = read;
    }

    public String getTitle() {
        return title;
    }

    public String getSenderName() {
        return senderName;
    }

    public UUID getSenderUuid() {
        return senderUuid;
    }

    public String getReason() {
        return reason;
    }

    public long getTime() {
        return time;
    }

    public boolean isRead() {
        return !read.isEmpty();
    }

    public boolean isReadBy(UUID uuid) {
        return read.contains(uuid);
    }

    public List<UUID> getReadPlayers() {
        return read;
    }

    public void setReadBy(UUID uuid) {
        this.read.add(uuid);
    }
}
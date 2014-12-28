package com.v3ld1n.commands;

import java.util.UUID;

public class Report {
    private String title;
    private String senderName;
    private UUID senderUuid;
    private String reason;
    private boolean read;

    public Report(String title, String senderName, UUID senderUuid, String reason) {
        this.title = title;
        this.senderName = senderName;
        this.senderUuid = senderUuid;
        this.reason = reason;
        this.read = false;
    }

    public Report(String title, String senderName, UUID senderUuid, String reason, boolean read) {
        this.title = title;
        this.senderName = senderName;
        this.senderUuid = senderUuid;
        this.reason = reason;
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

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
package com.v3ld1n.commands;

import org.bukkit.ChatColor;

public class CommandUsage {
    private String command;
    private String description;
    private String permission;

    public CommandUsage(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public CommandUsage(String command, String description, String permission) {
        this.command = command;
        this.description = description;
        this.permission = permission;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }

    @Override
    public String toString() {
        return command + ChatColor.GOLD + " - " + ChatColor.GRAY + description;
    }
}
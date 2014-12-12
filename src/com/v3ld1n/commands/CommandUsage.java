package com.v3ld1n.commands;

import org.bukkit.ChatColor;

public class CommandUsage {
    String command;
    String description;

    public CommandUsage(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return command + ChatColor.GOLD + " - " + ChatColor.GRAY + description;
    }
}
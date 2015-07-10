package com.v3ld1n.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandUsage {
    private V3LD1NCommand command;
    private String arguments;
    private String description;
    private String permission;

    public CommandUsage(V3LD1NCommand command, String arguments, String description) {
        this.command = command;
        this.arguments = arguments;
        this.description = description;
    }

    public CommandUsage(V3LD1NCommand command, String arguments, String description, String permission) {
        this.command = command;
        this.arguments = arguments;
        this.description = description;
        this.permission = permission;
    }

    public V3LD1NCommand getCommand() {
        return command;
    }

    public String getArguments() {
        return arguments;
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }

    public void send(CommandSender user) {
        if (this.permission == null || user.hasPermission(this.permission)) {
            user.sendMessage(" - /" + command.getBukkitCommand().getName() + " " + this.toString());
        }
    }

    @Override
    public String toString() {
        return arguments + ChatColor.GOLD + " - " + ChatColor.GRAY + description;
    }
}
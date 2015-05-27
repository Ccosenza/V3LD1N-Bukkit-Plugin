package com.v3ld1n.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.v3ld1n.Message;

public abstract class V3LD1NCommand implements CommandExecutor {
    private List<CommandUsage> usages = new ArrayList<>();

    public List<CommandUsage> getUsages() {
        return usages;
    }

    public void addUsage(String command, String description) {
        this.usages.add(new CommandUsage(this, command, description));
    }

    public void addUsage(String command, String description, String permission) {
        this.usages.add(new CommandUsage(this, command, description, permission));
    }

    public void sendUsage(CommandSender user, String commandLabel, Command command) {
        Message.COMMAND_USAGE_TITLE.sendF(user, "/" + command.getName());
        Message.COMMAND_USAGE_DESCRIPTION.sendF(user, command.getDescription());
        sendUsageNoTitle(user, commandLabel);
    }

    public void sendUsageNoTitle(CommandSender user, String commandLabel) {
        for (CommandUsage usage : usages) {
            usage.send(user, commandLabel);
        }
    }

    public void sendArgumentUsage(CommandSender user, String commandLabel, Command command, String argument) {
        Message.COMMAND_USAGE_TITLE.sendF(user, "/" + command.getName());
        for (CommandUsage usage : usages) {
            if (usage.getPermission() == null || user.hasPermission(usage.getPermission())) {
                if (usage.getArguments().equalsIgnoreCase(argument)) {
                    user.sendMessage(" - /" + commandLabel + " " + usage.toString());
                }
            }
        }
    }

    protected void sendPermissionMessage(CommandSender user) {
        Message.COMMAND_NO_PERMISSION.send(user);
    }

    protected void sendPlayerMessage(CommandSender user) {
        Message.COMMAND_NOT_PLAYER.send(user);
    }

    protected void sendInvalidPlayerMessage(CommandSender user) {
        Message.COMMAND_INVALID_PLAYER.send(user);
    }
}
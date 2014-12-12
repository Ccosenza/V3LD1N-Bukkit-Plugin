package com.v3ld1n.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.v3ld1n.Message;

public abstract class V3LD1NCommand implements CommandExecutor {
    List<CommandUsage> usages = new ArrayList<>();

    public List<CommandUsage> getUsages() {
        return usages;
    }

    public void addUsage(String command, String description) {
        this.usages.add(new CommandUsage(command, description));
    }

    public void sendUsage(CommandSender user, String commandLabel, String commandDescription) {
        user.sendMessage(String.format(Message.COMMAND_USAGE_TITLE.toString(), "/" + commandLabel));
        user.sendMessage(String.format(Message.COMMAND_USAGE_DESCRIPTION.toString(), commandDescription));
        for (CommandUsage usage : usages) {
            user.sendMessage(" - /" + commandLabel + " " + usage.toString());
        }
    }

    public void sendArgumentUsage(CommandSender user, String commandLabel, String argument) {
        user.sendMessage(String.format(Message.COMMAND_USAGE_TITLE.toString(), "/" + commandLabel));
        for (CommandUsage usage : usages) {
            if (usage.getCommand().equalsIgnoreCase(argument)) {
                user.sendMessage(" - /" + commandLabel + " " + usage.toString());
            }
        }
    }
}
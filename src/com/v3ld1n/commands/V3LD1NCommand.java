package com.v3ld1n.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;

public abstract class V3LD1NCommand implements CommandExecutor {
    private List<CommandUsage> usages = new ArrayList<>();

    public List<CommandUsage> getUsages() {
        return usages;
    }

    public void addUsage(String command, String description) {
        this.usages.add(new CommandUsage(command, description));
    }

    public void addUsage(String command, String description, String permission) {
        this.usages.add(new CommandUsage(command, description, permission));
    }

    public void sendUsage(CommandSender user, String commandLabel, Command command) {
        user.sendMessage(String.format(Message.COMMAND_USAGE_TITLE.toString(), "/" + command.getName()));
        user.sendMessage(String.format(Message.COMMAND_USAGE_DESCRIPTION.toString(), command.getDescription()));
        sendUsageNoTitle(user, commandLabel);
    }

    public void sendUsageNoTitle(CommandSender user, String commandLabel) {
        for (CommandUsage usage : usages) {
            if (usage.getPermission() == null || user.hasPermission(usage.getPermission())) {
                user.sendMessage(" - /" + commandLabel + " " + usage.toString());
            }
        }
    }

    public void sendArgumentUsage(CommandSender user, String commandLabel, Command command, String argument) {
        user.sendMessage(String.format(Message.COMMAND_USAGE_TITLE.toString(), "/" + command.getName()));
        for (CommandUsage usage : usages) {
            if (usage.getPermission() == null || user.hasPermission(usage.getPermission())) {
                if (usage.getCommand().equalsIgnoreCase(argument)) {
                    user.sendMessage(" - /" + commandLabel + " " + usage.toString());
                }
            }
        }
    }

    protected void message(CommandSender user, Message message) {
        user.sendMessage(message.toString());
    }

    protected void messageF(CommandSender user, Message message, Object... format) {
        user.sendMessage(String.format(message.toString(), format));
    }

    protected void aMessage(CommandSender user, Message message) {
        ChatUtil.sendMessage(user, message.toString(), 2);
    }

    protected void aMessageF(CommandSender user, Message message, Object... format) {
        ChatUtil.sendMessage(user, String.format(message.toString(), format), 2);
    }
}
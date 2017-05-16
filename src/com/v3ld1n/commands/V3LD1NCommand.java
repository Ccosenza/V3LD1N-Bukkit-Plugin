package com.v3ld1n.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ListType;

public abstract class V3LD1NCommand implements CommandExecutor {
    private PluginCommand bukkitCmd;
    private List<CommandUsage> usages = new ArrayList<>();

    /**
     * Returns the Bukkit command for this command
     * @return the Bukkit command
     */
    public PluginCommand getBukkitCommand() {
        return bukkitCmd;
    }

    /**
     * Sets the Bukkit command for this command
     * @param name the Bukkit command
     */
    public void setBukkitCommand(PluginCommand bukkitCmd) {
        this.bukkitCmd = bukkitCmd;
    }

    /**
     * Returns the command's usages
     * @return the command's usages
     */
    public List<CommandUsage> getUsages() {
        return usages;
    }

    /**
     * Adds a usage
     * @param usage the command usage
     * @param description a description of the usage
     */
    public void addUsage(String usage, String description) {
        this.usages.add(new CommandUsage(this, usage, description));
    }

    /**
     * Adds a usage with a required permission
     * @param usage the command usage
     * @param description a description of the usage
     * @param permission the required permission
     */
    public void addUsage(String usage, String description, String permission) {
        this.usages.add(new CommandUsage(this, usage, description, permission));
    }

    /**
     * Sends the command usage to a user
     * @param user the user
     */
    public void sendUsage(CommandSender user) {
        user.sendMessage("");
        Message.get("command-usage-title").sendF(user, "/" + bukkitCmd.getName());
        List<String> aliases = getAliasesWithSlash(bukkitCmd);
        if (!aliases.isEmpty()) {
            ChatUtil.sendList(user, Message.get("command-usage-aliases").toString(), aliases, ListType.SHORT);
        }
        Message.get("command-usage-description").sendF(user, bukkitCmd.getDescription());
        sendUsageNoTitle(user);
    }

    /**
     * Sends the command usage without the name, description, and aliases to a user
     * @param user the user
     */
    public void sendUsageNoTitle(CommandSender user) {
        for (CommandUsage usage : usages) {
            usage.send(user);
        }
    }

    /**
     * Returns a command's aliases with a slash at the beginning
     * @param cmd the command
     * @return the command's aliases
     */
    private List<String> getAliasesWithSlash(PluginCommand cmd) {
        List<String> aliases = new ArrayList<>();
        for (String alias : cmd.getAliases()) {
            aliases.add("/" + alias);
        }
        return aliases;
    }

    /**
     * Sends the "no permission" message to a user
     * @param user the user
     * @param permission the required permission
     * @return whether the message was sent
     */
    protected boolean sendPermissionMessage(CommandSender user, String permission) {
        if (!user.hasPermission(permission)) {
            Message.get("command-permission").send(user);
            return true;
        }
        return false;
    }

    /**
     * Sends the "not a player" message to a user
     * @param user the user
     * @return whether the message was sent
     */
    protected boolean sendNotPlayerMessage(CommandSender user) {
        if (!(user instanceof Player)) {
            Message.get("command-not-player").send(user);
            return true;
        }
        return false;
    }

    /**
     * Sends the "invalid player" message to a user
     * @param user the user
     */
    protected void sendInvalidPlayerMessage(CommandSender user) {
        Message.get("command-invalid-player").send(user);
    }
}
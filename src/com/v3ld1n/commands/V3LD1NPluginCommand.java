package com.v3ld1n.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ConfigAccessor;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class V3LD1NPluginCommand extends V3LD1NCommand {
    private final static int HELP_PAGE_SIZE = 9;

    public V3LD1NPluginCommand() {
        this.addUsage("help [page]", "Show a list of all plugin commands");
        this.addUsage("reload", "Reload the plugin config");
        this.addUsage("version", "Send the plugin version");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.owner")) {
            if (args.length == 0) {
                this.sendUsage(sender);
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                boolean pageExists = false;
                if (args.length == 1) {
                    pageExists = displayHelp(sender, 1);
                } else if (args.length == 2 && StringUtil.isInteger(args[1])) {
                    pageExists = displayHelp(sender, StringUtil.toInteger(args[1], 1));
                }
                if (!pageExists) {
                    this.sendUsage(sender);
                }
                return true;
            } else if (args[0].equalsIgnoreCase("reload") && args.length == 1) {
                for (ConfigAccessor accessor : V3LD1N.getConfigs()) {
                    accessor.reloadConfig();
                }
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    String header = ConfigSetting.PLAYER_LIST_HEADER.getString();
                    String footer = ConfigSetting.PLAYER_LIST_FOOTER.getString();
                    if (header != null && footer != null) {
                        PlayerUtil.sendPlayerListHeaderFooter(p, header, footer);
                    }
                }
                Message.get("v3ld1nplugin-reload").aSend(sender);
                return true;
            } else if (args[0].equalsIgnoreCase("version") && args.length == 1) {
                PluginDescriptionFile desc = V3LD1N.getPlugin().getDescription();
                Message.get("v3ld1nplugin-version").sendF(sender, desc.getName(), desc.getVersion());
                return true;
            }
        } else {
            sendPermissionMessage(sender);
            return true;
        }
        this.sendUsage(sender);
        return true;
    }

    // Sends the command list to the sender, returns true if the entered page exists
    private static boolean displayHelp(CommandSender user, int pageNumber) {
        List<V3LD1NCommand> commands = new ArrayList<>(V3LD1N.getCommands());
        List<CommandUsage> allUsages = new ArrayList<>();
        for (V3LD1NCommand command : commands) {
            allUsages.addAll(command.getUsages());
        }
        List<CommandUsage> usagePage = ChatUtil.getPage(allUsages, pageNumber, HELP_PAGE_SIZE);
        int pageCount = ChatUtil.getNumberOfPages(allUsages, HELP_PAGE_SIZE);
        if (pageNumber > pageCount || pageNumber < 1) {
            return false;
        }
        Message.get("v3ld1nplugin-help-border-top").sendF(user, pageNumber, pageCount);
        for (CommandUsage usage : usagePage) {
            usage.send(user);
        }
        Message.get("v3ld1nplugin-help-border-bottom").send(user);
        return true;
    }
}
package com.v3ld1n.commands;

import java.util.ArrayList;
import java.util.Collections;
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
        this.addUsage("debug", "Toggle debug mode");
        this.addUsage("help", "Show a list of all plugin commands");
        this.addUsage("reload", "Reload the plugin config");
        this.addUsage("version", "Send the plugin version");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.owner")) {
            if (args.length == 0) {
                this.sendUsage(sender, label, command);
                return true;
            }
            if (args[0].equalsIgnoreCase("debug") && args.length == 1) {
                boolean debug = ConfigSetting.DEBUG.getBoolean();
                Message message = debug ? Message.V3LD1NPLUGIN_DISABLE_DEBUG : Message.V3LD1NPLUGIN_ENABLE_DEBUG;
                ConfigSetting.DEBUG.toggle();
                message.aSend(sender);
                return true;
            } else if (args[0].equalsIgnoreCase("help")) {
                if (args.length == 1) {
                    displayHelp(sender, 1);
                } else if (args.length == 2 && StringUtil.isInteger(args[1])) {
                    displayHelp(sender, StringUtil.toInteger(args[1], 1));
                } else {
                    this.sendUsage(sender, label, command);
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
                Message.V3LD1NPLUGIN_RELOAD.aSend(sender);
                return true;
            } else if (args[0].equalsIgnoreCase("version") && args.length == 1) {
                PluginDescriptionFile desc = V3LD1N.getPlugin().getDescription();
                Message.V3LD1NPLUGIN_VERSION.aSendF(sender, desc.getName(), desc.getVersion());
                return true;
            }
        } else {
            sendPermissionMessage(sender);
            return true;
        }
        this.sendUsage(sender, label, command);
        return true;
    }

    private static void displayHelp(CommandSender user, int page) {
        List<String> commands = new ArrayList<>(V3LD1N.getCommands().keySet());
        Collections.sort(commands);
        List<CommandUsage> allUsages = new ArrayList<>();
        for (String command : commands) {
            allUsages.addAll(V3LD1N.getCommands().get(command).getUsages());
        }
        List<CommandUsage> usagePage = ChatUtil.getPage(allUsages, page, HELP_PAGE_SIZE);
        int pages = ChatUtil.getNumberOfPages(allUsages, HELP_PAGE_SIZE);
        Message.V3LD1NPLUGIN_HELP_BORDER_TOP.sendF(user, page, pages);
        for (CommandUsage usage : usagePage) {
            for (String commandName : commands) {
                if (V3LD1N.getCommands().get(commandName).equals(usage.getCommand())) {
                    usage.send(user, commandName);
                }
            }
        }
        Message.V3LD1NPLUGIN_HELP_BORDER_BOTTOM.send(user);
    }
}
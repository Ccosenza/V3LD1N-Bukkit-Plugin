package com.v3ld1n.commands;

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
import com.v3ld1n.util.ConfigUtil;
import com.v3ld1n.util.PlayerUtil;

public class V3LD1NPluginCommand extends V3LD1NCommand {
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
                ConfigUtil.toggleSetting(ConfigSetting.DEBUG);
                ChatUtil.sendMessage(sender, message.toString(), 2);
                return true;
            } else if (args[0].equalsIgnoreCase("help") && args.length == 1) {
                Message.V3LD1NPLUGIN_HELP.send(sender);
                for (String v3ld1ncommand : V3LD1N.getCommands().keySet()) {
                    V3LD1N.getCommands().get(v3ld1ncommand).sendUsageNoTitle(sender, v3ld1ncommand);
                }
                return true;
            } else if (args[0].equalsIgnoreCase("reload") && args.length == 1) {
                for (ConfigAccessor accessor : V3LD1N.getConfigs()) {
                    accessor.reloadConfig();
                }
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if (ConfigSetting.PLAYER_LIST_HEADER.getString() != null && ConfigSetting.PLAYER_LIST_FOOTER.getString() != null) {
                        PlayerUtil.sendPlayerListHeaderFooter(p, ConfigSetting.PLAYER_LIST_HEADER.getString(), ConfigSetting.PLAYER_LIST_FOOTER.getString());
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
}
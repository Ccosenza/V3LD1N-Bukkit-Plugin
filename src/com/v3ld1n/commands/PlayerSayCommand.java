package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.v3ld1n.Message;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class PlayerSayCommand extends V3LD1NCommand {
    private final String DEFAULT_GROUP = "Veldian";

    public PlayerSayCommand() {
        this.addUsage("<player> <message ...>", "Send a chat message as a player");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.owner")) {
            if (args.length >= 2) {
                String message = StringUtil.fromArray(args, 1);
                String namePrefix = "";
                if (PlayerUtil.getOnlinePlayer(args[0]) != null) {
                    Player p = PlayerUtil.getOnlinePlayer(args[0]);
                    p.chat(message);
                    return true;
                } else if (Bukkit.getPluginManager().getPlugin("PermissionsEx") != null && PlayerUtil.getOfflinePlayer(args[0]) != null) {
                    String name = PlayerUtil.getOfflinePlayer(args[0]).getName();
                    PermissionUser user = PermissionsEx.getUser(name);
                    String prefix = user.getPrefix();
                    if (prefix.equals("")) {
                        prefix = PermissionsEx.getPermissionManager().getGroup(DEFAULT_GROUP).getPrefix();
                    }
                    prefix = StringUtil.formatText(prefix);
                    namePrefix = prefix + name;
                } else {
                    namePrefix = args[0];
                }
                Bukkit.broadcastMessage("<" + namePrefix + "> " + message);
                return true;
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}
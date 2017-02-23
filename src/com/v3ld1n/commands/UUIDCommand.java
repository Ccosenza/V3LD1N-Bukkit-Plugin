package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.PlayerUtil;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class UUIDCommand extends V3LD1NCommand {
    public UUIDCommand() {
        this.addUsage("", "Display your UUID");
        this.addUsage("<player>", "Display another player's UUID");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String uuid = "";
        String name = "";
        if (args.length == 0) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                uuid = p.getUniqueId().toString();
                name = p.getName();
            } else {
                sendPlayerMessage(sender);
                return true;
            }
        } else if (args.length == 1) {
            if (PlayerUtil.getUuid(args[0], true) != null) {
                uuid = PlayerUtil.getUuid(args[0], true).toString();
                name = args[0];
            } else {
                sendInvalidPlayerMessage(sender);
                return true;
            }
        } else {
            this.sendUsage(sender);
            return true;
        }
        if (sender instanceof Player) {
            TextComponent message = new TextComponent(uuid);
            message.setColor(ChatColor.YELLOW);
            String hoverFormat = String.format(Message.get("uuid-hover").toString(), name);
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverFormat).create()));
            message.setInsertion(uuid);
            ((Player) sender).spigot().sendMessage(message);
            return true;
        }
        sender.sendMessage(uuid + " (" + name + ")");
        return true;
    }
}
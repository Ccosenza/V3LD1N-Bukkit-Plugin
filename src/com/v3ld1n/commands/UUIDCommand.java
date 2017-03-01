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
        String name = "";
        if (args.length == 0 && sender instanceof Player) {
            // No player argument, command user is player
            name = sender.getName();
        } else if (args.length == 1 && PlayerUtil.getUuid(args[0], true) != null) {
            // Player is second argument
            name = args[0];
        } else {
            // Player doesn't exist
            sendInvalidPlayerMessage(sender);
            return true;
        }

        String uuid = PlayerUtil.getUuid(name, true).toString();
        send(uuid, name, sender);
        return true;
    }

    // Sends the UUID to the command user
    private void send(String uuid, String name, CommandSender user) {
        if (user instanceof Player) {
            TextComponent message = new TextComponent(uuid);
            message.setColor(ChatColor.YELLOW);
            String hoverMessage = Message.get("uuid-hover").toString().replaceAll("%newline%", "\n");
            String hoverFormat = String.format(hoverMessage, name);
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverFormat).create()));
            message.setInsertion(uuid);
            ((Player) user).spigot().sendMessage(message);
            return;
        }
        Message.get("uuid-console").sendF(user, uuid, name);
    }
}
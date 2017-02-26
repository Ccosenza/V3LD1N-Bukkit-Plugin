package com.v3ld1n.commands;

import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class SetHealthCommand extends V3LD1NCommand {
    public SetHealthCommand() {
        this.addUsage("<health>", "Set your health");
        this.addUsage("<health> <player>", "Set a player's health");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendPermissionMessage(sender, "v3ld1n.sethealth")) return true;
        
        if (args.length != 1 && args.length != 2) {
            this.sendUsage(sender);
            return true;
        }

        if (!StringUtil.isDouble(args[0])) {
            this.sendUsage(sender);
            return true;
        }
        double health = Double.parseDouble(args[0]);

        Player player;
        if (args.length == 1 && sender instanceof Player) {
            player = (Player) sender;
        } else if (args.length == 2 && PlayerUtil.getOnlinePlayer(args[1]) != null) {
            player = PlayerUtil.getOnlinePlayer(args[1]);
        } else {
            sendInvalidPlayerMessage(sender);
            return true;
        }

        set(player, health, sender);
        return true;
    }

    private void set(Player player, double health, CommandSender user) {
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        if (health < 0 || health > maxHealth) {
            Message.get("sethealth-limit").sendF(user, maxHealth);
            return;
        }
        player.setHealth(health);
        boolean playerIsSender = player.getName().equals(user.getName());
        Message ownMessage = Message.get("sethealth-set");
        Message otherMessage = Message.get("sethealth-set-other");
        Message message = playerIsSender ? ownMessage : otherMessage;
        message.aSendF(user, player.getName(), health);
    }
}
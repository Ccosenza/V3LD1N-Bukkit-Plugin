package com.v3ld1n.commands;

import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class SetMaxHealthCommand extends V3LD1NCommand {
    public SetMaxHealthCommand() {
        this.addUsage("<health>", "Set your maximum health");
        this.addUsage("<health> <player>", "Set a player's maximum health");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendPermissionMessage(sender, "v3ld1n.setmaxhealth")) return true;
        
        if (args.length != 1 && args.length != 2) {
            this.sendUsage(sender);
            return true;
        }

        if (!StringUtil.isDouble(args[0])) {
            this.sendUsage(sender);
            return true;
        }
        int health = Integer.parseInt(args[0]);

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

    private void set(Player player, int health, CommandSender user) {
        if (health < 0) {
            this.sendUsage(user);
            return;
        }
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        boolean playerIsSender = player.getName().equals(user.getName());
        Message ownMessage = Message.get("setmaxhealth-set");
        Message otherMessage = Message.get("setmaxhealth-set-other");
        Message message = playerIsSender ? ownMessage : otherMessage;
        message.aSendF(user, player.getName(), health);
    }
}
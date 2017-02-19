package com.v3ld1n.commands;

import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.MessageType;
import com.v3ld1n.util.PlayerUtil;

public class SetMaxHealthCommand extends V3LD1NCommand {
    private static final double LIMIT = 32000;

    public SetMaxHealthCommand() {
        this.addUsage("<health>", "Set your maximum health");
        this.addUsage("<health> <player>", "Set a player's maximum health");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.setmaxhealth")) {
            int l = args.length;
            if (l == 1 || l == 2) {
                double health;
                try {
                    health = Double.parseDouble(args[0]);
                } catch (IllegalArgumentException e) {
                    this.sendUsage(sender);
                    return true;
                }
                Player p;
                if (l == 1 && sender instanceof Player) {
                    p = (Player) sender;
                } else if (l == 2 && PlayerUtil.getOnlinePlayer(args[1]) != null) {
                    p = PlayerUtil.getOnlinePlayer(args[1]);
                } else {
                    sendInvalidPlayerMessage(sender);
                    return true;
                }
                if (health > 0 && health <= LIMIT) {
                    p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
                    boolean pIsSender = p.getName().equals(sender.getName());
                    String ownMessage = String.format(Message.get("setmaxhealth-set").toString(), args[0]);
                    String otherMessage = String.format(Message.get("setmaxhealth-set-other").toString(), p.getName(), args[0]);
                    String message = pIsSender ? ownMessage : otherMessage;
                    ChatUtil.sendMessage(sender, message, MessageType.ACTION_BAR);
                    return true;
                }
                Message.get("setmaxhealth-limit").sendF(sender, (int) LIMIT);
                return true;
            }
            this.sendUsage(sender);
            return true;
        }
        sendPermissionMessage(sender);
        return true;
    }
}
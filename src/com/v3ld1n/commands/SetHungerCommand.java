package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.MessageType;
import com.v3ld1n.util.PlayerUtil;

public class SetHungerCommand extends V3LD1NCommand {
    public SetHungerCommand() {
        this.addUsage("<hunger> hunger", "Set your hunger level");
        this.addUsage("<hunger> exhaustion", "Set your exhaustion level");
        this.addUsage("<hunger> saturation", "Set your saturation level");
        this.addUsage("<hunger> hunger <player>", "Set a player's hunger level");
        this.addUsage("<hunger> exhaustion <player>", "Set a player's exhaustion level");
        this.addUsage("<hunger> saturation <player>", "Set a player's saturation level");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.sethunger")) {
            int l = args.length;
            if (l == 2 || l == 3) {
                int foodLevel;
                try {
                    foodLevel = Integer.parseInt(args[0]);
                } catch (IllegalArgumentException e) {
                    this.sendUsage(sender);
                    return true;
                }
                Player p;
                if (l == 2 && sender instanceof Player) {
                    p = (Player) sender;
                } else if (l == 3 && PlayerUtil.getOnlinePlayer(args[2]) != null) {
                    p = PlayerUtil.getOnlinePlayer(args[2]);
                } else {
                    sendInvalidPlayerMessage(sender);
                    return true;
                }
                if (foodLevel >= 0 && foodLevel <= 20) {
                    switch (args[1].toLowerCase()) {
                    case "hunger":
                        p.setFoodLevel(foodLevel);
                        break;
                    case "exhaustion":
                        p.setExhaustion(foodLevel);
                        break;
                    case "saturation":
                        p.setSaturation(foodLevel);
                        break;
                    default:
                        this.sendUsage(sender);
                        return true;
                    }
                    boolean pIsSender = p.getName().equals(sender.getName());
                    String ownMessage = String.format(Message.get("sethunger-set").toString(), args[1], args[0]);
                    String otherMessage = String.format(Message.get("sethunger-set-other").toString(), p.getName(), args[1], args[0]);
                    String message = pIsSender ? ownMessage : otherMessage;
                    ChatUtil.sendMessage(sender, message, MessageType.ACTION_BAR);
                    return true;
                }
                Message.get("sethunger-limit").send(sender);
                return true;
            }
            this.sendUsage(sender);
            return true;
        }
        sendPermissionMessage(sender);
        return true;
    }
}
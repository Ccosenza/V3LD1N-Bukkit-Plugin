package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.PlayerUtil;

public class SetHotbarSlotCommand extends V3LD1NCommand {
    public SetHotbarSlotCommand() {
        this.addUsage("<slot>", "Set your selected hotbar slot");
        this.addUsage("<slot> <player>", "Set a player's selected hotbar slot");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.sethotbarslot")) {
            if (args.length > 0) {
                Player p;
                if (args.length == 1) {
                    if (sender instanceof Player) {
                        p = (Player) sender;
                    } else {
                        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
                        return true;
                    }
                } else if (args.length == 2) {
                    if (PlayerUtil.getOnlinePlayer(args[1]) != null) {
                        p = PlayerUtil.getOnlinePlayer(args[1]);
                    } else {
                        sender.sendMessage(Message.COMMAND_INVALID_PLAYER.toString());
                        return true;
                    }
                } else {
                    this.sendUsage(sender, label, command);
                    return true;
                }
                try {
                    p.getInventory().setHeldItemSlot(Integer.parseInt(args[0]) - 1);
                    String message;
                    if (p.getName().equals(sender.getName())) {
                        message = String.format(Message.SETHOTBARSLOT_SET_OWN.toString(), args[0]);
                    } else {
                        message = String.format(Message.SETHOTBARSLOT_SET.toString(), p.getName(), args[0]);
                    }
                    ChatUtil.sendMessage(sender, message, 2);
                } catch (Exception e) {
                    sender.sendMessage(Message.SETHOTBARSLOT_INVALID_SLOT.toString());
                }
                return true;
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}

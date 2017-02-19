package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.MessageType;
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
                        sendPlayerMessage(sender);
                        return true;
                    }
                } else if (args.length == 2) {
                    if (PlayerUtil.getOnlinePlayer(args[1]) != null) {
                        p = PlayerUtil.getOnlinePlayer(args[1]);
                    } else {
                        sendInvalidPlayerMessage(sender);
                        return true;
                    }
                } else {
                    this.sendUsage(sender);
                    return true;
                }
                try {
                    p.getInventory().setHeldItemSlot(Integer.parseInt(args[0]) - 1);
                    boolean pIsSender = p.getName().equals(sender.getName());
                    String ownMessage = String.format(Message.get("sethotbarslot-set").toString(), args[0]);
                    String otherMessage = String.format(Message.get("sethotbarslot-set-other").toString(), p.getName(), args[0]);
                    String message = pIsSender ? ownMessage : otherMessage;
                    ChatUtil.sendMessage(sender, message, MessageType.ACTION_BAR);
                } catch (Exception e) {
                	Message.get("sethotbarslot-invalid-slot").send(sender);
                }
                return true;
            }
            this.sendUsage(sender);
            return true;
        }
        sendPermissionMessage(sender);
        return true;
    }
}

package com.v3ld1n.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.Message;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class SetHotbarSlotCommand extends V3LD1NCommand {
    public SetHotbarSlotCommand() {
        this.addUsage("<slot>", "Set your selected hotbar slot");
        this.addUsage("<slot> <player>", "Set a player's selected hotbar slot");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendPermissionMessage(sender, "v3ld1n.sethotbarslot")) return true;
        
        if (args.length != 1 && args.length != 2) {
            this.sendUsage(sender);
            return true;
        }

        if (!StringUtil.isInteger(args[0])) {
            this.sendUsage(sender);
            return true;
        }
        int slot = Integer.parseInt(args[0]);

        Player player;
        if (args.length == 1 && sender instanceof Player) {
            // No player argument, command user is player
            player = (Player) sender;
        } else if (args.length == 2 && PlayerUtil.getOnlinePlayer(args[1]) != null) {
            // Player is second argument
            player = PlayerUtil.getOnlinePlayer(args[1]);
        } else {
            // Player doesn't exist
            sendInvalidPlayerMessage(sender);
            return true;
        }

        set(player, slot, sender);
        return true;
    }

    private void set(Player player, int slot, CommandSender user) {
        if (slot < 1 || slot > 9) {
            Message.get("sethotbarslot-invalid-slot").send(user);
            return;
        }
        player.getInventory().setHeldItemSlot(slot - 1);
        boolean playerIsSender = player.getName().equals(user.getName());
        Message ownMessage = Message.get("sethotbarslot-set");
        Message otherMessage = Message.get("sethotbarslot-set-other");
        Message message = playerIsSender ? ownMessage : otherMessage;
        message.aSendF(user, player.getName(), slot);
    }
}

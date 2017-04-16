package com.v3ld1n.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.Message;
import com.v3ld1n.util.ItemUtil;

public class HideFlagsCommand extends V3LD1NCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendPermissionMessage(sender, "v3ld1n.owner")) return true;
        if (sendNotPlayerMessage(sender)) return true;
        Player player = (Player) sender;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            Message.get("command-no-item").send(player);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("unhide")) {
            unhide(item, player, sender);
            return true;
        }

        hide(item, player, sender);
        return true;
    }

    // Hides flags for the player's item
    private void hide(ItemStack item, Player player, CommandSender user) {
        ItemStack hiddenFlagsItem = ItemUtil.hideFlags(item);
        player.getInventory().setItemInMainHand(hiddenFlagsItem);
        Message.get("hideflags-hide").send(player);
    }

    // Unhides flags for the player's item
    private void unhide(ItemStack item, Player player, CommandSender user) {
        ItemStack unhiddenFlagsItem = ItemUtil.resetHideFlags(item);
        player.getInventory().setItemInMainHand(unhiddenFlagsItem);
        Message.get("hideflags-unhide").send(player);
    }
}
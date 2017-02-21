package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.Message;
import com.v3ld1n.util.StringUtil;

public class GiveAllCommand extends V3LD1NCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("v3ld1n.owner")) {
            sendPermissionMessage(sender);
            return true;
        }

        if (!(sender instanceof Player)) {
            sendPlayerMessage(sender);
            return true;
        }
        Player player = (Player) sender;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            Message.get("command-no-item").send(player);
            return true;
        }

        giveAll(player, item);
        return true;
    }

    private void giveAll(Player player, ItemStack item) {
        int amount = item.getAmount();
        for (Player otherPlayer : Bukkit.getServer().getOnlinePlayers()) {
            otherPlayer.getInventory().addItem(item);
        }
        String itemName = StringUtil.getItemName(item);
        Message.get("giveall-give").aSendF(player, amount, itemName);
    }
}
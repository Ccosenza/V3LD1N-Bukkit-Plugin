package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;

public class GiveAllCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.isOp()) {
                Player p = (Player) sender;
                ItemStack item = p.getItemInHand();
                if (item.getType() != Material.AIR) {
                    int amount = item.getAmount();
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        player.getInventory().addItem(item);
                    }
                    String itemString = item.getType().toString().toLowerCase();
                    itemString = itemString.replaceAll("_", " ");
                    ChatUtil.sendMessage(p, String.format(Message.GIVEALL_GIVE.toString(), amount, itemString), 2);
                    return true;
                }
                ChatUtil.sendMessage(p, Message.GIVEALL_NO_ITEM.toString(), 2);
                return true;
            }
            sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}
package com.v3ld1n.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.v3ld1n.Message;

public class PlayersCommand extends V3LD1NCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("v3ld1n.players")) {
                Player p = (Player) sender;
                Inventory inv = Bukkit.createInventory(null, 27, "Players");
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                    SkullMeta meta = (SkullMeta) head.getItemMeta();
                    meta.setOwner(player.getName());
                    head.setItemMeta(meta);
                    inv.addItem(head);
                    p.openInventory(inv);
                }
                return true;
            }
            sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}

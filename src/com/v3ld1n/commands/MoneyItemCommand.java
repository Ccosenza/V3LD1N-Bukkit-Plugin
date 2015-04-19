package com.v3ld1n.commands;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.v3ld1n.Message;

public class MoneyItemCommand extends V3LD1NCommand {
    public MoneyItemCommand() {
        this.addUsage("<amount>", "Creates a money item");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 1) {
                double amount;
                try {
                    amount = Double.parseDouble(args[0]);
                } catch (IllegalArgumentException e) {
                    this.sendUsage(sender, label, command);
                    return true;
                }
                ItemStack item = new ItemStack(Material.EMERALD);
                ItemMeta meta = item.getItemMeta();
                DecimalFormat df = new DecimalFormat("0.##");
                meta.setDisplayName("§r§e" + df.format(amount) + " §6Velds");
                List<String> lore = new ArrayList<>();
                lore.add(Message.VELDS_LORE.toString());
                meta.setLore(lore);
                meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, false);
                item.setItemMeta(meta);
                p.getInventory().addItem(item);
                return true;
            }
            this.sendUsage(sender, label, command);
            return true;
        }
        sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
        return true;
    }
}
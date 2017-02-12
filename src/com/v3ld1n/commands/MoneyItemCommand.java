package com.v3ld1n.commands;

import java.text.DecimalFormat;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.Message;
import com.v3ld1n.util.ItemUtil;

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
                    this.sendUsage(sender);
                    return true;
                }
                if (amount < 0) {
                	this.sendUsage(sender);
                	return true;
                }
                ItemStack item = new ItemStack(Material.EMERALD);
                DecimalFormat df = new DecimalFormat("0.##");
                String moneyName = amount == 1 ? Message.MONEY_NAME.toString() : Message.MONEY_NAME_PLURAL.toString();
                ItemUtil.setName(item, "§e" + df.format(amount) + " " + moneyName);
                ItemUtil.addLore(item, Message.MONEY_LORE.toString());
                ItemUtil.addEnchantment(item, Enchantment.PROTECTION_ENVIRONMENTAL, 10);
                item = ItemUtil.hideFlags(item);
                p.getInventory().addItem(item);
                return true;
            }
            this.sendUsage(sender);
            return true;
        }
        sendPlayerMessage(sender);
        return true;
    }
}
package com.v3ld1n.commands;

import java.text.DecimalFormat;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.util.ItemUtil;
import com.v3ld1n.util.StringUtil;

public class MoneyItemCommand extends V3LD1NCommand {
    public MoneyItemCommand() {
        this.addUsage("<amount>", "Creates a money item");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendPermissionMessage(sender, "v3ld1n.owner")) return true;
        if (sendNotPlayerMessage(sender)) return true;
        Player player = (Player) sender;

        if (args.length != 1) {
            this.sendUsage(sender);
            return true;
        }

        if (!StringUtil.isDouble(args[0])) {
            this.sendUsage(sender);
            return true;
        }

        double amount = Double.parseDouble(args[0]);
        if (amount < 0) {
            this.sendUsage(sender);
            return true;
        }
        
        create(amount, player);
        return true;
    }

    private void create(double amount, Player player) {
        ItemStack item = new ItemStack(Material.valueOf(ConfigSetting.MONEY_ITEM_ITEM.getString()));
        DecimalFormat df = new DecimalFormat("0.##");
        String moneyName = amount == 1 ? Message.get("money-name").toString() : Message.get("money-name-plural").toString();
        ItemUtil.setName(item, "§e" + df.format(amount) + " " + moneyName);
        ItemUtil.addLore(item, Message.get("money-lore").toString());
        ItemUtil.addEnchantment(item, Enchantment.DURABILITY, 10);
        item = ItemUtil.hideFlags(item);
        player.getInventory().addItem(item);
    }
}
package com.v3ld1n.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.Message;
import com.v3ld1n.util.ItemUtil;
import com.v3ld1n.util.StringUtil;

public class ItemLoreCommand extends V3LD1NCommand {
    public ItemLoreCommand() {
        this.addUsage("set <line> <lore>", "Set a line of the lore of the item you're holding");
        this.addUsage("add <lore>", "Add a line to the lore of the item you're holding");
        this.addUsage("remove", "Remove the lore of the item you're holding");
        this.addUsage("remove <line>", "Remove a line from the lore of the item you're holding");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendPermissionMessage(sender, "v3ld1n.itemlore")) return true;
        if (sendNotPlayerMessage(sender)) return true;
        Player player = (Player) sender;

        if (args.length == 0) {
            this.sendUsage(player);
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            Message.get("command-no-item").send(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 1) removeAll(item, player);
            if (args.length == 2) removeLine(item, args[1], player);
            return true;
        }

        if (args.length >= 2 && args[0].equalsIgnoreCase("add")) {
            String lore = StringUtil.formatText(StringUtil.fromArray(args, 1));
            add(item, lore, player);
            return true;
        } else if (args.length >= 3 && args[0].equalsIgnoreCase("set")) {
            String lore = StringUtil.formatText(StringUtil.fromArray(args, 2));
            set(item, args[1], lore, player);
            return true;
        }
        this.sendUsage(player);
        return true;
    }

    // Adds a line to the item's lore
    private void add(ItemStack item, String lore, Player player) {
        ItemUtil.addLore(item, lore);
        Message.get("itemlore-add").sendF(player, lore);
    }

    // Sets a line of the item's lore
    private void set(ItemStack item, String line, String lore, Player player) {
        try {
            int lineNumber = Integer.parseInt(line);
            ItemUtil.setLoreAtLine(item, lore, lineNumber - 1);
            Message.get("itemlore-set").sendF(player, lineNumber, lore);
        } catch (Exception e) {
            Message.get("itemlore-invalid-line").send(player);
        }
    }

    // Removes all lore from the item
    private void removeAll(ItemStack item, Player player) {
        if (item.getItemMeta().hasLore()) {
            ItemUtil.setLore(item, null);
            Message.get("itemlore-remove").send(player);
        } else {
            Message.get("itemlore-no-lore").send(player);
        }
    }

    // Removes a line of the item's lore
    private void removeLine(ItemStack item, String line, Player player) {
        try {
            int lineNumber = Integer.parseInt(line);
            ItemUtil.removeLore(item, lineNumber - 1);
            Message.get("itemlore-remove-line").sendF(player, lineNumber);
        } catch (Exception e) {
            Message.get("itemlore-invalid-line").send(player);
        }
    }
}
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
        if (sender.hasPermission("v3ld1n.itemlore")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length >= 1) {
                    ItemStack i = p.getItemInHand();
                    if (i.getType() != Material.AIR) {
                        if (args[0].equalsIgnoreCase("remove")) {
                            if (args.length == 1) {
                                if (i.getItemMeta().hasLore()) {
                                    ItemUtil.setLore(i, null);
                                    message(p, Message.ITEMLORE_REMOVE);
                                } else {
                                    message(p, Message.ITEMLORE_NO_LORE);
                                }
                            } else if (args.length == 2) {
                                int line;
                                try {
                                    line = Integer.parseInt(args[1]);
                                    ItemUtil.removeLore(i, line - 1);
                                    messageF(p, Message.ITEMLORE_REMOVE_LINE, line);
                                } catch (Exception e) {
                                    message(p, Message.ITEMLORE_INVALID_LINE);
                                    return true;
                                }
                            }
                            return true;
                        }
                        if (args.length >= 2) {
                            String lore = StringUtil.formatText(StringUtil.fromArray(args, 1));
                            if (args[0].equalsIgnoreCase("add")) {
                                ItemUtil.addLore(i, lore);
                                messageF(p, Message.ITEMLORE_ADD, lore);
                            }
                            if (args.length >= 3) {
                                lore = StringUtil.fromArray(args, 2);
                                if (args[0].equalsIgnoreCase("set")) {
                                    int line;
                                    try {
                                        line = Integer.parseInt(args[1]);
                                        ItemUtil.setLoreAtLine(i, lore, line - 1);
                                        messageF(p, Message.ITEMLORE_SET, line, lore);
                                    } catch (Exception e) {
                                        message(p, Message.ITEMLORE_INVALID_LINE);
                                        return true;
                                    }
                                }
                            }
                            return true;
                        }
                        this.sendUsage(p, label, command);
                        return true;
                    }
                    message(p, Message.ITEMLORE_NO_ITEM);
                    return true;
                }
                this.sendUsage(p, label, command);
                return true;
            }
            message(sender, Message.COMMAND_NOT_PLAYER);
            return true;
        }
        message(sender, Message.COMMAND_NO_PERMISSION);
        return true;
    }
}
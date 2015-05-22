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
                                    Message.ITEMLORE_REMOVE.send(p);
                                } else {
                                    Message.ITEMLORE_NO_LORE.send(p);
                                }
                            } else if (args.length == 2) {
                                int line;
                                try {
                                    line = Integer.parseInt(args[1]);
                                    ItemUtil.removeLore(i, line - 1);
                                    Message.ITEMLORE_REMOVE_LINE.sendF(p, line);
                                } catch (Exception e) {
                                    Message.ITEMLORE_INVALID_LINE.send(p);
                                    return true;
                                }
                            }
                            return true;
                        }
                        if (args.length >= 2) {
                            String lore = StringUtil.formatText(StringUtil.fromArray(args, 1));
                            if (args[0].equalsIgnoreCase("add")) {
                                ItemUtil.addLore(i, lore);
                                Message.ITEMLORE_ADD.sendF(p, lore);
                            }
                            if (args.length >= 3) {
                                lore = StringUtil.formatText(StringUtil.fromArray(args, 2));
                                if (args[0].equalsIgnoreCase("set")) {
                                    int line;
                                    try {
                                        line = Integer.parseInt(args[1]);
                                        ItemUtil.setLoreAtLine(i, lore, line - 1);
                                        Message.ITEMLORE_SET.sendF(p, line, lore);
                                    } catch (Exception e) {
                                        Message.ITEMLORE_INVALID_LINE.send(p);
                                        return true;
                                    }
                                }
                            }
                            return true;
                        }
                        this.sendUsage(p, label, command);
                        return true;
                    }
                    Message.COMMAND_NO_ITEM.send(p);
                    return true;
                }
                this.sendUsage(p, label, command);
                return true;
            }
            sendPlayerMessage(sender);
            return true;
        }
        sendPermissionMessage(sender);
        return true;
    }
}
package com.v3ld1n.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.v3ld1n.Message;
import com.v3ld1n.util.StringUtil;

public class ItemNameCommand extends V3LD1NCommand {
    public ItemNameCommand() {
        this.addUsage("<name>", "Set the name of the item you're holding");
        this.addUsage("remove", "Remove the name of the item you're holding");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.itemname")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                if (args.length >= 1) {
                    ItemStack i = p.getItemInHand();
                    if (i.getType() != Material.AIR) {
                        ItemMeta meta = i.getItemMeta();
                        if (args.length == 1 && args[0].equalsIgnoreCase("remove")) {
                            if (meta.hasDisplayName()) {
                                meta.setDisplayName(null);
                                p.sendMessage(Message.ITEMNAME_REMOVE.toString());
                            } else {
                                p.sendMessage(Message.ITEMNAME_NOT_NAMED.toString());
                            }
                        } else {
                            String name = StringUtil.formatText(StringUtil.fromArray(args, 0));
                            meta.setDisplayName(ChatColor.RESET + name);
                            p.sendMessage(String.format(Message.ITEMNAME_SET.toString(), name));
                        }
                        i.setItemMeta(meta);
                        return true;
                    }
                    p.sendMessage(Message.ITEMNAME_NO_ITEM.toString());
                    return true;
                }
                this.sendUsage(p, label, command);
                return true;
            }
            sender.sendMessage(Message.COMMAND_NOT_PLAYER.toString());
            return true;
        }
        sender.sendMessage(Message.COMMAND_NO_PERMISSION.toString());
        return true;
    }
}
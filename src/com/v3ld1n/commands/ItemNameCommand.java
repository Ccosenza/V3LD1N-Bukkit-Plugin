package com.v3ld1n.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.Message;
import com.v3ld1n.util.ItemUtil;
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
                        String name = null;
                        if (args.length == 1 && args[0].equalsIgnoreCase("remove")) {
                            boolean hasName = i.getItemMeta().hasDisplayName();
                            Message message = hasName ? Message.ITEMNAME_REMOVE : Message.ITEMNAME_NOT_NAMED;
                            p.sendMessage(message.toString());
                        } else {
                            name = StringUtil.formatText(StringUtil.fromArray(args, 0));
                            Message.ITEMNAME_SET.sendF(p, name);
                        }
                        ItemUtil.setName(i, name);
                        return true;
                    }
                    Message.ITEMNAME_NO_ITEM.send(p);
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
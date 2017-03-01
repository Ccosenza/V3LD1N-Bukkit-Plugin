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
        if (sendPermissionMessage(sender, "v3ld1n.itemname")) return true;
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

        if (args.length == 1 && args[0].equalsIgnoreCase("remove")) {
            remove(item, player);
        } else {
            String name = StringUtil.formatText(StringUtil.fromArray(args, 0));
            set(item, name, player);
        }
        return true;
    }

    // Sets the item's name
    private void set(ItemStack item, String name, Player player) {
        ItemUtil.setName(item, name);
        Message.get("itemname-set").sendF(player, name);
    }

    // Removes the item's name
    private void remove(ItemStack item, Player player) {
        boolean hasName = item.getItemMeta().hasDisplayName();
        ItemUtil.setName(item, null);
        Message message = hasName ? Message.get("itemname-remove") : Message.get("itemname-no-name");
        player.sendMessage(message.toString());
    }
}
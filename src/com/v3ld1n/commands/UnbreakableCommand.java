package com.v3ld1n.commands;

import net.minecraft.server.v1_11_R1.NBTTagInt;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.Message;
import com.v3ld1n.util.ItemUtil;

public class UnbreakableCommand extends V3LD1NCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendPermissionMessage(sender, "v3ld1n.owner")) return true;
        if (sendNotPlayerMessage(sender)) return true;
        Player player = (Player) sender;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            Message.get("command-no-item").send(player);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
            reset(item, player, sender);
            return true;
        }

        set(item, player, sender);
        return true;
    }

    // Makes the player's item unbreakable
    private void set(ItemStack item, Player player, CommandSender user) {
        ItemStack unbreakableItem = ItemUtil.setTag(item, "Unbreakable", new NBTTagInt(1));
        player.getInventory().setItemInMainHand(unbreakableItem);
        Message.get("unbreakable-set").send(player);
    }

    // Makes the player's item breakable again
    private void reset(ItemStack item, Player player, CommandSender user) {
        ItemStack resetItem = ItemUtil.setTag(item, "Unbreakable", new NBTTagInt(0));
        player.getInventory().setItemInMainHand(resetItem);
        Message.get("unbreakable-reset").send(player);
    }
}
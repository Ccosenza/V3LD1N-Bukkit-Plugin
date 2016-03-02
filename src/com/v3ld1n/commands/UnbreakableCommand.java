package com.v3ld1n.commands;

import net.minecraft.server.v1_9_R1.NBTTagInt;

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
        if (sender instanceof Player) {
            if (sender.hasPermission("v3ld1n.owner")) {
                Player p = (Player) sender;
                ItemStack item = p.getInventory().getItemInMainHand();
                if (item.getType() != Material.AIR) {
                    ItemStack i = ItemUtil.setTag(item, "Unbreakable", new NBTTagInt(1));
                    p.getInventory().setItemInMainHand(i);
                    Message.UNBREAKABLE_SET.send(p);
                    return true;
                }
                Message.COMMAND_NO_ITEM.send(p);
                return true;
            }
            sendPermissionMessage(sender);
            return true;
        }
        sendPlayerMessage(sender);
        return true;
    }
}
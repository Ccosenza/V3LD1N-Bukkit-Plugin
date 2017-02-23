package com.v3ld1n.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.Message;

import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.NBTTagInt;

public class MapColorCommand extends V3LD1NCommand {
    private static final int LIMIT = 16777215;

    public MapColorCommand() {
        this.addUsage("<color>", "Set the color of the map you're holding");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendPermissionMessage(sender, "v3ld1n.mapcolor")) return true;
        if (sendNotPlayerMessage(sender)) return true;
        Player p = (Player) sender;

        if (args.length != 1) {
            sendUsage(sender);
            return true;
        }

        int color;
        try {
            color = Integer.parseInt(args[0]);
        } catch (IllegalArgumentException e) {
            this.sendUsage(sender);
            return true;
        }

        if (color < 0 || color > LIMIT) {
            Message.get("mapcolor-limit").sendF(sender, LIMIT);
            return true;
        }

        ItemStack item = p.getInventory().getItemInMainHand();
        if (item.getType() != Material.MAP) {
            Message.get("mapcolor-not-map").send(p);
            return true;
        }
        ItemStack coloredMap = setColor(item, color);
        p.getInventory().setItemInMainHand(coloredMap);
        Message.get("mapcolor-set").sendF(p, color);
        return true;
    }

    public static ItemStack setColor(ItemStack item, int color) {
        net.minecraft.server.v1_11_R1.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = stack.hasTag() ? stack.getTag() : new NBTTagCompound();
        NBTTagCompound display = tag.getCompound("display");
        display.set("MapColor", new NBTTagInt(color));
        stack.setTag(tag);
        return CraftItemStack.asBukkitCopy(stack);
    }
}

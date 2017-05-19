package com.v3ld1n.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

import com.v3ld1n.Message;

public class MapColorCommand extends V3LD1NCommand {
    private static final int LIMIT = 255;

    public MapColorCommand() {
        this.addUsage("<red> <green> <blue>", "Set the color of the map you're holding");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendPermissionMessage(sender, "v3ld1n.mapcolor")) return true;
        if (sendNotPlayerMessage(sender)) return true;
        Player p = (Player) sender;

        ItemStack item = p.getInventory().getItemInMainHand();
        if (item.getType() != Material.MAP) {
            Message.get("mapcolor-not-map").send(p);
            return true;
        }

        if (args.length != 3) {
            sendUsage(sender);
            return true;
        }

        int red;
        int green;
        int blue;
        try {
            red = Integer.parseInt(args[0]);
            green = Integer.parseInt(args[1]);
            blue = Integer.parseInt(args[2]);
        } catch (IllegalArgumentException e) {
            this.sendUsage(sender);
            return true;
        }

        if (red < 0 || red > LIMIT || green < 0 || green > LIMIT || blue < 0 || blue > LIMIT) {
            Message.get("mapcolor-limit").sendF(sender, LIMIT);
            return true;
        }       
        
        MapMeta map = (MapMeta) item.getItemMeta();
        map.setColor(org.bukkit.Color.fromRGB(red, green, blue));
        item.setItemMeta(map);

        Message.get("mapcolor-set").sendF(p, red, green, blue);
        return true;
    }
}

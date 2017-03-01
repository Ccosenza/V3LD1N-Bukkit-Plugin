package com.v3ld1n.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.v3ld1n.Message;
import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.ListType;

public class PlayersCommand extends V3LD1NCommand {
    public PlayersCommand() {
        this.addUsage("fulllist", "Displays a list of all players");
        this.addUsage("heads", "Opens an inventory of all online players' heads");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sendPermissionMessage(sender, "v3ld1n.players")) return true;

        if (args.length != 1) {
            this.sendUsage(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("fulllist")) {
            sendFullList(sender);
        } else if (args[0].equalsIgnoreCase("heads")) {
            if (sendNotPlayerMessage(sender)) return true;
            Player player = (Player) sender;
            displayHeads(player);
        }
        return true;
    }

    // Send list of all player names
    private void sendFullList(CommandSender user) {
        List<String> names = new ArrayList<>();
        OfflinePlayer[] allPlayers = Bukkit.getServer().getOfflinePlayers();
        for (OfflinePlayer player : Arrays.asList(allPlayers)) {
            names.add(player.getName());
        }
        String listTitle = String.format(Message.get("players-list-title").toString(), names.size());
        ChatUtil.sendList(user, listTitle, names, ListType.SHORT);
    }

    // Open inventory with online players' heads
    private void displayHeads(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, Message.get("players-heads-title").toString());
        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwner(onlinePlayer.getName());
            head.setItemMeta(meta);
            inv.addItem(head);
        }
        player.openInventory(inv);
    }
}

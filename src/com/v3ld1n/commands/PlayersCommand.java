package com.v3ld1n.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class PlayersCommand extends V3LD1NCommand {
    private String usageInfo = "info <player>";

    public PlayersCommand() {
        this.addUsage(usageInfo, "View information about a player");
        this.addUsage("list", "Displays a list of players who are currently online");
        this.addUsage("fulllist", "Displays a list of all players");
        this.addUsage("total", "Displays the total amount of players who joined the server");
        this.addUsage("online", "Displays the amount of players who are currently on the server");
        this.addUsage("heads", "Opens an inventory of all online players' heads");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("v3ld1n.players")) {
            if (args.length == 0) {
                this.sendUsage(sender, label, command);
                return true;
            }
            if (args[0].equalsIgnoreCase("info") && args.length == 2) {
                if (PlayerUtil.getOfflinePlayer(args[1]) != null) {
                    sendPlayerInfo(PlayerUtil.getOnlinePlayer(args[1]), sender);
                    return true;
                }
                sendInvalidPlayerMessage(sender);
                return true;
            } else if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("fulllist") && args.length == 1) {
                Collection<? extends OfflinePlayer> players = new ArrayList<>();
                switch (args[0]) {
                case "list":
                    players = Bukkit.getServer().getOnlinePlayers();
                    break;
                case "fulllist":
                    OfflinePlayer[] offline = Bukkit.getServer().getOfflinePlayers();
                    players = Arrays.asList(offline);
                    break;
                default:
                    break;
                }
                List<String> names = new ArrayList<>();
                for (OfflinePlayer player : players) {
                    names.add(player.getName());
                }
                ChatUtil.sendList(sender, Message.PLAYERS_LIST_TITLE.toString(), names, ListType.SHORT);
                return true;
            } else if ((args[0].equalsIgnoreCase("total") || args[0].equalsIgnoreCase("online")) && args.length == 1) {
                int players = 0;
                switch (args[0]) {
                case "total":
                    players = Bukkit.getServer().getOfflinePlayers().length;
                    break;
                case "online":
                    players = Bukkit.getServer().getOnlinePlayers().size();
                    break;
                default:
                    break;
                }
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    String title = String.format(Message.PLAYERS_AMOUNT_TITLE.toString(), StringUtil.upperCaseFirst(args[0]));
                    String subtitle = String.format(Message.PLAYERS_AMOUNT_SUBTITLE.toString(), players);
                    PlayerUtil.displayTitle(p, "{text:\"" + title + "\"}", 2, 2, 2);
                    PlayerUtil.displaySubtitle(p, "{text:\"" + subtitle + "\"}", 2, 2, 2, false);
                    return true;
                }
                Message.PLAYERS_AMOUNT_NOT_PLAYER.aSendF(sender, players);
                return true;
            } else if (args[0].equalsIgnoreCase("heads") && args.length == 1 && sender instanceof Player) {
                Player p = (Player) sender;
                Inventory inv = Bukkit.createInventory(null, 27, "Player Heads");
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
                    SkullMeta meta = (SkullMeta) head.getItemMeta();
                    meta.setOwner(player.getName());
                    head.setItemMeta(meta);
                    inv.addItem(head);
                }
                p.openInventory(inv);
                return true;
            }
        } else {
            sendPermissionMessage(sender);
            return true;
        }
        this.sendUsage(sender, label, command);
        return true;
    }

    private void sendPlayerInfo(Player player, CommandSender to) {
        StringBuilder borderB = new StringBuilder();
        for (int i = 0; i < player.getName().length(); i++) {
            borderB.append(ChatColor.DARK_BLUE + "=");
        }
        String border = borderB.toString();
        to.sendMessage(border);
        to.sendMessage(ChatColor.AQUA + player.getName());
        to.sendMessage(border);
        HashMap<String, Object> info = PlayerUtil.getInfo(player);
        Set<String> namesSet = info.keySet();
        List<String> names = new ArrayList<>(namesSet);
        ChatColor color1;
        ChatColor color2;
        for (int i = 0; i < info.size(); i++) {
            if (i % 2 == 0) {
                color1 = ChatColor.GOLD;
                color2 = ChatColor.GREEN;
            } else {
                color1 = ChatColor.YELLOW;
                color2 = ChatColor.DARK_GREEN;
            }
            to.sendMessage(color1 + names.get(i) + ": " + color2 + info.get(names.get(i)));
        }
    }
}

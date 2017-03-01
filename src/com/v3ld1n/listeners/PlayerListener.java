package com.v3ld1n.listeners;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.logging.Level;

import com.v3ld1n.*;
import com.v3ld1n.tasks.SoundTask;
import com.v3ld1n.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener implements Listener {
    private Random random = new Random();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        
        if (action == Action.RIGHT_CLICK_BLOCK) {
            Material blockType = event.getClickedBlock().getType();

            if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
                //Signs
                Sign signState = (Sign) event.getClickedBlock().getState();

                for (com.v3ld1n.blocks.Sign sign : V3LD1N.getSigns()) {
                    String signText = StringUtil.formatText(sign.getText());

                    if (signState.getLine(0).equals(signText)) {
                        sign.use(player, signState);
                    }
                }

                for (SoundTask task : V3LD1N.getSoundTasks()) {
                    for (Sign sign : task.getSigns()) {
                        if (signState.equals(sign)) {
                            long seconds = (task.getNextTime() / 1000) - (TimeUtil.getTime() / 1000);
                            String time = TimeUtil.fromSeconds(seconds);
                            Message.get("task-sound-time").aSendF(player, task.getName().toUpperCase(), time);
                        }
                    }
                }
            }
        }
        // Money items
        if (action.name().startsWith("RIGHT_CLICK")) {
            if (itemInHand.getType() == Material.valueOf(ConfigSetting.MONEY_ITEM_ITEM.getString()) && itemInHand.hasItemMeta()) {
                ItemMeta handItemMeta = itemInHand.getItemMeta();
                String handItemName = handItemMeta.getDisplayName();
                String moneyItemName = Message.get("money-name").toString();
                String moneyItemLore = Message.get("money-lore").toString();
                Enchantment moneyItemEnchant = Enchantment.DURABILITY;

                boolean sameName = handItemName.contains(moneyItemName);
                boolean sameLore = handItemMeta.hasLore() && handItemMeta.getLore().get(0).equals(moneyItemLore);
                boolean sameEnchant = handItemMeta.hasEnchant(moneyItemEnchant) && handItemMeta.getEnchantLevel(moneyItemEnchant) == 10;

                if (sameName && sameLore && sameEnchant) {
                    event.setCancelled(true);
                    String amountInName = handItemName.substring(4, handItemName.indexOf(" "));

                    try {
                        double amount = Double.parseDouble(amountInName);
                        DecimalFormat decimalFormat = new DecimalFormat("0.##");
                        amountInName = decimalFormat.format(amount);

                        V3LD1N.getEconomy().depositPlayer(player, amount);
                        Message.get("money-added").sendF(player, amountInName);
                    } catch (Exception e) {
                        Message.get("money-invalid-amount").logF(Level.WARNING, player.getName(), amountInName);
                    }

                    PlayerUtil.takeItem(player, itemInHand, 1);
                }
            }
        }
    }

    // Displays trails
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!PlayerUtil.hasTrail(player)) return;

        Location trailLocation = player.getLocation();
        trailLocation.setY(trailLocation.getY() + 0.15);

        ConfigSetting setting = ConfigSetting.PARTICLE_TRAILS;
        if (ConfigSetting.PLAYER_EFFECTS_PLAYERS.getStringList().contains(player.getName())) {
            setting = ConfigSetting.PARTICLE_TRAILS_PLAYER_EFFECTS;
        }

        String trailName = PlayerData.TRAILS.getString(player);
        Particle trail = Particle.fromString(trailName, setting.getString());
        trail.display(trailLocation);
    }

    // Join message, join MOTD, player list, and resource pack
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            int playerNumber = Bukkit.getOfflinePlayers().length;
            event.setJoinMessage(String.format(Message.get("new-player-join").toString(), player.getName(), playerNumber));
        }

        ChatUtil.sendMotd(player);

        if (ConfigSetting.PLAYER_LIST_HEADER.getString() != null && ConfigSetting.PLAYER_LIST_FOOTER.getString() != null) {
            String header = ConfigSetting.PLAYER_LIST_HEADER.getString();
            String footer = ConfigSetting.PLAYER_LIST_FOOTER.getString();
            PlayerUtil.sendPlayerListHeaderFooter(player, header, footer);
        }

        if (PlayerData.AUTO_RESOURCE_PACK.getString(player) != null) {
            final String packName = PlayerData.AUTO_RESOURCE_PACK.getString(player);
            if (V3LD1N.getResourcePack(packName) != null) {
                PlayerAnimation.BED_LEAVE.playToPlayer(player);
                Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        player.setResourcePack(V3LD1N.getResourcePack(packName).getUrl());
                    }
                }, 1);
            }
        }
    }

    // Variables in chat
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String message = StringUtil.replacePlayerVariables(event.getMessage(), event.getPlayer());
        event.setMessage(message);
    }

    // Variables in commands
    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = StringUtil.replacePlayerVariables(event.getMessage(), event.getPlayer());
        event.setMessage(message);
    }

    // Variables on signs
    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();

        for (int i = 0; i < 4; i++) {
            String line = event.getLine(i);
            String newLine = StringUtil.replaceSignVariables(line, (Sign) event.getBlock().getState(), event.getPlayer());
            newLine = StringUtil.replacePlayerVariables(newLine, player);
            event.setLine(i, newLine);
        }

        if (!player.hasPermission("v3ld1n.createsigns")) {
            String firstLine = event.getLine(0);
            for (com.v3ld1n.blocks.Sign sign : V3LD1N.getSigns()) {
                if (firstLine.replaceAll("§", "&").equals(sign.getText())) {
                    event.setCancelled(true);
                    Message.get("permission-signs").sendF(player, firstLine);
                }
            }
        }
    }

    // Variables in written books
    @EventHandler
    public void onBookEdit(PlayerEditBookEvent event) {
        BookMeta newBookMeta = event.getNewBookMeta();
        Player player = event.getPlayer();

        for (String page : newBookMeta.getPages()) {
            String newPage = StringUtil.formatText(StringUtil.replacePlayerVariables(page, player));
            newBookMeta.setPage(newBookMeta.getPages().indexOf(page) + 1, newPage);
        }

        if (newBookMeta.hasTitle()) {
            String title = newBookMeta.getTitle();
            String newTitle = StringUtil.formatText(StringUtil.replacePlayerVariables(title, player));
            newBookMeta.setTitle(newTitle);
        }

        event.setNewBookMeta(newBookMeta);
    }

    // Disables item dropping
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (ConfigSetting.CANCEL_DROP_WORLDS.getList().contains(event.getPlayer().getWorld().getName())) {
            event.setCancelled(true);
        }
    }

    // Disables items dropping on death and removes projectiles on death
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (ConfigSetting.CANCEL_DROP_WORLDS.getList().contains(event.getEntity().getWorld().getName())) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        }

        if (ConfigSetting.REMOVE_PROJECTILES_WORLDS.getList().contains(event.getEntity().getWorld().getName())) {
            Player player = event.getEntity();
            for (Entity entity : player.getWorld().getEntities()) {
                if (!(entity instanceof Projectile)) continue;
                Projectile projectile = (Projectile) entity;

                if (projectile.getShooter() == player) {
                    projectile.remove();
                }
            }
        }
    }

    // Removes vehicles after exiting them
    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        final Vehicle vehicle = event.getVehicle();

        if (!ConfigSetting.REMOVE_VEHICLES_WORLDS.getList().contains(vehicle.getWorld().getName())) return;
        if (!(event.getExited() instanceof Player)) return;
        if (!ConfigSetting.REMOVE_VEHICLES_VEHICLES.getStringList().contains(vehicle.getType().toString())) return;

        Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if (vehicle.getPassenger() == null) {
                    vehicle.remove();
                }
            }
        }, ConfigSetting.REMOVE_VEHICLES_SECONDS.getInt() * 20);
    }

    // Sets the server list MOTD
    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        if (ConfigSetting.SERVER_LIST_MOTD.getValue() != null) {
            if (!ConfigSetting.SERVER_LIST_MOTD.getStringList().isEmpty()) {
                int randomInt = random.nextInt(ConfigSetting.SERVER_LIST_MOTD.getStringList().size());
                String randomMotd = ConfigSetting.SERVER_LIST_MOTD.getStringList().get(randomInt);
                String ic = "(?i)";
                String motd = StringUtil.formatText(randomMotd)
                        .replaceFirst(ic + "%newline%", "\n")
                        .replaceAll(ic + "%players%", Integer.toString(event.getNumPlayers()))
                        .replaceAll(ic + "%maxplayers%", Integer.toString(event.getMaxPlayers()))
                        .replaceAll(ic + "%ip%", event.getAddress().toString());
                event.setMotd(motd);
            }
        }
        if (ConfigSetting.SERVER_LIST_MAX_PLAYERS.getValue() != null) {
            event.setMaxPlayers(ConfigSetting.SERVER_LIST_MAX_PLAYERS.getInt());
        }
    }
}
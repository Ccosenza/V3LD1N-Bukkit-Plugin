package com.v3ld1n.listeners;

import java.util.Random;

import com.v3ld1n.*;
import com.v3ld1n.util.*;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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
import org.bukkit.inventory.meta.BookMeta;

public class PlayerListener implements Listener {
    private Random random = new Random();

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Action a = event.getAction();
        
        if (a == Action.RIGHT_CLICK_BLOCK) {
            //Ender Crystal spawn egg
            if (p.getItemInHand().getType() == Material.MONSTER_EGG && p.getItemInHand().getDurability() == ConfigSetting.ENDER_CRYSTAL_EGG_DATA.getInt()) {
                event.setCancelled(true);
                Location loc = p.getTargetBlock(null, 5).getLocation();
                if (V3LD1N.getWorldGuard().canBuild(p, loc) && V3LD1N.getWorldGuard().canBuild(p, loc.add(0, 1, 0))) {
                    if (p.getGameMode() != GameMode.CREATIVE) {
                        p.getInventory().remove(p.getItemInHand());
                    }
                    p.getWorld().spawnEntity(loc, EntityType.ENDER_CRYSTAL);
                    Location particleLoc = loc.add(0, 1.6, 0);
                    Particle.fromString(ConfigSetting.PARTICLE_SPAWN_ENDER_CRYSTAL.getString()).display(particleLoc);
                } else {
                    p.sendMessage(Message.WORLDGUARD_PERMISSION.toString());
                }
            //Signs
            } else if (event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN_POST) {
                Sign signState = (Sign) event.getClickedBlock().getState();
                for (com.v3ld1n.blocks.Sign sign : V3LD1N.getSigns()) {
                    if (signState.getLine(0).equals(StringUtil.formatText(sign.getText()))) {
                        Location loc = signState.getBlock().getLocation().add(0.5, 0.5, 0.5);
                        for (String command : sign.getPlayerCommands()) {
                            Bukkit.dispatchCommand(p, StringUtil.replaceSignVariables(command, signState, p));
                        }
                        for (String command : sign.getConsoleCommands()) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtil.replaceSignVariables(command, signState, p));
                        }
                        for (Particle particle : sign.getParticles()) {
                            particle.display(loc);
                        }
                        for (Sound sound : sign.getSounds()) {
                            sound.play(loc);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (PlayerUtil.hasTrail(event.getPlayer())) {
            Player p = event.getPlayer();
            Location loc = p.getLocation();
            loc.setY(loc.getY() + 0.15);
            ConfigSetting setting = ConfigSetting.PARTICLE_TRAILS;
            if (ConfigSetting.PLAYER_EFFECTS_PLAYERS.getStringList().contains(p.getName())) {
                setting = ConfigSetting.PARTICLE_TRAILS_PLAYER_EFFECTS;
            }
            Particle trail = Particle.fromString(PlayerData.TRAILS.getString(p.getUniqueId()), setting.getString());
            trail.display(loc);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player p = event.getPlayer();
        ChatUtil.sendMotd(p);
        if (ConfigSetting.PLAYER_LIST_HEADER.getString() != null && ConfigSetting.PLAYER_LIST_FOOTER.getString() != null) {
            PlayerUtil.sendPlayerListHeaderFooter(p, ConfigSetting.PLAYER_LIST_HEADER.getString(), ConfigSetting.PLAYER_LIST_FOOTER.getString());
        }
        if (PlayerData.AUTO_RESOURCE_PACK.getBoolean(p.getUniqueId())) {
            PlayerAnimation.BED_LEAVE.playTo(p);
            Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    p.setResourcePack(ConfigSetting.RESOURCE_PACK.getString());
                }
            }, 1L);
        }
        if (ConfigUtil.getUnreadReports(p.getUniqueId()) > 0) {
            ChatUtil.sendUnreadReports(p);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String msg = StringUtil.replacePlayerVariables(event.getMessage(), event.getPlayer());
        event.setMessage(msg);
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String msg = StringUtil.replacePlayerVariables(event.getMessage(), event.getPlayer());
        event.setMessage(msg);
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player p = event.getPlayer();
        for (int i = 0; i < 4; i++) {
            String line = event.getLine(i);
            String newLine = StringUtil.replaceSignVariables(line, (Sign) event.getBlock().getState(), event.getPlayer());
            event.setLine(i, newLine);
        }
        if (!p.hasPermission("v3ld1n.createsigns")) {
            String firstLine = event.getLine(0);
            for (com.v3ld1n.blocks.Sign sign : V3LD1N.getSigns()) {
                if (firstLine.replaceAll("§", "&").equals(sign.getText())) {
                    event.setCancelled(true);
                    p.sendMessage(String.format(Message.SIGN_PERMISSION.toString(), firstLine));
                }
            }
        }
    }

    @EventHandler
    public void onBookEdit(PlayerEditBookEvent event) {
        BookMeta newBookMeta = event.getNewBookMeta();
        for (String page : newBookMeta.getPages()) {
            String newPage = StringUtil.formatText(StringUtil.replacePlayerVariables(page, event.getPlayer()));
            newBookMeta.setPage(newBookMeta.getPages().indexOf(page) + 1, newPage);
        }
        String newTitle = StringUtil.formatText(StringUtil.replacePlayerVariables(newBookMeta.getTitle(), event.getPlayer()));
        newBookMeta.setTitle(newTitle);
        event.setNewBookMeta(newBookMeta);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (ConfigSetting.CANCEL_DROP_WORLDS.getList().contains(event.getPlayer().getWorld().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (ConfigSetting.CANCEL_DROP_WORLDS.getList().contains(event.getEntity().getWorld())) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
        if (ConfigSetting.REMOVE_PROJECTILE_WORLDS.getList().contains(event.getEntity().getWorld())) {
            Player p = event.getEntity();
            for (Entity e : p.getWorld().getEntities()) {
                if (e instanceof Projectile) {
                    Projectile pr = (Projectile) e;
                    if (pr.getShooter() == p) {
                        pr.remove();
                    }
                }
            }
        }
    }

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
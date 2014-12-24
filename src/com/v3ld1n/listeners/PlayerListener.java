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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class PlayerListener implements Listener {
    private Random random = new Random();

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Action a = event.getAction();
        
        if (a == Action.RIGHT_CLICK_BLOCK) {
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
            } else if (event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN_POST) {
                Sign sign = (Sign) event.getClickedBlock().getState();
                for (String key : Config.SIGNS.getConfig().getKeys(false)) {
                    if (sign.getLine(0).equals(StringUtil.formatText(ConfigUtil.getSignText(key)))) {
                        if (ConfigUtil.getSignPlayerCommands(key) != null) {
                            for (String command : ConfigUtil.getSignPlayerCommands(key)) {
                                Bukkit.dispatchCommand(p, StringUtil.replaceSignVariables(command, sign, p));
                            }
                        }
                        if (ConfigUtil.getSignConsoleCommands(key) != null) {
                            for (String command : ConfigUtil.getSignConsoleCommands(key)) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtil.replaceSignVariables(command, sign, p));
                            }
                        }
                        if (ConfigUtil.getSignSound(key) != null) {
                            SoundUtil.playSoundString(ConfigUtil.getSignSound(key), sign.getLocation());
                        }
                        if (ConfigUtil.getSignParticleName(key) != null) {
                            try {
                                Particle.fromString(ConfigUtil.getSignParticle(key)).display(sign.getBlock().getLocation().add(new Location(sign.getBlock().getLocation().getWorld(), 0.5, 0.5, 0.5)));
                            } catch (IllegalArgumentException e) {
                                V3LD1N.getPlugin().getLogger().info(String.format(Message.SIGN_INVALID_PARTICLE.toString(), key));
                            }
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
            Particle trail = Particle.fromString(PlayerData.TRAILS.getString(p.getUniqueId()), ConfigSetting.PARTICLE_TRAILS.getString());
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
            PlayerAnimation.BED_LEAVE.playToPlayer(p);
            Bukkit.getServer().getScheduler().runTaskLater(V3LD1N.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    p.setResourcePack(ConfigSetting.RESOURCE_PACK.getString());
                }
            }, 1L);
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
        if (ConfigSetting.SERVER_LIST_MAX_PLAYERS.getValue() != null) {
            event.setMaxPlayers(ConfigSetting.SERVER_LIST_MAX_PLAYERS.getInt());
        }
    }
}
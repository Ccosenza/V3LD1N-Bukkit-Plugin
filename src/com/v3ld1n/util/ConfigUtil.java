package com.v3ld1n.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.Config;
import com.v3ld1n.ConfigSetting;

public class ConfigUtil {
    private ConfigUtil() {
    }

    public static void setPlayerListHeader(String header) {
        ConfigSetting.PLAYER_LIST_HEADER.setValue(header);
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            TabTitleManager.setHeader(p, header);
        }
    }

    public static void setPlayerListFooter(String footer) {
        ConfigSetting.PLAYER_LIST_FOOTER.setValue(footer);
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            TabTitleManager.setFooter(p, footer);
        }
    }

    public static void toggleDebug() {
        if (ConfigSetting.DEBUG.getValue() != null) {
            ConfigSetting.DEBUG.setValue(null);
        } else {
            ConfigSetting.DEBUG.setValue(true);
        }
    }

    public static boolean isWarpEnabled(String warp) {
        return Config.WARPS.getConfig().get(warp + ".enabled") != null && Config.WARPS.getConfig().getBoolean(warp + ".enabled");
    }

    public static List<String> getWarps() {
        List<String> warps = new ArrayList<>();
        if (Config.WARPS.getConfig() != null) {
            for (String key : Config.WARPS.getConfig().getKeys(false)) {
                warps.add(key);
            }
        }
        return warps;
    }

    public static List<Particle> getWarpParticles(String warp) {
        List<Particle> particles = new ArrayList<>();
        if (Config.WARPS.getConfig().get(warp + ".particles") != null) {
            for (String particleString : Config.WARPS.getConfig().getStringList(warp + ".particles")) {
                particles.add(Particle.fromString((particleString)));
            }
        }
        return particles;
    }

    public static void toggleWarp(String warp) {
        if (Config.WARPS.getConfig().get(warp + ".enabled") != null) {
            Config.WARPS.getConfig().set(warp + ".enabled", null);
            Config.WARPS.saveConfig();
        } else {
            Config.WARPS.getConfig().set(warp + ".enabled", true);
            Config.WARPS.saveConfig();
        }
    }

    public static void setWarpEnabled(String warp, boolean enabled) {
        Config.WARPS.getConfig().set(warp + ".enabled", enabled);
        Config.WARPS.saveConfig();
    }

    /**
     * Returns the text of a custom sign
     * @param sign the config name of the sign
     * @return the text of the sign
     */
    public static String getSignText(String sign) {
        return Config.SIGNS.getConfig().getString(sign + ".text");
    }

    /**
     * Returns the player commands of a custom sign
     * @param sign the config name of the sign
     * @return the player commands of the sign
     */
    public static List<String> getSignPlayerCommands(String sign) {
        return Config.SIGNS.getConfig().getStringList(sign + ".player-commands");
    }

    /**
     * Returns the console commands of a custom sign
     * @param sign the config name of the sign
     * @return the console commands of the sign
     */
    public static List<String> getSignConsoleCommands(String sign) {
        return Config.SIGNS.getConfig().getStringList(sign + ".console-commands");
    }

    /**
     * Returns the click sound of a custom sign
     * @param sign the config name of the sign
     * @return the click sound of the sign
     */
    public static String getSignSound(String sign) {
        if (Config.SIGNS.getConfig().getString(sign + ".sound") != null) {
            return Config.SIGNS.getConfig().getString(sign + ".sound");
        }
        return null;
    }

    /**
     * Returns the click sound of a custom sign
     * @param sign the config name of the sign
     * @return the click sound of the sign
     */
    public static String getSignSoundName(String sign) {
        if (Config.SIGNS.getConfig().getString(sign + ".sound") != null) {
            return Config.SIGNS.getConfig().getString(sign + ".sound").split("\\|")[0];
        }
        return null;
    }

    /**
     * Returns the click particle of a custom sign
     * @param sign the config name of the sign
     * @return the click particle of the sign
     */
    public static String getSignParticle(String sign) {
        if (Config.SIGNS.getConfig().getString(sign + ".particle") != null) {
            return Config.SIGNS.getConfig().getString(sign + ".particle");
        }
        return null;
    }

    /**
     * Returns the name of the click particle of a custom sign
     * @param sign the config name of the sign
     * @return the name of the particle
     */
    public static String getSignParticleName(String sign) {
        if (Config.SIGNS.getConfig().getString(sign + ".particle") != null) {
            return Config.SIGNS.getConfig().getString(sign + ".particle").split("\\|")[0];
        }
        return null;
    }

    /**
     * Returns an item stack from a config string
     * @param configSetting the string
     * @return an item stack
     */
    public static ItemStack parseItem(String configSetting) {
        String[] split = configSetting.split("\\|");
        ItemStack item = new ItemStack(Material.valueOf(split[0].toUpperCase()), 1);
        if (split.length >= 2) {
            item.setAmount(Integer.parseInt(split[1]));
            if (split.length >= 3) {
                item.setDurability(Short.parseShort(split[2]));
            }
        }
        return item;
    }

    /**
     * Spawns a falling block from a config string
     * @param configSetting the string
     * @param location the location to spawn the block
     * @param dropItem whether the falling block should drop an item
     * @return the falling block object
     */
    @SuppressWarnings("deprecation")
    public static FallingBlock spawnFallingBlock(String configSetting, Location location, boolean dropItem) {
        String[] split = configSetting.split("\\|");
        byte data = 0;
        if (split.length >= 2) {
            data = Byte.parseByte(split[1]);
        }
        FallingBlock block = location.getWorld().spawnFallingBlock(location, Material.valueOf(split[0]), data);
        block.setDropItem(dropItem);
        return block;
    }
}
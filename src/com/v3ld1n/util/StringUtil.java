package com.v3ld1n.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;
import com.v3ld1n.V3LD1N;

public class StringUtil {
    private StringUtil() {
    }

    /**
     * Sends a message to the console if debug is set to true
     * @param message the message to send to the console
     */
    public static void logDebugMessage(String message) {
        if (ConfigSetting.DEBUG.getBoolean()) {
            V3LD1N.getPlugin().getLogger().info(message);
        }
    }

    /**
     * Returns the current server time
     * @return the current server time
     */
    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(calendar.getTime());
    }

    public static String getItemName(ItemStack item) {
        String name = fromEnum(item.getType(), true);
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            name = item.getItemMeta().getDisplayName();
        }
        return name;
    }

    public static String getEntityName(Entity entity) {
        String name = fromEnum(entity.getType(), true);
        if (entity.getCustomName() != null) {
            name = entity.getCustomName();
        }
        if (entity instanceof Player) {
            name = ((Player) entity).getName();
        }
        return name;
    }

    /**
     * Returns a string with player variables
     * @param string the string to edit
     * @param player the player
     * @return the string with variables replaced
     */
    @SuppressWarnings("deprecation")
    public static String replacePlayerVariables(String string, Player player) {
        String ignoreCase = "(?i)";
        String replaced = string
                .replaceAll(ignoreCase + "%name%", player.getName())
                .replaceAll(ignoreCase + "%uuid%", player.getUniqueId().toString())
                .replaceAll(ignoreCase + "%displayname%", player.getDisplayName())
                .replaceAll(ignoreCase + "%world%", player.getWorld().getName())
                .replaceAll(ignoreCase + "%biome%", fromEnum(player.getLocation().getBlock().getBiome(), true))
                .replaceAll(ignoreCase + "%block%", fromEnum(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType(), true))
                .replaceAll(ignoreCase + "%health%", Double.toString(player.getHealth()))
                .replaceAll(ignoreCase + "%maxhealth%", Double.toString(player.getMaxHealth()))
                .replaceAll(ignoreCase + "%hunger%", Integer.toString(player.getFoodLevel()))
                .replaceAll(ignoreCase + "%xp%", Integer.toString(player.getTotalExperience()))
                .replaceAll(ignoreCase + "%entities%", Integer.toString(WorldUtil.getAllEntities().size()))
                .replaceAll(ignoreCase + "%worldentities%", Integer.toString(player.getWorld().getEntities().size()))
                .replaceAll(ignoreCase + "%blockentities%", Integer.toString(WorldUtil.getBlockEntities(player.getWorld()).size()))
                .replaceAll(ignoreCase + "%players%", Integer.toString(Bukkit.getServer().getOnlinePlayers().size()))
                .replaceAll(ignoreCase + "%maxplayers%", Integer.toString(Bukkit.getServer().getMaxPlayers()))
                .replaceAll(ignoreCase + "%worldplayers%", Integer.toString(player.getWorld().getPlayers().size()))
                .replaceAll(ignoreCase + "%worldtype%", player.getWorld().getWorldType().getName().toLowerCase())
                .replaceAll(ignoreCase + "%viewdistance%", Integer.toString(Bukkit.getServer().getViewDistance()))
                .replaceAll(ignoreCase + "%mobspawnradius%", Integer.toString(Bukkit.getSpawnRadius() * 16))
                .replaceAll(ignoreCase + "%worldbordersize%", Double.toString(player.getWorld().getWorldBorder().getSize()))
                .replaceAll(ignoreCase + "%version%", Bukkit.getBukkitVersion())
                .replaceAll(ignoreCase + "%motd%", Bukkit.getServer().getMotd())
                .replaceAll(ignoreCase + "%weathertime%", Integer.toString(player.getWorld().getWeatherDuration() / 20))
                .replaceAll(ignoreCase + "%lightningtime%", Integer.toString(player.getWorld().getThunderDuration() / 20))
                .replaceAll(ignoreCase + "%worldtime%", Long.toString(player.getWorld().getTime()))
                .replaceAll(ignoreCase + "%servertime%", getCurrentTime());
        String none = Message.NONE.toString();
        if (WorldUtil.getNearestPlayer(player) != null) {
            replaced = replaced.replaceAll(ignoreCase + "%player%", WorldUtil.getNearestPlayer(player).getName());
        } else {
            replaced = replaced.replaceAll(ignoreCase + "%player%", none);
        }
        if (player.getItemInHand().getType() != Material.AIR) {
            replaced = replaced.replaceAll(ignoreCase + "%item%", getItemName(player.getItemInHand()));
        } else {
            replaced = replaced.replaceAll(ignoreCase + "%item%", none);
        }
        if (player.getTargetBlock(null, 5).getType() != Material.AIR) {
            replaced = replaced.replaceAll(ignoreCase + "%targetblock%", fromEnum(player.getTargetBlock(null, 5).getType(), true));
        } else {
            replaced = replaced.replaceAll(ignoreCase + "%targetblock%", none);
        }
        if (player.getVehicle() != null) {
            replaced = replaced.replaceAll(ignoreCase + "%vehicle%", fromEnum(player.getVehicle().getType(), true));
        } else {
            replaced = replaced.replaceAll(ignoreCase + "%vehicle%", none);
        }
        return replaced;
    }

    /**
     * Returns a string with sign variables
     * @param string the string to edit
     * @param sign the sign
     * @param player the player
     * @return the string with variables replaced
     */
    public static String replaceSignVariables(String string, Sign sign, Player player) {
        return string
                .replaceAll("%line1%", sign.getLine(0))
                .replaceAll("%line2%", sign.getLine(1))
                .replaceAll("%line3%", sign.getLine(2))
                .replaceAll("%line4%", sign.getLine(3))
                .replaceAll("%player%", player.getName());
    }
    
    /**
     * Returns a string with formatting codes
     * @param string the string to format
     * @return the string with formatting codes
     */
    public static String formatText(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String upperCaseFirst(String string) {
        String[] words = string.split(" ");
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < words.length; i++) {
            sb.append(Character.toUpperCase(words[i].charAt(0)));
            sb.append(words[i].substring(1));
            if(i < words.length - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    public static String fromEnum(Enum<?> toString, boolean upperCase) {
        String name = toString.name().toLowerCase();
        String replaced = name.replaceAll("_", " ");
        String string = replaced;
        if (upperCase) {
            string = upperCaseFirst(string);
        }
        return string;
    }

    public static String fromArray(Object[] array, int fromIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = fromIndex; i < array.length; i++) {
            sb.append(array[i]).append(" ");
        }
        String string = sb.toString();
        string = string.substring(0, string.length() - 1);
        return string;
    }
}
package com.v3ld1n.util;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.v3ld1n.ConfigSetting;
import com.v3ld1n.Message;

public final class StringUtil {
    private StringUtil() {
    }

    /**
     * Returns an item's name from its Material, or its custom name if it has one
     * @param item the item
     * @return the item's name
     */
    public static String getItemName(ItemStack item) {
        String name = fromEnum(item.getType(), true);
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            name = item.getItemMeta().getDisplayName();
        }
        return name;
    }

    /**
     * Returns an entity's name from its EntityType, or its custom name if it has one
     * @param entity the entity
     * @return the entity's name
     */
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
    public static String replacePlayerVariables(String string, Player player) {
        String ic = "(?i)";
        String vars = string
                .replaceAll(ic + "%name%", player.getName())
                .replaceAll(ic + "%uuid%", player.getUniqueId().toString())
                .replaceAll(ic + "%displayname%", player.getDisplayName())
                .replaceAll(ic + "%world%", player.getWorld().getName())
                .replaceAll(ic + "%biome%", fromEnum(player.getLocation().getBlock().getBiome(), true))
                .replaceAll(ic + "%block%", fromEnum(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType(), true))
                .replaceAll(ic + "%health%", Double.toString(player.getHealth()))
                .replaceAll(ic + "%maxhealth%", Double.toString(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()))
                .replaceAll(ic + "%hunger%", Integer.toString(player.getFoodLevel()))
                .replaceAll(ic + "%xp%", Integer.toString(player.getTotalExperience()))
                .replaceAll(ic + "%gamemode%", fromEnum(player.getGameMode(), true))
                .replaceAll(ic + "%speed%", Double.toString(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue()))
                .replaceAll(ic + "%ping%", Integer.toString(PlayerUtil.getPing(player)))
                .replaceAll(ic + "%entities%", Integer.toString(WorldUtil.getAllEntities().size()))
                .replaceAll(ic + "%worldentities%", Integer.toString(player.getWorld().getEntities().size()))
                .replaceAll(ic + "%blockentities%", Integer.toString(WorldUtil.getBlockEntities(player.getWorld()).size()))
                .replaceAll(ic + "%players%", Integer.toString(Bukkit.getServer().getOnlinePlayers().size()))
                .replaceAll(ic + "%maxplayers%", Integer.toString(Bukkit.getServer().getMaxPlayers()))
                .replaceAll(ic + "%worldplayers%", Integer.toString(player.getWorld().getPlayers().size()))
                .replaceAll(ic + "%worldtype%", fromEnum(player.getWorld().getWorldType(), true))
                .replaceAll(ic + "%viewdistance%", Integer.toString(Bukkit.getServer().getViewDistance()))
                .replaceAll(ic + "%mobspawnradius%", Integer.toString(Bukkit.getSpawnRadius() * 16))
                .replaceAll(ic + "%worldbordersize%", Double.toString(player.getWorld().getWorldBorder().getSize()))
                .replaceAll(ic + "%version%", Bukkit.getBukkitVersion())
                .replaceAll(ic + "%motd%", Bukkit.getServer().getMotd())
                .replaceAll(ic + "%weathertime%", TimeUtil.fromSeconds(player.getWorld().getWeatherDuration() / 20))
                .replaceAll(ic + "%weatherticks%", Integer.toString(player.getWorld().getWeatherDuration()))
                .replaceAll(ic + "%thundertime%", TimeUtil.fromSeconds(player.getWorld().getThunderDuration() / 20))
                .replaceAll(ic + "%worldtime%", Long.toString(player.getWorld().getTime()))
                .replaceAll(ic + "%servertime%", TimeUtil.format(TimeUtil.getTime(), "MMMM d, YYYY, h:mm:ss a"))
                .replaceAll(ic + "%lft%", "\u21E6")
                .replaceAll(ic + "%up%", "\u21E7")
                .replaceAll(ic + "%rht%", "\u21E8")
                .replaceAll(ic + "%dwn%", "\u21E9");
        String none = Message.NONE.toString();
        Player np = WorldUtil.getNearestPlayer(player);
        Entity ne = WorldUtil.getNearestEntity(player);
        ItemStack mi = player.getInventory().getItemInMainHand();
        ItemStack oi = player.getInventory().getItemInOffHand();
        Material tb = player.getTargetBlock((Set<Material>) null, 5).getType();
        Entity v = player.getVehicle();
        String nps = ic + "%player%";
        String nes = ic + "%entity%";
        String mis = ic + "%mainitem%";
        String ois = ic + "%offitem%";
        String tbs = ic + "%targetblock%";
        String vs = ic + "%vehicle%";
        vars = np != null ? vars.replaceAll(nps, np.getName()) : vars.replaceAll(nps, none);
        vars = ne != null ? vars.replaceAll(nes, getEntityName(ne)) : vars.replaceAll(nes, none);
        vars = mi.getType() != Material.AIR ? vars.replaceAll(mis, getItemName(mi)) : vars.replaceAll(mis, none);
        vars = oi.getType() != Material.AIR ? vars.replaceAll(ois, getItemName(oi)) : vars.replaceAll(ois, none);
        vars = tb != Material.AIR ? vars.replaceAll(tbs, fromEnum(tb, true)) : vars.replaceAll(tbs, none);
        vars = v != null ? vars.replaceAll(vs, fromEnum(v.getType(), true)) : vars.replaceAll(vs, none);
        return vars;
    }

    /**
     * Returns a string with sign variables
     * @param string the string to edit
     * @param sign the sign
     * @param player the player
     * @return the string with variables replaced
     */
    public static String replaceSignVariables(String string, Sign sign, CommandSender user) {
        return string
                .replaceAll("%line1%", sign.getLine(0))
                .replaceAll("%line2%", sign.getLine(1))
                .replaceAll("%line3%", sign.getLine(2))
                .replaceAll("%line4%", sign.getLine(3))
                .replaceAll("%player%", user.getName());
    }
    
    /**
     * Returns a string with formatting codes
     * @param string the string to format
     * @return the string with formatting codes
     */
    public static String formatText(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Capitalizes the first letter of every word in a string
     * @param string the string
     * @return the string with uppercase letters
     */
    public static String upperCaseFirst(String string) {
        String[] words = string.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            sb.append(Character.toUpperCase(words[i].charAt(0)));
            sb.append(words[i].substring(1));
            if (i < words.length - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    /**
     * Returns an enum name as a string
     * @param toString the enum
     * @param upperCase whether the first letters should be capitalized
     * @return the enum name as a string
     */
    public static String fromEnum(Enum<?> toString, boolean upperCase) {
        String name = toString.name().toLowerCase();
        String replaced = name.replaceAll("_", " ");
        String string = replaced;
        if (upperCase) {
            string = upperCaseFirst(string);
        }
        return string;
    }

    /**
     * Returns an array as a string
     * @param array the array
     * @param fromIndex the index to start from
     * @return the array as a string
     */
    public static String fromArray(Object[] array, int fromIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = fromIndex; i < array.length; i++) {
            sb.append(array[i]).append(" ");
        }
        String string = sb.toString();
        string = string.substring(0, string.length() - 1);
        return string;
    }

    /**
     * Returns a substring if the string is longer than the character limit
     * @param string the string
     * @param charLimit the character limit
     * @return the substring
     */
    public static String substring(String string, int charLimit) {
        String newString = string;
        if (newString.length() > charLimit) {
            newString = newString.substring(0, charLimit);
        }
        return newString;
    }

    /**
     * Returns a location as a string
     * @param location the location
     * @return the location as a string
     */
    public static String fromLocation(Location location) {
        DecimalFormat df = new DecimalFormat("##.##");
        StringBuilder sb = new StringBuilder();
        sb.append(location.getWorld().getName())
        .append(" | ").append(df.format(location.getX()))
        .append(" | ").append(df.format(location.getY()))
        .append(" | ").append(df.format(location.getZ()))
        .append(" | ").append(df.format(location.getYaw()))
        .append(" | ").append(df.format(location.getPitch()));
        return sb.toString();
    }

    /**
     * Reads JSON text from a URL
     * @param url the URL
     * @return the JSON element
     */
    public static JsonElement readJsonFromUrl(String url) {
        JsonElement element = null;
        try {
            URL readFrom = new URL(url);
            Scanner s = new Scanner(readFrom.openStream());
            if (s.hasNext()) {
                String line = s.nextLine();
                element = new JsonParser().parse(line);
            }
            s.close();
        } catch (IOException e) {
            if (ConfigSetting.DEBUG.getBoolean()) {
                e.printStackTrace();
            }
        }
        return element;
    }

    /**
     * Adds dashes to a UUID
     * @param uuid the UUID
     * @return the UUID with dashes
     */
    public static String dashUUID(String uuid) {
        StringBuffer sb = new StringBuffer(uuid);
        sb.insert(8, "-");
        sb = new StringBuffer(sb.toString());
        sb.insert(13, "-");
        sb = new StringBuffer(sb.toString());
        sb.insert(18, "-");
        sb = new StringBuffer(sb.toString());
        sb.insert(23, "-");
        return sb.toString();
    }

    /**
     * Returns whether a string only contains integers
     * @param string the string
     * @return whether a string is an integer
     */
    public static boolean isInteger(String string) {
        return string.matches("^\\d+$");
    }

    /**
     * Returns a string as an integer
     * @param string the string
     * @return the string as an integer
     */
    public static int toInteger(String string) {
        int integer = 0;
        if (isInteger(string)) {
            try {
                integer = Integer.parseInt(string);
            } catch (Exception e) {
                integer = 0;
            }
        }
        return integer;
    }

    /**
     * Returns a string as an integer
     * @param string the string
     * @param defaultInt what to return if the string is not an integer
     * @return the string as an integer
     */
    public static int toInteger(String string, int defaultInt) {
        int integer = defaultInt;
        if (isInteger(string)) {
            try {
                integer = Integer.parseInt(string);
            } catch (Exception e) {
                integer = defaultInt;
            }
        }
        return integer;
    }
}
package com.v3ld1n.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtil {
    private ItemUtil() {
    }

    public static void setName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name == null ? null : ChatColor.RESET + name);
        item.setItemMeta(meta);
    }

    public static void setLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore == null ? null : lore);
        item.setItemMeta(meta);
    }

    public static void setLoreAtLine(ItemStack item, String lore, int line) {
        ItemMeta meta = item.getItemMeta();
        List<String> itemLore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
        itemLore.set(line, lore);
        meta.setLore(itemLore);
        item.setItemMeta(meta);
    }

    public static void addLore(ItemStack item, String line) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
        lore.add(line);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static void removeLore(ItemStack item, int line) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
        lore.remove(line);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public static ItemStack smelt(ItemStack item) {
        Material type = item.getType();
        short data = item.getDurability();
        switch (item.getType()) {
        case PORK:
            type = Material.GRILLED_PORK;
            break;
        case RAW_BEEF:
            type = Material.COOKED_BEEF;
            break;
        case RAW_CHICKEN:
            type = Material.COOKED_CHICKEN;
            break;
        case RAW_FISH:
            type = Material.COOKED_FISH;
            break;
        case POTATO:
            type = Material.BAKED_POTATO;
            break;
        case MUTTON:
            type = Material.COOKED_MUTTON;
            break;
        case RABBIT:
            type = Material.COOKED_RABBIT;
            break;
        case IRON_ORE:
            type = Material.IRON_INGOT;
            break;
        case GOLD_ORE:
            type = Material.GOLD_INGOT;
            break;
        case SAND:
            type = Material.GLASS;
            break;
        case COBBLESTONE:
            type = Material.STONE;
            break;
        case CLAY_BALL:
            type = Material.CLAY_BRICK;
            break;
        case NETHERRACK:
            type = Material.NETHER_BRICK_ITEM;
            break;
        case CLAY:
            type = Material.HARD_CLAY;
            break;
        case SMOOTH_BRICK:
            type = Material.SMOOTH_BRICK;
            data = 2;
            break;
        case DIAMOND_ORE:
            type = Material.DIAMOND;
            break;
        case LAPIS_ORE:
            type = Material.INK_SACK;
            data = 4;
            break;
        case REDSTONE_ORE:
            type = Material.REDSTONE;
            break;
        case COAL_ORE:
            type = Material.COAL;
            break;
        case EMERALD_ORE:
            type = Material.EMERALD;
            break;
        case QUARTZ_ORE:
            type = Material.QUARTZ;
            break;
        case LOG:
            type = Material.COAL;
            data = 1;
            break;
        case LOG_2:
            type = Material.COAL;
            data = 1;
            break;
        case CACTUS:
            type = Material.INK_SACK;
            data = 2;
            break;
        case SPONGE:
            type = Material.SPONGE;
            data = 0;
            break;
        default:
            break;
        }
        ItemStack smeltedItem = item;
        smeltedItem.setType(type);
        smeltedItem.setDurability(data);
        return smeltedItem;
    }
}
package com.v3ld1n.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtil {
    private ItemUtil() {
    }

    /**
     * Sets the name of an item
     * @param item the item
     * @param name the new name
     */
    public static void setName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name == null ? null : ChatColor.RESET + name);
        item.setItemMeta(meta);
    }

    /**
     * Sets the lore of an item
     * @param item the item
     * @param lore the new lore
     */
    public static void setLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore == null ? null : lore);
        item.setItemMeta(meta);
    }

    /**
     * Sets the text at a line of an item's lore
     * @param item the item
     * @param lore the text
     * @param line the line to set to the text
     */
    public static void setLoreAtLine(ItemStack item, String lore, int line) {
        ItemMeta meta = item.getItemMeta();
        List<String> itemLore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
        itemLore.set(line, lore);
        meta.setLore(itemLore);
        item.setItemMeta(meta);
    }

    /**
     * Adds a line to an item's lore
     * @param item the item
     * @param line the text to add
     */
    public static void addLore(ItemStack item, String line) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
        lore.add(line);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    /**
     * Removes a line from an item's lore
     * @param item the item
     * @param line the line to remove
     */
    public static void removeLore(ItemStack item, int line) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
        lore.remove(line);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    /**
     * Adds an enchantment to an item
     * @param item the item
     * @param ench the enchantment
     * @param level the level of the enchantment
     */
    public static void addEnchantment(ItemStack item, Enchantment ench, int level) {
        ItemMeta meta = item.getItemMeta();
        meta.addEnchant(ench, level, true);
        item.setItemMeta(meta);
    }

    /**
     * Smelts an item
     * @param item the item
     */
    public static void smelt(ItemStack item) {
        Material type = item.getType();
        short data = item.getDurability();
        Iterator<Recipe> iter = Bukkit.recipeIterator();
        while (iter.hasNext()) {
           Recipe recipe = iter.next();
           if (recipe instanceof FurnaceRecipe) {
               if (((FurnaceRecipe) recipe).getInput().getType() == item.getType()) {
                   type = recipe.getResult().getType();
                   data = recipe.getResult().getDurability();
                   break;
               }
           }
        }
        item.setType(type);
        item.setDurability(data);
    }
}
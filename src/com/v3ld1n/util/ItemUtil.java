package com.v3ld1n.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.server.v1_10_R1.NBTBase;
import net.minecraft.server.v1_10_R1.NBTTagByte;
import net.minecraft.server.v1_10_R1.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
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
     * Hides item descriptions
     * @param item the item
     * @param enchantments whether to hide enchantments
     * @param attributes whether to hide attributes
     * @param unbreakable whether to hide unbreakable
     * @param canDestroy whether to hide can destroy blocks
     * @param canPlaceOn whether to hide can place on blocks
     * @param other whether to hide other descriptions (banner patterns, firework effects, etc.)
     */
    public static ItemStack hideFlags(ItemStack item, boolean enchantments, boolean attributes, boolean unbreakable, boolean canDestroy, boolean canPlaceOn, boolean other) {
        byte hide = 0;
        if (enchantments) hide += 1;
        if (attributes) hide += 2;
        if (unbreakable) hide += 4;
        if (canDestroy) hide += 8;
        if (canPlaceOn) hide += 16;
        if (other) hide += 32;
        return setTag(item, "HideFlags", new NBTTagByte(hide));
    }

    /**
     * Hides all item descriptions
     * @param item the item
     */
    public static ItemStack hideFlags(ItemStack item) {
        return hideFlags(item, true, true, true, true, true, true);
    }

    /**
     * Sets an NBT tag on an item
     * @param item the item
     * @param tagName the tag name
     * @param value the tag value
     * @return the item with the new tag
     */
    public static ItemStack setTag(ItemStack item, String tagName, NBTBase value) {
        net.minecraft.server.v1_10_R1.ItemStack stack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = stack.hasTag() ? stack.getTag() : new NBTTagCompound();
        tag.set(tagName, value);
        stack.setTag(tag);
        return CraftItemStack.asBukkitCopy(stack);
    }

    /**
     * Returns all crafting recipes
     * @return a list of recipes
     */
    public static List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        Iterator<Recipe> iter = Bukkit.recipeIterator();
        while (iter.hasNext()) {
            recipes.add(iter.next());
        }
        return recipes;
    }

    /**
     * Returns all crafting recipes
     * @return a list of recipes
     */
    public static List<Recipe> getCraftingRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (!(recipe instanceof FurnaceRecipe)) {
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    /**
     * Returns all furnace recipes
     * @return a list of recipes
     */
    public static List<FurnaceRecipe> getFurnaceRecipes() {
        List<FurnaceRecipe> recipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe instanceof FurnaceRecipe) {
                recipes.add((FurnaceRecipe) recipe);
            }
        }
        return recipes;
    }

    /**
     * Returns all crafting recipes for an item
     * @param item the item
     * @return a list of recipes
     */
    public static List<Recipe> getCraftingRecipes(ItemStack item) {
        List<Recipe> recipes = new ArrayList<>();
        for (Recipe recipe : getAllRecipes()) {
            if (!(recipe instanceof FurnaceRecipe)) {
                if (recipe.getResult().getType() == item.getType()) {
                    recipes.add(recipe);
                }
            }
        }
        return recipes;
    }

    /**
     * Smelts an item
     * @param item the item
     */
    public static void smelt(ItemStack item) {
        Material type = item.getType();
        short data = item.getDurability();
        for (FurnaceRecipe recipe : getFurnaceRecipes()) {
           if ((recipe).getInput().getType() == item.getType()) {
               type = recipe.getResult().getType();
               data = recipe.getResult().getDurability();
               break;
           }
        }
        item.setType(type);
        item.setDurability(data);
    }
}
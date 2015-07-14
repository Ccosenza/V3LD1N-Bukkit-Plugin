package com.v3ld1n.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.v3ld1n.Config;
import com.v3ld1n.util.ConfigUtil;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.Sound;
import com.v3ld1n.util.StringUtil;

public class V3LD1NItem implements Listener {
    private final FileConfiguration config = Config.ITEMS.getConfig();
    private final String id;
    private Material material;
    private String name;
    protected List<String> particles = new ArrayList<>();
    protected static final Random random = new Random();
    protected List<Action> leftClickActions = new ArrayList<>();
    protected List<Action> rightClickActions = new ArrayList<>();

    public V3LD1NItem(String id) {
        this.id = id;
        this.setName(this.getStringSetting("name"));
        this.setMaterial(Material.valueOf(this.getStringSetting("item")));
        if (this.getStringListSetting("particles") != null) {
            this.setParticles(this.getStringListSetting("particles"));
        }
        leftClickActions.add(Action.LEFT_CLICK_AIR);
        leftClickActions.add(Action.LEFT_CLICK_BLOCK);
        rightClickActions.add(Action.RIGHT_CLICK_AIR);
        rightClickActions.add(Action.RIGHT_CLICK_BLOCK);
    }

    public String getId() {
        return id;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getParticles() {
        return particles;
    }

    public void setParticles(List<String> particles) {
        this.particles = particles;
    }

    public void displayParticles(Location location) {
        Particle.displayList(location, this.particles);
    }

    public void displayParticles(Location location, Player player) {
        Particle.displayList(location, player, this.particles);
    }

    public boolean isLeftClick(Action action) {
        return leftClickActions.contains(action);
    }

    public boolean isRightClick(Action action) {
        return rightClickActions.contains(action);
    }

    public boolean equalsItem(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName() != null) {
            if (item.getType() == this.material && item.getItemMeta().getDisplayName().equals(StringUtil.formatText(this.name))) {
                return true;
            }
        }
        return false;
    }


    /**
     * Checks if the projectile is a specific type, its shooter is a player, and the shooter is holding Ratchet's Bow
     * @param pr the projectile
     * @param types the list of valid types
     * @return whether the projectile is valid
     */
    public boolean projectileIsValid(Projectile pr, EntityType... types) {
        List<EntityType> typeList = Arrays.asList(types);
        boolean typeIsValid = typeList.contains(pr.getType());

        boolean shooterIsPlayer = pr.getShooter() != null && pr.getShooter() instanceof Player;

        Player shooter = (Player) pr.getShooter();
        boolean holdingItem = this.equalsItem(shooter.getItemInHand());

        return typeIsValid && shooterIsPlayer && holdingItem;
    }

    public String getStringSetting(String settingName) {
        return config.getString(id + "." + settingName);
    }

    public int getIntSetting(String settingName) {
        return config.getInt(id + "." + settingName);
    }

    public double getDoubleSetting(String settingName) {
        return config.getDouble(id + "." + settingName);
    }

    public boolean getBooleanSetting(String settingName) {
        return config.getBoolean(id + "." + settingName);
    }

    public List<String> getStringListSetting(String settingName) {
        return config.getStringList(id + "." + settingName);
    }

    public Location getLocationSetting(String settingName) {
        String setting = config.getString(id + "." + settingName);
        return ConfigUtil.locationFromString(setting);
    }

    public Vector getVectorSetting(String settingName) {
        return config.getVector(id + "." + settingName);
    }

    public Particle getParticleSetting(String settingName) {
        return Particle.fromString(config.getString(id + "." + settingName));
    }

    public Sound getSoundSetting(String settingName) {
        return Sound.fromString(config.getString(id + "." + settingName));
    }

    @Override
    public String toString() {
        return id + "(" + material + ", " + name + ")";
    }
}
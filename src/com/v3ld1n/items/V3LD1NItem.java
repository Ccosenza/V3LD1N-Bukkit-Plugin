package com.v3ld1n.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.v3ld1n.Config;
import com.v3ld1n.util.ConfigUtil;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.StringUtil;

public class V3LD1NItem implements Listener {
    private final String id;
    private Material material;
    private String name;
    protected List<String> particles = new ArrayList<>();
    protected static final Random random = new Random();
    protected List<Action> useActions = new ArrayList<>();

    public V3LD1NItem(String id) {
        this.id = id;
        this.setName(this.getStringSetting("name"));
        this.setMaterial(Material.valueOf(this.getStringSetting("item")));
        if (this.getStringListSetting("particles") != null) {
            this.setParticles(this.getStringListSetting("particles"));
        }
        useActions.add(Action.RIGHT_CLICK_AIR);
        useActions.add(Action.RIGHT_CLICK_BLOCK);
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

    public boolean equalsItem(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName() != null) {
            if (item.getType() == this.material && item.getItemMeta().getDisplayName().equals(StringUtil.formatText(this.name))) {
                return true;
            }
        }
        return false;
    }

    public String getStringSetting(String settingName) {
        return Config.ITEMS.getConfig().getString(id + "." + settingName);
    }

    public int getIntSetting(String settingName) {
        return Config.ITEMS.getConfig().getInt(id + "." + settingName);
    }

    public double getDoubleSetting(String settingName) {
        return Config.ITEMS.getConfig().getDouble(id + "." + settingName);
    }

    public boolean getBooleanSetting(String settingName) {
        return Config.ITEMS.getConfig().getBoolean(id + "." + settingName);
    }

    public List<String> getStringListSetting(String settingName) {
        return Config.ITEMS.getConfig().getStringList(id + "." + settingName);
    }

    public Location getLocationSetting(String settingName) {
        String setting = Config.ITEMS.getConfig().getString(id + "." + settingName);
        return ConfigUtil.locationFromString(setting);
    }

    public Vector getVectorSetting(String settingName) {
        return Config.ITEMS.getConfig().getVector(id + "." + settingName);
    }

    public Particle getParticleSetting(String settingName) {
        return Particle.fromString(Config.ITEMS.getConfig().getString(id + "." + settingName));
    }

    @Override
    public String toString() {
        return id + "(" + material + ", " + name + ")";
    }
}
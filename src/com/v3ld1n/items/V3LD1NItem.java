package com.v3ld1n.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
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
    protected Settings settings = this.new Settings();
    protected List<Particle> particles = new ArrayList<>();
    protected List<Sound> sounds = new ArrayList<>();
    protected static final Random random = new Random();

    public V3LD1NItem(String id) {
        this.id = id;
        this.name = this.settings.getString("name");
        this.material = Material.valueOf(this.settings.getString("item"));

        if (this.settings.getStringList("particles") != null) {
            this.particles = this.settings.getParticles("particles");
        }
        if (this.settings.getStringList("sounds") != null) {
            this.sounds = this.settings.getSounds("sounds");
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void displayParticles(Location location) {
        Particle.displayList(particles, location);
    }

    public void displayParticlesToPlayer(Location location, Player player) {
        Particle.displayListToPlayer(particles, location, player);
    }

    public void playSounds(Location location) {
        Sound.playList(sounds, location);
    }

    public void playSoundsToPlayer(Location location, Player player) {
        Sound.playListToPlayer(sounds, location, player);
    }

    public boolean isLeftClick(Action action, EquipmentSlot hand) {
        return action.name().startsWith("LEFT_CLICK") && hand == EquipmentSlot.HAND;
    }

    public boolean isRightClick(Action action, EquipmentSlot hand) {
        return action.name().startsWith("RIGHT_CLICK") && hand == EquipmentSlot.HAND;
    }

    /**
     * Checks if an item has this item's name and material
     * @param item the item to compare with
     * @return whether the item is equal
     */
    public boolean equalsItem(ItemStack item) {
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;
        if (item.getItemMeta().getDisplayName() == null) return false;

        String formattedName = StringUtil.formatText(this.name);
        boolean nameEqual = item.getItemMeta().getDisplayName().equals(formattedName);
        boolean materialEqual = item.getType() == this.material;
        return nameEqual && materialEqual;
    }

    /**
     * Checks if the entity is a player holding this item
     * @param entity the entity
     * @return whether the entity is a player holding this item
     */
    public boolean entityIsHoldingItem(Entity entity) {
        if (entity == null) return false;
        if (entity.getType() != EntityType.PLAYER) return false;

        Player player = (Player) entity;
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        boolean isHoldingItem = this.equalsItem(mainHand);
        return isHoldingItem;
    }

    /**
     * Checks if an entity is a projectile and a specific type, its shooter is a player, and the shooter is holding this item
     * @param entity the projectile
     * @param types the list of valid types
     * @return whether the projectile is valid
     */
    public boolean projectileIsValid(Entity entity, EntityType... types) {
        if (!(entity instanceof Projectile)) return false;
        Projectile projectile = (Projectile) entity;

        List<EntityType> typeList = Arrays.asList(types);
        boolean typeIsValid = typeList.contains(projectile.getType());
        if (!typeIsValid) return false;

        boolean shooterIsPlayer = projectile.getShooter() != null && projectile.getShooter() instanceof Player;
        if (!shooterIsPlayer) return false;

        Player shooter = (Player) projectile.getShooter();

        return typeIsValid && shooterIsPlayer && entityIsHoldingItem(shooter);
    }

    @Override
    public String toString() {
        return id + "(" + material + ", " + name + ")";
    }

    public class Settings {
        public String getString(String settingName) {
            return config.getString(id + "." + settingName);
        }

        public int getInt(String settingName) {
            return config.getInt(id + "." + settingName);
        }

        public double getDouble(String settingName) {
            return config.getDouble(id + "." + settingName);
        }

        public boolean getBoolean(String settingName) {
            return config.getBoolean(id + "." + settingName);
        }

        public List<String> getStringList(String settingName) {
            return config.getStringList(id + "." + settingName);
        }

        public Location getLocation(String settingName) {
            String setting = config.getString(id + "." + settingName);
            return ConfigUtil.locationFromString(setting);
        }

        public Vector getVector(String settingName) {
            return config.getVector(id + "." + settingName);
        }

        public List<Particle> getParticles(String settingName) {
            return Particle.fromList(config.getStringList(id + "." + settingName));
        }

        public List<Sound> getSounds(String settingName) {
            return Sound.fromList(config.getStringList(id + "." + settingName));
        }
    }
}
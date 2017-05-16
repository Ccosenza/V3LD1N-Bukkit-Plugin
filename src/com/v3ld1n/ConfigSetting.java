package com.v3ld1n;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.v3ld1n.util.ConfigUtil;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.Sound;

public enum ConfigSetting {
    SCOREBOARD_PREFIX("scoreboard-prefix", "config.yml", "v3ld1n_"),
    DEFAULT_PERMISSION_GROUP("default-permission-group", "config.yml", "Veldian"),
    DISABLE_FIREWORK_DAMAGE("disable-firework-damage", "config.yml", true),

    AUTO_SAVE_ENABLED("auto-save.enabled", "config.yml", true),
    AUTO_SAVE_TICKS("auto-save.ticks", "config.yml", 12000),

    SERVER_LIST_MOTD("server-list.motd", "config.yml"),
    SERVER_LIST_MAX_PLAYERS("server-list.max-players", "config.yml"),

    PLAYER_LIST_HEADER("player-list.header", "config.yml"),
    PLAYER_LIST_FOOTER("player-list.footer", "config.yml"),
    PLAYER_LIST_PING_ENABLED("player-list.ping.enabled", "config.yml", false),
    PLAYER_LIST_PING_TICKS("player-list.ping.ticks", "config.yml", 20),

    MONEY_REWARD_ENABLED("money-reward.enabled", "config.yml", false),
    MONEY_REWARD_BASE_AMOUNT("money-reward.base-amount", "config.yml", 0),
    MONEY_REWARD_HOUR_MULTIPLIER("money-reward.hour-multiplier", "config.yml", 1),
    MONEY_REWARD_TICKS("money-reward.ticks", "config.yml", 72000),

    MONEY_ITEM_ITEM("money-item.item", "config.yml", "EMERALD"),

    PROJECTILE_JUMP_SPEED("projectile-jump-speed", "items.yml", 1.0),

    PARTICLE_PLAYER_EFFECTS_LIGHTNING("player-effects-lightning", "particles.yml"),
    PARTICLE_TRAILS("trails.normal", "particles.yml"),
    PARTICLE_TRAILS_PLAYER_EFFECTS("trails.player-effects", "particles.yml"),
    PARTICLE_TRAILS_BLOCKED("trails.blocked", "particles.yml"),
    PARTICLE_RATCHETSBOW_FIREBALL("ratchetsbow.fireball", "particles.yml"),
    PARTICLE_RATCHETSBOW_ARROW("ratchetsbow.arrow", "particles.yml"),
    PARTICLE_RATCHETSBOW_TRIPLE_ARROWS("ratchetsbow.triple-arrows", "particles.yml"),
    PARTICLE_RATCHETSBOW_FIREWORK("ratchetsbow.firework-arrow", "particles.yml"),
    PARTICLE_RATCHETSBOW_SNOWBALL("ratchetsbow.snowball", "particles.yml"),
    PARTICLE_RATCHETSBOW_ENDER_PEARL("ratchetsbow.ender-pearl", "particles.yml"),
    PARTICLE_RATCHETSBOW_EGG("ratchetsbow.egg", "particles.yml"),
    PARTICLE_RATCHETSBOW_SKULL("ratchetsbow.wither-skull", "particles.yml"),
    PARTICLE_RATCHETSBOW_BLUE_SKULL("ratchetsbow.blue-wither-skull", "particles.yml"),

    PLAYER_EFFECTS_PLAYERS("player-effects.players", "player-data.yml"),
    PLAYER_EFFECTS_CHANCE("player-effects.chance", "player-data.yml", 20),
    PLAYER_EFFECTS_TICKS("player-effects.ticks", "player-data.yml", 400),

    CANCEL_SPAWN_WORLDS("cancel-spawn.worlds", "world-options.yml"),
    CANCEL_DROP_WORLDS("cancel-drop.worlds", "world-options.yml"),
    REMOVE_ARROWS_WORLDS("remove-arrows.worlds", "world-options.yml"),
    REMOVE_PROJECTILES_WORLDS("remove-projectiles-on-death.worlds", "world-options.yml"),
    REMOVE_VEHICLES_WORLDS("remove-vehicles.worlds", "world-options.yml"),
    REMOVE_VEHICLES_SECONDS("remove-vehicles.seconds", "world-options.yml"),
    REMOVE_VEHICLES_VEHICLES("remove-vehicles.vehicles", "world-options.yml");

    private final String name;
    private final String fileName;
    private final Object defaultValue;

    private ConfigSetting(String name, String fileName) {
        this.name = name;
        this.fileName = fileName;
        this.defaultValue = null;
    }

    private ConfigSetting(String name, String fileName, Object defaultValue) {
        this.name = name;
        this.fileName = fileName;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }

    public Object getValue() {
        if (V3LD1N.getConfig(fileName).getConfig().get(name) != null) {
            return V3LD1N.getConfig(fileName).getConfig().get(name);
        }
        return defaultValue;
    }

    public String getString() {
        if (V3LD1N.getConfig(fileName).getConfig().get(name) != null) {
            return V3LD1N.getConfig(fileName).getConfig().getString(name);
        }
        return (String) defaultValue;
    }

    public int getInt() {
        if (V3LD1N.getConfig(fileName).getConfig().get(name) != null) {
            return V3LD1N.getConfig(fileName).getConfig().getInt(name);
        }
        return (int) defaultValue;
    }

    public double getDouble() {
        if (V3LD1N.getConfig(fileName).getConfig().get(name) != null) {
            return V3LD1N.getConfig(fileName).getConfig().getDouble(name);
        }
        return (double) defaultValue;
    }

    public boolean getBoolean() {
        if (V3LD1N.getConfig(fileName).getConfig().get(name) != null) {
            return V3LD1N.getConfig(fileName).getConfig().getBoolean(name);
        }
        return (boolean) defaultValue;
    }

    public List<?> getList() {
        if (V3LD1N.getConfig(fileName).getConfig().get(name) != null) {
            return V3LD1N.getConfig(fileName).getConfig().getList(name);
        }
        return (List<?>) defaultValue;
    }

    public List<String> getStringList() {
        if (V3LD1N.getConfig(fileName).getConfig().get(name) != null) {
            return V3LD1N.getConfig(fileName).getConfig().getStringList(name);
        }
        List<String> defaultList = new ArrayList<>();
        defaultList.add(defaultValue.toString());
        return defaultList;
    }

    public List<Particle> getParticles() {
        if (V3LD1N.getConfig(fileName).getConfig().get(name) != null) {
            return Particle.fromList(V3LD1N.getConfig(fileName).getConfig().getStringList(name));
        }
        List<Particle> defaultList = new ArrayList<>();
        return defaultList;
    }

    public List<Sound> getSounds() {
        if (V3LD1N.getConfig(fileName).getConfig().get(name) != null) {
            return Sound.fromList(V3LD1N.getConfig(fileName).getConfig().getStringList(name));
        }
        List<Sound> defaultList = new ArrayList<>();
        return defaultList;
    }

    public Location getLocation() {
        if (V3LD1N.getConfig(fileName).getConfig().get(name) != null) {
            String setting = V3LD1N.getConfig(fileName).getConfig().getString(name);
            return ConfigUtil.locationFromString(setting);
        }
        return (Location) defaultValue;
    }

    public Vector getVector() {
        if (V3LD1N.getConfig(fileName).getConfig().get(name) != null) {
            return V3LD1N.getConfig(fileName).getConfig().getVector(name);
        }
        return (Vector) defaultValue;
    }

    public void setValue(Object value) {
        V3LD1N.getConfig(fileName).getConfig().set(name, value);
        V3LD1N.getConfig(fileName).saveConfig();
    }
}
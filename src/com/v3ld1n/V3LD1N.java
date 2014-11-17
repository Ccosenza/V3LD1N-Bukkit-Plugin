package com.v3ld1n;

import java.util.ArrayList;
import java.util.List;

import com.v3ld1n.listeners.EntityListener;
import com.v3ld1n.listeners.PlayerListener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.v3ld1n.commands.*;
import com.v3ld1n.items.*;
import com.v3ld1n.items.ratchet.*;
import com.v3ld1n.tasks.*;
import com.v3ld1n.util.ConfigAccessor;
import com.v3ld1n.util.ConfigUtil;
import com.v3ld1n.util.Particle;

public class V3LD1N extends JavaPlugin {
    private static V3LD1N plugin;
    private static List<ConfigAccessor> configs;
    private static WorldGuardPlugin worldGuard;
    private static List<V3LD1NItem> items;
    private static List<ItemTask> itemTasks;
    //private static List<ParticleTask> particleTasks;
    //private static List<SoundTask> soundTasks;
    //private static List<TeleportTask> teleportTasks;
    static final PluginManager pluginManager = Bukkit.getServer().getPluginManager();

    @Override
    public void onEnable() {
        plugin = this;
        String bukkitVersion = "1.7.10-R0.1-SNAPSHOT";
        if (!Bukkit.getBukkitVersion().equals(bukkitVersion)) {
            plugin.getLogger().warning(String.format(Message.INVALID_BUKKIT_VERSION.toString(), plugin.getDescription().getVersion(), bukkitVersion, Bukkit.getBukkitVersion()));
        }
        configs = new ArrayList<>();
        items = new ArrayList<>();
        itemTasks = new ArrayList<>();
        //particleTasks = new ArrayList<>();
        //soundTasks = new ArrayList<>();
        //teleportTasks = new ArrayList<>();
        loadConfig();
        setupWorldGuard();
        loadItems();
        loadItemTasks();
        pluginManager.registerEvents(new PlayerListener(), plugin);
        pluginManager.registerEvents(new EntityListener(), plugin);
        getCommand("v3ld1nplugin").setExecutor(new V3LD1NCommand());
        //getCommand("faq").setExecutor(new FAQCommand());
        getCommand("particle").setExecutor(new ParticleCommand());
        getCommand("trail").setExecutor(new TrailCommand());
        getCommand("sethealth").setExecutor(new SetHealthCommand());
        getCommand("setmaxhealth").setExecutor(new SetMaxHealthCommand());
        getCommand("ratchetsbow").setExecutor(new RatchetsBowCommand());
        getCommand("fireworkarrows").setExecutor(new FireworkArrowsCommand());
        getCommand("resourcepack").setExecutor(new ResourcePackCommand());
        getCommand("autoresourcepack").setExecutor(new AutoResourcePackCommand());
        getCommand("motd").setExecutor(new MotdCommand());
        //getCommand("nextsound").setExecutor(new NextSoundCommand());
        getCommand("editsign").setExecutor(new EditSignCommand());
        getCommand("setfulltime").setExecutor(new SetFullTimeCommand());
        getCommand("playanimation").setExecutor(new PlayAnimationCommand());
        getCommand("sidebarmessage").setExecutor(new SidebarMessageCommand());
        //getCommand("uuid").setExecutor(new UUIDCommand());
        getCommand("push").setExecutor(new PushCommand());
        getCommand("sethotbarslot").setExecutor(new SetHotbarSlotCommand());
        getCommand("setmotd").setExecutor(new SetMotdCommand());
        getCommand("actionbarmessage").setExecutor(new ActionBarMessageCommand());
        getCommand("timeplayed").setExecutor(new TimePlayedCommand());
        getCommand("playerlist").setExecutor(new PlayerListCommand());
        WarpCommand warpcommand = new WarpCommand();
        getCommand("creative").setExecutor(warpcommand);
        getCommand("factions").setExecutor(warpcommand);
        getCommand("kitpvp").setExecutor(warpcommand);
        getCommand("store").setExecutor(warpcommand);
        getCommand("storetop").setExecutor(warpcommand);
        getCommand("timetravel").setExecutor(warpcommand);
        getCommand("v3ld1n").setExecutor(warpcommand);
        getCommand("veldin").setExecutor(warpcommand);
        getCommand("warproom").setExecutor(warpcommand);
    }

    @Override
    public void onDisable() {
        configs = null;
        items = null;
        itemTasks = null;
        //particleTasks = null;
        //soundTasks = null;
        //teleportTasks = null;
        plugin = null;
    }

    public void loadConfig() {
        configs.add(new ConfigAccessor(plugin, "config.yml"));
        configs.add(new ConfigAccessor(plugin, "faq.yml"));
        configs.add(new ConfigAccessor(plugin, "info-messages.yml"));
        configs.add(new ConfigAccessor(plugin, "items.yml"));
        configs.add(new ConfigAccessor(plugin, "messages.yml"));
        configs.add(new ConfigAccessor(plugin, "motd.yml"));
        configs.add(new ConfigAccessor(plugin, "particles.yml"));
        configs.add(new ConfigAccessor(plugin, "player-data.yml"));
        configs.add(new ConfigAccessor(plugin, "signs.yml"));
        configs.add(new ConfigAccessor(plugin, "tasks-block.yml"));
        configs.add(new ConfigAccessor(plugin, "tasks-item.yml"));
        configs.add(new ConfigAccessor(plugin, "tasks-particle.yml"));
        configs.add(new ConfigAccessor(plugin, "tasks-sound.yml"));
        configs.add(new ConfigAccessor(plugin, "tasks-teleport.yml"));
        configs.add(new ConfigAccessor(plugin, "warps.yml"));
        configs.add(new ConfigAccessor(plugin, "world-options.yml"));
    }

    public static void setupWorldGuard() {
        if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            worldGuard = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        } else {
            worldGuard = null;
        }
    }

    public static void loadItems() {
        items.add(new FireworkBow());
        items.add(new FlightFeather());
        items.add(new LightningBow());
        items.add(new WitherSword());
        items.add(new RatchetAxe());
        items.add(new RatchetBoots());
        items.add(new RatchetBow());
        items.add(new RatchetChestplate());
        items.add(new RatchetFirework());
        items.add(new RatchetFireworkStar());
        items.add(new RatchetFishingRod());
        items.add(new RatchetFlintAndSteel());
        items.add(new RatchetHelmet());
        items.add(new RatchetHoe());
        items.add(new RatchetLeggings());
        items.add(new RatchetShears());
        items.add(new RatchetShovel());
        items.add(new RatchetSword());
        for (V3LD1NItem item : items) {
            pluginManager.registerEvents(item, plugin);
            if (ConfigSetting.DEBUG.getBoolean()) {
                plugin.getLogger().info(String.format(Message.LOADING_ITEM.toString(), item.getId()));
            }
        }
    }

    /**
     * Sets up item tasks from the config file
     */
    public void loadItemTasks() {
        try {
            if (Config.TASKS_ITEM.getConfig() != null) {
                FileConfiguration config = Config.TASKS_ITEM.getConfig();
                for (String key : config.getKeys(false)) {
                    long ticks = config.getLong(key + ".ticks");
                    String runMode = config.getString(key + ".run-mode");
                    Location location = ConfigUtil.locationFromString(config.getString(key + ".location"));
                    List<ItemStack> items = new ArrayList<>();
                    for (String itemString : config.getStringList(key + ".items")) {
                        items.add(ConfigUtil.itemFromString(itemString));
                    }
                    double radius = config.getDouble(key + ".radius");
                    List<Particle> particles = new ArrayList<>();
                    for (String particleString : config.getStringList(key + ".particles")) {
                        particles.add(Particle.fromString(particleString));
                    }
                    itemTasks.add(new ItemTask(key, ticks, runMode, location, items, radius, particles));
                }
                for (final ItemTask itemTask : itemTasks) {
                    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            itemTask.run();
                        }
                    }, itemTask.getTicks(), itemTask.getTicks());
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning(Message.TASK_ITEM_ERROR.toString());
            e.printStackTrace();
        }
    }

    /**
     * Returns the plugin instance
     * @return the plugin instance
     */
    public static V3LD1N getPlugin() {
        return plugin;
    }

    public static ConfigAccessor getConfig(String fileName) {
        for (ConfigAccessor config : configs) {
            if (config.getFileName().equals(fileName)) {
                return config;
            }
        }
        return null;
    }

    public static List<ConfigAccessor> getConfigs() {
        return configs;
    }

    public static WorldGuardPlugin getWorldGuard() {
        return worldGuard;
    }
}
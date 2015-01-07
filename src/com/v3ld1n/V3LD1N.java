package com.v3ld1n;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.v3ld1n.listeners.EntityListener;
import com.v3ld1n.listeners.PlayerListener;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.v3ld1n.commands.*;
import com.v3ld1n.items.*;
import com.v3ld1n.items.ratchet.*;
import com.v3ld1n.tasks.*;
import com.v3ld1n.util.ConfigAccessor;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.StringUtil;

public class V3LD1N extends JavaPlugin {
    private static V3LD1N plugin;
    private static List<ConfigAccessor> configs;
    private static WorldGuardPlugin worldGuard;
    private static List<V3LD1NItem> items;
    private static List<FAQ> questions;
    private static List<Report> reports;
    private static List<ItemTask> itemTasks;
    private static List<ParticleTask> particleTasks;
    private static List<SoundTask> soundTasks;
    private static List<TeleportTask> teleportTasks;
    private static final PluginManager pluginManager = Bukkit.getServer().getPluginManager();

    @Override
    public void onEnable() {
        plugin = this;
        String bukkitVersion = "1.8-R0.1-SNAPSHOT";
        if (!Bukkit.getBukkitVersion().equals(bukkitVersion)) {
            plugin.getLogger().warning(String.format(Message.INVALID_BUKKIT_VERSION.toString(), plugin.getDescription().getVersion(), bukkitVersion, Bukkit.getBukkitVersion()));
        }
        configs = new ArrayList<>();
        items = new ArrayList<>();
        questions = new ArrayList<>();
        reports = new ArrayList<>();
        itemTasks = new ArrayList<>();
        particleTasks = new ArrayList<>();
        soundTasks = new ArrayList<>();
        teleportTasks = new ArrayList<>();
        loadConfig();
        setupWorldGuard();
        loadItems();
        loadQuestions();
        loadReports();
        loadItemTasks();
        loadParticleTasks();
        loadSoundTasks();
        loadTeleportTasks();
        pluginManager.registerEvents(new PlayerListener(), plugin);
        pluginManager.registerEvents(new EntityListener(), plugin);
        getCommand("v3ld1nplugin").setExecutor(new V3LD1NPluginCommand());
        getCommand("faq").setExecutor(new FAQCommand());
        getCommand("trail").setExecutor(new TrailCommand());
        getCommand("sethealth").setExecutor(new SetHealthCommand());
        getCommand("setmaxhealth").setExecutor(new SetMaxHealthCommand());
        getCommand("ratchetsbow").setExecutor(new RatchetsBowCommand());
        getCommand("fireworkarrows").setExecutor(new FireworkArrowsCommand());
        getCommand("resourcepack").setExecutor(new ResourcePackCommand());
        getCommand("autoresourcepack").setExecutor(new AutoResourcePackCommand());
        getCommand("motd").setExecutor(new MotdCommand());
        getCommand("nextsound").setExecutor(new NextSoundCommand());
        getCommand("editsign").setExecutor(new EditSignCommand());
        getCommand("setfulltime").setExecutor(new SetFullTimeCommand());
        getCommand("playanimation").setExecutor(new PlayAnimationCommand());
        getCommand("sidebarmessage").setExecutor(new SidebarMessageCommand());
        getCommand("uuid").setExecutor(new UUIDCommand());
        getCommand("push").setExecutor(new PushCommand());
        getCommand("sethotbarslot").setExecutor(new SetHotbarSlotCommand());
        getCommand("v3ld1nmotd").setExecutor(new V3LD1NMotdCommand());
        getCommand("actionbarmessage").setExecutor(new ActionBarMessageCommand());
        getCommand("timeplayed").setExecutor(new TimePlayedCommand());
        getCommand("playerlist").setExecutor(new PlayerListCommand());
        getCommand("giveall").setExecutor(new GiveAllCommand());
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("totalplayers").setExecutor(new TotalPlayersCommand());
        getCommand("players").setExecutor(new PlayersCommand());
        getCommand("v3ld1nwarp").setExecutor(new WarpCommand());
        StringUtil.logDebugMessage(String.format(Message.LOADING_COMMANDS.toString(), this.getDescription().getCommands().size()));
        if (ConfigSetting.PLAYER_LIST_PING_ENABLED.getBoolean()) {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            final Scoreboard board = manager.getNewScoreboard();
            String name = ConfigSetting.SIDEBAR_PREFIX + "ping";
            if (name.length() > 16) {
                name = name.substring(0, 16);
            }
            final Objective objective = board.registerNewObjective(name, "dummy");
            objective.setDisplayName("Ping");
            objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    if (ConfigSetting.PLAYER_LIST_PING_ENABLED.getBoolean()) {
                        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                            objective.getScore(player.getName()).setScore(PlayerUtil.getPing(player));
                            player.setScoreboard(board);
                        }
                    }
                }
            }, ConfigSetting.PLAYER_LIST_PING_TICKS.getInt(), ConfigSetting.PLAYER_LIST_PING_TICKS.getInt());
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (ConfigSetting.REPORTS_AUTO_SAVE_ENABLED.getBoolean()) {
                    saveReports();
                }
            }
        }, ConfigSetting.REPORTS_AUTO_SAVE_TICKS.getInt(), ConfigSetting.REPORTS_AUTO_SAVE_TICKS.getInt());
    }

    @Override
    public void onDisable() {
        saveReports();
        configs = null;
        items = null;
        questions = null;
        reports = null;
        itemTasks = null;
        particleTasks = null;
        soundTasks = null;
        teleportTasks = null;
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
        configs.add(new ConfigAccessor(plugin, "reports.yml"));
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
        items.add(new RatchetPickaxe());
        items.add(new RatchetShears());
        items.add(new RatchetShovel());
        items.add(new RatchetSword());
        for (V3LD1NItem item : items) {
            pluginManager.registerEvents(item, plugin);
            StringUtil.logDebugMessage(String.format(Message.LOADING_ITEM.toString(), item.getId()));
        }
    }

    public static void loadQuestions() {
        try {
            String sectionName = "questions";
            if (Config.FAQ.getConfig().getConfigurationSection(sectionName) != null) {
                FileConfiguration config = Config.FAQ.getConfig();
                String section = sectionName + ".";
                for (String key : config.getConfigurationSection(sectionName).getKeys(false)) {
                    String name = config.getString(section + key + ".name");
                    String question = config.getString(section + key + ".question");
                    String answer = config.getString(section + key + ".answer");
                    String nameColor = config.getString(section + key + ".name-color");
                    String questionColor = config.getString(section + key + ".question-color");
                    String answerColor = config.getString(section + key + ".answer-color");
                    FAQ faq = new FAQ(Integer.parseInt(key), name, question, answer, nameColor, questionColor, answerColor);
                    questions.add(faq);
                }
                StringUtil.logDebugMessage(String.format(Message.LOADING_QUESTIONS.toString(), questions.size()));
            }
        } catch (Exception e) {
            plugin.getLogger().warning(Message.FAQ_LOAD_ERROR.toString());
            e.printStackTrace();
        }
    }

    public static void loadReports() {
        try {
            String sectionName = "reports";
            if (Config.REPORTS.getConfig().getConfigurationSection(sectionName) != null) {
                FileConfiguration config = Config.REPORTS.getConfig();
                String section = sectionName + ".";
                for (String key : config.getConfigurationSection("reports").getKeys(false)) {
                    String title = key;
                    String senderName = config.getString(section + key + ".sender-name");
                    UUID senderUuid = UUID.fromString(config.getString(section + key + ".sender-uuid"));
                    String reason = config.getString(section + key + ".reason");
                    boolean read = false;
                    if (config.get(section + key + ".read") != null) {
                        read = config.getBoolean(section + key + ".read");
                    }
                    Report report = new Report(title, senderName, senderUuid, reason, read);
                    reports.add(report);
                }
                StringUtil.logDebugMessage(String.format(Message.LOADING_REPORTS.toString(), reports.size()));
            }
        } catch (Exception e) {
            plugin.getLogger().warning(Message.REPORT_LOAD_ERROR.toString());
            e.printStackTrace();
        }
    }

    public static void saveReports() {
        try {
            String sectionName = "reports";
            String section = sectionName + ".";
            FileConfiguration config = Config.REPORTS.getConfig();
            for (Report report : reports) {
                config.set(section + report.getTitle() + ".sender-name", report.getSenderName());
                config.set(section + report.getTitle() + ".sender-uuid", report.getSenderUuid().toString());
                config.set(section + report.getTitle() + ".reason", report.getReason());
                config.set(section + report.getTitle() + ".read", report.isRead());
            }
            Config.REPORTS.saveConfig();
            StringUtil.logDebugMessage(String.format(Message.SAVING_REPORTS.toString(), reports.size()));
        } catch (Exception e) {
            plugin.getLogger().warning(Message.REPORT_SAVE_ERROR.toString());
            e.printStackTrace();
        }
    }

    public static void addReport(Report report) {
        reports.add(report);
    }

    public void loadItemTasks() {
        try {
            if (Config.TASKS_ITEM.getConfig() != null) {
                FileConfiguration config = Config.TASKS_ITEM.getConfig();
                for (String key : config.getKeys(false)) {
                    long ticks = config.getLong(key + ".ticks");
                    final ItemTask newTask = new ItemTask(key);
                    itemTasks.add(newTask);
                    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            newTask.run();
                        }
                    }, ticks, ticks);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning(Message.TASK_ITEM_ERROR.toString());
            e.printStackTrace();
        }
    }

    public void loadParticleTasks() {
        try {
            if (Config.TASKS_PARTICLE.getConfig() != null) {
                FileConfiguration config = Config.TASKS_PARTICLE.getConfig();
                for (String key : config.getKeys(false)) {
                    long ticks = config.getLong(key + ".ticks");
                    final ParticleTask newTask = new ParticleTask(key);
                    particleTasks.add(newTask);
                    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            newTask.run();
                        }
                    }, ticks, ticks);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning(Message.TASK_PARTICLE_ERROR.toString());
            e.printStackTrace();
        }
    }

    public void loadSoundTasks() {
        try {
            if (Config.TASKS_SOUND.getConfig() != null) {
                FileConfiguration config = Config.TASKS_SOUND.getConfig();
                for (String key : config.getKeys(false)) {
                    long ticks = config.getLong(key + ".ticks");
                    final SoundTask newTask = new SoundTask(key);
                    soundTasks.add(newTask);
                    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            newTask.run();
                        }
                    }, ticks, ticks);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning(Message.TASK_SOUND_ERROR.toString());
            e.printStackTrace();
        }
    }

    public void loadTeleportTasks() {
        try {
            if (Config.TASKS_TELEPORT.getConfig() != null) {
                FileConfiguration config = Config.TASKS_TELEPORT.getConfig();
                for (String key : config.getKeys(false)) {
                    long ticks = config.getLong(key + ".ticks");
                    final TeleportTask newTask = new TeleportTask(key);
                    teleportTasks.add(newTask);
                    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            newTask.run();
                        }
                    }, ticks, ticks);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().warning(Message.TASK_TELEPORT_ERROR.toString());
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

    public static List<FAQ> getQuestions() {
        return questions;
    }

    public static List<Report> getReports() {
        return reports;
    }

    public static List<ItemTask> getItemTasks() {
        return itemTasks;
    }

    public static List<ParticleTask> getParticleTasks() {
        return particleTasks;
    }

    public static List<SoundTask> getSoundTasks() {
        return soundTasks;
    }

    public static List<TeleportTask> getTeleportTasks() {
        return teleportTasks;
    }

    public static WorldGuardPlugin getWorldGuard() {
        return worldGuard;
    }
}
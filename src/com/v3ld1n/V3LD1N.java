package com.v3ld1n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
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
import com.v3ld1n.blocks.Sign;
import com.v3ld1n.commands.*;
import com.v3ld1n.items.*;
import com.v3ld1n.items.ratchet.*;
import com.v3ld1n.tasks.*;
import com.v3ld1n.util.ConfigAccessor;
import com.v3ld1n.util.Particle;
import com.v3ld1n.util.PlayerUtil;
import com.v3ld1n.util.Sound;
import com.v3ld1n.util.StringUtil;

public class V3LD1N extends JavaPlugin {
    private static V3LD1N plugin;
    private static List<ConfigAccessor> configs;
    private static WorldGuardPlugin worldGuard;
    public static HashMap<String, V3LD1NCommand> commands;
    private static List<V3LD1NItem> items;
    private static List<FAQ> questions;
    private static List<Report> reports;
    private static List<Warp> warps;
    private static List<Sign> signs;
    private static List<ItemTask> itemTasks;
    private static List<ParticleTask> particleTasks;
    private static List<SoundTask> soundTasks;
    private static List<TeleportTask> teleportTasks;
    private static final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
    private final Random random = new Random();

    private static RideCommand rideCommand = new RideCommand();
    public static HashMap<UUID, Boolean> usingRideCommand;

    @Override
    public void onEnable() {
        plugin = this;
        configs = new ArrayList<>();
        String bukkitVersion = "1.8.3-R0.1-SNAPSHOT";
        if (!Bukkit.getBukkitVersion().equals(bukkitVersion)) {
            plugin.getLogger().warning(String.format(Message.INVALID_BUKKIT_VERSION.toString(), plugin.getDescription().getName(), plugin.getDescription().getVersion(), bukkitVersion, Bukkit.getBukkitVersion()));
        }
        items = new ArrayList<>();
        questions = new ArrayList<>();
        reports = new ArrayList<>();
        warps = new ArrayList<>();
        signs = new ArrayList<>();
        itemTasks = new ArrayList<>();
        particleTasks = new ArrayList<>();
        soundTasks = new ArrayList<>();
        teleportTasks = new ArrayList<>();
        usingRideCommand = new HashMap<>();
        commands = new HashMap<>();
        loadConfig();
        setupWorldGuard();
        loadItems();
        loadQuestions();
        loadReports();
        loadWarps();
        loadSigns();
        loadItemTasks();
        loadParticleTasks();
        loadSoundTasks();
        loadTeleportTasks();
        pluginManager.registerEvents(new PlayerListener(), plugin);
        pluginManager.registerEvents(new EntityListener(), plugin);
        pluginManager.registerEvents(new WarpCommand(), plugin);
        pluginManager.registerEvents(rideCommand, plugin);
        loadCommands();
        StringUtil.logDebugMessage(String.format(Message.LOADING_COMMANDS.toString(), this.getDescription().getCommands().size()));
        //Ping on player list
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        String name = ConfigSetting.SCOREBOARD_PREFIX + "ping";
        if (name.length() > 16) {
            name = name.substring(0, 16);
        }
        final Objective objective = board.registerNewObjective(name, "dummy");
        objective.setDisplayName("Ping");
        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (ConfigSetting.PLAYER_LIST_PING_ENABLED.getValue() != null) {
                    if (ConfigSetting.PLAYER_LIST_PING_ENABLED.getBoolean()) {
                        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                            objective.getScore(player.getName()).setScore(PlayerUtil.getPing(player));
                            if (player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null) {
                                player.setScoreboard(board);
                            }
                        }
                    }
                }
            }
        }, ConfigSetting.PLAYER_LIST_PING_TICKS.getInt(), ConfigSetting.PLAYER_LIST_PING_TICKS.getInt());
        //Auto-save reports and warps
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (ConfigSetting.AUTO_SAVE_ENABLED.getBoolean()) {
                    saveReports();
                    saveWarps();
                    saveSigns();
                }
            }
        }, ConfigSetting.AUTO_SAVE_TICKS.getInt(), ConfigSetting.AUTO_SAVE_TICKS.getInt());
        //Player effects
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (!Bukkit.getServer().getOnlinePlayers().isEmpty()) {
                    if (random.nextInt(100) + 1 <= ConfigSetting.PLAYER_EFFECTS_CHANCE.getInt()) {
                        Player player = PlayerUtil.getRandomPlayer();
                        if (ConfigSetting.PLAYER_EFFECTS_PLAYERS.getStringList().contains(player.getName())) {
                            player.getWorld().strikeLightningEffect(player.getLocation());
                            Particle.fromString(ConfigSetting.PARTICLE_PLAYER_EFFECTS_LIGHTNING.getString()).display(player.getLocation());
                        }
                    }
                }
            }
        }, ConfigSetting.PLAYER_EFFECTS_TICKS.getInt(), ConfigSetting.PLAYER_EFFECTS_TICKS.getInt());
    }

    @Override
    public void onDisable() {
        saveReports();
        saveWarps();
        configs = null;
        items = null;
        questions = null;
        reports = null;
        warps = null;
        itemTasks = null;
        particleTasks = null;
        soundTasks = null;
        teleportTasks = null;
        usingRideCommand = null;
        plugin = null;
    }

    private void loadConfig() {
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

    private static void setupWorldGuard() {
        if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            worldGuard = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        } else {
            worldGuard = null;
        }
    }

    private static void loadCommands() {
        commands.put("v3ld1nplugin", new V3LD1NPluginCommand());
        commands.put("faq", new FAQCommand());
        commands.put("trail", new TrailCommand());
        commands.put("sethealth", new SetHealthCommand());
        commands.put("setmaxhealth", new SetMaxHealthCommand());
        commands.put("ratchetsbow", new RatchetsBowCommand());
        commands.put("fireworkarrows", new FireworkArrowsCommand());
        commands.put("resourcepack", new ResourcePackCommand());
        commands.put("autoresourcepack", new AutoResourcePackCommand());
        commands.put("motd", new MotdCommand());
        commands.put("nextsound", new NextSoundCommand());
        commands.put("editsign", new EditSignCommand());
        commands.put("setfulltime", new SetFullTimeCommand());
        commands.put("playanimation", new PlayAnimationCommand());
        commands.put("sidebarmessage", new SidebarMessageCommand());
        commands.put("uuid", new UUIDCommand());
        commands.put("push", new PushCommand());
        commands.put("sethotbarslot", new SetHotbarSlotCommand());
        commands.put("v3ld1nmotd", new V3LD1NMotdCommand());
        commands.put("actionbarmessage", new ActionBarMessageCommand());
        commands.put("timeplayed", new TimePlayedCommand());
        commands.put("playerlist", new PlayerListCommand());
        commands.put("giveall", new GiveAllCommand());
        commands.put("report", new ReportCommand());
        commands.put("players", new PlayersCommand());
        commands.put("v3ld1nwarp", new V3LD1NWarpCommand());
        commands.put("ride", rideCommand);
        for (String command : commands.keySet()) {
            plugin.getCommand(command).setExecutor(commands.get(command));
        }
    }

    private static void loadItems() {
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

    private static void loadQuestions() {
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

    private static void loadReports() {
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
                    List<String> readStrings = new ArrayList<>();
                    if (config.get(section + key + ".read-by") != null) {
                        readStrings = config.getStringList(section + key + ".read-by");
                    }
                    List<UUID> read = new ArrayList<>();
                    for (String uuidString : readStrings) {
                        read.add(UUID.fromString(uuidString));
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

    private static void saveReports() {
        try {
            String sectionName = "reports";
            String section = sectionName + ".";
            FileConfiguration config = Config.REPORTS.getConfig();
            for (Report report : reports) {
                String title = report.getTitle();
                config.set(section + title + ".sender-name", report.getSenderName());
                config.set(section + title + ".sender-uuid", report.getSenderUuid().toString());
                config.set(section + title + ".reason", report.getReason());
                List<UUID> read = report.getReadPlayers();
                List<String> readStrings = new ArrayList<>();
                for (UUID uuid : read) {
                    readStrings.add(uuid.toString());
                }
                config.set(section + report.getTitle() + ".read-by", readStrings);
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

    private static void loadWarps() {
        try {
            String sectionName = "warps";
            if (Config.WARPS.getConfig().getConfigurationSection(sectionName) != null) {
                FileConfiguration config = Config.WARPS.getConfig();
                String section = sectionName + ".";
                for (String key : config.getConfigurationSection("warps").getKeys(false)) {
                    String name = key;
                    List<Particle> particles = new ArrayList<>();
                    if (config.getStringList(section + key + ".particles") != null) {
                        for (String particleString : config.getStringList(section + key + ".particles")) {
                            particles.add(Particle.fromString(particleString));
                        }
                    }
                    List<Sound> sounds = new ArrayList<>();
                    if (config.getStringList(section + key + ".sounds") != null) {
                        for (String soundString : config.getStringList(section + key + ".sounds")) {
                            sounds.add(Sound.fromString(soundString));
                        }
                    }
                    Warp warp = new Warp(name, particles, sounds);
                    warps.add(warp);
                }
                StringUtil.logDebugMessage(String.format(Message.LOADING_WARPS.toString(), warps.size()));
            }
        } catch (Exception e) {
            plugin.getLogger().warning(Message.WARP_LOAD_ERROR.toString());
            e.printStackTrace();
        }
    }

    private static void saveWarps() {
        try {
            String sectionName = "warps";
            String section = sectionName + ".";
            FileConfiguration config = Config.WARPS.getConfig();
            for (Warp warp : warps) {
                String name = warp.getName();
                List<Particle> particles = warp.getParticles();
                List<String> particleStrings = new ArrayList<>();
                for (Particle particle : particles) {
                    particleStrings.add(particle.toString());
                }
                config.set(section + name + ".particles", particleStrings);
                config.set(section + name + ".sounds", warp.getSounds());
            }
            Config.WARPS.saveConfig();
            StringUtil.logDebugMessage(String.format(Message.SAVING_WARPS.toString(), warps.size()));
        } catch (Exception e) {
            plugin.getLogger().warning(Message.WARP_SAVE_ERROR.toString());
            e.printStackTrace();
        }
    }

    private static void loadSigns() {
        try {
            String sectionName = "signs";
            if (Config.SIGNS.getConfig().getConfigurationSection(sectionName) != null) {
                FileConfiguration config = Config.SIGNS.getConfig();
                String section = sectionName + ".";
                for (String key : config.getConfigurationSection("signs").getKeys(false)) {
                    String text = key;
                    List<String> playerCommands = new ArrayList<>();
                    if (config.getStringList(section + key + ".player-commands") != null) {
                        for (String command : config.getStringList(section + key + ".player-commands")) {
                            playerCommands.add(command);
                        }
                    }
                    List<String> consoleCommands = new ArrayList<>();
                    if (config.getStringList(section + key + ".console-commands") != null) {
                        for (String command : config.getStringList(section + key + ".console-commands")) {
                            consoleCommands.add(command);
                        }
                    }
                    List<Particle> particles = new ArrayList<>();
                    if (config.getStringList(section + key + ".particles") != null) {
                        for (String particleString : config.getStringList(section + key + ".particles")) {
                            particles.add(Particle.fromString(particleString));
                        }
                    }
                    List<Sound> sounds = new ArrayList<>();
                    if (config.getStringList(section + key + ".sounds") != null) {
                        for (String soundString : config.getStringList(section + key + ".sounds")) {
                            sounds.add(Sound.fromString(soundString));
                        }
                    }
                    Sign sign = new Sign(text, playerCommands, consoleCommands, particles, sounds);
                    signs.add(sign);
                }
                StringUtil.logDebugMessage(String.format(Message.LOADING_SIGNS.toString(), signs.size()));
            }
        } catch (Exception e) {
            plugin.getLogger().warning(Message.SIGN_LOAD_ERROR.toString());
            e.printStackTrace();
        }
    }

    private static void saveSigns() {
        try {
            String sectionName = "signs";
            String section = sectionName + ".";
            FileConfiguration config = Config.SIGNS.getConfig();
            for (Sign sign : signs) {
                String text = sign.getText();
                List<String> particleStrings = new ArrayList<>();
                for (Particle particle : sign.getParticles()) {
                    particleStrings.add(particle.toString());
                }
                List<String> soundStrings = new ArrayList<>();
                for (Sound sound : sign.getSounds()) {
                    soundStrings.add(sound.toString());
                }
                config.set(section + text + ".player-commands", sign.getPlayerCommands());
                config.set(section + text + ".console-commands", sign.getConsoleCommands());
                config.set(section + text + ".particles", particleStrings);
                config.set(section + text + ".sounds", soundStrings);
            }
            Config.SIGNS.saveConfig();
            StringUtil.logDebugMessage(String.format(Message.SAVING_SIGNS.toString(), signs.size()));
        } catch (Exception e) {
            plugin.getLogger().warning(Message.SIGN_SAVE_ERROR.toString());
            e.printStackTrace();
        }
    }

    public static void addWarp(Warp warp) {
        for (Warp oldWarp : warps) {
            if (oldWarp.getName().equalsIgnoreCase(warp.getName())) {
                return;
            }
        }
        warps.add(warp);
    }

    public static void removeWarp(String warp) {
        Iterator<Warp> iterator = warps.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getName().equalsIgnoreCase(warp)) {
                iterator.remove();
            }
        }
    }

    private void loadItemTasks() {
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

    private void loadParticleTasks() {
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

    private void loadSoundTasks() {
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

    private void loadTeleportTasks() {
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

    static ConfigAccessor getConfig(String fileName) {
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

    public static HashMap<String, V3LD1NCommand> getCommands() {
        return commands;
    }

    public static List<FAQ> getQuestions() {
        return questions;
    }

    public static List<Report> getReports() {
        return reports;
    }

    public static List<Warp> getWarps() {
        return warps;
    }

    public static List<Sign> getSigns() {
        return signs;
    }

    public static List<SoundTask> getSoundTasks() {
        return soundTasks;
    }

    public static WorldGuardPlugin getWorldGuard() {
        return worldGuard;
    }
}
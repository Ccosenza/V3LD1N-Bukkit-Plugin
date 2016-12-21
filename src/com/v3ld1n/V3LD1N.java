package com.v3ld1n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

import com.v3ld1n.listeners.EntityListener;
import com.v3ld1n.listeners.PlayerListener;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
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

public class V3LD1N extends JavaPlugin {
    private final Random random = new Random();
    private final String bukkitVersion = "1.11-R0.1-SNAPSHOT";
    private static final PluginManager pluginManager = Bukkit.getServer().getPluginManager();

    private static V3LD1N plugin;
    private static List<ConfigAccessor> configs;
    private static WorldGuardPlugin worldGuard;
    private static List<V3LD1NCommand> commands;
    private static List<V3LD1NItem> items;
    private static List<FAQ> questions;
    private static List<Report> reports;
    private static List<Warp> warps;
    private static List<Sign> signs;
    private static List<ChangelogDay> changelogDays;
    private static List<ResourcePack> resourcePacks;
    private static List<String> resourcePackNames;
    private static List<ItemTask> itemTasks;
    private static List<ParticleTask> particleTasks;
    private static List<SoundTask> soundTasks;
    private static List<TeleportTask> teleportTasks;
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;
    private static RideCommand rideCommand = new RideCommand();
    public static HashMap<UUID, RideType> usingRideCommand;
    public static Set<UUID> usingRatchetFirework;

    @Override
    public void onEnable() {
        plugin = this;
        configs = new ArrayList<>();
        loadConfig();
        if (!Bukkit.getBukkitVersion().equals(bukkitVersion)) {
            PluginDescriptionFile d = plugin.getDescription();
            Message.INVALID_BUKKIT_VERSION.logF(Level.WARNING, d.getName(), d.getVersion(), bukkitVersion, Bukkit.getBukkitVersion());
        }
        commands = new ArrayList<>();
        items = new ArrayList<>();
        questions = new ArrayList<>();
        reports = new ArrayList<>();
        warps = new ArrayList<>();
        signs = new ArrayList<>();
        changelogDays = new ArrayList<>();
        resourcePacks = new ArrayList<>();
        resourcePackNames = new ArrayList<>();
        itemTasks = new ArrayList<>();
        particleTasks = new ArrayList<>();
        soundTasks = new ArrayList<>();
        teleportTasks = new ArrayList<>();
        usingRideCommand = new HashMap<>();
        usingRatchetFirework = new HashSet<>();
        setupWorldGuard();
        setupVault();
        loadItems();
        loadQuestions();
        loadReports();
        loadWarps();
        loadSigns();
        loadChangelog();
        loadResourcePacks();
        loadItemTasks();
        loadParticleTasks();
        loadSoundTasks();
        loadTeleportTasks();
        pluginManager.registerEvents(new PlayerListener(), plugin);
        pluginManager.registerEvents(new EntityListener(), plugin);
        pluginManager.registerEvents(new WarpCommand(), plugin);
        pluginManager.registerEvents(rideCommand, plugin);
        loadCommands();
        Message.LOADING_COMMANDS.logDebugF(this.getDescription().getCommands().size());
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
        //Auto-save save-data files
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (ConfigSetting.AUTO_SAVE_ENABLED.getBoolean()) {
                    saveReports();
                    saveWarps();
                    saveChangelog();
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
                            String particle = ConfigSetting.PARTICLE_PLAYER_EFFECTS_LIGHTNING.getString();
                            Particle.fromString(particle).display(player.getLocation());
                        }
                    }
                }
            }
        }, ConfigSetting.PLAYER_EFFECTS_TICKS.getInt(), ConfigSetting.PLAYER_EFFECTS_TICKS.getInt());
        //Velds reward
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (ConfigSetting.VELDS_REWARD_ENABLED.getBoolean()) {
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        int amount = ConfigSetting.VELDS_REWARD_BASE_AMOUNT.getInt();
                        int multiplier = ConfigSetting.VELDS_REWARD_HOUR_MULTIPLIER.getInt();
                        amount += (PlayerUtil.getHoursPlayed(p) * multiplier);
                        econ.depositPlayer(p, amount);
                    }
                }
            }
        }, ConfigSetting.VELDS_REWARD_TICKS.getInt(), ConfigSetting.VELDS_REWARD_TICKS.getInt());
    }

    @Override
    public void onDisable() {
        saveReports();
        saveWarps();
        saveChangelog();
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
        for (Config config : Config.values()) {
            configs.add(new ConfigAccessor(plugin, config.getFileName()));
        }
    }

    private static void setupWorldGuard() {
        if (Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            worldGuard = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        } else {
            worldGuard = null;
        }
    }

    private static void setupVault() {
        setupEconomy();
        setupChat();
        setupPermissions();
    }

    private static boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private static boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = plugin.getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private static boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    private static void addCommand(String name, V3LD1NCommand command) {
        command.setBukkitCommand(plugin.getCommand(name));
        plugin.getCommand(name).setExecutor(command);
        commands.add(command);
    }

    private static void loadCommands() {
        addCommand("autoresourcepack", new AutoResourcePackCommand());
        addCommand("changelog", new ChangelogCommand());
        addCommand("damage", new DamageCommand());
        addCommand("editsign", new EditSignCommand());
        addCommand("faq", new FAQCommand());
        addCommand("fireworkarrows", new FireworkArrowsCommand());
        addCommand("giveall", new GiveAllCommand());
        addCommand("itemlore", new ItemLoreCommand());
        addCommand("itemname", new ItemNameCommand());
        addCommand("moneyitem", new MoneyItemCommand());
        addCommand("motd", new MotdCommand());
        addCommand("names", new NamesCommand());
        addCommand("playanimation", new PlayAnimationCommand());
        addCommand("playerlist", new PlayerListCommand());
        addCommand("players", new PlayersCommand());
        addCommand("playersay", new PlayerSayCommand());
        addCommand("push", new PushCommand());
        addCommand("ratchetsbow", new RatchetsBowCommand());
        addCommand("report", new ReportCommand());
        addCommand("resourcepack", new ResourcePackCommand());
        addCommand("ride", rideCommand);
        addCommand("sendmessage", new SendMessageCommand());
        addCommand("sethealth", new SetHealthCommand());
        addCommand("sethotbarslot", new SetHotbarSlotCommand());
        addCommand("setmaxhealth", new SetMaxHealthCommand());
        addCommand("sethunger", new SetHungerCommand());
        addCommand("sidebarmessage", new SidebarMessageCommand());
        addCommand("timeplayed", new TimePlayedCommand());
        addCommand("trail", new TrailCommand());
        addCommand("unbreakable", new UnbreakableCommand());
        addCommand("uuid", new UUIDCommand());
        addCommand("v3ld1nmotd", new V3LD1NMotdCommand());
        addCommand("v3ld1nplugin", new V3LD1NPluginCommand());
        addCommand("v3ld1nwarp", new V3LD1NWarpCommand());
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
        }
        Message.LOADING_ITEMS.logDebugF(items.size());
    }

    private static void loadQuestions() {
        try {
            String sectionName = "questions";
            if (Config.FAQ.getConfig().getConfigurationSection(sectionName) != null) {
                FileConfiguration config = Config.FAQ.getConfig();
                String section = sectionName + ".";
                for (String key : config.getConfigurationSection(sectionName).getKeys(false)) {
                    String question = config.getString(section + key + ".question");
                    List<String> answer = config.getStringList(section + key + ".answer");
                    FAQ faq = new FAQ(key, question, answer);
                    questions.add(faq);
                }
                Message.LOADING_QUESTIONS.logDebugF(questions.size());
            }
        } catch (Exception e) {
            Message.FAQ_LOAD_ERROR.log(Level.WARNING);
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
                    long time = config.getLong(section + key + ".time");
                    List<String> readStrings = new ArrayList<>();
                    if (config.get(section + key + ".read-by") != null) {
                        readStrings = config.getStringList(section + key + ".read-by");
                    }
                    List<UUID> read = new ArrayList<>();
                    for (String uuidString : readStrings) {
                        read.add(UUID.fromString(uuidString));
                    }
                    Report report = new Report(title, senderName, senderUuid, reason, time, read);
                    reports.add(report);
                }
                Message.LOADING_REPORTS.logDebugF(reports.size());
            }
        } catch (Exception e) {
            Message.REPORT_LOAD_ERROR.log(Level.WARNING);
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
                config.set(section + title + ".time", report.getTime());
                List<UUID> read = report.getReadPlayers();
                List<String> readStrings = new ArrayList<>();
                for (UUID uuid : read) {
                    readStrings.add(uuid.toString());
                }
                config.set(section + report.getTitle() + ".read-by", readStrings);
            }
            Config.REPORTS.saveConfig();
            Message.SAVING_REPORTS.logDebugF(reports.size());
        } catch (Exception e) {
            Message.REPORT_SAVE_ERROR.log(Level.WARNING);
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
                Message.LOADING_WARPS.logDebugF(warps.size());
            }
        } catch (Exception e) {
            Message.WARP_LOAD_ERROR.log(Level.WARNING);
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
            Message.SAVING_WARPS.logDebugF(warps.size());
        } catch (Exception e) {
            Message.WARP_SAVE_ERROR.log(Level.WARNING);
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
                Message.LOADING_SIGNS.logDebugF(signs.size());
            }
        } catch (Exception e) {
            Message.SIGN_LOAD_ERROR.log(Level.WARNING);
            e.printStackTrace();
        }
    }

    private static void loadChangelog() {
        try {
            String sectionName = "changes";
            if (Config.CHANGELOG.getConfig().getConfigurationSection(sectionName) != null) {
                FileConfiguration config = Config.CHANGELOG.getConfig();
                String section = sectionName + ".";
                for (String dayKey : config.getConfigurationSection("changes").getKeys(false)) {
                    for (String key : config.getConfigurationSection(section + dayKey).getKeys(false)) {
                        if (!key.equalsIgnoreCase("link")) {
                            long time = Long.parseLong(key);
                            String player = config.getString(section + dayKey + "." + key + ".player");
                            String changed = config.getString(section + dayKey + "." + key + ".change");
                            Change change = new Change(time, player, changed);
                            addChange(change, dayKey);
                            if (config.getString(section + dayKey + ".link") != null) {
                                String linkSetting = config.getString(section + dayKey + ".link");
                                change.getDay().setLink(linkSetting);
                            }
                        }
                    }
                }
                Message.LOADING_CHANGELOG.logDebugF(changelogDays.size());
            }
        } catch (Exception e) {
            Message.CHANGELOG_LOAD_ERROR.log(Level.WARNING);
            e.printStackTrace();
        }
    }

    private static void saveChangelog() {
        try {
            String sectionName = "changes";
            String section = sectionName + ".";
            FileConfiguration config = Config.CHANGELOG.getConfig();
            for (ChangelogDay cld : changelogDays) {
                String day = cld.getDate() + ".";
                if (!cld.getLink().equals("")) {
                    config.set(section + day + ".link", cld.getLink());
                } else {
                    config.set(section + day + ".link", null);
                }
                for (Change change : cld.getChanges()) {
                    long time = change.getTime();
                    config.set(section + day + time + ".player", change.getPlayer());
                    config.set(section + day + time + ".change", change.getChange());
                }
            }
            Config.CHANGELOG.saveConfig();
            Message.SAVING_CHANGELOG.logDebugF(changelogDays.size());
        } catch (Exception e) {
            Message.CHANGELOG_SAVE_ERROR.log(Level.WARNING);
            e.printStackTrace();
        }
    }

    public static void addChange(Change change, String day) {
        ChangelogDay newDay = new ChangelogDay(day, new ArrayList<Change>());
        ChangelogDay add;
        if (ChangelogDay.findDay(day) != null) {
            add = ChangelogDay.findDay(day);
        } else {
            add = newDay;
            changelogDays.add(add);
        }
        add.addChange(change);
        change.setDay(add);
    }

    private static void loadResourcePacks() {
        try {
            String sectionName = "resource-packs";
            if (Config.CONFIG.getConfig().getConfigurationSection(sectionName) != null) {
                FileConfiguration config = Config.CONFIG.getConfig();
                String section = sectionName + ".";
                for (String key : config.getConfigurationSection("resource-packs").getKeys(false)) {
                    String url = config.getString(section + key);
                    ResourcePack resourcePack = new ResourcePack(key, url);
                    resourcePacks.add(resourcePack);
                    resourcePackNames.add(key);
                }
                Message.LOADING_RESOURCE_PACKS.logDebugF(resourcePacks.size());
            }
        } catch (Exception e) {
            Message.RESOURCE_PACK_LOAD_ERROR.log(Level.WARNING);
            e.printStackTrace();
        }
    }

    public static ResourcePack getResourcePack(String name) {
        for (ResourcePack rp : resourcePacks) {
            if (rp.getName().equalsIgnoreCase(name)) {
                return rp;
            }
        }
        return null;
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
                Message.LOADING_ITEM_TASKS.logDebugF(itemTasks.size());
            }
        } catch (Exception e) {
            Message.TASK_ITEM_ERROR.log(Level.WARNING);
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
                Message.LOADING_PARTICLE_TASKS.logDebugF(particleTasks.size());
            }
        } catch (Exception e) {
            Message.TASK_PARTICLE_ERROR.log(Level.WARNING);
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
                Message.LOADING_SOUND_TASKS.logDebugF(soundTasks.size());
            }
        } catch (Exception e) {
            Message.TASK_SOUND_ERROR.log(Level.WARNING);
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
                Message.LOADING_TELEPORT_TASKS.logDebugF(teleportTasks.size());
            }
        } catch (Exception e) {
            Message.TASK_TELEPORT_ERROR.log(Level.WARNING);
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
        ConfigAccessor ca = null;
        for (ConfigAccessor config : configs) {
            if (config.getFileName().equals(fileName)) {
                ca = config;
            }
        }
        return ca;
    }

    public static List<ConfigAccessor> getConfigs() {
        return configs;
    }

    public static List<V3LD1NCommand> getCommands() {
        return commands;
    }

    public static V3LD1NCommand getCommandFromName(String name) {
        V3LD1NCommand vc = null;
        for (V3LD1NCommand command : commands) {
            String cmdName = command.getBukkitCommand().getName();
            if (cmdName.equalsIgnoreCase(name)) {
                vc = command;
            }
        }
        return vc;
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

    public static List<ChangelogDay> getChangelogDays() {
        return changelogDays;
    }

    public static List<ResourcePack> getResourcePacks() {
        return resourcePacks;
    }

    public static List<String> getResourcePackNames() {
        return resourcePackNames;
    }

    public static List<SoundTask> getSoundTasks() {
        return soundTasks;
    }

    public static WorldGuardPlugin getWorldGuard() {
        return worldGuard;
    }

    public static Economy getEconomy() {
        return econ;
    }
}
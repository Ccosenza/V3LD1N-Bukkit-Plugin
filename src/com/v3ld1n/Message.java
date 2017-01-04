package com.v3ld1n;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.v3ld1n.util.ChatUtil;
import com.v3ld1n.util.MessageType;
import com.v3ld1n.util.StringUtil;

public enum Message {
    INVALID_BUKKIT_VERSION("invalid-bukkit-version"),
    LINK_HOVER("link-hover"),
    COMMAND_HOVER("command-hover"),
    NONE("none"),
    NEW_PLAYER_JOIN("new-player-join"),

    MOTD_WELCOME("motd.welcome"),
    MOTD_WELCOME_BACK("motd.welcome-back"),

    VELDS_ADDED("velds.added"),
    VELDS_LORE("velds.lore"),
    VELDS_INVALID_AMOUNT("velds.invalid-amount"),

    SHORT_LIST_ITEM("lists.short-item"),
    LONG_LIST_ITEM("lists.long-item"),

    CANNOT_USE_ITEM("cancel-interact.interact-item"),
    CANNOT_USE_CONTAINER("cancel-interact.interact-container"),

    SIGN_PERMISSION("permissions.signs"),
    WORLDGUARD_PERMISSION("permissions.worldguard"),

    ITEM_RELOAD_ERROR("errors.loading.items"),
    FAQ_LOAD_ERROR("errors.loading.faq"),
    REPORT_LOAD_ERROR("errors.loading.reports"),
    WARP_LOAD_ERROR("errors.loading.warps"),
    SIGN_LOAD_ERROR("errors.loading.signs"),
    CHANGELOG_LOAD_ERROR("errors.loading.changelog"),
    RESOURCE_PACK_LOAD_ERROR("errors.loading.resource-packs"),

    REPORT_SAVE_ERROR("errors.saving.reports"),
    WARP_SAVE_ERROR("errors.saving.warps"),
    CHANGELOG_SAVE_ERROR("errors.saving.changelog"),

    TASK_ITEM_ERROR("errors.loading.tasks.item"),
    TASK_PARTICLE_ERROR("errors.loading.tasks.particle"),
    TASK_SOUND_ERROR("errors.loading.tasks.sound"),
    TASK_TELEPORT_ERROR("errors.loading.tasks.teleport"),

    CHAT_ERROR("errors.chat.failed"),

    LOADING_ITEMS("debug.loading.items"),
    LOADING_ITEM_TASKS("debug.loading.tasks.item"),
    LOADING_PARTICLE_TASKS("debug.loading.tasks.particle"),
    LOADING_SOUND_TASKS("debug.loading.tasks.sound"),
    LOADING_TELEPORT_TASKS("debug.loading.tasks.teleport"),
    LOADING_QUESTIONS("debug.loading.questions"),
    LOADING_REPORTS("debug.loading.reports"),
    LOADING_WARPS("debug.loading.warps"),
    LOADING_SIGNS("debug.loading.signs"),
    LOADING_CHANGELOG("debug.loading.changelog"),
    LOADING_RESOURCE_PACKS("debug.loading.resource-packs"),
    LOADING_COMMANDS("debug.loading.commands"),

    SAVING_REPORTS("debug.saving.reports"),
    SAVING_WARPS("debug.saving.warps"),
    SAVING_CHANGELOG("debug.saving.changelog"),

    RATCHETS_SHOVEL_DAMAGE("items.ratchets-shovel.damage"),

    TASK_SOUND_TIME("tasks.sound.time"),

    COMMAND_NOT_PLAYER("commands.player"),
    COMMAND_NO_PERMISSION("commands.permission"),
    COMMAND_INVALID_PLAYER("commands.invalid-player"),
    COMMAND_NO_ITEM("commands.no-item"),
    COMMAND_USAGE_TITLE("commands.usage.title"),
    COMMAND_USAGE_ALIASES("commands.usage.aliases"),
    COMMAND_USAGE_DESCRIPTION("commands.usage.description"),

    AUTORESOURCEPACK_SET("commands.autoresourcepack.set"),
    AUTORESOURCEPACK_REMOVE_COMMAND("commands.autoresourcepack.remove-command"),
    AUTORESOURCEPACK_REMOVE("commands.autoresourcepack.remove"),
    AUTORESOURCEPACK_INFO("commands.autoresourcepack.info"),
    AUTORESOURCEPACK_ERROR("commands.autoresourcepack.error"),

    CHANGELOG_BORDER_TOP("commands.changelog.border-top"),
    CHANGELOG_BORDER_BOTTOM("commands.changelog.border-bottom"),
    CHANGELOG_HOVER_TOP("commands.changelog.hover-top"),
    CHANGELOG_LIST_ITEM("commands.changelog.list-item"),
    CHANGELOG_HELP("commands.changelog.help"),
    CHANGELOG_LOG("commands.changelog.log"),
    CHANGELOG_LINK_SET("commands.changelog.link.set"),
    CHANGELOG_LINK_REMOVE("commands.changelog.link.remove"),
    CHANGELOG_LINK_ERROR("commands.changelog.link.error"),
    CHANGELOG_NO_PERMISSION("commands.changelog.no-permission"),
    CHANGELOG_ERROR("commands.changelog.error"),

    DAMAGE_DAMAGE("commands.damage.damage"),
    DAMAGE_DAMAGE_SELF("commands.damage.damage-self"),
    DAMAGE_INVULNERABLE("commands.damage.invulnerable"),

    EDITSIGN_SET("commands.editsign.set"),
    EDITSIGN_ADD("commands.editsign.add"),
    EDITSIGN_REMOVE("commands.editsign.remove"),
    EDITSIGN_INVALID_BLOCK("commands.editsign.invalid-block"),
    EDITSIGN_INVALID_LINE("commands.editsign.invalid-line"),
    EDITSIGN_INVALID_ARGUMENTS("commands.editsign.invalid-arguments"),

    FAQ_BORDER_TOP("commands.faq.border-top"),
    FAQ_BORDER_BOTTOM("commands.faq.border-bottom"),
    FAQ_HELP("commands.faq.help"),
    FAQ_QUESTION("commands.faq.question"),
    FAQ_ANSWER("commands.faq.answer"),
    FAQ_ANSWER_COLOR("commands.faq.answer-color"),
    FAQ_ERROR("commands.faq.error"),

    FIREWORKARROWS_SET("commands.fireworkarrows.set"),
    FIREWORKARROWS_INVALID_SHAPE("commands.fireworkarrows.invalid-shape"),
    FIREWORKARROWS_LIST_TITLE("commands.fireworkarrows.list-title"),

    GIVEALL_GIVE("commands.giveall.give"),

    ITEMLORE_SET("commands.itemlore.set"),
    ITEMLORE_ADD("commands.itemlore.add"),
    ITEMLORE_REMOVE("commands.itemlore.remove"),
    ITEMLORE_REMOVE_LINE("commands.itemlore.remove-line"),
    ITEMLORE_NO_LORE("commands.itemlore.no-lore"),
    ITEMLORE_INVALID_LINE("commands.itemlore.invalid-line"),

    ITEMNAME_SET("commands.itemname.set"),
    ITEMNAME_REMOVE("commands.itemname.remove"),
    ITEMNAME_NOT_NAMED("commands.itemname.not-named"),

    MOTD_SHOWING("commands.motd.show"),
    MOTD_NO_PERMISSION_OTHERS("commands.motd.others-permission"),

    NAMES_BORDER("commands.names.border"),
    NAMES_ORIGINAL("commands.names.original"),
    NAMES_CHANGED("commands.names.changed"),
    NAMES_NO_PREVIOUS_NAMES("commands.names.no-previous-names"),
    NAMES_CURRENT("commands.names.current"),

    NEXTSOUND_NOW_PLAYING("commands.nextsound.now-playing"),
    NEXTSOUND_NO_SOUND_TASKS("commands.nextsound.no-sound-tasks"),

    PLAYANIMATION_PLAY("commands.playanimation.play"),
    PLAYANIMATION_PLAY_OTHER("commands.playanimation.play-other"),
    PLAYANIMATION_LIST_TITLE("commands.playanimation.list-title"),
    PLAYANIMATION_NO_PERMISSION_OTHERS("commands.playanimation.others-permission"),

    PLAYERS_LIST_TITLE("commands.players.list-title"),
    PLAYERS_AMOUNT_TITLE("commands.players.amount.title"),
    PLAYERS_AMOUNT_SUBTITLE("commands.players.amount.subtitle"),
    PLAYERS_AMOUNT_NOT_PLAYER("commands.players.amount.not-player"),

    PLAYERLIST_SET("commands.playerlist.set"),
    PLAYERLIST_RESET("commands.playerlist.reset"),
    PLAYERLIST_PING_DISPLAY("commands.playerlist.ping.display"),
    PLAYERLIST_PING_TIME("commands.playerlist.ping.time"),
    PLAYERLIST_ENABLE_PING("commands.playerlist.ping.enable"),
    PLAYERLIST_DISABLE_PING("commands.playerlist.ping.disable"),
    PLAYERLIST_SET_PING_TIME("commands.playerlist.ping.set-time"),

    PUSH_PUSH("commands.push.push"),
    PUSH_PUSH_TO_PLAYER("commands.push.push-to-player"),
    PUSH_INVALID_SPEED("commands.push.invalid-speed"),
    PUSH_ERROR("commands.push.error"),

    RATCHETSBOW_SET("commands.ratchetsbow.set"),
    RATCHETSBOW_INVALID_PROJECTILE("commands.ratchetsbow.invalid-projectile"),
    RATCHETSBOW_LIST_TITLE("commands.ratchetsbow.list-title"),

    REPORT_BORDER_TOP("commands.report.border-top"),
    REPORT_BORDER_BOTTOM("commands.report.border-bottom"),
    REPORT_SEND("commands.report.send"),
    REPORT_UNREAD("commands.report.unread"),
    REPORT_NO_TITLE("commands.report.no-title"),
    REPORT_NO_REASON("commands.report.no-reason"),
    REPORT_INVALID("commands.report.invalid"),
    REPORT_DELETE("commands.report.delete"),
    REPORT_DELETE_NO_PERMISSION("commands.report.delete-no-permission"),
    REPORT_LIST_TOP("commands.report.list.top"),
    REPORT_LIST_HELP("commands.report.list.help"),
    REPORT_LIST_EMPTY("commands.report.list.empty"),
    REPORT_READ_TITLE("commands.report.read.title"),
    REPORT_READ_SENDER("commands.report.read.sender"),
    REPORT_READ_REASON("commands.report.read.reason"),
    REPORT_READ_TIME("commands.report.read.time"),
    REPORT_READ_BACK("commands.report.read.back"),
    REPORT_READBY_LIST_TITLE("commands.report.readby.list-title"),
    REPORT_READBY_NO_PERMISSION("commands.report.readby.no-permission"),

    RESOURCEPACK_LIST_TITLE("commands.resourcepack.list-title"),
    RESOURCEPACK_OUTDATED("commands.resourcepack.outdated"),
    RESOURCEPACK_ERROR("commands.resourcepack.error"),

    RIDE_USE("commands.ride.use"),
    RIDE_RIDE("commands.ride.ride"),
    RIDE_HOLD("commands.ride.hold"),
    RIDE_NO_TIME("commands.ride.no-time"),
    RIDE_INVALID_TYPE("commands.ride.invalid-type"),

    SENDMESSAGE_LIST_TITLE("commands.sendmessage.list-title"),
    SENDMESSAGE_INVALID_TYPE("commands.sendmessage.invalid-type"),

    SETFULLTIME_SET("commands.setfulltime.set"),

    SETHEALTH_SET("commands.sethealth.set"),
    SETHEALTH_SET_OWN("commands.sethealth.set-own"),
    SETHEALTH_LIMIT("commands.sethealth.limit"),

    SETHOTBARSLOT_SET("commands.sethotbarslot.set"),
    SETHOTBARSLOT_SET_OWN("commands.sethotbarslot.set-own"),
    SETHOTBARSLOT_INVALID_SLOT("commands.sethotbarslot.invalid-slot"),

    SETHUNGER_SET("commands.sethunger.set"),
    SETHUNGER_SET_OWN("commands.sethunger.set-own"),
    SETHUNGER_LIMIT("commands.sethunger.limit"),

    SETMAXHEALTH_SET("commands.setmaxhealth.set"),
    SETMAXHEALTH_SET_OWN("commands.setmaxhealth.set-own"),
    SETMAXHEALTH_LIMIT("commands.setmaxhealth.limit"),

    SIDEBARMESSAGE_DISPLAY("commands.sidebarmessage.display"),
    SIDEBARMESSAGE_INVALID_TIME("commands.sidebarmessage.invalid-time"),

    TIMEPLAYED_TIME("commands.timeplayed.time"),
    TIMEPLAYED_SIDEBAR_NAME("commands.timeplayed.sidebar-name"),

    TRAIL_SET_OWN("commands.trail.set-own"),
    TRAIL_SET_OTHER("commands.trail.set-other"),
    TRAIL_REMOVE_OWN("commands.trail.remove-own"),
    TRAIL_REMOVE_OTHER("commands.trail.remove-other"),
    TRAIL_BLOCKED("commands.trail.blocked"),
    TRAIL_NO_PERMISSION_OTHERS("commands.trail.others-permission"),
    TRAIL_INVALID("commands.trail.invalid"),

    UNBREAKABLE_SET("commands.unbreakable.set"),

    UUID_HOVER("commands.uuid.hover"),

    V3LD1NMOTD_ADD("commands.v3ld1nmotd.add"),
    V3LD1NMOTD_REMOVE("commands.v3ld1nmotd.remove"),
    V3LD1NMOTD_LIST_TITLE("commands.v3ld1nmotd.list-title"),
    V3LD1NMOTD_INVALID("commands.v3ld1nmotd.invalid"),

    V3LD1NPLUGIN_AVAILABLE("commands.v3ld1nplugin.available"),
    V3LD1NPLUGIN_DASH("commands.v3ld1nplugin.dash"),
    V3LD1NPLUGIN_COMMAND_NOT_PLAYER("commands.v3ld1nplugin.command-not-player"),
    V3LD1NPLUGIN_HELP_BORDER_TOP("commands.v3ld1nplugin.help.border-top"),
    V3LD1NPLUGIN_HELP_BORDER_BOTTOM("commands.v3ld1nplugin.help.border-bottom"),
    V3LD1NPLUGIN_RELOAD("commands.v3ld1nplugin.reload"),
    V3LD1NPLUGIN_SETRESOURCEPACK("commands.v3ld1nplugin.setresourcepack"),
    V3LD1NPLUGIN_VERSION("commands.v3ld1nplugin.version"),
    V3LD1NPLUGIN_ENABLE_DEBUG("commands.v3ld1nplugin.debug.enable"),
    V3LD1NPLUGIN_DISABLE_DEBUG("commands.v3ld1nplugin.debug.disable"),

    V3LD1NWARP_ADD("commands.v3ld1nwarp.add"),
    V3LD1NWARP_REMOVE("commands.v3ld1nwarp.remove"),
    V3LD1NWARP_LIST_TITLE("commands.v3ld1nwarp.list-title"),
    V3LD1NWARP_INVALID("commands.v3ld1nwarp.invalid");

    private final String name;

    private Message(String message) {
        this.name = message;
    }

    @Override
    public String toString() {
        String string = name;
        if (V3LD1N.getConfig("messages.yml").getConfig().getString(name) != null) {
            string = StringUtil.formatText(V3LD1N.getConfig("messages.yml").getConfig().getString(name));
        }
        return string;
    }

    public String getName() {
        return name;
    }

    /**
     * Sends the message to a user
     * @param user the user
     */
    public void send(CommandSender user) {
        user.sendMessage(this.toString());
    }

    /**
     * Formats the message, then sends it to a user
     * @param user the user
     * @param format the strings to format the message with
     */
    public void sendF(CommandSender user, Object... format) {
        user.sendMessage(String.format(this.toString(), format));
    }

    /**
     * Sends the message to a user as a type 2 message (above action bar)
     * @param user the user
     */
    public void aSend(CommandSender user) {
        ChatUtil.sendMessage(user, this.toString(), MessageType.ACTION_BAR);
    }

    /**
     * Formats the message, then sends it to a user as a type 2 message (above action bar)
     * @param user the user
     * @param format the strings to format the message with
     */
    public void aSendF(CommandSender user, Object... format) {
        ChatUtil.sendMessage(user, String.format(this.toString(), format), MessageType.ACTION_BAR);
    }

    /**
     * Logs the message
     * @param level the log level
     */
    public void log(Level level) {
        Bukkit.getLogger().log(level, this.toString());
    }

    /**
     * Formats the message, then logs it
     * @param level the log level
     * @param format the strings to format the message with
     */
    public void logF(Level level, Object... format) {
        Bukkit.getLogger().log(level, String.format(this.toString(), format));
    }

    /**
     * Logs the message if debug mode is enabled
     */
    public void logDebug() {
        if (ConfigSetting.DEBUG.getBoolean()) {
            Bukkit.getLogger().info(this.toString());
        }
    }

    /**
     * Formats the message, then logs it if debug is enabled
     * @param format the strings to format the message with
     */
    public void logDebugF(Object... format) {
        if (ConfigSetting.DEBUG.getBoolean()) {
            Bukkit.getLogger().info(String.format(this.toString(), format));
        }
    }
}
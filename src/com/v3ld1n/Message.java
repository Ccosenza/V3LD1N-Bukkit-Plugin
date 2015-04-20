package com.v3ld1n;

import com.v3ld1n.util.StringUtil;

public enum Message {
    INVALID_BUKKIT_VERSION("invalid-bukkit-version"),
    LINK_HOVER("link-hover"),
    COMMAND_HOVER("command-hover"),
    NONE("none"),

    VELDS_ADDED("velds.added"),
    VELDS_LORE("velds.lore"),

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

    REPORT_SAVE_ERROR("errors.saving.reports"),
    WARP_SAVE_ERROR("errors.saving.warps"),
    CHANGELOG_SAVE_ERROR("errors.saving.changelog"),

    TASK_ITEM_ERROR("errors.loading.tasks.item"),
    TASK_PARTICLE_ERROR("errors.loading.tasks.particle"),
    TASK_SOUND_ERROR("errors.loading.tasks.sound"),
    TASK_TELEPORT_ERROR("errors.loading.tasks.teleport"),

    CHAT_ERROR("errors.chat.failed"),

    LOADING_ITEM("debug.loading.item"),
    LOADING_TASK("debug.loading.task"),
    LOADING_QUESTIONS("debug.loading.questions"),
    LOADING_REPORTS("debug.loading.reports"),
    LOADING_WARPS("debug.loading.warps"),
    LOADING_SIGNS("debug.loading.signs"),
    LOADING_CHANGELOG("debug.loading.changelog"),
    LOADING_COMMANDS("debug.loading.commands"),

    SAVING_REPORTS("debug.saving.reports"),
    SAVING_WARPS("debug.saving.warps"),
    SAVING_CHANGELOG("debug.saving.changelog"),

    RATCHETS_SHOVEL_DAMAGE("items.ratchets-shovel.damage"),

    COMMAND_NOT_PLAYER("commands.player"),
    COMMAND_NO_PERMISSION("commands.permission"),
    COMMAND_INVALID_PLAYER("commands.invalid-player"),
    COMMAND_USAGE_TITLE("commands.usage-title"),
    COMMAND_USAGE_DESCRIPTION("commands.usage-description"),

    AUTORESOURCEPACK_ENABLE("commands.autoresourcepack.enable"),
    AUTORESOURCEPACK_DISABLE("commands.autoresourcepack.disable"),

    CHANGELOG_BORDER_TOP("commands.changelog.border-top"),
    CHANGELOG_BORDER_BOTTOM("commands.changelog.border-bottom"),
    CHANGELOG_HOVER_TOP("commands.changelog.hover-top"),
    CHANGELOG_LIST_ITEM("commands.changelog.list-item"),
    CHANGELOG_LOG("commands.changelog.log"),
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

    FAQ_BORDER("commands.faq.border"),
    FAQ_TOP("commands.faq.top"),
    FAQ_HELP("commands.faq.help"),
    FAQ_QUESTION("commands.faq.question"),
    FAQ_ANSWER("commands.faq.answer"),
    FAQ_BACK("commands.faq.back"),
    FAQ_INVALID_QUESTION("commands.faq.invalid-question"),

    FIREWORKARROWS_SET("commands.fireworkarrows.set"),
    FIREWORKARROWS_INVALID_SHAPE("commands.fireworkarrows.invalid-shape"),
    FIREWORKARROWS_LIST_TITLE("commands.fireworkarrows.list-title"),

    GIVEALL_GIVE("commands.giveall.give"),
    GIVEALL_NO_ITEM("commands.giveall.no-item"),

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
    PLAYANIMATION_LIST_TITLE("commands.playanimation.list-title"),
    PLAYANIMATION_NO_PERMISSION_OTHERS("commands.playanimation.others-permission"),
    PLAYANIMATION_ERROR("commands.playanimation.error"),

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

    REPORT_SEND("commands.report.send"),
    REPORT_UNREAD("commands.report.unread"),
    REPORT_NO_TITLE("commands.report.no-title"),
    REPORT_NO_REASON("commands.report.no-reason"),
    REPORT_INVALID("commands.report.invalid"),
    REPORT_DELETE("commands.report.delete"),
    REPORT_DELETE_NO_PERMISSION("commands.report.delete-no-permission"),
    REPORT_LIST_TOP("commands.report.list.top"),
    REPORT_LIST_BORDER("commands.report.list.border"),
    REPORT_LIST_HELP("commands.report.list.help"),
    REPORT_LIST_EMPTY("commands.report.list.empty"),
    REPORT_READ_TITLE("commands.report.read.title"),
    REPORT_READ_SENDER("commands.report.read.sender"),
    REPORT_READ_REASON("commands.report.read.reason"),
    REPORT_READ_TIME("commands.report.read.time"),
    REPORT_READ_BACK("commands.report.read.back"),
    REPORT_READBY_LIST_TITLE("commands.report.readby.list-title"),
    REPORT_READBY_NO_PERMISSION("commands.report.readby.no-permission"),

    RESOURCEPACK_ERROR("commands.resourcepack.error"),

    RIDE_USE("commands.ride.use"),
    RIDE_RIDE("commands.ride.ride"),
    RIDE_NO_TIME("commands.ride.no-time"),

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

    TRAIL_SET_OWN("commands.trail.set-own"),
    TRAIL_SET_OTHER("commands.trail.set-other"),
    TRAIL_REMOVE_OWN("commands.trail.remove-own"),
    TRAIL_REMOVE_OTHER("commands.trail.remove-other"),
    TRAIL_NO_PERMISSION_OTHERS("commands.trail.others-permission"),
    TRAIL_INVALID("commands.trail.invalid"),

    UUID_HOVER("commands.uuid.hover"),

    V3LD1NMOTD_ADD("commands.v3ld1nmotd.add"),
    V3LD1NMOTD_REMOVE("commands.v3ld1nmotd.remove"),
    V3LD1NMOTD_LIST_TITLE("commands.v3ld1nmotd.list-title"),
    V3LD1NMOTD_INVALID("commands.v3ld1nmotd.invalid"),

    V3LD1NPLUGIN_AVAILABLE("commands.v3ld1nplugin.available"),
    V3LD1NPLUGIN_DASH("commands.v3ld1nplugin.dash"),
    V3LD1NPLUGIN_COMMAND_NOT_PLAYER("commands.v3ld1nplugin.command-not-player"),
    V3LD1NPLUGIN_HELP("commands.v3ld1nplugin.help"),
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
        if (V3LD1N.getConfig("messages.yml").getConfig().getString(name) != null) {
            return StringUtil.formatText(V3LD1N.getConfig("messages.yml").getConfig().getString(name));
        }
        return name;
    }

    public String getName() {
        return name;
    }
}
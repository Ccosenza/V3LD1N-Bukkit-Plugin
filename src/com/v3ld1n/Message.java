package com.v3ld1n;

import com.v3ld1n.util.StringUtil;

public enum Message {
    INVALID_BUKKIT_VERSION("invalid-bukkit-version"),
    LINK_HOVER("link-hover"),
    COMMAND_HOVER("command-hover"),
    NONE("none"),

    LIST_ITEM("lists.item"),

    SIGN_INVALID_PARTICLE("signs.invalid-particle"),
    CANNOT_USE_ITEM("cancel-interact.interact-item"),
    CANNOT_USE_CONTAINER("cancel-interact.interact-container"),

    WORLDGUARD_PERMISSION("worldguard.permission"),

    MOTD_SHOWING("motd.showing"),
    MOTD_NO_PERMISSION_OTHERS("commands.motd.others-permission"),
    MOTD_ERROR("motd.error"),

    ITEM_RELOAD_ERROR("errors.loading.items"),
    FAQ_LOAD_ERROR("errors.loading.faq"),
    REPORT_LOAD_ERROR("errors.loading.reports"),
    WARP_LOAD_ERROR("errors.loading.warps"),

    REPORT_SAVE_ERROR("errors.saving.reports"),
    WARP_SAVE_ERROR("errors.saving.warps"),

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
    LOADING_COMMANDS("debug.loading.commands"),

    SAVING_REPORTS("debug.saving.reports"),
    SAVING_WARPS("debug.saving.warps"),

    RATCHETS_SHOVEL_DAMAGE("items.ratchets-shovel.damage"),

    COMMAND_NOT_PLAYER("commands.player"),
    COMMAND_NOT_PLAYER_ENTITY("commands.player-entity"),
    COMMAND_NO_PERMISSION("commands.permission"),
    COMMAND_INVALID_PLAYER("commands.invalid-player"),
    COMMAND_USAGE_TITLE("commands.usage-title"),
    COMMAND_USAGE_DESCRIPTION("commands.usage-description"),

    AUTORESOURCEPACK_ENABLE("commands.autoresourcepack.enable"),
    AUTORESOURCEPACK_DISABLE("commands.autoresourcepack.disable"),

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

    GIVEALL_GIVE("commands.giveall.give"),
    GIVEALL_NO_ITEM("commands.giveall.no-item"),

    NEXTSOUND_NOW_PLAYING("commands.nextsound.now-playing"),
    NEXTSOUND_NO_SOUND_TASKS("commands.nextsound.no-sound-tasks"),

    PLAYANIMATION_PLAY("commands.playanimation.play"),
    PLAYANIMATION_LIST_TITLE("commands.playanimation.list-title"),
    PLAYANIMATION_NO_PERMISSION_OTHERS("commands.playanimation.others-permission"),
    PLAYANIMATION_ERROR("commands.playanimation.error"),

    PLAYERLIST_SET("commands.playerlist.set"),
    PLAYERLIST_RESET("commands.playerlist.reset"),

    PUSH_PUSH("commands.push.push"),
    PUSH_PUSH_TO_PLAYER("commands.push.push-to-player"),
    PUSH_INVALID_SPEED("commands.push.invalid-speed"),
    PUSH_ERROR("commands.push.error"),

    RATCHETSBOW_SET("commands.ratchetsbow.set"),
    RATCHETSBOW_INVALID_PROJECTILE("commands.ratchetsbow.invalid-projectile"),

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
    REPORT_READ_BACK("commands.report.read.back"),
    REPORT_READBY_LIST("commands.report.readby.list"),
    REPORT_READBY_LIST_ITEM("commands.report.readby.list-item"),
    REPORT_READBY_NO_PERMISSION("commands.report.readby.no-permission"),

    RESOURCEPACK_ERROR("commands.resourcepack.error"),

    SETFULLTIME_SET("commands.setfulltime.set"),

    SETHEALTH_SET("commands.sethealth.set"),
    SETHEALTH_LIMIT("commands.sethealth.limit"),

    SETHOTBARSLOT_SET("commands.sethotbarslot.set"),
    SETHOTBARSLOT_SET_OWN("commands.sethotbarslot.set-own"),
    SETHOTBARSLOT_INVALID_SLOT("commands.sethotbarslot.invalid-slot"),

    SETMAXHEALTH_SET("commands.setmaxhealth.set"),
    SETMAXHEALTH_LIMIT("commands.setmaxhealth.limit"),

    SIDEBARMESSAGE_DISPLAY("commands.sidebarmessage.display"),
    SIDEBARMESSAGE_INVALID_TIME("commands.sidebarmessage.invalid-time"),

    TOTALPLAYERS_PLAYERS("commands.totalplayers.players"),

    TRAIL_SET_OWN("commands.trail.set-own"),
    TRAIL_SET_OTHER("commands.trail.set-other"),
    TRAIL_REMOVE_OWN("commands.trail.remove-own"),
    TRAIL_REMOVE_OTHER("commands.trail.remove-other"),
    TRAIL_NO_PERMISSION_OTHERS("commands.trail.others-permission"),
    TRAIL_INVALID("commands.trail.invalid"),

    UUID_HOVER("commands.uuid.hover"),

    V3LD1NMOTD_ADD("commands.v3ld1nmotd.add"),
    V3LD1NMOTD_REMOVE("commands.v3ld1nmotd.remove"),
    V3LD1NMOTD_LIST("commands.v3ld1nmotd.list"),
    V3LD1NMOTD_LIST_ITEM("commands.v3ld1nmotd.list-item"),
    V3LD1NMOTD_INVALID("commands.v3ld1nmotd.invalid"),

    V3LD1NPLUGIN_AVAILABLE("commands.v3ld1nplugin.available"),
    V3LD1NPLUGIN_DASH("commands.v3ld1nplugin.dash"),
    V3LD1NPLUGIN_COMMAND_NOT_PLAYER("commands.v3ld1nplugin.command-not-player"),
    V3LD1NPLUGIN_DISABLE_DEBUG("commands.v3ld1nplugin.disable-debug"),
    V3LD1NPLUGIN_ENABLE_DEBUG("commands.v3ld1nplugin.enable-debug"),
    V3LD1NPLUGIN_DISABLE_WARP("commands.v3ld1nplugin.disable-warp"),
    V3LD1NPLUGIN_ENABLE_WARP("commands.v3ld1nplugin.enable-warp"),
    V3LD1NPLUGIN_TOGGLE_ALL_WARPS("commands.v3ld1nplugin.toggle-all-warps"),
    V3LD1NPLUGIN_INVALID_WARP("commands.v3ld1nplugin.invalid-warp"),
    V3LD1NPLUGIN_RELOAD("commands.v3ld1nplugin.reload"),
    V3LD1NPLUGIN_SETRESOURCEPACK("commands.v3ld1nplugin.setresourcepack"),
    V3LD1NPLUGIN_VERSION("commands.v3ld1nplugin.version"),
    V3LD1NPLUGIN_WARP_LIST_TITLE("commands.v3ld1nplugin.warp-list-title"),

    WARP_DISABLED("commands.warps.disabled");

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
package com.v3ld1n;

import com.v3ld1n.util.StringUtil;

public enum Message {
    INVALID_BUKKIT_VERSION("invalid-bukkit-version"),
    LINK_HOVER("link-hover"),
    COMMAND_HOVER("command-hover"),

    SIGN_INVALID_PARTICLE("signs.invalid-particle"),
    CANNOT_USE_ITEM("cancel-interact.interact-item"),
    CANNOT_USE_CONTAINER("cancel-interact.interact-container"),

    WORLDGUARD_PERMISSION("worldguard.permission"),

    MOTD_SHOWING("motd.showing"),
    MOTD_NO_PERMISSION_OTHERS("commands.motd.others-permission"),
    MOTD_ERROR("motd.error"),

    ITEM_RELOAD_ERROR("errors.loading.items"),
    FAQ_LOAD_ERROR("errors.loading.faq"),

    CHAT_STRINGS_NO_PLAYER("chat-strings.no-player"),

    TASK_ITEM_ERROR("errors.loading.tasks.item"),
    TASK_PARTICLE_ERROR("errors.loading.tasks.particle"),
    TASK_SOUND_ERROR("errors.loading.tasks.sound"),
    TASK_TELEPORT_ERROR("errors.loading.tasks.teleport"),

    CHAT_ERROR("errors.chat.failed"),

    LOADING_ITEM("debug.loading.item"),
    LOADING_TASK("debug.loading.task"),
    LOADING_QUESTIONS("debug.loading.questions"),

    RATCHETS_SHOVEL_DAMAGE("items.ratchets-shovel.damage"),

    COMMAND_NOT_PLAYER("commands.player"),
    COMMAND_NOT_PLAYER_ENTITY("commands.player-entity"),
    COMMAND_NO_PERMISSION("commands.permission"),
    COMMAND_INVALID_ARGUMENT("commands.invalid-argument"),
    COMMAND_INVALID_ARGUMENTS("commands.invalid-arguments"),
    COMMAND_INVALID_PLAYER("commands.invalid-player"),

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

    NEXTSOUND_NOW_PLAYING("commands.nextsound.now-playing"),
    NEXTSOUND_NO_SOUND_PLAYERS("commands.nextsound.no-sound-players"),

    PARTICLE_RELATIVE("commands.particle.relative"),
    PARTICLE_TARGET("commands.particle.target"),
    PARTICLE_NO_WORLD("commands.particle.no-world"),
    PARTICLE_INVALID_WORLD("commands.particle.invalid-world"),
    PARTICLE_INVALID("commands.particle.invalid-particle"),
    PARTICLE_SPAWN("commands.particle.spawn"),

    PLAYANIMATION_PLAY("commands.playanimation.play"),
    PLAYANIMATION_LIST("commands.playanimation.list"),
    PLAYANIMATION_LIST_ITEM("commands.playanimation.list-item"),
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
    V3LD1NPLUGIN_SETBLOGPOST("commands.v3ld1nplugin.setblogpost"),
    V3LD1NPLUGIN_SETRESOURCEPACK("commands.v3ld1nplugin.setresourcepack"),
    V3LD1NPLUGIN_VERSION("commands.v3ld1nplugin.version"),
    V3LD1NPLUGIN_WARPS("commands.v3ld1nplugin.warps"),
    V3LD1NPLUGIN_WARPS_ITEM("commands.v3ld1nplugin.warps-item"),

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
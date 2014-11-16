package com.v3ld1n.util;

import net.minecraft.server.v1_7_R4.PacketPlayOutNamedSoundEffect;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class SoundUtil {
    private SoundUtil() {
    }

    /**
     * Plays a sound to a player at a location
     * @param sound the sound name
     * @param location the location
     * @param player the player who can hear the sound
     * @param volume the volume of the sound
     * @param pitch the pitch of the sound
     */
    public static void playSound(String sound, Location location, Player player, float volume, float pitch) {
        PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(sound, location.getX(), location.getY(), location.getZ(), volume, pitch);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    /**
     * Plays a sound at a location
     * @param sound the sound name
     * @param location the location
     * @param volume the volume of the sound
     * @param pitch the pitch of the sound
     */
    public static void playSound(String sound, Location location, float volume, float pitch) {
        PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(sound, location.getX(), location.getY(), location.getZ(), volume, pitch);
        for (Player p : location.getWorld().getPlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    /**
     * Plays a sound from a config string
     * @param sound the string
     * @param location the location to play the sound
     */
    public static void playSoundString(String sound, Location location) {
        String[] split = sound.split("\\|");
        float volume = 1;
        float pitch = 1;
        if (split.length >= 2) {
            volume = Float.parseFloat(split[1]);
            if (split.length >= 3) {
                pitch = Float.parseFloat(split[2]);
            }
        }
        SoundUtil.playSound(split[0], location, volume, pitch);
    }
}
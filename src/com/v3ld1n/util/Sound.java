package com.v3ld1n.util;

import net.minecraft.server.v1_8_R1.PacketPlayOutNamedSoundEffect;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Sound {
    private String name;
    private float volume;
    private float pitch;
    
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private float volume;
        private float pitch;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setVolume(float volume) {
            this.volume = volume;
            return this;
        }

        public Builder setPitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        public Sound build() {
            return new Sound(this);
        }
    }

    private Sound(Builder builder) {
        this.name = builder.name;
        this.volume = builder.volume;
        this.pitch = builder.pitch;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getVolume() {
        return this.volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void play(Location location) {
        PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(name, location.getX(), location.getY(), location.getZ(), volume, pitch);
        for (Player p : location.getWorld().getPlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void play(Location location, Player player) {
        PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(name, location.getX(), location.getY(), location.getZ(), volume, pitch);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static Sound fromString(String sound) {
        String[] split = sound.split("\\|");
        Builder builder = builder()
                .setName(split[0]);
        builder.setVolume(1);
        builder.setPitch(1);
        if (split.length >= 2) {
            builder.setVolume(Float.parseFloat(split[1]));
            if (split.length >= 3) {
                builder.setPitch(Float.parseFloat(split[2]));
            }
        }
        return builder.build();
    }

    @Override
    public String toString() {
        String string = name + "|" + volume + "|" + pitch;
        return string;
    }
}
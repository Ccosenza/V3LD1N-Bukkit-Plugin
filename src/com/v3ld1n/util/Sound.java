package com.v3ld1n.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
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
        for (Player p : location.getWorld().getPlayers()) {
            playToPlayer(location, p);
        }
    }

    public void playToPlayer(Location location, Player player) {
        player.playSound(location, this.name, volume, pitch);
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

    public static List<Sound> fromList(List<String> soundList) {
        List<Sound> sounds = new ArrayList<>();
        for (String sound : soundList) {
            sounds.add(fromString(sound));
        }
        return sounds;
    }

    public static void playList(List<Sound> sounds, Location location) {
        for (Player p : location.getWorld().getPlayers()) {
            playListToPlayer(sounds, location, p);
        }
    }

    public static void playListToPlayer(List<Sound> sounds, Location location, Player player) {
        for (Sound sound : sounds) {
            sound.playToPlayer(location, player);
        }
    }

    @Override
    public String toString() {
        String string = name + "|" + volume + "|" + pitch;
        return string;
    }
}
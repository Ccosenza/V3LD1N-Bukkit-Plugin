package com.v3ld1n.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_9_R1.EnumParticle;
import net.minecraft.server.v1_9_R1.PacketPlayOutWorldParticles;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Particle {
    private String name;
    private boolean force;
    private float offsetX = 0;
    private float offsetY = 0;
    private float offsetZ = 0;
    private float speed = 0;
    private int count = 1;
    private int[] data = new int[2];
    
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private boolean force;
        private float offsetX = 0;
        private float offsetY = 0;
        private float offsetZ = 0;
        private float speed = 0;
        private int count = 1;
        private int[] data = {0, 0};

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setForced(boolean force) {
            this.force = force;
            return this;
        }

        public Builder setOffsetX(float offset) {
            this.offsetX = offset;
            return this;
        }

        public Builder setOffsetY(float offset) {
            this.offsetY = offset;
            return this;
        }

        public Builder setOffsetZ(float offset) {
            this.offsetZ = offset;
            return this;
        }

        public Builder setSpeed(float speed) {
            this.speed = speed;
            return this;
        }

        public Builder setCount(int count) {
            this.count = count;
            return this;
        }

        public Builder setData(int itemId, int itemData) {
            this.data[0] = itemId;
            this.data[1] = itemData;
            return this;
        }

        public Particle build() {
            return new Particle(this);
        }
    }

    private Particle(Builder builder) {
        this.name = builder.name;
        this.force = builder.force;
        this.offsetX = builder.offsetX;
        this.offsetY = builder.offsetY;
        this.offsetZ = builder.offsetZ;
        this.speed = builder.speed;
        this.count = builder.count;
        this.data = builder.data;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isForced() {
        return this.force;
    }

    public void setForced(boolean force) {
        this.force = force;
    }

    public float getOffsetX() {
        return this.offsetX;
    }

    public void setOffsetX(float offset) {
        this.offsetX = offset;
    }

    public float getOffsetY() {
        return this.offsetY;
    }

    public void setOffsetY(float offset) {
        this.offsetY = offset;
    }

    public float getOffsetZ() {
        return this.offsetZ;
    }

    public void setOffsetZ(float offset) {
        this.offsetZ = offset;
    }

    public float getSpeed() {
        return this.speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setData(int itemId, int itemData) {
        this.data[0] = itemId;
        this.data[1] = itemData;
    }

    public int[] getData() {
        return this.data;
    }

    public void display(Location location) {
        for (Player p : location.getWorld().getPlayers()) {
            displayToPlayer(location, p);
        }
    }

    public void displayToPlayer(Location location, Player player) {
        PacketPlayOutWorldParticles packet = createPacket(location);
        PlayerUtil.sendPacket(packet, player);
    }

    private PacketPlayOutWorldParticles createPacket(Location location) {
        EnumParticle particle = EnumParticle.BARRIER;
        String newName = this.name;
        String[] names = {"blockcrack_", "blockdust_", "iconcrack_"};
        for (String n : names) {
            if (this.name.startsWith(n)) {
                newName = n;
                String[] split = this.name.split("_");
                this.data[0] = StringUtil.toInteger(split[1]);
                if (split.length == 3) this.data[1] = StringUtil.toInteger(split[2]);
                break;
            }
        }
        for (EnumParticle enumparticle : EnumParticle.values()) {
            if (newName.equals(enumparticle.b())) {
                particle = enumparticle;
            }
        }
        float x = (float) location.getX();
        float y = (float) location.getY();
        float z = (float) location.getZ();
        float ox = this.offsetX;
        float oy = this.offsetY;
        float oz = this.offsetZ;
        PacketPlayOutWorldParticles packet;
        packet = new PacketPlayOutWorldParticles(particle, force, x, y, z, ox, oy, oz, speed, count, data);
        return packet;
    }

    public static Particle fromString(String particle) {
        String[] split = particle.split("\\|");
        Builder builder = builder()
                .setName(split[0])
                .setOffsetX(Float.parseFloat(split[1]))
                .setOffsetY(Float.parseFloat(split[2]))
                .setOffsetZ(Float.parseFloat(split[3]))
                .setSpeed(Float.parseFloat(split[4]))
                .setCount(Integer.parseInt(split[5]));
        if (split.length >= 7) {
            builder.setForced(Boolean.parseBoolean(split[6]));
        }
        return builder.build();
    }

    public static Particle fromString(String particleName, String particle) {
        String[] split = particle.split("\\|");
        Builder builder = builder()
                .setName(particleName)
                .setOffsetX(Float.parseFloat(split[0]))
                .setOffsetY(Float.parseFloat(split[1]))
                .setOffsetZ(Float.parseFloat(split[2]))
                .setSpeed(Float.parseFloat(split[3]))
                .setCount(Integer.parseInt(split[4]));
        if (split.length >= 6) {
            builder.setForced(Boolean.parseBoolean(split[5]));
        }
        return builder.build();
    }

    public static List<Particle> fromList(List<String> particleList) {
        List<Particle> particles = new ArrayList<>();
        for (String particle : particleList) {
            particles.add(fromString(particle));
        }
        return particles;
    }

    public static void displayList(List<Particle> particles, Location location) {
        for (Player p : location.getWorld().getPlayers()) {
            displayListToPlayer(particles, location, p);
        }
    }

    public static void displayListToPlayer(List<Particle> particles, Location location, Player player) {
        for (Particle particle : particles) {
            particle.displayToPlayer(location, player);
        }
    }

    @Override
    public String toString() {
        String string = name + "|" + offsetX + "|" + offsetY + "|" + offsetZ + "|" + speed + "|" + count + "|" + force;
        return string;
    }
}
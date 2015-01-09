package com.v3ld1n.util;

import net.minecraft.server.v1_8_R1.EnumParticle;
import net.minecraft.server.v1_8_R1.PacketPlayOutWorldParticles;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Particle {
    private String name;
    private boolean force;
    private float offsetX = 0;
    private float offsetY = 0;
    private float offsetZ = 0;
    private float speed = 0;
    private int count = 1;
    
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
    }

    /**
     * Returns the name of the particle
     * @return the particle's name
     */
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

    public void display(Location location) {
        EnumParticle particle = EnumParticle.BARRIER;
        for (EnumParticle enumparticle : EnumParticle.values()) {
            if (this.name.equals(enumparticle.b())) {
                particle = enumparticle;
            }
        }
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, force, (float) location.getX(), (float) location.getY(), (float) location.getZ(), this.offsetX, this.offsetY, this.offsetZ, this.speed, this.count);
        for (Player p : location.getWorld().getPlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void display(Location location, Player player) {
        EnumParticle particle = EnumParticle.BARRIER;
        for (EnumParticle enumparticle : EnumParticle.values()) {
            if (this.name.equals(enumparticle.b())) {
                particle = enumparticle;
            }
        }
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, force, (float) location.getX(), (float) location.getY(), (float) location.getZ(), this.offsetX, this.offsetY, this.offsetZ, this.speed, this.count);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
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
        if (split.length > 6) {
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
        if (split.length > 5) {
            builder.setForced(Boolean.parseBoolean(split[5]));
        }
        return builder.build();
    }

    @Override
    public String toString() {
        String string = name + "|" + offsetX + "|" + offsetY + "|" + offsetZ + "|" + speed + "|" + count + "|" + force;
        return string;
    }
}
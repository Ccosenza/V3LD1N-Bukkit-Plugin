package com.v3ld1n.commands;

import java.util.List;

import com.v3ld1n.util.Particle;

public class Warp {
    String name;
    List<Particle> particles;
    List<String> sounds;

    public Warp(String name, List<Particle> particles, List<String> sounds) {
        this.name = name;
        this.particles = particles;
        this.sounds = sounds;
    }

    public String getName() {
        return name;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public List<String> getSounds() {
        return sounds;
    }
}
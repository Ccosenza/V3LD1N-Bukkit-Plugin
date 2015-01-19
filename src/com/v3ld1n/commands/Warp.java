package com.v3ld1n.commands;

import java.util.List;

import com.v3ld1n.util.Particle;
import com.v3ld1n.util.Sound;

public class Warp {
    String name;
    List<Particle> particles;
    List<Sound> sounds;

    public Warp(String name, List<Particle> particles, List<Sound> sounds) {
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

    public List<Sound> getSounds() {
        return sounds;
    }
}
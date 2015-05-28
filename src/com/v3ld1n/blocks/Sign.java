package com.v3ld1n.blocks;

import java.util.List;

import com.v3ld1n.util.Particle;
import com.v3ld1n.util.Sound;

public class Sign {
    private String text;
    private List<String> playerCommands;
    private List<String> consoleCommands;
    private List<Particle> particles;
    private List<Sound> sounds;

    public Sign(String text, List<String> playerCmds, List<String> consoleCmds, List<Particle> particles, List<Sound> sounds) {
        this.text = text;
        this.playerCommands = playerCmds;
        this.consoleCommands = consoleCmds;
        this.particles = particles;
        this.sounds = sounds;
    }

    public String getText() {
        return text;
    }

    public List<String> getPlayerCommands() {
        return playerCommands;
    }

    public List<String> getConsoleCommands() {
        return consoleCommands;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public List<Sound> getSounds() {
        return sounds;
    }
}
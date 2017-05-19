package com.v3ld1n.blocks;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.v3ld1n.util.Particle;
import com.v3ld1n.util.Sound;
import com.v3ld1n.util.StringUtil;

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

    private void runCommand(String command, Player signUser, CommandSender sender, org.bukkit.block.Sign signState) {
        String newCommand = StringUtil.replaceSignVariables(command, signState, signUser);
        Bukkit.dispatchCommand(sender, newCommand);
    }

    public void use(Player player, org.bukkit.block.Sign signState) {
        Location center = signState.getLocation().add(0.5, 0.5, 0.5);
        for (String command : playerCommands) {
            runCommand(command, player, player, signState);
        }
        for (String command : consoleCommands) {
            runCommand(command, player, Bukkit.getConsoleSender(), signState);
        }
        Particle.displayList(particles, center);
        Sound.playList(sounds, center);
    }
}
package com.v3ld1n.tasks;

import java.util.Random;

import org.bukkit.Location;

public abstract class Task {
    protected final String name;
    private final long ticks;
    protected final String runMode;
    protected Location location;
    protected Random random;

    public Task(String name, long ticks, String runMode, Location location) {
        this.name = name;
        this.ticks = ticks;
        this.runMode = runMode;
        this.location = location;
    }

    abstract void run();

    public String getName() {
        return name;
    }

    public long getTicks() {
        return ticks;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
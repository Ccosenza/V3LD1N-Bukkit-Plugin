package com.v3ld1n.commands;

public class ResourcePack {
    private String name;
    private String url;

    public ResourcePack(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
package com.v3ld1n.util;

public enum ParticleType {
    EXPLODE("explode", 0),
    LARGE_EXPLODE("largeexplode", 1),
    HUGE_EXPLOSION("hugeexplosion", 2),
    FIREWORKS_SPARK("fireworksSpark", 3),
    BUBBLE("bubble", 4),
    SPLASH("splash", 5),
    WAKE("wake", 6),
    SUSPENDED("suspended", 7),
    DEPTH_SUSPEND("depthsuspend", 8),
    CRIT("crit", 9),
    MAGIC_CRIT("magicCrit", 10),
    SMOKE("smoke", 11),
    LARGE_SMOKE("largesmoke", 12),
    SPELL("spell", 13),
    INSTANT_SPELL("instantSpell", 14),
    MOB_SPELL("mobSpell", 15),
    MOB_SPELL_AMBIENT("mobSpellAmbient", 16),
    WITCH_MAGIC("witchMagic", 17),
    DRIP_WATER("dripWater", 18),
    DRIP_LAVA("dripLava", 19),
    ANGRY_VILLAGER("angryVillager", 20),
    HAPPY_VILLAGER("happyVillager", 21),
    TOWN_AURA("townaura", 22),
    NOTE("note", 23),
    PORTAL("portal", 24),
    ENCHANTMENT_TABLE("enchantmenttable", 25),
    FLAME("flame", 26),
    LAVA("lava", 27),
    FOOTSTEP("footstep", 28),
    CLOUD("cloud", 29),
    RED_DUST("reddust", 30),
    SNOWBALL_POOF("snowballpoof", 31),
    SNOW_SHOVEL("snowshovel", 32),
    SLIME("slime", 33),
    HEART("heart", 34),
    BARRIER("barrier", 35),
    ICON_CRACK("iconcrack_", 36),
    BLOCK_CRACK("blockcrack_", 37),
    BLOCK_DUST("blockdust_", 38),
    DROPLET("droplet", 39),
    TAKE("take", 40),
    MOB_APPEARANCE("mobappearance", 41);

    private final String name;
    private final int id;

    private ParticleType(String name, int id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Returns the particle's name
     * @return the particle name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the particle's ID
     * @return the particle ID
     */
    public int getId() {
        return id;
    }
}
package com.v3ld1n.util;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;

public class TimeUtil {
    private TimeUtil() {
    }

    public static long getTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static String format(long time, String timeFormat) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat(timeFormat);
        return format.format(date);
    }

    public static String fromSeconds(int seconds) {
        int second = seconds;
        int minute = seconds / 60;
        second -= minute * 60;
        int hour = minute / 60;
        minute -= hour * 60;
        return ChatColor.RED.toString() + hour + ChatColor.GOLD + "h " + ChatColor.RED + minute + ChatColor.GOLD + "m " + ChatColor.RED + second + ChatColor.GOLD + "s";
    }
}
package com.kevinpelgrims.pillreminder.utils;

public class Formatter {
    public static String formatTime(int hour, int minute) {
        return String.format("%02d:%02d", hour, minute);
    }
}

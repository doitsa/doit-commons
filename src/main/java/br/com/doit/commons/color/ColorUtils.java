package br.com.doit.commons.color;

import java.util.Random;

public class ColorUtils {
    public static boolean isDark(String hexColor) {
        Integer color = Integer.parseInt(hexColor.substring(1), 16);

        return (color >>> 16) // R
                + (color >>> 8 & 0x00ff) // G
                + (color & 0x0000ff) // B
                < 500;
    }

    public static String colorFor(Long seed) {
        return String.format("#%06X", 0xFFFFFF & new Random(seed).nextLong());
    }
}
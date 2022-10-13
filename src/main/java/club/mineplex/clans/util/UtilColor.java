package club.mineplex.clans.util;

import java.awt.*;

public class UtilColor {
    private UtilColor() {
    }

    public static int getChromaColor() {
        return Color.HSBtoRGB(
                ((System.currentTimeMillis() - (3 * 10) - (3 * 10)) % 2_000L) / 2_000.0F,
                0.8F,
                0.8F
        );
    }

}

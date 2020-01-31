package org.nting.toolkit.ui;

public class Colors {

    public static final int TRANSPARENT = 0x00FFFFFF;
    public static final int WHITE = 0xFFFFFFFF;
    public static final int BLACK = 0xFF000000;
    public static final int GREY = 0xFF808080;
    public static final int DARK_GREY = 0xFF262626;

    /**
     * Creates a transparent black color with the provided alpha; which value should be in the range [0, 255].
     */
    public static int TRN_BLACK(int alpha) {
        return (alpha << 24);
    }
}

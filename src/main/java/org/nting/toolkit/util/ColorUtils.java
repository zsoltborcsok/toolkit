package org.nting.toolkit.util;

import org.nting.data.Property;

import playn.core.Color;
import pythagoras.f.MathUtil;

public class ColorUtils {

    public static boolean isTransparent(int color) {
        return 0 <= color && color < 0x01000000;
    }

    public static boolean isNotTransparent(int color) {
        return color < 0 || 0x01000000 <= color;
    }

    public static int HSVtoRGB(float hue, float saturation, float brightness) {
        saturation /= 100.0f;
        brightness /= 100.0f;

        float c = brightness * saturation;
        float h1 = hue / 60;
        float x = c * (1 - Math.abs((h1 % 2) - 1));
        float m = brightness - c;
        float[] rgb = new float[] { 0, 0, 0 };

        if (h1 < 1)
            rgb = new float[] { c, x, 0 };
        else if (h1 < 2)
            rgb = new float[] { x, c, 0 };
        else if (h1 < 3)
            rgb = new float[] { 0, c, x };
        else if (h1 < 4)
            rgb = new float[] { 0, x, c };
        else if (h1 < 5)
            rgb = new float[] { x, 0, c };
        else if (h1 <= 6)
            rgb = new float[] { c, 0, x };

        return Color.rgb((int) (255 * (rgb[0] + m)), (int) (255 * (rgb[1] + m)), (int) (255 * (rgb[2] + m)));
    }

    public static int darken(int color, float percent) {
        int alpha = color >> 24;
        alpha = alpha >= 0 ? alpha : alpha + 256;
        int red = (color >> 16) % 256;
        red = red >= 0 ? red : red + 256;
        int green = (color >> 8) % 256;
        green = green >= 0 ? green : green + 256;
        int blue = color % 256;
        blue = blue >= 0 ? blue : blue + 256;

        float x = 1 - (percent / 100.0f);
        red = (int) (red * x);
        green = (int) (green * x);
        blue = (int) (blue * x);

        return Color.argb(alpha, red, green, blue);
    }

    public static void darken(Property<Integer> color, float percent) {
        color.setValue(darken(color.getValue(), percent));
    }

    public static int darkenAbsolute(int color, float absPercent) {
        int alpha = color >> 24;
        alpha = alpha >= 0 ? alpha : alpha + 256;
        int red = (color >> 16) % 256;
        red = red >= 0 ? red : red + 256;
        int green = (color >> 8) % 256;
        green = green >= 0 ? green : green + 256;
        int blue = color % 256;
        blue = blue >= 0 ? blue : blue + 256;

        float brightness = Math.max(Math.max(red, green), blue) / 255.0f;
        if (brightness * 100 <= absPercent) {
            return Color.argb(alpha, 0, 0, 0);
        } else {
            float d = (brightness - (absPercent / 100.0f)) / brightness;

            red = (int) (red * d);
            green = (int) (green * d);
            blue = (int) (blue * d);

            return Color.argb(alpha, red, green, blue);
        }
    }

    public static void darkenAbsolute(Property<Integer> color, float absPercent) {
        color.setValue(darkenAbsolute(color.getValue(), absPercent));
    }

    public static int lighten(int color, float percent) {
        int alpha = color >> 24;
        alpha = alpha >= 0 ? alpha : alpha + 256;
        int red = (color >> 16) % 256;
        red = red >= 0 ? red : red + 256;
        int green = (color >> 8) % 256;
        green = green >= 0 ? green : green + 256;
        int blue = color % 256;
        blue = blue >= 0 ? blue : blue + 256;

        float brightness = Math.max(Math.max(red, green), blue) / 255.0f;
        if (brightness == 0) {
            return color;
        } else {
            float d = Math.min(1, brightness + percent * brightness / 100.0f) / brightness;

            red = (int) Math.min(red * d, 255);
            green = (int) Math.min(green * d, 255);
            blue = (int) Math.min(blue * d, 255);

            return Color.argb(alpha, red, green, blue);
        }
    }

    public static void lighten(Property<Integer> color, float percent) {
        color.setValue(lighten(color.getValue(), percent));
    }

    public static int lightenAbsolute(int color, float absPercent) {
        int alpha = color >> 24;
        alpha = alpha >= 0 ? alpha : alpha + 256;
        int red = (color >> 16) % 256;
        red = red >= 0 ? red : red + 256;
        int green = (color >> 8) % 256;
        green = green >= 0 ? green : green + 256;
        int blue = color % 256;
        blue = blue >= 0 ? blue : blue + 256;

        float brightness = Math.max(Math.max(red, green), blue) / 255.0f;
        if (brightness == 0) {
            int x = MathUtil.round(absPercent * 255.0f / 100.0f);
            return Color.argb(alpha, x, x, x);
        } else {
            float d = Math.min(1, brightness + absPercent / 100.0f) / brightness;

            red = (int) Math.min(red * d, 255);
            green = (int) Math.min(green * d, 255);
            blue = (int) Math.min(blue * d, 255);

            return Color.argb(alpha, red, green, blue);
        }
    }

    public static void lightenAbsolute(Property<Integer> color, float absPercent) {
        color.setValue(lightenAbsolute(color.getValue(), absPercent));
    }

    public static int drawOver(int color1, int color2) {
        int alpha1 = color1 >> 24;
        alpha1 = alpha1 >= 0 ? alpha1 : alpha1 + 256;
        int red1 = (color1 >> 16) % 256;
        red1 = red1 >= 0 ? red1 : red1 + 256;
        int green1 = (color1 >> 8) % 256;
        green1 = green1 >= 0 ? green1 : green1 + 256;
        int blue1 = color1 % 256;
        blue1 = blue1 >= 0 ? blue1 : blue1 + 256;

        int alpha2 = color2 >> 24;
        alpha2 = alpha2 >= 0 ? alpha2 : alpha2 + 256;
        int red2 = (color2 >> 16) % 256;
        red2 = red2 >= 0 ? red2 : red2 + 256;
        int green2 = (color2 >> 8) % 256;
        green2 = green2 >= 0 ? green2 : green2 + 256;
        int blue2 = color2 % 256;
        blue2 = blue2 >= 0 ? blue2 : blue2 + 256;

        float a1p = alpha1 / 255.0f;
        float a2p = alpha2 / 255.0f;
        float a2np = 1.0f - a2p;
        int red = MathUtil.round(a1p * red1 * a2np + red2 * a2p);
        int green = MathUtil.round(a1p * green1 * a2np + green2 * a2p);
        int blue = MathUtil.round(a1p * blue1 * a2np + blue2 * a2p);
        return Color.argb(255, red, green, blue);
    }

    public static int grayScale(int color) {
        int alpha = color >> 24;
        alpha = alpha >= 0 ? alpha : alpha + 256;
        int red = (color >> 16) % 256;
        red = red >= 0 ? red : red + 256;
        int green = (color >> 8) % 256;
        green = green >= 0 ? green : green + 256;
        int blue = color % 256;
        blue = blue >= 0 ? blue : blue + 256;

        int luminosity = MathUtil.round(0.2126f * red + 0.7152f * green + 0.0722f * blue);

        return Color.argb(alpha, luminosity, luminosity, luminosity);
    }

    public static void grayScale(Property<Integer> color) {
        color.setValue(grayScale(color.getValue()));
    }

    public static int mix(int color1, int color2, float weight) {
        int alpha1 = color1 >> 24;
        alpha1 = alpha1 >= 0 ? alpha1 : alpha1 + 256;
        int red1 = (color1 >> 16) % 256;
        red1 = red1 >= 0 ? red1 : red1 + 256;
        int green1 = (color1 >> 8) % 256;
        green1 = green1 >= 0 ? green1 : green1 + 256;
        int blue1 = color1 % 256;
        blue1 = blue1 >= 0 ? blue1 : blue1 + 256;

        int alpha2 = color2 >> 24;
        alpha2 = alpha2 >= 0 ? alpha2 : alpha2 + 256;
        int red2 = (color2 >> 16) % 256;
        red2 = red2 >= 0 ? red2 : red2 + 256;
        int green2 = (color2 >> 8) % 256;
        green2 = green2 >= 0 ? green2 : green2 + 256;
        int blue2 = color2 % 256;
        blue2 = blue2 >= 0 ? blue2 : blue2 + 256;

        float p = weight / 100.0f;
        float w = p * 2 - 1;
        float a = (alpha1 - alpha2) / 255.0f;
        float w1 = ((w * a == -1 ? w : (w + a) / (1 + w * a)) + 1) / 2.0f;
        float w2 = 1 - w1;

        int red = MathUtil.round(red1 * w1 + red2 * w2);
        int green = MathUtil.round(green1 * w1 + green2 * w2);
        int blue = MathUtil.round(blue1 * w1 + blue2 * w2);
        int alpha = MathUtil.round(alpha1 * p + alpha2 * (1 - p));
        return Color.argb(alpha, red, green, blue);
    }

    public static int moreTransparent(int color, float percent) {
        int alpha = color >> 24;
        alpha = alpha >= 0 ? alpha : alpha + 256;
        int red = (color >> 16) % 256;
        red = red >= 0 ? red : red + 256;
        int green = (color >> 8) % 256;
        green = green >= 0 ? green : green + 256;
        int blue = color % 256;
        blue = blue >= 0 ? blue : blue + 256;

        if (alpha == 0) {
            return color;
        } else {
            alpha = Math.max(0, alpha - MathUtil.round(percent * alpha / 100.0f));
            return Color.argb(alpha, red, green, blue);
        }
    }

    public static int moreTransparentAbsolute(int color, int transparency) {
        int alpha = color >> 24;
        alpha = alpha >= 0 ? alpha : alpha + 256;
        int red = (color >> 16) % 256;
        red = red >= 0 ? red : red + 256;
        int green = (color >> 8) % 256;
        green = green >= 0 ? green : green + 256;
        int blue = color % 256;
        blue = blue >= 0 ? blue : blue + 256;

        return Color.argb(alpha - transparency, red, green, blue);
    }

    public static int adjustAlpha(int alpha, int color) {
        int red = (color >> 16) % 256;
        red = red >= 0 ? red : red + 256;
        int green = (color >> 8) % 256;
        green = green >= 0 ? green : green + 256;
        int blue = color % 256;
        blue = blue >= 0 ? blue : blue + 256;

        return Color.argb(alpha, red, green, blue);
    }

    public static void adjustAlpha(int alpha, Property<Integer> color) {
        color.setValue(adjustAlpha(alpha, color.getValue()));
    }

    public static String asString(int color) {
        int alpha = color >> 24;
        alpha = alpha >= 0 ? alpha : alpha + 256;
        int red = (color >> 16) % 256;
        red = red >= 0 ? red : red + 256;
        int green = (color >> 8) % 256;
        green = green >= 0 ? green : green + 256;
        int blue = color % 256;
        blue = blue >= 0 ? blue : blue + 256;

        String s = red + "," + green + "," + blue;
        if (alpha != 255) {
            s = alpha + "," + s;
        }
        return s;
    }
}

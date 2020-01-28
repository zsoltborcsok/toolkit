package org.nting.toolkit.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import playn.core.Color;

public class ColorUtilsTest {

    @Test
    public void testHSVtoRGB() {
        assertEquals(0xFF000000, ColorUtils.HSVtoRGB(0, 0, 0));
        assertEquals(0xFFFF0000, ColorUtils.HSVtoRGB(360, 100, 100));
        assertEquals(0xFFFFFFFF, ColorUtils.HSVtoRGB(360, 0, 100));
        assertEquals(0xFFB7DEFF, ColorUtils.HSVtoRGB(207, 28, 100));
        assertEquals(0xFF99993D, ColorUtils.HSVtoRGB(60, 60, 60));
    }

    @Test
    public void testDarken() {
        assertEquals(0xFF660000, ColorUtils.darken(0xFF800000, 20));
        assertEquals(0xFF7F4000, ColorUtils.darken(0xFFFF8000, 50));
        assertEquals(0xFF204060, ColorUtils.darken(0xFF4080C0, 50));
    }

    @Test
    public void testDarkenAbsolute() {
        assertEquals(0xFF4D0000, ColorUtils.darkenAbsolute(0xFF800000, 20));
        assertEquals(0xFF7F4000, ColorUtils.darkenAbsolute(0xFFFF8000, 50)); // HSV(30, 100, 100) -> HVS(30, 100,
        // 50)
        assertEquals(0xFF152B40, ColorUtils.darkenAbsolute(0xFF4080C0, 50)); // HSV(210, 60, 75) -> HSV(210, 60, 25)
        assertEquals(0xFF7F3500, ColorUtils.darkenAbsolute(0xFFCC5500, 30));
    }

    @Test
    public void testLighten() {
        assertEquals(0xFF990000, ColorUtils.lighten(0xFF800000, 20));
        assertEquals(0xFFFF8000, ColorUtils.lighten(0xFFFF8000, 50));
        assertEquals(0xFF55AAFF, ColorUtils.lighten(0xFF4080C0, 50));
        assertEquals(0xFF306090, ColorUtils.lighten(0xFF204060, 50));
    }

    @Test
    public void testLightenAbsolute() {
        assertEquals(0xFFB30000, ColorUtils.lightenAbsolute(0xFF800000, 20));
        assertEquals(0xFFFF0000, ColorUtils.lightenAbsolute(0xFF7F0000, 60));
        assertEquals(0xFF808080, ColorUtils.lightenAbsolute(0xFF000000, 50));
        assertEquals(0xFFFE7E00, ColorUtils.lightenAbsolute(0xFF7F3F00, 50)); // HSV(30, 100, 50) -> HSV(30, 100,
        // 100)
        assertEquals(0xFF3E7DBF, ColorUtils.lightenAbsolute(0xFF152A40, 50)); // HSV(210, 60, 25) -> HSV(210, 60,
        // 75)
        assertEquals(0xFFB30000, ColorUtils.lightenAbsolute(0xFF800000, 20));
    }

    @Test
    public void testDrawOver() {
        assertEquals(0xFF0000FF, ColorUtils.drawOver(0xFFFF0000, 0xFF0000FF));
        assertEquals(0xFF7F0080, ColorUtils.drawOver(0xFFFF0000, 0x800000FF));
    }

    @Test
    public void testGrayScale() {
        assertEquals(0xFF000000, ColorUtils.grayScale(0xFF000000));
        assertEquals(Color.rgb(30, 30, 30), ColorUtils.grayScale(Color.rgb(47, 14, 138)));
        assertEquals(0xFF808080, ColorUtils.grayScale(0xFF808080));
        assertEquals(0xFFFFFFFF, ColorUtils.grayScale(0xFFFFFFFF));
    }

    @Test
    public void testMix() {
        assertEquals(0xFF800080, ColorUtils.mix(0xFFFF0000, 0xFF0000FF, 50));
        assertEquals(0xFF4000BF, ColorUtils.mix(0xFFFF0000, 0xFF0000FF, 25));
        assertEquals(0xBF3F00C0, ColorUtils.mix(0x7FFF0000, 0xFF0000FF, 50));
    }
}
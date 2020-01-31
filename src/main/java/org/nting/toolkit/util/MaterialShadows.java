package org.nting.toolkit.util;

import org.nting.toolkit.ui.shape.RectangleShadowShape;

import playn.core.Canvas;
import pythagoras.f.Dimension;

// https://github.com/angular/material/blob/master/src/core/style/variables.scss
public class MaterialShadows {

    public static final int UMBRA = 0x33000000;
    public static final int PENUMBRA = 0x24000000;
    public static final int AMBIENT = 0x1E000000;

    public static void paintShadow1(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 1, 3, 0).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 1, 1, 0).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 2, 1, -1).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow2(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 1, 5, 0).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 2, 2, 0).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 3, 1, -2).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow3(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 1, 8, 0).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 3, 4, 0).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 3, 3, -2).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow4(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 2, 4, -1).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 4, 5, 0).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 1, 10, 0).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow5(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 3, 5, -1).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 5, 8, 0).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 1, 14, 0).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow6(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 3, 5, -1).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 6, 10, 0).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 1, 18, 0).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow7(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 4, 5, -2).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 7, 10, 1).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 2, 16, 1).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow8(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 5, 5, -3).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 8, 10, 1).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 3, 14, 2).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow9(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 5, 6, -3).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 9, 12, 1).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 3, 16, 2).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow10(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 6, 6, -3).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 10, 14, 1).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 4, 18, 3).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow11(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 6, 7, -4).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 11, 15, 1).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 4, 20, 3).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow12(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 7, 8, -4).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 12, 17, 2).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 5, 22, 4).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow13(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 7, 8, -4).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 13, 19, 2).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 5, 24, 4).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow14(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 7, 9, -4).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 14, 21, 2).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 5, 26, 4).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow15(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 8, 9, -5).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 15, 22, 2).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 6, 28, 5).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow16(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 8, 10, -5).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 16, 24, 2).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 6, 30, 5).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow17(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 8, 11, -5).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 17, 26, 2).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 6, 32, 5).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow18(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 9, 11, -5).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 18, 28, 2).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 7, 34, 6).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow19(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 9, 12, -6).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 19, 29, 2).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 7, 36, 6).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow20(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 10, 13, -6).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 20, 31, 3).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 8, 38, 7).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow21(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 10, 13, -6).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 21, 33, 3).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 8, 40, 7).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow22(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 10, 14, -6).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 22, 35, 3).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 8, 42, 7).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow23(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 11, 14, -7).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 23, 36, 3).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 9, 44, 8).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow24(Canvas canvas, Dimension size) {
        new RectangleShadowShape(0, 11, 15, -7).fillColor(UMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 24, 38, 3).fillColor(PENUMBRA).size(size).paint(canvas);
        new RectangleShadowShape(0, 24, 38, 3).fillColor(AMBIENT).size(size).paint(canvas);
    }

    public static void paintShadow(int shadowSize, Canvas canvas, Dimension size) {
        switch (shadowSize) {
        case 1:
            paintShadow1(canvas, size);
            break;
        case 2:
            paintShadow2(canvas, size);
            break;
        case 3:
            paintShadow3(canvas, size);
            break;
        case 4:
            paintShadow4(canvas, size);
            break;
        case 5:
            paintShadow5(canvas, size);
            break;
        case 6:
            paintShadow6(canvas, size);
            break;
        case 7:
            paintShadow7(canvas, size);
            break;
        case 8:
            paintShadow8(canvas, size);
            break;
        case 9:
            paintShadow9(canvas, size);
            break;
        case 10:
            paintShadow10(canvas, size);
            break;
        case 11:
            paintShadow11(canvas, size);
            break;
        case 12:
            paintShadow12(canvas, size);
            break;
        case 13:
            paintShadow13(canvas, size);
            break;
        case 14:
            paintShadow14(canvas, size);
            break;
        case 15:
            paintShadow15(canvas, size);
            break;
        case 16:
            paintShadow16(canvas, size);
            break;
        case 17:
            paintShadow17(canvas, size);
            break;
        case 18:
            paintShadow18(canvas, size);
            break;
        case 19:
            paintShadow19(canvas, size);
            break;
        case 20:
            paintShadow20(canvas, size);
            break;
        case 21:
            paintShadow21(canvas, size);
            break;
        case 22:
            paintShadow22(canvas, size);
            break;
        case 23:
            paintShadow23(canvas, size);
            break;
        case 24:
            paintShadow24(canvas, size);
            break;
        }
    }
}

package org.nting.toolkit.ui.stone;

import org.nting.toolkit.ui.shape.BasicShape;

import playn.core.Canvas;
import playn.core.Gradient;
import playn.core.PlayN;
import pythagoras.f.Dimension;

public class GradientBackground extends Background {

    private final int color1;
    private final int color2;
    private final boolean horizontal;

    public GradientBackground(BasicShape shape, int color1, int color2) {
        this(null, shape, color1, color2, true);
    }

    public GradientBackground(BasicShape shape, int color1, int color2, boolean horizontal) {
        this(null, shape, color1, color2, horizontal);
    }

    public GradientBackground(Background background, BasicShape shape, int color1, int color2) {
        this(background, shape, color1, color2, true);
    }

    public GradientBackground(Background background, BasicShape shape, int color1, int color2, boolean horizontal) {
        super(background, shape);

        this.color1 = color1;
        this.color2 = color2;
        this.horizontal = horizontal;
    }

    @Override
    protected void doPaint(Canvas canvas, Dimension size) {
        try {
            Gradient gradient;
            if (horizontal) {
                gradient = PlayN.graphics().createLinearGradient(0, 0, 0, size.height, new int[] { color1, color2 },
                        new float[] { 0.0f, 1.0f });
            } else {
                gradient = PlayN.graphics().createLinearGradient(0, 0, size.width, 0, new int[] { color1, color2 },
                        new float[] { 0.0f, 1.0f });
            }

            shape.size(size.width, size.height).fillGradient(gradient).paint(canvas);
        } catch (Exception e) {
            PlayN.log(getClass()).warn(e.getMessage());
        }
    }
}

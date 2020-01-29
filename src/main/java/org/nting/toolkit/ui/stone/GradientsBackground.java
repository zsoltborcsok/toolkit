package org.nting.toolkit.ui.stone;

import org.nting.toolkit.ui.shape.BasicShape;

import playn.core.Canvas;
import playn.core.Gradient;
import playn.core.PlayN;
import pythagoras.f.Dimension;

public class GradientsBackground extends Background {

    private final int[] colors;
    private final float[] positions;
    private final boolean horizontal;

    public GradientsBackground(BasicShape<?> shape, boolean horizontal, int... colors) {
        this(null, shape, horizontal, colors, null);
    }

    public GradientsBackground(BasicShape<?> shape, boolean horizontal, int[] colors, float[] positions) {
        this(null, shape, horizontal, colors, positions);
    }

    public GradientsBackground(Background background, BasicShape<?> shape, boolean horizontal, int... colors) {
        this(background, shape, horizontal, colors, null);
    }

    public GradientsBackground(Background background, BasicShape<?> shape, boolean horizontal, int[] colors,
            float[] positions) {
        super(background, shape);

        if (positions == null) {
            float fraction = 1.0f / (colors.length - 1);

            positions = new float[colors.length];
            positions[0] = 0.0f;
            positions[colors.length - 1] = 1.0f;
            for (int i = 1; i < colors.length - 1; i++) {
                positions[i] = i * fraction;
            }
        }

        this.colors = colors;
        this.positions = positions;
        this.horizontal = horizontal;
    }

    @Override
    protected void doPaint(Canvas canvas, Dimension size) {
        Gradient gradient;
        if (horizontal && 0 < size.height) {
            gradient = PlayN.graphics().createLinearGradient(0, 0, 0, size.height, colors, positions);
        } else if (!horizontal && 0 < size.width) {
            gradient = PlayN.graphics().createLinearGradient(0, 0, size.width, 0, colors, positions);
        } else {
            return;
        }

        shape.size(size.width, size.height).fillGradient(gradient).paint(canvas);
    }
}

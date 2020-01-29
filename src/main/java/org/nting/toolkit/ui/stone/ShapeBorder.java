package org.nting.toolkit.ui.stone;

import org.nting.toolkit.ui.shape.BasicShape;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class ShapeBorder extends Border {

    private BasicShape<?> shape;

    public ShapeBorder(Stone stone, int thickness, BasicShape<?> shape) {
        super(stone, thickness, thickness, thickness, thickness);

        this.shape = shape;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        super.paint(canvas, size);

        shape.size(size.width, size.height).paint(canvas);
    }
}

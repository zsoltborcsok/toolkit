package org.nting.toolkit.ui.stone;

import org.nting.toolkit.ui.shape.Shape;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class FixShapeContent extends Content {

    private final Dimension size;
    private final Shape<?> shape;

    public FixShapeContent(Dimension size, Shape<?> shape) {
        this.size = size;
        this.shape = shape;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        shape.paint(canvas);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(size);
    }
}

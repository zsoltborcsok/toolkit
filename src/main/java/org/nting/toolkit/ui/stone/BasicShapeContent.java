package org.nting.toolkit.ui.stone;

import org.nting.toolkit.ui.shape.BasicShape;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class BasicShapeContent extends Content {

    private final Dimension size;
    private final BasicShape<?> shape;

    public BasicShapeContent(BasicShape<?> shape) {
        this(new Dimension(0, 0), shape);
    }

    public BasicShapeContent(Dimension size, BasicShape<?> shape) {
        this.size = size;
        this.shape = shape;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        shape.size(size.width, size.height).paint(canvas);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(size);
    }

    public BasicShape<?> getShape() {
        return shape;
    }
}

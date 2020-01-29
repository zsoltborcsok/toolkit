package org.nting.toolkit.ui.stone;

import org.nting.toolkit.ui.shape.BasicShape;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class Background extends Stone {

    protected static final Dimension EMPTY = new Dimension();

    protected BasicShape<?> shape;

    public Background(BasicShape<?> shape) {
        this(null, shape);
    }

    public Background(Background background, BasicShape<?> shape) {
        super(background);

        this.shape = shape;
    }

    @Override
    public final void paint(Canvas canvas, Dimension size) {
        if (stone != null) {
            stone.paint(canvas, size);
        }

        doPaint(canvas, size);
    }

    protected void doPaint(Canvas canvas, Dimension size) {
        shape.size(size.width, size.height).paint(canvas);
    }

    @Override
    public Dimension getPreferredSize() {
        return EMPTY;
    }
}

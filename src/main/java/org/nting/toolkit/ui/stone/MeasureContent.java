package org.nting.toolkit.ui.stone;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class MeasureContent extends Content {

    private final Dimension measuredSize = new Dimension(0, 0);

    public MeasureContent(Stone stone) {
        super(stone);
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        measuredSize.width = size.width;
        measuredSize.height = size.height;

        stone.paint(canvas, size);
    }

    @Override
    public Dimension getPreferredSize() {
        return stone.getPreferredSize();
    }

    public Dimension getMeasuredSize() {
        return measuredSize;
    }
}

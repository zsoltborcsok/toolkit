package org.nting.toolkit.ui.stone;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class FixedSizeContent extends Content {

    private Dimension size;

    public FixedSizeContent(Content content, Dimension size) {
        super(content);
        this.size = size;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        stone.paint(canvas, size);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(size);
    }
}

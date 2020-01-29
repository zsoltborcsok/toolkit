package org.nting.toolkit.ui.stone;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class ColorContent extends Content {

    private Dimension size;
    private Background background;

    public ColorContent(Dimension size, Background background) {
        super(background);

        this.size = size;
        this.background = background;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        background.paint(canvas, size);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(size);
    }
}

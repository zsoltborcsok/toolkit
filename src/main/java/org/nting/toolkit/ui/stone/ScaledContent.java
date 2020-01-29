package org.nting.toolkit.ui.stone;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class ScaledContent extends Content {

    private final float scaleX;
    private final float scaleY;

    public ScaledContent(Content content, float scaleX, float scaleY) {
        super(content);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public ScaledContent(Content content, float scale) {
        this(content, scale, scale);
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        canvas.save();
        canvas.scale(scaleX, scaleY);
        stone.paint(canvas, size);
        canvas.restore();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferredSize = stone.getPreferredSize();
        preferredSize.width *= scaleX;
        preferredSize.height *= scaleY;
        return preferredSize;
    }
}

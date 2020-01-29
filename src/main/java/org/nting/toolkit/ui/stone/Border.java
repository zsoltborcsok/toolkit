package org.nting.toolkit.ui.stone;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class Border extends Content {

    protected int topThickness;
    protected int rightThickness;
    protected int bottomThickness;
    protected int leftThickness;

    public Border(Stone stone, int topThickness, int rightThickness, int bottomThickness, int leftThickness) {
        super(stone);

        this.topThickness = topThickness;
        this.rightThickness = rightThickness;
        this.bottomThickness = bottomThickness;
        this.leftThickness = leftThickness;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        canvas.translate(leftThickness, topThickness);
        Dimension childSize = new Dimension(size.width - (leftThickness + rightThickness),
                size.height - (topThickness + bottomThickness));
        stone.paint(canvas, childSize);
        canvas.translate(-leftThickness, -topThickness);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferredSize = stone.getPreferredSize();
        preferredSize.width += leftThickness + rightThickness;
        preferredSize.height += topThickness + bottomThickness;

        return preferredSize;
    }
}

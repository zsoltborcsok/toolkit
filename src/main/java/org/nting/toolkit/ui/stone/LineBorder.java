package org.nting.toolkit.ui.stone;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class LineBorder extends Border {

    private int color;

    public LineBorder(Stone stone, int topThickness, int rightThickness, int bottomThickness, int leftThickness,
            int color) {
        super(stone, topThickness, rightThickness, bottomThickness, leftThickness);

        this.color = color;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        super.paint(canvas, size);

        // NOTE: the JAVA platform can't handle properly the case when thickness is even.
        canvas.setStrokeColor(color);
        if (topThickness > 0) {
            canvas.setStrokeWidth(topThickness);
            float offset = (topThickness) / 2f;
            canvas.drawLine(0 + offset, 0 + offset, size.width - offset, 0 + offset);
        }
        if (rightThickness > 0) {
            canvas.setStrokeWidth(rightThickness);
            float offset = (rightThickness) / 2f;
            canvas.drawLine(size.width - offset, 0 + offset, size.width - offset, size.height - offset);
        }
        if (bottomThickness > 0) {
            canvas.setStrokeWidth(bottomThickness);
            float offset = (bottomThickness) / 2f;
            canvas.drawLine(0 + offset, size.height - offset, size.width - offset, size.height - offset);
        }
        if (leftThickness > 0) {
            canvas.setStrokeWidth(leftThickness);
            float offset = (leftThickness) / 2f;
            canvas.drawLine(0 + offset, 0 + offset, 0 + offset, size.height - offset);
        }

        canvas.setStrokeWidth(1);
    }
}

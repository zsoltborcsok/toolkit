package org.nting.toolkit.ui.shape;

import com.google.common.base.MoreObjects;

import playn.core.Canvas;
import playn.core.Platform;
import playn.core.PlayN;

public class RectangleShape extends BasicShape<RectangleShape> {

    private float x;
    private float y;

    public RectangleShape() {
        this(0, 0, 0, 0);
    }

    public RectangleShape(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void paint(Canvas canvas) {
        if (fillGradient != null) {
            canvas.setFillGradient(fillGradient);
            canvas.fillRect(x, y, width, height);
        } else if (fillPattern != null) {
            canvas.setFillPattern(fillPattern);
            canvas.fillRect(x, y, width, height);
        } else if (fillColor != 0) {
            canvas.setFillColor(fillColor);
            canvas.fillRect(x, y, width, height);
        }

        if (strokeColor != 0) {
            canvas.setStrokeColor(strokeColor);
            canvas.setStrokeWidth(strokeWidth);
            strokeRect(canvas, x, y, width, height);
        }
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper().add("x", x).add("y", y);
    }

    public static void strokeRect(Canvas canvas, float x, float y, float width, float height) {
        width -= 1;
        height -= 1;

        if (PlayN.platformType() == Platform.Type.HTML) {
            x += 0.5f;
            y += 0.5f;
        }

        canvas.strokeRect(x, y, width, height);
    }
}

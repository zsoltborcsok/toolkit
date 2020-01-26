package org.nting.toolkit.ui.shape;

import com.google.common.base.MoreObjects;

import playn.core.Canvas;

public class CircleShape extends Shape<CircleShape> {

    private float x;
    private float y;
    private float radius;

    public CircleShape(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public void paint(Canvas canvas) {
        if (fillGradient != null) {
            canvas.setFillGradient(fillGradient);
            canvas.fillCircle(x, y, radius);
        } else if (fillPattern != null) {
            canvas.setFillPattern(fillPattern);
            canvas.fillCircle(x, y, radius);
        } else if (fillColor != 0) {
            canvas.setFillColor(fillColor);
            canvas.fillCircle(x, y, radius);
        }

        if (strokeColor != 0) {
            canvas.setStrokeColor(strokeColor);
            canvas.setStrokeWidth(strokeWidth);
            canvas.strokeCircle(x, y, radius);
        }
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper().add("x", x).add("y", y).add("radius", radius);
    }
}

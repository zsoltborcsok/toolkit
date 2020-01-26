package org.nting.toolkit.ui.shape;

import com.google.common.base.MoreObjects;

import playn.core.Canvas;
import playn.core.Path;
import playn.core.Platform;
import playn.core.PlayN;

public class RoundedRectangleShape extends BasicShape<RoundedRectangleShape> {

    private float x;
    private float y;
    private float radius;

    public RoundedRectangleShape(float radius) {
        this(0, 0, 0, 0, radius);
    }

    public RoundedRectangleShape(float x, float y, float width, float height, float radius) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.radius = radius;
    }

    @Override
    public void paint(Canvas canvas) {
        if (fillGradient != null) {
            canvas.setFillGradient(fillGradient);
            fillRoundRect(canvas, x, y, width, height, radius);
        } else if (fillPattern != null) {
            canvas.setFillPattern(fillPattern);
            fillRoundRect(canvas, x, y, width, height, radius);
        } else if (fillColor != 0) {
            canvas.setFillColor(fillColor);
            fillRoundRect(canvas, x, y, width, height, radius);
        }

        if (strokeColor != 0) {
            canvas.setStrokeColor(strokeColor);
            canvas.setStrokeWidth(strokeWidth);
            strokeRoundRect(canvas, x, y, width, height, radius);
        }
    }

    private void fillRoundRect(Canvas canvas, float x, float y, float width, float height, float radius) {
        Path path = canvas.createPath();

        path.moveTo(radius, 0);
        path.lineTo(width - radius, 0);
        path.quadraticCurveTo(width, 0, width, radius);
        path.lineTo(width, height - radius);
        path.quadraticCurveTo(width, height, width - radius, height);
        path.lineTo(radius, height);
        path.quadraticCurveTo(0, height, 0, height - radius);
        path.lineTo(0, radius);
        path.quadraticCurveTo(0, 0, radius, 0);
        path.close();

        canvas.translate(x, y);
        canvas.fillPath(path);
        canvas.translate(-x, -y);
    }

    private void strokeRoundRect(Canvas canvas, float x, float y, float width, float height, float radius) {
        Path path = canvas.createPath();

        if (PlayN.platformType() == Platform.Type.HTML) {
            width -= 0.5f;
            height -= 0.5f;

            path.moveTo(radius, 0.5f);
            path.lineTo(width - radius, 0.5f);
            path.quadraticCurveTo(width, 0.5f, width, radius);
            path.lineTo(width, height - radius);
            path.quadraticCurveTo(width, height, width - radius, height);
            path.lineTo(radius, height);
            path.quadraticCurveTo(0.5f, height, 0.5f, height - radius);
            path.lineTo(0.5f, radius);
            path.quadraticCurveTo(0.5f, 0.5f, radius, 0.5f);
            path.close();
        } else {
            width -= 1;
            height -= 1;

            path.moveTo(radius, 0);
            path.lineTo(width - radius, 0);
            path.quadraticCurveTo(width, 0, width, radius);
            path.lineTo(width, height - radius);
            path.quadraticCurveTo(width, height, width - radius, height);
            path.lineTo(radius, height);
            path.quadraticCurveTo(0, height, 0, height - radius);
            path.lineTo(0, radius);
            path.quadraticCurveTo(0, 0, radius, 0);
            path.close();
        }

        canvas.translate(x, y);
        canvas.strokePath(path);
        canvas.translate(-x, -y);
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper().add("x", x).add("y", y).add("radius", radius);
    }
}

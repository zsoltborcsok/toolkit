package org.nting.toolkit.ui.shape;

import com.google.common.base.MoreObjects;

import playn.core.Canvas;
import playn.core.Path;
import playn.core.Platform;
import playn.core.PlayN;

public class EllipsisShape extends BasicShape<EllipsisShape> {

    private final float x;
    private final float y;
    private Path path;

    public EllipsisShape() {
        this(0, 0, 0, 0);
    }

    public EllipsisShape(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void paint(Canvas canvas) {
        if (width == height) {
            paintCircle(canvas, width / 2);
        } else {
            paintEllipsis(canvas);
        }
    }

    public EllipsisShape size(float width, float height) {
        if (this.width != width || this.height == height) {
            path = null;
        }
        return super.size(width, height);
    }

    private void paintCircle(Canvas canvas, float radius) {
        if (fillGradient != null) {
            canvas.setFillGradient(fillGradient);
            canvas.fillCircle(x + radius, y + radius, radius);
        } else if (fillPattern != null) {
            canvas.setFillPattern(fillPattern);
            canvas.fillCircle(x + radius, y + radius, radius);
        } else if (fillColor != 0) {
            canvas.setFillColor(fillColor);
            canvas.fillCircle(x + radius, y + radius, radius);
        }

        if (strokeColor != 0) {
            canvas.setStrokeColor(strokeColor);
            canvas.setStrokeWidth(strokeWidth);
            canvas.strokeCircle(x + radius, y + radius, radius - 0.5f);
        }
    }

    private void paintEllipsis(Canvas canvas) {
        if (strokeColor != 0) {
            width -= 1;
            height -= 1;
        }

        if (path == null) {
            float kappa = 0.5522848f;
            float ox = (width / 2) * kappa; // control point offset horizontal
            float oy = (height / 2) * kappa;// control point offset vertical
            float xe = x + width;           // x-end
            float ye = y + height;          // y-end
            float xm = x + width / 2;       // x-middle
            float ym = y + height / 2;      // y-middle

            path = canvas.createPath();
            path.moveTo(x, ym);
            path.bezierTo(x, ym - oy, xm - ox, y, xm, y);
            path.bezierTo(xm + ox, y, xe, ym - oy, xe, ym);
            path.bezierTo(xe, ym + oy, xm + ox, ye, xm, ye);
            path.bezierTo(xm - ox, ye, x, ym + oy, x, ym);
            path.close();
        }

        if (fillGradient != null) {
            canvas.setFillGradient(fillGradient);
            canvas.fillPath(path);
        } else if (fillPattern != null) {
            canvas.setFillPattern(fillPattern);
            canvas.fillPath(path);
        } else if (fillColor != 0) {
            canvas.setFillColor(fillColor);
            canvas.fillPath(path);
        }

        if (strokeColor != 0) {
            canvas.setStrokeColor(strokeColor);
            canvas.setStrokeWidth(strokeWidth);
            if (PlayN.platformType() == Platform.Type.HTML) {
                canvas.translate(0.5f, 0.5f);
                canvas.strokePath(path);
                canvas.translate(-0.5f, -0.5f);
            } else {
                canvas.strokePath(path);
            }
        }
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper().add("x", x).add("y", y);
    }
}

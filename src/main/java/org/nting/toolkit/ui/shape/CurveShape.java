package org.nting.toolkit.ui.shape;

import org.nting.data.Property;
import org.nting.data.property.ObjectProperty;

import com.google.common.base.MoreObjects;

import playn.core.Canvas;
import playn.core.Path;
import pythagoras.f.Rectangle;

// The offsets are y offsets and thickness is also, so only horizontal curves are supported!
public class CurveShape extends BasicShape<CurveShape> {

    // Relative positions (0..1) to width and height.
    private float x1, y1;
    private float xCtrl1, yCtrl1;
    private float xCtrl2, yCtrl2;
    private float x2, y2;
    private float thickness;

    // Relative offsets (-1..1) to width and height.
    private float offset1 = 0;
    private float offsetCtrl1 = 0;
    private float offsetCtrl2 = 0;
    private float offset2 = 0;
    public final Property<Float> offset = new ObjectProperty<>(0f); // In radian to animate the curve.

    public CurveShape() {
    }

    public CurveShape(float x1, float y1, float xCtrl1, float yCtrl1, float xCtrl2, float yCtrl2, float x2, float y2,
            float thickness) {
        this.x1 = x1;
        this.y1 = y1;
        this.xCtrl1 = xCtrl1;
        this.yCtrl1 = yCtrl1;
        this.xCtrl2 = xCtrl2;
        this.yCtrl2 = yCtrl2;
        this.x2 = x2;
        this.y2 = y2;
        this.thickness = thickness;
    }

    public CurveShape offsets(float offset1, float offsetCtrl1, float offsetCtrl2, float offset2) {
        this.offset1 = offset1;
        this.offsetCtrl1 = offsetCtrl1;
        this.offsetCtrl2 = offsetCtrl2;
        this.offset2 = offset2;
        return this;
    }

    public CurveShape thickness(int thickness) {
        this.thickness = thickness / 100f;
        return this;
    }

    public CurveShape from(int x, int y, int offset) {
        x1 = x / 100f;
        y1 = y / 100f;
        offset1 = offset / 100f;
        return this;
    }

    public CurveShape from(int y) {
        return from(0, y, 0);
    }

    public CurveShape from(int y, int offset) {
        return from(0, y, offset);
    }

    public CurveShape control1(int x, int y, int offset) {
        xCtrl1 = x / 100f;
        yCtrl1 = y / 100f;
        offsetCtrl1 = offset / 100f;
        return this;
    }

    public CurveShape control1(int y, int offset) {
        return control1(33, y, offset);
    }

    public CurveShape control1(int y) {
        return control1(33, y, 0);
    }

    public CurveShape control2(int x, int y, int offset) {
        xCtrl2 = x / 100f;
        yCtrl2 = y / 100f;
        offsetCtrl2 = offset / 100f;
        return this;
    }

    public CurveShape control2(int y, int offset) {
        return control2(66, y, offset);
    }

    public CurveShape control2(int y) {
        return control2(66, y, 0);
    }

    public CurveShape to(int x, int y, int offset) {
        x2 = x / 100f;
        y2 = y / 100f;
        offset2 = offset / 100f;
        return this;
    }

    public CurveShape to(int y) {
        return to(100, y, 0);
    }

    public CurveShape to(int y, int offset) {
        return to(100, y, offset);
    }

    public boolean isHorizontal() {
        return Math.abs(y2 - y1) < Math.abs(x2 - x1);
    }

    public Rectangle getCurveBounds() {
        Rectangle curveBounds = getCurveBounds(x1, y1, xCtrl1, yCtrl1, xCtrl2, yCtrl2, x2, y2);
        curveBounds.add(getCurveBounds(x1, y1 + offset1, xCtrl1, yCtrl1 + offsetCtrl1, xCtrl2, yCtrl2 + offsetCtrl2, x2,
                y2 + offset2));
        curveBounds.add(curveBounds.x, curveBounds.maxY() + thickness);
        return curveBounds;
    }

    @Override
    public void paint(Canvas canvas) {
        float offset = (float) Math.sin(this.offset.getValue());

        Path path = canvas.createPath();

        float x = width * x1;
        float y = height * (y1 + offset1 * offset);
        path.moveTo(x, y);

        float c1x = width * xCtrl1;
        float c1y = height * (yCtrl1 + offsetCtrl1 * offset);
        float c2x = width * xCtrl2;
        float c2y = height * (yCtrl2 + offsetCtrl2 * offset);
        float xx = width * x2;
        float yy = height * (y2 + offset2 * offset);
        path.bezierTo(c1x, c1y, c2x, c2y, xx, yy);

        float thickness = this.thickness * height;
        path.lineTo(xx, yy + thickness);
        path.bezierTo(c2x, c2y + thickness, c1x, c1y + thickness, x, y + thickness);
        path.lineTo(x, y);
        path.close();

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
    }

    public static Rectangle getCurveBounds(float ax, float ay, float bx, float by, float cx, float cy, float dx,
            float dy) {
        float px, py, qx, qy, rx, ry, sx, sy, tx, ty, tobx, toby, tocx, tocy, todx, tody, toqx, toqy, torx, tory, totx,
                toty;
        float x, y, minx, miny, maxx, maxy;

        minx = miny = Float.POSITIVE_INFINITY;
        maxx = maxy = Float.NEGATIVE_INFINITY;

        tobx = bx - ax;
        toby = by - ay; // directions
        tocx = cx - bx;
        tocy = cy - by;
        todx = dx - cx;
        tody = dy - cy;
        float step = 1 / 40f; // precission
        for (int i = 0; i < 41; i++) {
            float d = i * step;
            px = ax + d * tobx;
            py = ay + d * toby;
            qx = bx + d * tocx;
            qy = by + d * tocy;
            rx = cx + d * todx;
            ry = cy + d * tody;
            toqx = qx - px;
            toqy = qy - py;
            torx = rx - qx;
            tory = ry - qy;

            sx = px + d * toqx;
            sy = py + d * toqy;
            tx = qx + d * torx;
            ty = qy + d * tory;
            totx = tx - sx;
            toty = ty - sy;

            x = sx + d * totx;
            y = sy + d * toty;
            minx = Math.min(minx, x);
            miny = Math.min(miny, y);
            maxx = Math.max(maxx, x);
            maxy = Math.max(maxy, y);
        }

        return new Rectangle(minx, miny, maxx - minx, maxy - miny);
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper().add("x1", x1).add("y1", y1).add("xCtrl1", xCtrl1).add("yCtrl1", yCtrl1)
                .add("xCtrl2", xCtrl2).add("yCtrl2", yCtrl2).add("x2", x2).add("y2", y2).add("thickness", thickness)
                .add("offset1", offset1).add("offsetCtrl1", offsetCtrl1).add("offsetCtrl2", offsetCtrl2)
                .add("offset2", offset2).add("offset", offset);
    }
}

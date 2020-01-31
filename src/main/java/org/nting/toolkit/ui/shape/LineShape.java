package org.nting.toolkit.ui.shape;

import static org.nting.toolkit.ui.Colors.WHITE;

import com.google.common.base.MoreObjects;

import playn.core.Canvas;
import playn.core.Path;
import playn.core.Platform;
import playn.core.PlayN;

public class LineShape extends BasicShape<LineShape> {

    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private Canvas.LineCap lineCap = Canvas.LineCap.SQUARE;

    public LineShape(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        strokeColor = WHITE;
    }

    public LineShape lineCap(Canvas.LineCap lineCap) {
        this.lineCap = lineCap;
        return this;
    }

    @Override
    public LineShape size(float width, float height) {
        x2 = x1 + width;
        y2 = y1 + height;

        return super.size(width, height);
    }

    @Override
    public void paint(Canvas canvas) {
        Path path = canvas.createPath();
        if (PlayN.platformType() == Platform.Type.HTML) {
            path.moveTo(x1 + 0.5f, y1 + 0.5f);
            path.lineTo(x2 + 0.5f, y2 + 0.5f);
            // path.close(); Cause problems with alpha channel (line is drawn twice)...
        } else {
            path.moveTo(x1, y1);
            path.lineTo(x2, y2);
        }

        canvas.setLineCap(lineCap);
        canvas.setStrokeColor(strokeColor);
        canvas.setStrokeWidth(strokeWidth);
        canvas.strokePath(path);
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper().add("x1", x1).add("y1", y1).add("x2", x2).add("y2", y2).add("lineCap", lineCap);
    }
}

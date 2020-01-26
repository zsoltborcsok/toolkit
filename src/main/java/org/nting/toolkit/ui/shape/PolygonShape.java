package org.nting.toolkit.ui.shape;

import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

import playn.core.Canvas;
import playn.core.Path;
import playn.core.Platform;
import playn.core.PlayN;
import pythagoras.f.Point;

public class PolygonShape extends Shape<PolygonShape> {

    protected final List<Point> points = Lists.newArrayList();

    public PolygonShape(List<Point> points) {
        this.points.addAll(points);
    }

    @Override
    public void paint(Canvas canvas) {
        if (fillGradient != null) {
            canvas.setFillGradient(fillGradient);
            canvas.fillPath(createFillPath(canvas));
        } else if (fillPattern != null) {
            canvas.setFillPattern(fillPattern);
            canvas.fillPath(createFillPath(canvas));
        } else if (fillColor != 0) {
            canvas.setFillColor(fillColor);
            canvas.fillPath(createFillPath(canvas));
        }

        if (strokeColor != 0) {
            canvas.setStrokeColor(strokeColor);
            canvas.setStrokeWidth(strokeWidth);
            canvas.strokePath(createStrokePath(canvas));
        }
    }

    private Path createFillPath(Canvas canvas) {
        Path path = canvas.createPath();
        Point firstPoint = points.get(0);
        path.moveTo(firstPoint.x, firstPoint.y);
        for (int i = 1; i < points.size(); i++) {
            Point point = points.get(i);
            path.lineTo(point.x, point.y);
        }
        path.close();

        return path;
    }

    private Path createStrokePath(Canvas canvas) {
        if (PlayN.platformType() == Platform.Type.HTML) {
            Path path = canvas.createPath();
            Point firstPoint = points.get(0);
            path.moveTo(firstPoint.x + 0.5f, firstPoint.y + 0.5f);
            for (int i = 1; i < points.size(); i++) {
                Point point = points.get(i);
                path.lineTo(point.x + 0.5f, point.y + 0.5f);
            }
            path.close();

            return path;
        } else {
            return createFillPath(canvas);
        }
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper().add("points", points);
    }
}

package org.nting.toolkit.ui.stone;

import java.util.List;

import com.google.common.collect.Lists;

import playn.core.Canvas;
import playn.core.Path;
import pythagoras.f.Circle;
import pythagoras.f.Dimension;
import pythagoras.f.Line;
import pythagoras.f.Rectangle;

public class ScalableContent extends Content {

    private final List<Line> lines = Lists.newArrayList();
    private final List<Circle> circles = Lists.newArrayList();
    private final List<Rectangle> rectangles = Lists.newArrayList();
    private final List<float[]> polygons = Lists.newArrayList();
    private final int color;
    private final int thickness;

    public ScalableContent(int color, int thickness) {
        this.color = color;
        this.thickness = thickness;
    }

    public ScalableContent addLine(float relativeX1, float relativeY1, float relativeX2, float relativeY2) {
        lines.add(new Line(relativeX1, relativeY1, relativeX2, relativeY2));
        return this;
    }

    public ScalableContent addCircle(float relativeX, float relativeY, float relativeRadius) {
        circles.add(new Circle(relativeX, relativeY, relativeRadius));
        return this;
    }

    public ScalableContent addRectangle(float relativeX, float relativeY, float relativeWidth, float relativeHeight) {
        rectangles.add(new Rectangle(relativeX, relativeY, relativeWidth, relativeHeight));
        return this;
    }

    public ScalableContent addPolygon(float... relativeCoordinates) {
        polygons.add(relativeCoordinates);
        return this;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        canvas.setStrokeColor(color);
        for (Line line : lines) {
            canvas.setStrokeWidth(thickness);
            canvas.drawLine(size.width * line.x1, size.height * line.y1, size.width * line.x2, size.height * line.y2);
        }

        canvas.setFillColor(color);
        for (Circle circle : circles) {
            float x = size.width * circle.x;
            float y = size.height * circle.y;
            float radius = size.width * circle.radius;
            canvas.fillCircle(x, y, radius);
        }
        for (Rectangle rectangle : rectangles) {
            canvas.fillRect(size.width * rectangle.x, size.height * rectangle.y, size.width * rectangle.width,
                    size.height * rectangle.height);
        }
        for (float[] polygon : polygons) {
            Path path = canvas.createPath().moveTo(size.width * polygon[0], size.height * polygon[1]);
            for (int i = 2; i < polygon.length; i = i + 2) {
                path.lineTo(size.width * polygon[i], size.height * polygon[i + 1]);
            }
            canvas.fillPath(path.close());
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return Background.EMPTY;
    }
}

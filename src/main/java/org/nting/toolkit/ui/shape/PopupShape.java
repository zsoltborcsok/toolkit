package org.nting.toolkit.ui.shape;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import pythagoras.f.Point;

public class PopupShape extends PolygonShape {

    public PopupShape(float width, float height, int type, int padding) {
        super(Collections.emptyList());

        int shapeType = 0;
        if (type == 1 || type == 2 || type == 5 || type == 6) {
            shapeType = 7 - type;
        } else if (type == 0 || type == 4) {
            shapeType = type + 3;
        } else if (type == 3 || type == 7) {
            shapeType = type - 3;
        }

        points.addAll(getBasePoints(width, height, shapeType, padding));
    }

    private Collection<Point> getBasePoints(float width, float height, int shapeType, int padding) {
        List<Point> points = Lists.newLinkedList();
        if (shapeType == 1 || shapeType == 2 || shapeType == 5 || shapeType == 6) {
            points.add(new Point(0, 0));
            points.add(new Point(padding, 0));
            points.add(new Point(2 * padding, -padding));
            points.add(new Point(3 * padding, 0));
            points.add(new Point(width, 0));
            points.add(new Point(width, height));
            points.add(new Point(0, height));

            if (shapeType == 2 || shapeType == 5) {
                for (Point point : points) {
                    point.x = -point.x + width;
                }
            }
            if (shapeType == 6 || shapeType == 5) {
                for (Point point : points) {
                    point.y = -point.y + height;
                }
            }
        } else {
            points.add(new Point(0, 0));
            points.add(new Point(0, padding));
            points.add(new Point(-padding, 2 * padding));
            points.add(new Point(0, 3 * padding));
            points.add(new Point(0, height));
            points.add(new Point(width, height));
            points.add(new Point(width, 0));

            if (shapeType == 3 || shapeType == 4) {
                for (Point point : points) {
                    point.x = -point.x + width;
                }
            }
            if (shapeType == 7 || shapeType == 4) {
                for (Point point : points) {
                    point.y = -point.y + height;
                }
            }
        }

        return points;
    }
}

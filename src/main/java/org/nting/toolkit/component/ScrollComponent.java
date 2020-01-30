package org.nting.toolkit.component;

import org.nting.data.Property;
import org.nting.toolkit.Component;

import pythagoras.f.Dimension;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;

public abstract class ScrollComponent extends AbstractComponent {

    public enum ScrollBarPolicy {
        AS_NEEDED, NEVER, ALWAYS
    }

    public final Property<Float> overdriveX = createProperty("overdriveX", 0f);
    public final Property<Float> overdriveY = createProperty("overdriveY", 0f);

    public ScrollComponent(String... reLayoutPropertyNames) {
        super(reLayoutPropertyNames);
    }

    protected void checkOverdrive(Point startViewPosition, Dimension startSize, float x, float y) {
        if (0 < overdriveX.getValue()) {
            x += startSize.width - width.getValue();
        }
        if (0 < overdriveY.getValue()) {
            y += startSize.height - height.getValue();
        }

        Dimension viewPrefSize = getViewPrefSize();
        Point newViewPosition = startViewPosition.add(startSize.width, startSize.height).add(-x, -y);
        overdriveX.setValue(-shrinkIntervalToZero(startSize.width, viewPrefSize.width, newViewPosition.x));
        float v = -shrinkIntervalToZero(startSize.height, viewPrefSize.height, newViewPosition.y);
        overdriveY.setValue(v);
    }

    protected void cancelOverdrive() {
        overdriveX.setValue(0f);
        overdriveY.setValue(0f);
    }

    public abstract Component getView();

    public abstract Point getViewPosition();

    public abstract void setViewPosition(Point p);

    public abstract void scrollPage(boolean down);

    public abstract void scrollRectToVisible(Rectangle contentRect);

    protected abstract Dimension getViewPrefSize();

    private float shrinkIntervalToZero(float minValue, float maxValue, float value) {
        if (value < minValue) {
            return value - minValue;
        } else if (maxValue < value) {
            return value - maxValue;
        } else {
            return 0;
        }
    }
}

package org.nting.toolkit.layout;

import org.nting.data.Property;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.Component;
import org.nting.toolkit.animation.Easing;
import org.nting.toolkit.animation.Tween;
import org.nting.toolkit.component.AbstractComponent;
import org.nting.toolkit.util.SimpleMap;

import pythagoras.f.Dimension;
import pythagoras.f.Point;

public class PivotAnimationLayout implements LayoutManager {

    public static PivotAnimationLayout pivotLayout(LayoutManager mainLayoutManager) {
        return new PivotAnimationLayout(mainLayoutManager);
    }

    private final LayoutManager baseLayoutManager;

    private final SimpleMap<AbstractComponent, PivotInfo> childPivotInfoList = new SimpleMap<>();

    public PivotAnimationLayout(LayoutManager baseLayoutManager) {
        this.baseLayoutManager = baseLayoutManager;
    }

    /** Move child from the given position. */
    public PivotAnimationLayout moveChildXY(AbstractComponent child, float x, float y, int duration, Easing easing) {
        childPivotInfoList.put(child, new PivotInfo(child, x, y, duration, easing, false));
        return this;
    }

    /** Move child from the given X position. */
    public PivotAnimationLayout moveChildX(AbstractComponent child, float x, int duration, Easing easing) {
        childPivotInfoList.put(child, new PivotInfo(child, x, null, duration, easing, false));
        return this;
    }

    /** Move child from the given Y position. */
    public PivotAnimationLayout moveChildY(AbstractComponent child, float y, int duration, Easing easing) {
        childPivotInfoList.put(child, new PivotInfo(child, null, y, duration, easing, false));
        return this;
    }

    /** Translate child by the given offsets. */
    public PivotAnimationLayout translateChildXY(AbstractComponent child, float offsetX, float offsetY, int duration,
            Easing easing) {
        childPivotInfoList.put(child, new PivotInfo(child, offsetX, offsetY, duration, easing, true));
        return this;
    }

    /** Translate child by the given X offset. */
    public PivotAnimationLayout translateChildX(AbstractComponent child, float offsetX, int duration, Easing easing) {
        childPivotInfoList.put(child, new PivotInfo(child, offsetX, null, duration, easing, true));
        return this;
    }

    /** Translate child by the given Y offset. */
    public PivotAnimationLayout translateChildY(AbstractComponent child, float offsetY, int duration, Easing easing) {
        childPivotInfoList.put(child, new PivotInfo(child, null, offsetY, duration, easing, true));
        return this;
    }

    @Override
    public void layout(Component component) {
        baseLayoutManager.layout(component);

        for (AbstractComponent child : childPivotInfoList.keySet()) {
            if (child.getParent() == component) {
                PivotInfo pivotInfo = childPivotInfoList.get(child);
                if (!pivotInfo.isFinished()) {
                    Point position = child.getPosition();
                    float x = pivotInfo.getX(position.x);
                    float y = pivotInfo.getY(position.y);
                    setComponentPosition(child, x, y);
                } else {
                    childPivotInfoList.remove(child);
                }
            } else {
                childPivotInfoList.remove(child);
            }
        }
    }

    @Override
    public Dimension preferredLayoutSize(Component component) {
        return baseLayoutManager.preferredLayoutSize(component);
    }

    private static class PivotInfo {

        private final Float x;
        private final Float y;
        private final boolean relative;

        private final Property<Float> animationProperty = new ObjectProperty<>(0f);

        public PivotInfo(final AbstractComponent child, Float x, Float y, int duration, Easing easing,
                boolean relative) {
            this.x = x;
            this.y = y;
            this.relative = relative;

            child.addBehavior(new Tween<>(animationProperty, 0f, 1f, duration, easing));
            animationProperty.addValueChangeListener(event -> child.reLayout());
        }

        public boolean isFinished() {
            return 1f <= animationProperty.getValue();
        }

        public float getX(float targetX) {
            if (x == null) {
                return targetX;
            } else if (!relative) {
                return targetX * animationProperty.getValue() + x * (1 - animationProperty.getValue());
            } else {
                return targetX + x * (1 - animationProperty.getValue());
            }
        }

        public float getY(float targetY) {
            if (y == null) {
                return targetY;
            } else if (!relative) {
                return targetY * animationProperty.getValue() + y * (1 - animationProperty.getValue());
            } else {
                return targetY + y * (1 - animationProperty.getValue());
            }
        }
    }
}

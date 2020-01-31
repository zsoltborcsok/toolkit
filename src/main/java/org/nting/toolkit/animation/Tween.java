package org.nting.toolkit.animation;

import org.nting.data.Property;

public class Tween<T> extends Animation {

    private final Property<T> property;
    private final T fromValue;
    private final T toValue;

    public Tween(Property<T> property, T toValue, int duration) {
        this(property, property.getValue(), toValue, duration, null, 0);
    }

    public Tween(Property<T> property, T fromValue, T toValue, int duration) {
        this(property, fromValue, toValue, duration, null, 0);
    }

    public Tween(Property<T> property, T fromValue, T toValue, int duration, Easing easing) {
        this(property, fromValue, toValue, duration, easing, 0);
    }

    public Tween(Property<T> property, T fromValue, T toValue, int duration, Easing easing, int startDelay) {
        super(duration, easing, startDelay);
        this.property = property;
        this.fromValue = fromValue != null ? fromValue : property.getValue();
        this.toValue = toValue;
    }

    @SuppressWarnings({ "unchecked", "RedundantCast" })
    @Override
    protected void updateState(int animationTime) {
        if (getDuration() == 0) {
            if (animationTime < 0) {
                setPropertyValue(fromValue);
            } else {
                setPropertyValue(toValue);
            }
        } else if (toValue instanceof Integer) {
            int from = (Integer) fromValue;
            int diff = (Integer) toValue - (Integer) fromValue;
            setPropertyValue((T) (Integer) (from + mulDiv(diff, animationTime, getDuration())));
        } else if (toValue instanceof Long) {
            long from = (Long) fromValue;
            long diff = (Long) toValue - (Long) fromValue;
            setPropertyValue((T) (Long) (from + mulDiv(diff, animationTime, getDuration())));
        } else if (toValue instanceof Float) {
            float from = (Float) fromValue;
            float diff = (Float) toValue - (Float) fromValue;
            setPropertyValue((T) (Float) (from + mulDiv(diff, animationTime, getDuration())));
        } else if (toValue instanceof Double) {
            double from = (Double) fromValue;
            double diff = (Double) toValue - (Double) fromValue;
            setPropertyValue((T) (Double) (from + mulDiv(diff, animationTime, getDuration())));
        } else if (toValue instanceof Boolean) {
            if (animationTime * 2 < getDuration()) {
                setPropertyValue(fromValue);
            } else {
                setPropertyValue(toValue);
            }
        }
    }

    private void setPropertyValue(T value) {
        property.setValue(value);
    }

    private static int mulDiv(int f1, int f2, int f3) {
        return (int) ((long) f1 * f2 / f3);
    }

    private static long mulDiv(long f1, long f2, long f3) {
        return f1 * f2 / f3;
    }

    private static float mulDiv(float f1, float f2, float f3) {
        return (float) ((double) f1 * f2 / f3);
    }

    private static double mulDiv(double f1, double f2, double f3) {
        return f1 * f2 / f3;
    }
}

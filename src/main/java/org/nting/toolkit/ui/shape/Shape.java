package org.nting.toolkit.ui.shape;

import static org.nting.toolkit.util.ColorUtils.asString;

import com.google.common.base.MoreObjects;

import playn.core.Canvas;
import playn.core.Gradient;
import playn.core.Pattern;

@SuppressWarnings("unchecked")
public abstract class Shape<T extends Shape<T>> {

    protected int strokeColor = 0;
    protected float strokeWidth = 1.0f;
    protected int fillColor = 0;
    protected Pattern fillPattern = null;
    protected Gradient fillGradient = null;

    public T strokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        return (T) this;
    }

    public T strokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        return (T) this;
    }

    public T fillColor(int fillColor) {
        this.fillColor = fillColor;
        return (T) this;
    }

    public T fillGradient(Gradient fillGradient) {
        this.fillGradient = fillGradient;
        return (T) this;
    }

    public T fillPattern(Pattern fillPattern) {
        this.fillPattern = fillPattern;
        return (T) this;
    }

    public abstract void paint(Canvas canvas);

    @Override
    public final String toString() {
        return toStringHelper().toString();
    }

    protected MoreObjects.ToStringHelper toStringHelper() {
        return MoreObjects.toStringHelper(this).add("strokeColor", asString(strokeColor))
                .add("strokeWidth", strokeWidth).add("fillColor", asString(fillColor));
    }
}

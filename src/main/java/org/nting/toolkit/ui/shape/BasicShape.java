package org.nting.toolkit.ui.shape;

import com.google.common.base.MoreObjects;

import pythagoras.f.Dimension;

public abstract class BasicShape<T extends BasicShape<T>> extends Shape<T> {

    protected float width;
    protected float height;

    public T size(Dimension dimension) {
        return size(dimension.width, dimension.height);
    }

    @SuppressWarnings("unchecked")
    public T size(float width, float height) {
        this.width = width;
        this.height = height;
        return (T) this;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper().add("width", width).add("height", height);
    }
}

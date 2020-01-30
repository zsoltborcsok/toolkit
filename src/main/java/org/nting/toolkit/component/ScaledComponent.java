package org.nting.toolkit.component;

import org.nting.data.Property;
import org.nting.toolkit.ui.ComponentUI;

import playn.core.Canvas;

public abstract class ScaledComponent extends AbstractComponent {

    public final Property<Float> scaleX = createProperty("scaleX", 1f);
    public final Property<Float> scaleY = createProperty("scaleY", 1f);

    public ScaledComponent(String... reLayoutPropertyNames) {
        super(reLayoutPropertyNames);
        setClip(true);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setComponentUI(ComponentUI componentUI) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void doPaintChildren(Canvas canvas) {
        canvas.save();
        canvas.scale(scaleX.getValue(), scaleY.getValue());
        super.doPaintChildren(canvas);
        canvas.restore();
    }
}

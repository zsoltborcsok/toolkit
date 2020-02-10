package org.nting.toolkit.component;

import org.nting.data.Property;
import org.nting.toolkit.ui.ComponentUI;
import org.nting.toolkit.ui.stone.Stone;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class StoneComponent extends AbstractComponent {

    public final Property<Stone> stone = createProperty("stone", null);
    public final Property<Float> baselinePosition = createProperty("baselinePosition", -1f);

    public StoneComponent(Stone stone) {
        super("stone");

        this.stone.setValue(stone);
        setFocusable(false);
    }

    @SuppressWarnings("unchecked")
    public <COMPONENT extends StoneComponent> COMPONENT baselinePosition(float baselinePosition) {
        this.baselinePosition.setValue(baselinePosition);
        return (COMPONENT) this;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setComponentUI(ComponentUI componentUI) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Dimension getPreferredSize() {
        return stone.getValue().getPreferredSize();
    }

    @Override
    public void doPaintComponent(Canvas canvas) {
        stone.getValue().paint(canvas, getSize());
    }

    @Override
    public void update(float delta) {
        if (0 < getBehaviors().size()) {
            repaint();
        }
        super.update(delta);
    }

    @Override
    public float getBaselinePosition() {
        return baselinePosition.getValue();
    }
}

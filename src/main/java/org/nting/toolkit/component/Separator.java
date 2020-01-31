package org.nting.toolkit.component;

import static org.nting.toolkit.ui.Colors.DARK_GREY;

import org.nting.data.Property;
import org.nting.toolkit.ui.ComponentUI;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class Separator extends AbstractComponent {

    public final Property<Integer> color = createProperty("color", DARK_GREY);
    public final Property<Orientation> orientation = createProperty("orientation", null);
    public final Property<Integer> margin = createProperty("margin", 0);
    public final Property<Integer> padding = createProperty("padding", 0);

    public Separator(Orientation orientation, String... reLayoutPropertyNames) {
        super(reLayoutPropertyNames);

        this.orientation.setValue(orientation);
        setFocusable(false);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setComponentUI(ComponentUI componentUI) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Dimension getPreferredSize() {
        if (orientation.getValue() == Orientation.HORIZONTAL) {
            return new Dimension(0, 1 + 2 * padding.getValue());
        } else {
            return new Dimension(1 + 2 * padding.getValue(), 0);
        }
    }

    @Override
    public void doPaintComponent(Canvas canvas) {
        canvas.setFillColor(color.getValue());
        Dimension size = getSize();
        float margin = this.margin.getValue();
        float padding = this.padding.getValue();
        if (orientation.getValue() == Orientation.HORIZONTAL) {
            canvas.fillRect(margin, padding, size.width - (margin * 2), 1);
        } else {
            canvas.fillRect(padding, margin, 1, size.height - (margin * 2));
        }
    }
}

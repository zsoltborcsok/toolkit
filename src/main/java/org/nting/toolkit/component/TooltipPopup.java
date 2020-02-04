package org.nting.toolkit.component;

import static org.nting.toolkit.ui.Colors.DARK_GREY;

import org.nting.data.Property;
import org.nting.toolkit.Component;
import org.nting.toolkit.layout.LayoutManager;

import pythagoras.f.Rectangle;

public class TooltipPopup extends StandardPopup {

    public final Property<Integer> color = createProperty("color", DARK_GREY);

    public TooltipPopup(Alignment alignment, Orientation orientation) {
        super(alignment, orientation);

        setFocusable(false);
    }

    public TooltipPopup(Alignment alignment, Orientation orientation, LayoutManager layoutManager) {
        super(alignment, orientation, layoutManager);

        setFocusable(false);
    }

    @Override
    public void showRelativeTo(Component component) {
        showRelativeTo(component, 1); // Padding is not used.
    }

    @Override
    public void showRelativeTo(Rectangle rectangle) {
        showRelativeTo(rectangle.location(), rectangle.size(), 1); // Padding is not used.
    }
}

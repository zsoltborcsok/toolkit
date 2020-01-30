package org.nting.toolkit.component;

import org.nting.toolkit.Component;
import org.nting.toolkit.layout.LayoutManager;

import pythagoras.f.Rectangle;

public class WindowPopup extends StandardPopup {

    public WindowPopup(Alignment alignment, Orientation orientation) {
        super(alignment, orientation);
    }

    public WindowPopup(Alignment alignment, Orientation orientation, LayoutManager layoutManager) {
        super(alignment, orientation, layoutManager);
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

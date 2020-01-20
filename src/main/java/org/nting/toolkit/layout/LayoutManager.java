package org.nting.toolkit.layout;

import org.nting.toolkit.Component;

import pythagoras.f.Dimension;

public interface LayoutManager {

    /**
     * Layout the children of the component.
     */
    void layout(Component component);

    /**
     * Gets the preferred size of the component computed from preferred size children component and/or defined by the
     * layout.
     */
    Dimension preferredLayoutSize(Component component);

    default void setComponentSize(Component component, float width, float height) {
        component.setSize(width, height);
    }

    default void setComponentPosition(Component component, float x, float y) {
        component.setPosition(x, y);
    }
}

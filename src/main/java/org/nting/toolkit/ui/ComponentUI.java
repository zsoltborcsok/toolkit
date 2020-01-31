package org.nting.toolkit.ui;

import static org.nting.toolkit.ui.Colors.TRANSPARENT;

import org.nting.toolkit.Component;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public interface ComponentUI<C extends Component> {

    /** Add properties to the component if needed. */
    void initialize(C component);

    /** Remove the added properties. Component won't be rendered by this UI class anymore. */
    void terminate(C component);

    void paintComponent(C component, Canvas canvas);

    Dimension getPreferredSize(C component);

    void paintForeground(C component, Canvas canvas);

    default int getBackgroundColor(C component) {
        return TRANSPARENT;
    }

    boolean isComponentSupported(Component c);
}

package org.nting.toolkit.ui.style.material;

import static org.nting.toolkit.ui.Colors.WHITE;

import org.nting.toolkit.Component;
import org.nting.toolkit.component.StandardPopup;
import org.nting.toolkit.ui.ComponentUI;
import org.nting.toolkit.ui.shape.RectangleShape;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class MaterialStandardPopupUI<T extends StandardPopup> implements ComponentUI<T> {

    @Override
    public void initialize(T standardPopup) {
    }

    @Override
    public void terminate(T standardPopup) {
    }

    @Override
    public void paintComponent(T standardPopup, Canvas canvas) {
        Dimension size = standardPopup.getSize();
        new RectangleShape(0, 0, size.width, size.height).fillColor(WHITE).paint(canvas);
    }

    @Override
    public Dimension getPreferredSize(T standardPopup) {
        return standardPopup.getLayoutManager().preferredLayoutSize(standardPopup);
    }

    @Override
    public void paintForeground(T popup, Canvas canvas) {
    }

    @Override
    public boolean isComponentSupported(Component c) {
        return c instanceof StandardPopup;
    }
}

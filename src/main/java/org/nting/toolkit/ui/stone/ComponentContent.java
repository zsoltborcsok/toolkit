package org.nting.toolkit.ui.stone;

import org.nting.toolkit.Component;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class ComponentContent<C extends Component, T> extends Content {

    protected final C component;

    public ComponentContent(C component) {
        this.component = component;
    }

    public void setValue(T value) {
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        component.setSize(size.width, size.height);
        component.repaint();
        component.paint(canvas);
    }

    @Override
    public Dimension getPreferredSize() {
        return component.getPreferredSize();
    }

    public Dimension getSize() {
        return component.getSize();
    }
}

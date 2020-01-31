package org.nting.toolkit.layout;

import java.util.ArrayList;
import java.util.List;

import org.nting.toolkit.Component;

import pythagoras.f.Dimension;

public class VerticalLayout implements LayoutManager {

    private final float defaultWidth;

    public VerticalLayout() {
        this(-1);
    }

    public VerticalLayout(float width) {
        this.defaultWidth = width;
    }

    @Override
    public void layout(Component component) {
        float width = 0;
        float height = 0;

        List<Component> children = component.getComponents();
        List<Float> preferredHeights = new ArrayList<>(children.size());
        for (Component child : children) {
            Dimension preferredSize = child.getPreferredSize();

            setComponentPosition(child, 0, height);
            preferredHeights.add(preferredSize.height);
            height += preferredSize.height;

            if (preferredSize.width > width) {
                width = preferredSize.width;
            }
        }

        if (component.getSize().width > width) {
            width = component.getSize().width;
        }
        for (int i = 0; i < children.size(); i++) {
            setComponentSize(children.get(i), width, preferredHeights.get(i));
        }
    }

    @Override
    public Dimension preferredLayoutSize(Component component) {
        float width = defaultWidth > 0 ? defaultWidth : 0;
        float height = 0;

        List<Component> children = component.getComponents();
        for (Component child : children) {
            Dimension preferredSize = child.getPreferredSize();
            if (preferredSize.width > width) {
                width = preferredSize.width;
            }
            height += preferredSize.height;
        }

        return new Dimension(width, height);
    }
}

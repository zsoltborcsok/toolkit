package org.nting.toolkit.layout;

import java.util.ArrayList;
import java.util.List;

import org.nting.toolkit.Component;

import pythagoras.f.Dimension;

public class HorizontalLayout implements LayoutManager {

    private final float defaultHeight;
    private final boolean leftAlignWhenTooSmall;

    public HorizontalLayout() {
        this(true);
    }

    public HorizontalLayout(boolean leftAlignWhenTooSmall) {
        this(leftAlignWhenTooSmall, -1);
    }

    public HorizontalLayout(boolean leftAlignWhenTooSmall, float height) {
        this.defaultHeight = height;
        this.leftAlignWhenTooSmall = leftAlignWhenTooSmall;
    }

    @Override
    public void layout(Component component) {
        float width = 0;
        float height = 0;

        List<Component> children = component.getComponents();
        List<Float> preferredWidths = new ArrayList<>(children.size());
        for (Component child : children) {
            Dimension preferredSize = child.getPreferredSize();

            preferredWidths.add(preferredSize.width);
            width += preferredSize.width;

            if (preferredSize.height > height) {
                height = preferredSize.height;
            }
        }

        Dimension size = component.getSize();
        if (width <= size.width || leftAlignWhenTooSmall) {
            float x = 0;
            for (int i = 0; i < children.size(); i++) {
                setComponentPosition(children.get(i), x, 0);
                x += preferredWidths.get(i);
            }
        } else { // too small and right align
            float x = size.width;
            for (int i = children.size() - 1; 0 <= i; i--) {
                setComponentPosition(children.get(i), x - preferredWidths.get(i), 0);
                x -= preferredWidths.get(i);
            }
        }

        if (size.height > height) {
            height = size.height;
        }
        for (int i = 0; i < children.size(); i++) {
            setComponentSize(children.get(i), preferredWidths.get(i), height);
        }
    }

    @Override
    public Dimension preferredLayoutSize(Component component) {
        float width = 0;
        float height = defaultHeight > 0 ? defaultHeight : 0;

        List<Component> children = component.getComponents();
        for (Component child : children) {
            Dimension preferredSize = child.getPreferredSize();
            width += preferredSize.width;
            if (preferredSize.height > height) {
                height = preferredSize.height;
            }
        }

        return new Dimension(width, height);
    }
}

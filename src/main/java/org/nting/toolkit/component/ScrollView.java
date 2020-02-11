package org.nting.toolkit.component;

import java.util.List;

import org.nting.toolkit.Component;
import org.nting.toolkit.layout.LayoutManager;
import org.nting.toolkit.layout.VerticalLayout;

import playn.core.Canvas;
import pythagoras.f.Dimension;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;

/** Optimize children painting by ignoring painting of invisible children. */
public class ScrollView extends Panel {

    public static ScrollView createVerticalScrollView(List<? extends Component> components) {
        ScrollView scrollView = new ScrollView(new VerticalLayout());
        components.forEach(scrollView::addComponent);
        return scrollView;
    }

    public ScrollView(String... reLayoutPropertyNames) {
        super(reLayoutPropertyNames);
    }

    public ScrollView(LayoutManager layoutManager, String... reLayoutPropertyNames) {
        super(layoutManager, reLayoutPropertyNames);
    }

    public void doPaintChild(Component child, Canvas canvas) {
        Rectangle childBounds = child.getBounds();
        Point position = getPosition();
        childBounds.translate(position.x, position.y);

        Dimension parentSize = getParent().getSize();
        Rectangle visibleBounds = new Rectangle(0, 0, parentSize.width, parentSize.height);

        if (visibleBounds.intersects(childBounds)) {
            super.doPaintChild(child, canvas);
        } else {
            child.painted();
        }
    }
}

package org.nting.toolkit.ui.style.material;

import static org.nting.toolkit.ui.Colors.WHITE;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.BORDER_COLOR;
import static org.nting.toolkit.ui.style.material.SplitPanePropertyIds.COLOR;
import static org.nting.toolkit.ui.style.material.SplitPanePropertyIds.HOVER_COLOR;

import org.nting.toolkit.Component;
import org.nting.toolkit.component.Orientation;
import org.nting.toolkit.component.SplitPane;
import org.nting.toolkit.layout.LayoutManager;
import org.nting.toolkit.ui.ComponentUI;
import org.nting.toolkit.ui.shape.LineShape;
import org.nting.toolkit.ui.shape.RectangleShape;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class MaterialSplitPaneUI implements ComponentUI<SplitPane> {

    @Override
    public void initialize(SplitPane splitPane) {
        splitPane.setLayoutManager(new SplitPaneLayout());
        splitPane.createProperty(COLOR, BORDER_COLOR);
        splitPane.createProperty(HOVER_COLOR, WHITE);
    }

    @Override
    public void terminate(SplitPane splitPane) {
        splitPane.setLayoutManager(null);
        splitPane.removeProperty(COLOR);
        splitPane.removeProperty(HOVER_COLOR);
    }

    @Override
    public void paintComponent(SplitPane splitPane, Canvas canvas) {
        int dividerLocation = splitPane.getDividerLocation();
        if (splitPane.orientation.getValue() == Orientation.HORIZONTAL) {
            new LineShape(dividerLocation, 0, dividerLocation, splitPane.height.getValue())
                    .strokeColor(COLOR.getValueOf(splitPane)).paint(canvas);
        } else {
            new LineShape(0, dividerLocation, splitPane.width.getValue(), dividerLocation)
                    .strokeColor(COLOR.getValueOf(splitPane)).paint(canvas);
        }
    }

    @Override
    public void paintForeground(SplitPane splitPane, Canvas canvas) {
        if (splitPane.mouseOverSplit.getValue()) {
            int dividerLocation = splitPane.getDividerLocation();
            if (splitPane.orientation.getValue() == Orientation.HORIZONTAL) {
                float height = splitPane.height.getValue();
                new RectangleShape(dividerLocation + 1, 0, 6, height).fillColor(COLOR.getValueOf(splitPane))
                        .paint(canvas);
                new LineShape(dividerLocation + 3, (int) height / 2 - 10, dividerLocation + 3, (int) height / 2 + 10)
                        .strokeWidth(2).strokeColor(HOVER_COLOR.getValueOf(splitPane)).paint(canvas);
            } else {
                float width = splitPane.width.getValue();
                new RectangleShape(0, dividerLocation + 1, width, 6).fillColor(COLOR.getValueOf(splitPane))
                        .paint(canvas);
                new LineShape((int) width / 2 - 10, dividerLocation + 3, (int) width / 2 + 10, dividerLocation + 3)
                        .strokeWidth(2).strokeColor(HOVER_COLOR.getValueOf(splitPane)).paint(canvas);
            }
        }
    }

    @Override
    public Dimension getPreferredSize(SplitPane splitPane) {
        return splitPane.getLayoutManager().preferredLayoutSize(splitPane);
    }

    @Override
    public boolean isComponentSupported(Component c) {
        return c instanceof SplitPane;
    }

    public static class SplitPaneLayout implements LayoutManager {

        @Override
        public void layout(Component component) {
            SplitPane splitPane = (SplitPane) component;
            Component leftComponent = splitPane.getLeftComponent();
            Component rightComponent = splitPane.getRightComponent();
            int dividerLocation = splitPane.getDividerLocation();
            float width = splitPane.width.getValue();
            float height = splitPane.height.getValue();

            if (splitPane.orientation.getValue() == Orientation.HORIZONTAL) {
                setComponentPosition(leftComponent, 0, 0);
                setComponentSize(leftComponent, dividerLocation, height);

                setComponentPosition(rightComponent, dividerLocation + 1, 0);
                setComponentSize(rightComponent, width - dividerLocation - 1, height);

            } else {
                setComponentPosition(leftComponent, 0, 0);
                setComponentSize(leftComponent, width, dividerLocation);

                setComponentPosition(rightComponent, 0, dividerLocation + 1);
                setComponentSize(rightComponent, width, height - dividerLocation - 1);
            }
        }

        @Override
        public Dimension preferredLayoutSize(org.nting.toolkit.Component component) {
            Dimension prefSize = new Dimension(0, 0);
            if (component.getParent() != null) {
                prefSize.width = component.getParent().getSize().width;
                prefSize.height = component.getParent().getSize().height;
            }

            return prefSize;
        }
    }
}

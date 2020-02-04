package org.nting.toolkit.ui.style.material;

import static org.nting.toolkit.ui.Colors.TRANSPARENT;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.DISABLED_OPACITY_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.DIVIDER_OPACITY_COLOR;
import static org.nting.toolkit.ui.style.material.ScrollPanePropertyIds.SCROLLBAR_COLOR;
import static org.nting.toolkit.ui.style.material.ScrollPanePropertyIds.SLIDER_COLOR;
import static org.nting.toolkit.ui.style.material.ScrollPanePropertyIds.SLIDER_HOVER_COLOR;

import org.nting.toolkit.Component;
import org.nting.toolkit.component.ScrollPane;
import org.nting.toolkit.ui.ComponentUI;
import org.nting.toolkit.ui.shape.RectangleShape;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class MaterialScrollPaneUI implements ComponentUI<ScrollPane> {

    @Override
    public void initialize(ScrollPane scrollPane) {
        scrollPane.createProperty(SCROLLBAR_COLOR, TRANSPARENT);
        scrollPane.createProperty(SLIDER_COLOR, DIVIDER_OPACITY_COLOR);
        scrollPane.createProperty(SLIDER_HOVER_COLOR, DISABLED_OPACITY_COLOR);
    }

    @Override
    public void terminate(ScrollPane scrollPane) {
        scrollPane.removeProperty(SCROLLBAR_COLOR);
        scrollPane.removeProperty(SLIDER_COLOR);
        scrollPane.removeProperty(SLIDER_HOVER_COLOR);
    }

    @Override
    public void paintComponent(ScrollPane scrollPane, Canvas canvas) {
    }

    @Override
    public void paintForeground(ScrollPane scrollPane, Canvas canvas) {
        Dimension size = scrollPane.getSize();
        int scrollBarWidth = scrollPane.scrollBarWidth.getValue();

        if (scrollPane.vsbVisible.getValue()) {
            float vScrollBarHeight = scrollPane.vScrollBarHeight(size);
            new RectangleShape(size.width - scrollBarWidth, 0, scrollBarWidth, size.height)
                    .fillColor(SCROLLBAR_COLOR.getValueOf(scrollPane)).paint(canvas);

            int SLIDER_COLORValue = scrollPane.vSliderHovered() ? SLIDER_HOVER_COLOR.getValueOf(scrollPane)
                    : SLIDER_COLOR.getValueOf(scrollPane);
            new RectangleShape(size.width - scrollBarWidth, scrollPane.vSliderPosition(vScrollBarHeight),
                    scrollBarWidth, scrollPane.vSliderHeight(vScrollBarHeight)).fillColor(SLIDER_COLORValue)
                            .paint(canvas);
        }
        if (scrollPane.hsbVisible.getValue()) {
            float hScrollBarWidth = scrollPane.hScrollBarWidth(size);
            new RectangleShape(0, size.height - scrollBarWidth, size.width, scrollBarWidth)
                    .fillColor(SCROLLBAR_COLOR.getValueOf(scrollPane)).paint(canvas);

            int SLIDER_COLORValue = scrollPane.hSliderHovered() ? SLIDER_HOVER_COLOR.getValueOf(scrollPane)
                    : SLIDER_COLOR.getValueOf(scrollPane);
            new RectangleShape(scrollPane.hSliderPosition(hScrollBarWidth), size.height - scrollBarWidth,
                    scrollPane.hSliderWidth(hScrollBarWidth), scrollBarWidth).fillColor(SLIDER_COLORValue)
                            .paint(canvas);
        }
    }

    @Override
    public Dimension getPreferredSize(ScrollPane scrollPane) {
        return scrollPane.getLayoutManager().preferredLayoutSize(scrollPane);
    }

    @Override
    public boolean isComponentSupported(Component c) {
        return c instanceof ScrollPane;
    }
}

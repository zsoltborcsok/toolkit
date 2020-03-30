package org.nting.toolkit.ui.style.material;

import org.nting.toolkit.component.ListComponent;
import org.nting.toolkit.component.ListComponent.ListCellRenderer;
import org.nting.toolkit.ui.shape.RectangleShape;

import playn.core.Canvas;
import pythagoras.f.Dimension;

// Idea for indicating selection is coming from the Gmail web app, when reading pane is enabled.
// Although
public class MaterialListCellRenderer implements ListCellRenderer {

    private final int selectedBackground;
    private final int hoveredBackground;
    private final int focusedBackground;
    private final int focusedForeground;

    public MaterialListCellRenderer(int selectedBackground, int hoveredBackground, int focusedBackground,
            int focusedForeground) {
        this.selectedBackground = selectedBackground;
        this.hoveredBackground = hoveredBackground;
        this.focusedBackground = focusedBackground;
        this.focusedForeground = focusedForeground;
    }

    @Override
    public void paintCellBackground(Canvas canvas, Dimension size, boolean selected, boolean focused, boolean hovered,
            boolean even, ListComponent<?> listComponent) {
        if (selected && selectedBackground != 0) {
            new RectangleShape(0, 0, size.width, size.height).fillColor(selectedBackground).paint(canvas);
        } else if (hovered && hoveredBackground != 0) {
            new RectangleShape(0, 0, size.width, size.height).fillColor(hoveredBackground).paint(canvas);
        } else if (focused && focusedBackground != 0 && listComponent.focused.getValue()) {
            new RectangleShape(0, 0, size.width, size.height).fillColor(focusedBackground).paint(canvas);
        }
    }

    @Override
    public void paintCellForeground(Canvas canvas, Dimension size, boolean selected, boolean focused, boolean hovered,
            boolean even, ListComponent<?> listComponent) {
        if (focused) {
            new RectangleShape(0, 0, 4, size.height).fillColor(focusedForeground).paint(canvas);
        }
    }
}

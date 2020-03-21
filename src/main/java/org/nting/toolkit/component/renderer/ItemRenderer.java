package org.nting.toolkit.component.renderer;

import java.util.List;

import org.nting.toolkit.component.ListComponent;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public interface ItemRenderer<T> {

    void paint(ListComponent<T> listComponent, T item, Canvas canvas, Dimension size, boolean selected);

    /** Expected to provide the same height for each item except the selected one, which may differ. */
    Dimension getPreferredSize(ListComponent<T> listComponent, boolean selected);

    int search(String searchText, List<T> items);

    /**
     * Returns the item of the given match.
     */
    T highlightMatch(int index);
}

package org.nting.toolkit;

import java.util.List;

import org.nting.data.util.Pair;
import org.nting.toolkit.component.Alignment;
import org.nting.toolkit.component.Orientation;
import org.nting.toolkit.layout.LayoutManager;
import org.nting.toolkit.ui.ComponentUI;

import pythagoras.f.Dimension;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;

public interface Component {

    // region Position and Size
    void setSize(float width, float height);

    Dimension getSize();

    void setPosition(float x, float y);

    Point getPosition();

    Rectangle getBounds();
    // endregion

    // region Tooltip
    void setTooltipText(String text);

    String getTooltipText();

    void setTooltipLocation(Alignment alignment, Orientation orientation);

    Pair<Alignment, Orientation> getTooltipLocation();
    // endregion

    // region Component hierarchy
    void setParent(Component parent);

    Component getParent();

    void addComponent(Component child);

    void addComponent(Component child, Object constraints);

    <C extends Component> C componentAt(Object constraints);

    void removeComponent(Component child);

    List<Component> getComponents();

    void setLayoutConstraint(Component child, Object constraints);

    Object getLayoutConstraints(Component component);

    void setLayoutManager(LayoutManager layoutManager);

    LayoutManager getLayoutManager();
    // endregion

    // region Rendering
    void setComponentUI(ComponentUI<?> componentUI);

    ComponentUI<?> getComponentUI();

    void setVisible(boolean visible);

    boolean isVisible();

    /** Sets the dirty flag to true. */
    void dirty();

    /** Sets the dirty flag to false. */
    void painted();

    /** Indicates that the component needs repaint. */
    boolean isDirty();
    // endregion

    // region Focus
    void setFocusable(boolean focusable);

    boolean isFocusable();

    void setFocusNeutral(boolean focusNeutral);

    boolean isFocusNeutral();

    void requestFocus();
    // endregion

    // region extra features
    void setId(String id);

    String getId();

    void setClip(boolean clip);

    boolean isClip();

    default float getBaselinePosition() {
        return -1;
    }

    /**
     * Returns the number of found matches.
     */
    default int search(String searchText) {
        return 0;
    }

    default void highlightMatch(int index) {
    }
    // endregion
}

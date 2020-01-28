package org.nting.toolkit;

import java.util.List;
import java.util.function.BiConsumer;

import org.nting.data.Registration;
import org.nting.data.util.Pair;
import org.nting.toolkit.animation.Behavior;
import org.nting.toolkit.component.Alignment;
import org.nting.toolkit.component.Orientation;
import org.nting.toolkit.event.KeyEvent;
import org.nting.toolkit.event.KeyListener;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.layout.LayoutManager;
import org.nting.toolkit.ui.ComponentUI;

import playn.core.Canvas;
import pythagoras.f.Dimension;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;

public interface Component {

    // region Position and Size
    void setSize(float width, float height);

    Dimension getSize();

    void setPosition(float x, float y);

    Point getPosition();

    default Rectangle getBounds() {
        return new Rectangle(getPosition(), getSize());
    }
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

    boolean isAttached();

    void addComponent(Component child);

    void addComponent(Component child, Object constraints);

    <C extends Component> C componentAt(Object constraints);

    void removeComponent(Component child);

    List<Component> getComponents();

    void setLayoutConstraint(Component child, Object constraints);

    Object getLayoutConstraints(Component component);

    void setLayoutManager(LayoutManager layoutManager);

    LayoutManager getLayoutManager();

    void reLayout();

    @SuppressWarnings({ "rawtypes", "unchecked" })
    default Dimension getPreferredSize() {
        ComponentUI componentUI = getComponentUI();
        if (componentUI != null) {
            return componentUI.getPreferredSize(this);
        } else {
            return getLayoutManager().preferredLayoutSize(this);
        }
    }
    // endregion

    // region Rendering
    void paint(Canvas canvas);

    @SuppressWarnings("rawtypes")
    void setComponentUI(ComponentUI componentUI);

    @SuppressWarnings("rawtypes")
    ComponentUI getComponentUI();

    void setVisible(boolean visible);

    boolean isVisible();

    /** Sets the dirty flag to true. */
    void repaint();

    /** Sets the dirty flag to false. */
    void painted();

    /** Indicates that the component needs repaint. */
    boolean isDirty();

    default void update(float delta) {
        getComponents().forEach(child -> child.update(delta));
    }
    // endregion

    // region Focus
    void setFocusable(boolean focusable);

    boolean isFocusable();

    void setFocusNeutral(boolean focusNeutral);

    boolean isFocusNeutral();

    void requestFocus();
    // endregion

    // region Event handling
    Registration addKeyListener(KeyListener keyListener);

    void fireKeyEvent(BiConsumer<KeyListener, KeyEvent> keyListenerMethod, KeyEvent keyEvent);

    Registration addMouseListener(MouseListener mouseListener);

    <E extends MouseEvent> void fireMouseEvent(BiConsumer<MouseListener, E> mouseListenerMethod, E mouseEvent);
    // endregion

    // region extra features
    void setId(Object id);

    Object getId();

    void setClip(boolean clip);

    boolean isClip();

    void addBehavior(Behavior behavior);

    void removeBehaviorOnAnyConsumedEvent(Behavior behavior);

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

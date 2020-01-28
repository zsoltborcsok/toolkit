package org.nting.toolkit;

import org.nting.toolkit.ui.ComponentUI;

import playn.core.Canvas;
import playn.core.PlayN;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

public interface PaintableComponent extends Component {

    default boolean renderingOptimisation() {
        return true;
    }

    @Override
    default void paint(Canvas canvas) {
        if (!isVisible()) {
            return;
        }

        if (isClip()) {
            canvas.save();
            Dimension size = getSize();
            canvas.clipRect(-1, -1, size.width + 2, size.height + 2);
        }
        try {
            if (renderingOptimisation()) {
                if (isDirty()) {
                    doPaintComponent(canvas);
                    doLayout();
                    getComponents().forEach(Component::repaint);
                }
                doPaintChildren(canvas);
                if (isDirty()) {
                    doPaintForeground(canvas);
                }
            } else {
                doPaintComponent(canvas);
                doLayout();
                doPaintChildren(canvas);
                doPaintForeground(canvas);
            }
        } catch (RuntimeException e) {
            PlayN.log(getClass()).error(e.getMessage(), e);
            setVisible(false);
        } finally {
            if (isClip()) {
                canvas.restore();
            }
        }

        painted();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    default void doPaintComponent(Canvas canvas) {
        ComponentUI componentUI = getComponentUI();
        if (componentUI != null) {
            componentUI.paintComponent(this, canvas);
        }
    }

    default void doLayout() {
        if (getLayoutManager() != null) {
            getLayoutManager().layout(this);
        }
    }

    default void doPaintChildren(Canvas canvas) {
        getComponents().forEach(child -> doPaintChild(child, canvas));
    }

    default void doPaintChild(Component child, Canvas canvas) {
        Point childPosition = child.getPosition();
        canvas.translate(childPosition.x, childPosition.y);
        child.paint(canvas);
        canvas.translate(-childPosition.x, -childPosition.y);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    default void doPaintForeground(Canvas canvas) {
        ComponentUI componentUI = getComponentUI();
        if (componentUI != null) {
            componentUI.paintForeground(this, canvas);
        }
    }
}

package org.nting.toolkit.event;

import static org.nting.toolkit.ToolkitServices.toolkitManager;
import static org.nting.toolkit.event.MouseEvent.MouseButton.BUTTON_LEFT;
import static pythagoras.f.MathUtil.isWithin;

import org.nting.toolkit.Component;
import org.nting.toolkit.util.ToolkitUtils;

import playn.core.Pointer;
import pythagoras.f.Point;

public class PointerDispatcher extends MouseDispatcher implements Pointer.Listener {

    private Pointer.Event lastEvent;
    private Pointer.Event lastPointerStartEvent;
    private boolean isLongPress;

    @Override
    public void onPointerStart(Pointer.Event event) {
        lastMousePosition.set(event.x(), event.y());
        Component source = ToolkitUtils.getDeepestComponentInToolkit(event.x(), event.y());
        Point sourcePosition = ToolkitUtils.getRootPosition(source);

        MouseEvent mouseEvent = newMouseEvent(source, event.time(), event.x() - sourcePosition.x,
                event.y() - sourcePosition.y, BUTTON_LEFT);
        fireMouseEnteredExited(mouseEvent);

        mouseEvent = newMouseEvent(source, event.time(), event.x() - sourcePosition.x, event.y() - sourcePosition.y,
                BUTTON_LEFT);
        fireMousePressed(mouseEvent);

        lastEvent = event;
        lastPointerStartEvent = event;
        isLongPress = true;
    }

    @Override
    public void onPointerEnd(Pointer.Event event) {
        Component source = ToolkitUtils.getDeepestComponentInToolkit(event.x(), event.y());
        Point sourcePosition = ToolkitUtils.getRootPosition(source);

        MouseEvent mouseEvent = newMouseEvent(source, event.time(), event.x() - sourcePosition.x,
                event.y() - sourcePosition.y, BUTTON_LEFT);
        fireMouseReleasedAndClicked(mouseEvent);

        mouseEvent = newMouseEvent(toolkitManager().root(), event.time(), event.x() - sourcePosition.x,
                event.y() - sourcePosition.y, BUTTON_LEFT);
        fireMouseEnteredExited(mouseEvent);

        lastEvent = null;
        lastPointerStartEvent = null;
        currentComponent = source;
        lastMousePress = null;
        lastDraggedComponent = null;
    }

    @Override
    public void onPointerDrag(Pointer.Event event) {
        lastMousePosition.set(event.x(), event.y());
        if (lastEvent != null && lastPointerStartEvent != null && !isLongPress(event)) {
            Component source = ToolkitUtils.getDeepestComponentInToolkit(lastPointerStartEvent.x(),
                    lastPointerStartEvent.y());
            Point sourcePosition = ToolkitUtils.getRootPosition(source);
            MouseMotionEvent mouseEvent = newMouseMotionEvent(source, event.time(), event.x() - sourcePosition.x,
                    event.y() - sourcePosition.y, event.x() - lastEvent.x(), event.y() - lastEvent.y(), BUTTON_LEFT);
            fireMouseDragged(mouseEvent);
        }

        lastEvent = event;
    }

    private boolean isLongPress(Pointer.Event event) {
        if (isLongPress) {
            float x0 = event.x() - lastPointerStartEvent.x();
            float y0 = event.y() - lastPointerStartEvent.y();
            isLongPress = isWithin(x0, -3, 3) && isWithin(y0, -3, 3);
        }
        return isLongPress;
    }

    @Override
    public void onPointerCancel(Pointer.Event event) {
        // Pending: fireMouseDragCanceled(...)?
    }
}

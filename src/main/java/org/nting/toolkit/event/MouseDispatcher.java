package org.nting.toolkit.event;

import static org.nting.toolkit.ToolkitServices.toolkitManager;
import static org.nting.toolkit.event.MouseEvent.MouseButton.BUTTON_LEFT;

import java.util.function.BiConsumer;

import org.nting.toolkit.Component;
import org.nting.toolkit.ToolkitRunnable;
import org.nting.toolkit.event.MouseEvent.MouseButton;
import org.nting.toolkit.util.ToolkitUtils;

import playn.core.Mouse.ButtonEvent;
import playn.core.Mouse.Listener;
import playn.core.Mouse.MotionEvent;
import playn.core.Mouse.WheelEvent;
import playn.core.PlayN;
import pythagoras.f.Point;

public class MouseDispatcher implements Listener {

    protected Component currentComponent;
    protected MouseEvent lastMousePress;
    protected MouseEvent lastMouseLongPress;
    protected MouseEvent lastMouseClick;
    private ToolkitRunnable longPressRunnable;
    protected Component lastDraggedComponent; // Mouse drag should go to originally dragged component...
    protected final Point lastMousePosition = new Point(0, 0);
    private boolean updatePopupsConsumesMousePressedAndClicked = false;

    public MouseDispatcher() {
        currentComponent = toolkitManager().root();
    }

    @Override
    public void onMouseDown(ButtonEvent event) {
        Component source = ToolkitUtils.getDeepestComponentInToolkit(event.x(), event.y());
        Point sourcePosition = ToolkitUtils.getRootPosition(source);

        MouseEvent mouseEvent = newMouseEvent(source, event.time(), event.x() - sourcePosition.x,
                event.y() - sourcePosition.y, MouseButton.fromInt(event.button()));

        fireMousePressed(mouseEvent);
    }

    @Override
    public void onMouseUp(ButtonEvent event) {
        Component source = ToolkitUtils.getDeepestComponentInToolkit(event.x(), event.y());
        Point sourcePosition = ToolkitUtils.getRootPosition(source);

        MouseEvent mouseEvent = newMouseEvent(source, event.time(), event.x() - sourcePosition.x,
                event.y() - sourcePosition.y, MouseButton.fromInt(event.button()));

        fireMouseReleasedAndClicked(mouseEvent);
        lastMousePress = null;
        lastDraggedComponent = null;
    }

    @Override
    public void onMouseMove(MotionEvent event) {
        lastMousePosition.set(event.x(), event.y());
        if (lastMousePress == null) {
            Component source = ToolkitUtils.getDeepestComponentInToolkit(event.x(), event.y());
            Point sourcePosition = ToolkitUtils.getRootPosition(source);

            MouseMotionEvent mouseEvent = newMouseMotionEvent(source, event.time(), event.x() - sourcePosition.x,
                    event.y() - sourcePosition.y, event.dx(), event.dy());
            fireMouseEnteredExited(mouseEvent);

            mouseEvent = newMouseMotionEvent(source, event.time(), event.x() - sourcePosition.x,
                    event.y() - sourcePosition.y, event.dx(), event.dy());
            fireMouseMoved(mouseEvent);
        } else {
            Component source = ToolkitUtils.getDeepestComponentAt(lastMousePress.getSource(), lastMousePress.getX(),
                    lastMousePress.getY());
            Point sourcePosition = ToolkitUtils.getRootPosition(source);
            MouseMotionEvent mouseEvent = newMouseMotionEvent(source, event.time(), event.x() - sourcePosition.x,
                    event.y() - sourcePosition.y, event.dx(), event.dy(), lastMousePress.getButton());

            fireMouseDragged(mouseEvent);
        }

    }

    @Override
    public void onMouseWheelScroll(WheelEvent event) {
        float velocity = event.velocity();
        if (velocity <= -120 || 120 <= velocity) {
            velocity /= -120;
        }
        MouseEvent mouseEvent = newMouseEvent(currentComponent, event.time(), velocity);

        fireMouseWheelScroll(mouseEvent);
        if (mouseEvent.isConsumed() && lastMousePress == null) {
            onMouseMove(new MotionEvent.Impl(null, event.time(), lastMousePosition.x, lastMousePosition.y, 0, 0));
        }
    }

    public void onDialogOpen() {
        Component source = ToolkitUtils.getDeepestComponentInToolkit(lastMousePosition.x(), lastMousePosition.y());
        Point sourcePosition = ToolkitUtils.getRootPosition(source);

        MouseMotionEvent mouseEvent = newMouseMotionEvent(source, 0, lastMousePosition.x() - sourcePosition.x,
                lastMousePosition.y() - sourcePosition.y, 0, 0);
        fireMouseEnteredExited(mouseEvent);
    }

    public void updatePopupsConsumesMousePressedAndClicked() {
        updatePopupsConsumesMousePressedAndClicked = true;
    }

    public Point getLastMousePosition(Component component) {
        Point position = ToolkitUtils.getRootPosition(component);
        return new Point(lastMousePosition.x - position.x, lastMousePosition.y - position.y);
    }

    protected MouseEvent newMouseEvent(Component source, double time, float x, float y, MouseButton button) {
        return toolkitManager().keyDispatcher().updateModifiers(new MouseEvent(source, time, x, y, button));
    }

    protected MouseEvent newMouseEvent(Component source, double time, float velocity) {
        return toolkitManager().keyDispatcher().updateModifiers(new MouseEvent(source, time, velocity));
    }

    protected MouseMotionEvent newMouseMotionEvent(Component source, double time, float x, float y, float dx,
            float dy) {
        KeyDispatcher keyDispatcher = toolkitManager().keyDispatcher();
        return keyDispatcher.updateModifiers(new MouseMotionEvent(source, time, x, y, dx, dy));
    }

    protected MouseMotionEvent newMouseMotionEvent(Component source, double time, float x, float y, float dx, float dy,
            MouseButton button) {
        KeyDispatcher keyDispatcher = toolkitManager().keyDispatcher();
        return keyDispatcher.updateModifiers(new MouseMotionEvent(source, time, x, y, dx, dy, button));
    }

    protected void fireMousePressed(MouseEvent mouseEvent) {
        final MouseEvent originalEvent = new MouseEvent(mouseEvent);
        updatePopupsConsumesMousePressedAndClicked = false;
        // TODO toolkitManager().root().updatePopups(mouseEvent.getSource());
        if (updatePopupsConsumesMousePressedAndClicked) {
            return;
        }

        if (mouseEvent.getButton() == BUTTON_LEFT) {
            toolkitManager().keyDispatcher().requestFocus(mouseEvent.getSource());
        }

        while (true) {
            Component source = mouseEvent.getSource();
            fireMouseEvent(source, MouseListener::mousePressed, mouseEvent);

            if (!mouseEvent.isConsumed() && source.getParent() != null) {
                Point position = source.getPosition();
                mouseEvent.translate(position.x, position.y);
                mouseEvent.setSource(source.getParent());
            } else {
                break;
            }
        }

        lastMousePress = mouseEvent;
        toolkitManager().schedule(longPressRunnable = new ToolkitRunnable(500) {
            @Override
            public void run() {
                fireMouseLongPressed(originalEvent);
            }
        });
    }

    protected void fireMouseLongPressed(MouseEvent mouseEvent) {
        while (true) {
            Component source = mouseEvent.getSource();
            fireMouseEvent(source, MouseListener::mouseLongPressed, mouseEvent);

            if (!mouseEvent.isConsumed() && source.getParent() != null) {
                Point position = source.getPosition();
                mouseEvent.translate(position.x, position.y);
                mouseEvent.setSource(source.getParent());
            } else {// last consumed mouse long press
                lastMouseLongPress = mouseEvent;
                break;
            }
        }
    }

    private void fireMouseWheelScroll(MouseEvent mouseEvent) {
        while (true) {
            Component source = mouseEvent.getSource();
            fireMouseEvent(source, MouseListener::mouseWheelScroll, mouseEvent);

            if (!mouseEvent.isConsumed() && source.getParent() != null) {
                mouseEvent.setSource(source.getParent());
            } else {
                break;
            }
        }
    }

    protected void fireMouseReleasedAndClicked(MouseEvent mouseEvent) {
        MouseEvent originalEvent = new MouseEvent(mouseEvent);

        while (true) {
            Component source = mouseEvent.getSource();
            fireMouseEvent(source, MouseListener::mouseReleased, mouseEvent);

            if (!mouseEvent.isConsumed() && source.getParent() != null) {
                Point position = source.getPosition();
                mouseEvent.translate(position.x, position.y);
                mouseEvent.setSource(source.getParent());
            } else {
                break;
            }
        }

        if (lastDraggedComponent != null && lastDraggedComponent != mouseEvent.getSource()) {
            // Ensure that last mouse drag handler gets release event as well!
            lastDraggedComponent.fireMouseEvent(MouseListener::mouseReleased, mouseEvent);
        }

        MouseEvent lastMouseRelease = mouseEvent;
        if (lastMousePress == null) {
            return;
        }

        if (lastMousePress.getButton() == lastMouseRelease.getButton()) {
            if (lastMousePress.getSource() == lastMouseRelease.getSource() && mouseEvent.getButton() == BUTTON_LEFT) {
                Component deepestComponentAtLastMousePress = ToolkitUtils.getDeepestComponentAt(
                        lastMousePress.getSource(), lastMousePress.getX(), lastMousePress.getY());
                if (deepestComponentAtLastMousePress == originalEvent.getSource()
                        && (lastMouseLongPress == null || lastMouseLongPress.getTime() != lastMousePress.getTime())) {
                    mouseEvent = originalEvent;
                    if (lastMouseClick != null) {
                        mouseEvent.updateClickCount(lastMouseClick);
                    }

                    while (true) {
                        Component source = mouseEvent.getSource();
                        fireMouseEvent(source, MouseListener::mouseClicked, mouseEvent);

                        if (!mouseEvent.isConsumed() && source.getParent() != null) {
                            Point position = source.getPosition();
                            mouseEvent.translate(position.x, position.y);
                            mouseEvent.setSource(source.getParent());
                        } else {
                            break;
                        }
                    }

                    lastMouseClick = mouseEvent;
                    if (!lastMousePress.isConsumed() && !lastMouseRelease.isConsumed() && !mouseEvent.isConsumed()) {
                        toolkitManager().keyDispatcher().checkForcedFocus();
                    }
                }
            } else {// Ensure that last mouse press handler gets release event as well!
                mouseEvent = originalEvent;

                Point position = ToolkitUtils.getRootPosition(mouseEvent.getSource());
                mouseEvent.translate(position.x, position.y);
                position = ToolkitUtils.getRootPosition(lastMousePress.getSource());
                mouseEvent.translate(-position.x, -position.y);
                mouseEvent.setSource(lastMousePress.getSource());

                lastMousePress.getSource().fireMouseEvent(MouseListener::mouseReleased, mouseEvent);
            }
        }

        if (longPressRunnable != null) {
            longPressRunnable.cancel();
            longPressRunnable = null;
        }
    }

    protected void fireMouseEnteredExited(MouseEvent mouseEvent) {
        if (!ToolkitUtils.isDescendingFrom(currentComponent, toolkitManager().root())) {
            currentComponent = toolkitManager().root();
        }

        Component source = mouseEvent.getSource();
        if (source == currentComponent || source == null) {
            return;
        } else if (ToolkitUtils.isDescendingFrom(source, currentComponent)) {
            Component component = source;
            while (component != currentComponent) {
                mouseEvent.setSource(component);
                component.fireMouseEvent(MouseListener::mouseEntered, mouseEvent);
                component = component.getParent();
            }
        } else if (ToolkitUtils.isDescendingFrom(currentComponent, source)) {
            Component component = currentComponent;
            while (component != source) {
                mouseEvent.setSource(component);
                component.fireMouseEvent(MouseListener::mouseExited, mouseEvent);
                component = component.getParent();
            }
        } else {
            Component commonParent = currentComponent;
            while (commonParent != null && !ToolkitUtils.isDescendingFrom(source, commonParent)) {
                mouseEvent.setSource(commonParent);
                commonParent.fireMouseEvent(MouseListener::mouseExited, mouseEvent);
                commonParent = commonParent.getParent();
            }

            Component component = source;
            while (component != commonParent) {
                mouseEvent.setSource(component);
                component.fireMouseEvent(MouseListener::mouseEntered, mouseEvent);
                component = component.getParent();
            }
        }

        currentComponent = source;
    }

    protected void fireMouseDragged(MouseMotionEvent mouseEvent) {
        if (lastMousePress != null && lastMousePress.getButton() == BUTTON_LEFT) {
            if (lastDraggedComponent != null) {
                Point positionLast = ToolkitUtils.getRootPosition(lastDraggedComponent);
                Point positionSource = ToolkitUtils.getRootPosition(mouseEvent.getSource());
                mouseEvent.translate(positionSource.x - positionLast.x, positionSource.y - positionLast.y);
                mouseEvent.setSource(lastDraggedComponent);
                lastDraggedComponent.fireMouseEvent(MouseListener::mouseDragged, mouseEvent);
            } else {
                while (true) {
                    Component source = mouseEvent.getSource();
                    fireMouseEvent(source, MouseListener::mouseDragged, mouseEvent);

                    if (!mouseEvent.isConsumed() && source.getParent() != null) {
                        Point position = source.getPosition();
                        mouseEvent.translate(position.x, position.y);
                        mouseEvent.setSource(source.getParent());
                    } else {
                        if (mouseEvent.isConsumed()) {
                            lastDraggedComponent = source;
                        }
                        break;
                    }
                }
            }
        }

        if (longPressRunnable != null && mouseEvent.isConsumed()) {
            longPressRunnable.cancel();
            longPressRunnable = null;
        }
    }

    protected void fireMouseMoved(MouseMotionEvent mouseEvent) {
        while (true) {
            Component source = mouseEvent.getSource();
            fireMouseEvent(source, MouseListener::mouseMoved, mouseEvent);

            if (!mouseEvent.isConsumed() && source.getParent() != null) {
                Point position = source.getPosition();
                mouseEvent.translate(position.x, position.y);
                mouseEvent.setSource(source.getParent());
            } else {
                break;
            }
        }
    }

    private <E extends MouseEvent> void fireMouseEvent(Component source,
            BiConsumer<MouseListener, E> mouseListenerMethod, E mouseEvent) {
        try {
            source.fireMouseEvent(mouseListenerMethod, mouseEvent);
        } catch (RuntimeException e) {
            PlayN.log(getClass()).error(e.getMessage(), e);
        }
    }
}

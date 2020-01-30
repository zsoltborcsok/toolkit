package org.nting.toolkit.event;

import static org.nting.toolkit.ToolkitServices.toolkitManager;
import static org.nting.toolkit.util.ToolkitUtils.isDescendingFrom;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

import org.nting.data.Property;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.Component;
import org.nting.toolkit.component.AbstractComponent;
import org.nting.toolkit.component.FieldComponent;
import org.nting.toolkit.util.ToolkitUtils;

import playn.core.Key;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.Listener;
import playn.core.Keyboard.TypedEvent;
import playn.core.Modifiers;
import playn.core.PlayN;

public class KeyDispatcher implements Listener {

    private final Property<Component> focusOwner = new ObjectProperty<>(null);
    private final Property<FieldComponent> forcedFocusOwner = new ObjectProperty<>(null);
    private KeyEvent lastKeyPressed;
    private Component previousFocusOwner = null;

    // These modifiers are used only for the mouse events.
    private boolean controlPressed = false;
    private boolean altPressed = false;
    private boolean shiftPressed = false;

    public KeyDispatcher() {
        focusOwner.addValueChangeListener(event -> {
            Component prevFocusedComponent = event.getPrevValue();
            if (prevFocusedComponent instanceof AbstractComponent) {
                Property<Boolean> focused = ((AbstractComponent) prevFocusedComponent).focused;
                ((ObjectProperty<Boolean>) focused).forceValue(false);
            }

            Component currentFocusedComponent = event.getValue();
            if (currentFocusedComponent instanceof AbstractComponent) {
                Property<Boolean> focused = ((AbstractComponent) currentFocusedComponent).focused;
                ((ObjectProperty<Boolean>) focused).forceValue(true);
            }
        });
    }

    public void requestFocus(Component component) {
        if (component != null && !ToolkitUtils.isChildOfToolkitRoot(component)) {
            return;
        }

        while (component != null) {
            if (component.isFocusNeutral()) {
                break;
            } else if (component.isFocusable()) {
                previousFocusOwner = focusOwner.getValue();
                focusOwner.setValue(component);
                break;
            } else {
                component = component.getParent();
            }
        }

        if (component == null) {
            previousFocusOwner = null;
        } else {
            toolkitManager().root().updatePopups(component);
            ToolkitUtils.scrollComponentToVisible(component);
        }
    }

    public void checkForcedFocus() {
        Component theFocusOwner = focusOwner.getValue();
        Component previousForcedFocusOwnerValue = forcedFocusOwner.getValue();
        if (theFocusOwner instanceof FieldComponent) {
            if (previousForcedFocusOwnerValue != theFocusOwner) {
                if (previousForcedFocusOwnerValue != null) {
                    forcedFocusOwner.setValue((FieldComponent) theFocusOwner);
                } else if (previousFocusOwner == theFocusOwner) {
                    forcedFocusOwner.setValue((FieldComponent) theFocusOwner);
                } else {
                    forcedFocusOwner.setValue(null);
                }
            }
        } else {
            forcedFocusOwner.setValue(null);
        }
    }

    public void componentIsRemoved(Component component) {
        if (isDescendingFrom(focusOwner.getValue(), component)) {
            requestFocus(component.getParent());
            if (previousFocusOwner == null) { // The focusOwner hasn't changed.
                focusOwner.setValue(null);
                requestFocus(getFocusable(true));
            }
        }
    }

    @Override
    public void onKeyDown(Event event) {
        Key key = event.key();
        if (key == Key.CONTROL) {
            controlPressed = true;
        } else if (key == Key.ALT) {
            altPressed = true;
        } else if (key == Key.SHIFT) {
            shiftPressed = true;
        }

        KeyEvent keyEvent = newKeyEvent(event.time(), key, event.modifiers());
        fireKeyEvent(KeyListener::keyPressed, keyEvent);
        lastKeyPressed = keyEvent;

        if (key == Key.BACKSPACE) {
            event.flags().setPreventDefault(true);
        } else if (key == Key.F10) {
            event.flags().setPreventDefault(true);
        } else if (key == Key.TAB) {
            event.flags().setPreventDefault(true);
            requestFocus(getFocusable(!event.modifiers().isShiftPressed()));
        } else if (event.modifiers().isAltPressed() && key == Key.LEFT) {
            event.flags().setPreventDefault(true);
        } else if (keyEvent.isConsumed() && key == Key.BACK) {
            event.flags().setPreventDefault(true);
        } else if (event.modifiers().isCtrlPressed() && key == Key.F && !event.modifiers().isAltPressed()) {
            event.flags().setPreventDefault(true);
        }
    }

    @Override
    public void onKeyUp(Event event) {
        Key key = event.key();
        if (key == Key.CONTROL) {
            controlPressed = false;
        } else if (key == Key.ALT) {
            altPressed = false;
        } else if (key == Key.SHIFT) {
            shiftPressed = false;
        }

        fireKeyEvent(KeyListener::keyReleased, newKeyEvent(event.time(), key, event.modifiers()));
        lastKeyPressed = null;
    }

    @Override
    public void onKeyTyped(TypedEvent event) {
        if (lastKeyPressed == null || !lastKeyPressed.isConsumed()) {
            fireKeyEvent(KeyListener::keyTyped, newKeyEvent(event.time(), event.typedChar()));
        }
    }

    public Property<Component> getFocusOwner() {
        return focusOwner;
    }

    public Property<FieldComponent> getForcedFocusOwner() {
        return forcedFocusOwner;
    }

    public MouseEvent updateModifiers(MouseEvent mouseEvent) {
        return mouseEvent.setModifiers(controlPressed, altPressed, shiftPressed);
    }

    public MouseMotionEvent updateModifiers(MouseMotionEvent mouseMotionEvent) {
        return mouseMotionEvent.setModifiers(controlPressed, altPressed, shiftPressed);
    }

    private KeyEvent newKeyEvent(double time, Key keyCode, Modifiers modifiers) {
        return new KeyEvent(focusOwner.getValue(), time, keyCode).setModifiers(modifiers.isCtrlPressed(),
                modifiers.isAltPressed(), modifiers.isShiftPressed());
    }

    private KeyEvent newKeyEvent(double time, char keyChar) {
        return new KeyEvent(focusOwner.getValue(), time, keyChar);
    }

    private Component getFocusable(boolean next) {
        Component componentToFocus = null;
        Component firstFocusable = null;
        boolean nextFocusable = false;

        Component theFocusOwner = getCurrentFocusOwner();
        Component focusCycleRoot = getFocusCycleRoot(theFocusOwner);
        List<Component> allComponents = ToolkitUtils.getAllComponents(focusCycleRoot);
        if (!next) {
            Collections.reverse(allComponents);
        }

        for (Component component : allComponents) {
            if (component.isFocusable()) {
                if (theFocusOwner == null) {
                    componentToFocus = component;
                    break;
                } else if (component == theFocusOwner) {
                    nextFocusable = true;
                } else if (nextFocusable) {
                    componentToFocus = component;
                    break;
                } else if (firstFocusable == null) {
                    firstFocusable = component;
                }
            }
        }

        if (componentToFocus == null) {
            componentToFocus = firstFocusable;
        }

        return componentToFocus;
    }

    private Component getCurrentFocusOwner() {
        Component root = toolkitManager().root();
        Component theFocusOwner = focusOwner.getValue();
        for (Component parent = theFocusOwner; parent != null; parent = parent.getParent()) {
            if (parent == root) {
                return theFocusOwner;
            } else if (!parent.isVisible()) {
                break;
            }
        }
        return null;
    }

    private Component getFocusCycleRoot(Component theFocusOwner) {
        Component focusCycleRoot = null;
        if (theFocusOwner != null) {
            focusCycleRoot = ToolkitUtils.getPopupAncestor(theFocusOwner);
        }
        return focusCycleRoot == null ? toolkitManager().root() : focusCycleRoot;
    }

    protected void fireKeyEvent(BiConsumer<KeyListener, KeyEvent> keyListenerMethod, KeyEvent keyEvent) {
        Component focusOwnerValue = focusOwner.getValue();
        while (focusOwnerValue != null) {
            try {
                focusOwnerValue.fireKeyEvent(keyListenerMethod, keyEvent);
            } catch (RuntimeException e) {
                PlayN.log(getClass()).error(e.getMessage(), e);
            }

            if (!keyEvent.isConsumed()) {
                focusOwnerValue = focusOwnerValue.getParent();
            } else {
                break;
            }
        }
    }
}

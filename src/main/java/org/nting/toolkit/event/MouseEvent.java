package org.nting.toolkit.event;

import org.nting.toolkit.Component;

import com.google.common.base.MoreObjects;

import playn.core.Mouse;

public class MouseEvent extends InputEvent {

    public enum MouseButton {
        BUTTON_LEFT, BUTTON_MIDDLE, BUTTON_RIGHT;

        public static MouseButton fromInt(int button) {
            if (button == Mouse.BUTTON_LEFT) {
                return BUTTON_LEFT;
            } else if (button == Mouse.BUTTON_MIDDLE) {
                return BUTTON_MIDDLE;
            } else if (button == Mouse.BUTTON_RIGHT) {
                return BUTTON_RIGHT;
            }
            return null;
        }
    }

    private float x;
    private float y;
    private MouseButton button;
    private float velocity;

    private int clickCount = 1; // for double clicks

    private boolean controlPressed = false;
    private boolean altPressed = false;
    private boolean shiftPressed = false;

    public MouseEvent(Component source, double time, float x, float y, MouseButton button) {
        super(source, time);

        this.x = x;
        this.y = y;
        this.button = button;
    }

    public MouseEvent(Component source, double time, float velocity) {
        super(source, time);

        this.velocity = velocity;
    }

    public MouseEvent(MouseEvent aMouseEvent) {
        super(aMouseEvent.getSource(), aMouseEvent.getTime());

        this.x = aMouseEvent.x;
        this.y = aMouseEvent.y;
        this.button = aMouseEvent.button;
        this.velocity = aMouseEvent.velocity;
        this.clickCount = aMouseEvent.clickCount;
        this.controlPressed = aMouseEvent.controlPressed;
        this.altPressed = aMouseEvent.altPressed;
        this.shiftPressed = aMouseEvent.shiftPressed;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public MouseButton getButton() {
        return button;
    }

    public float getVelocity() {
        consume();
        return velocity;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public void translate(float x, float y) {
        this.x += x;
        this.y += y;
    }

    MouseEvent setModifiers(boolean controlPressed, boolean altPressed, boolean shiftPressed) {
        this.controlPressed = controlPressed;
        this.altPressed = altPressed;
        this.shiftPressed = shiftPressed;
        return this;
    }

    public boolean isControlPressed() {
        return controlPressed;
    }

    public boolean isAltPressed() {
        return altPressed;
    }

    public boolean isShiftPressed() {
        return shiftPressed;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("x", x).add("y", y).add("button", button).add("velocity", velocity)
                .add("clickCount", clickCount).add("controlPressed", controlPressed).add("altPressed", altPressed)
                .add("shiftPressed", shiftPressed).add("source", getSource()).add("time", getTime())
                .add("consumed", isConsumed()).toString();
    }
}

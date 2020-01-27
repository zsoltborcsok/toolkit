package org.nting.toolkit.event;

import org.nting.toolkit.Component;

public class MouseMotionEvent extends MouseEvent {

    private final float dx, dy;

    public MouseMotionEvent(Component source, double time, float x, float y, float dx, float dy) {
        this(source, time, x, y, dx, dy, null);
    }

    public MouseMotionEvent(Component source, double time, float x, float y, float dx, float dy, MouseButton button) {
        super(source, time, x, y, button);

        this.dx = dx;
        this.dy = dy;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    @Override
    MouseMotionEvent setModifiers(boolean controlPressed, boolean altPressed, boolean shiftPressed) {
        return (MouseMotionEvent) super.setModifiers(controlPressed, altPressed, shiftPressed);
    }

    @Override
    public String toString() {
        return super.toString() + "&{dx=" + dx + ", dy=" + dy + "}";
    }
}

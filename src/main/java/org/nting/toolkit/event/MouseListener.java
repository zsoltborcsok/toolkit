package org.nting.toolkit.event;

/** Implement only those listener method(s) which is required. */
public interface MouseListener {

    /** Invoked when the mouse button has been clicked (pressed and released) on a component. */
    default void mouseClicked(MouseEvent e) {
    }

    default void mousePressed(MouseEvent e) {
    }

    default void mouseLongPressed(MouseEvent e) {
    }

    default void mouseReleased(MouseEvent e) {
    }

    default void mouseEntered(MouseEvent e) {
    }

    default void mouseExited(MouseEvent e) {
    }

    default void mouseWheelScroll(MouseEvent e) {
    }

    default void mouseDragged(MouseMotionEvent e) {
    }

    default void mouseMoved(MouseMotionEvent e) {
    }
}

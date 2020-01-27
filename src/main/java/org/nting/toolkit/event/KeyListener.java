package org.nting.toolkit.event;

/** Implement only those listener method(s) which is required. */
public interface KeyListener {

    default void keyPressed(KeyEvent e) {
    }

    default void keyReleased(KeyEvent e) {
    }

    default void keyTyped(KeyEvent e) {
    }
}

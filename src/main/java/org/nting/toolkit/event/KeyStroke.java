package org.nting.toolkit.event;

import playn.core.Key;

public class KeyStroke {

    public static KeyStroke onPress(Key keyCode) {
        return new KeyStroke(keyCode, false, false, false, false);
    }

    public static KeyStroke onAltPress(Key keyCode) {
        return new KeyStroke(keyCode, false, false, true, false);
    }

    public static KeyStroke onControlPress(Key keyCode) {
        return new KeyStroke(keyCode, false, true, false, false);
    }

    public static KeyStroke onShiftPress(Key keyCode) {
        return new KeyStroke(keyCode, false, false, false, true);
    }

    public static KeyStroke onRelease(Key keyCode) {
        return new KeyStroke(keyCode, true, false, false, false);
    }

    public static KeyStroke onPress(char keyChar) {
        return new KeyStroke(keyChar, false, false, false, false);
    }

    public static KeyStroke onRelease(char keyChar) {
        return new KeyStroke(keyChar, true, false, false, false);
    }

    private final boolean onKeyRelease;

    private final boolean controlPressed;
    private final boolean altPressed;
    private final boolean shiftPressed;

    private final Key keyCode;
    private final char keyChar;

    public KeyStroke(Key keyCode, boolean onKeyRelease, boolean controlPressed, boolean altPressed,
            boolean shiftPressed) {
        this.onKeyRelease = onKeyRelease;
        this.controlPressed = controlPressed;
        this.altPressed = altPressed;
        this.shiftPressed = shiftPressed;
        this.keyCode = keyCode;
        this.keyChar = KeyEvent.CHAR_UNDEFINED;
    }

    public KeyStroke(char keyChar, boolean onKeyRelease, boolean controlPressed, boolean altPressed,
            boolean shiftPressed) {
        this.onKeyRelease = onKeyRelease;
        this.controlPressed = controlPressed;
        this.altPressed = altPressed;
        this.shiftPressed = shiftPressed;

        this.keyCode = Key.UNKNOWN;
        this.keyChar = keyChar;
    }

    public boolean isOnKeyRelease() {
        return onKeyRelease;
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

    public Key getKeyCode() {
        return keyCode;
    }

    public char getKeyChar() {
        return keyChar;
    }
}

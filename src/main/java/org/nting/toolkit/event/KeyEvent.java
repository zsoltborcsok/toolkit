package org.nting.toolkit.event;

import org.nting.toolkit.Component;

import com.google.common.base.MoreObjects;

import playn.core.Key;

public class KeyEvent extends InputEvent {

    public static final char CHAR_UNDEFINED = 0xFFFF;

    private final Key keyCode;
    private final char keyChar;

    private boolean controlPressed = false;
    private boolean altPressed = false;
    private boolean shiftPressed = false;

    public KeyEvent(Component source, double time, Key keyCode) {
        super(source, time);

        this.keyCode = keyCode;
        this.keyChar = CHAR_UNDEFINED;
    }

    public KeyEvent(Component source, double time, char keyChar) {
        super(source, time);

        this.keyCode = Key.UNKNOWN;
        this.keyChar = keyChar;
    }

    public boolean isKeyCode(Key keyCode) {
        boolean keyCodeFound = this.keyCode == keyCode;
        if (keyCodeFound) {
            consume();
        }
        return keyCodeFound;
    }

    public boolean isKeyChar(char keyChar) {
        boolean keyCharFound = this.keyChar == keyChar;
        if (keyCharFound) {
            consume();
        }
        return keyCharFound;
    }

    public char getKeyChar(boolean consume) {
        if (consume) {
            consume();
        }
        return keyChar;
    }

    KeyEvent setModifiers(boolean controlPressed, boolean altPressed, boolean shiftPressed) {
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
        return MoreObjects.toStringHelper(this).add("keyCode", keyCode).add("keyChar", keyChar)
                .add("controlPressed", controlPressed).add("altPressed", altPressed).add("shiftPressed", shiftPressed)
                .add("source", getSource()).add("time", getTime()).add("consumed", isConsumed()).toString();
    }
}

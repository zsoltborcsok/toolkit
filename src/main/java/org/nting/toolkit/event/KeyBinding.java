package org.nting.toolkit.event;

import java.util.Map;

import org.nting.toolkit.util.SimpleMap;

import playn.core.Key;

public class KeyBinding implements KeyListener {

    private final ActionListener actionListener;
    private final SimpleMap<KeyStroke, Object> keyBindings = new SimpleMap<>();

    public KeyBinding(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        for (Map.Entry<KeyStroke, Object> binding : keyBindings.entries()) {
            KeyStroke keyStroke = binding.getKey();
            if (!keyStroke.isOnKeyRelease() && matches(keyStroke, e)) {
                fireActionPerformed(e, binding.getValue());
                break;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        for (Map.Entry<KeyStroke, Object> binding : keyBindings.entries()) {
            KeyStroke keyStroke = binding.getKey();
            if (keyStroke.isOnKeyRelease() && matches(keyStroke, e)) {
                fireActionPerformed(e, binding.getValue());
                break;
            }
        }

    }

    private boolean matches(KeyStroke keyStroke, KeyEvent keyEvent) {
        if (keyStroke.isAltPressed() == keyEvent.isAltPressed()
                && keyStroke.isControlPressed() == keyEvent.isControlPressed()
                && keyStroke.isShiftPressed() == keyEvent.isShiftPressed()) {
            Key keyCode = keyStroke.getKeyCode();
            if (keyCode != Key.UNKNOWN && keyEvent.isKeyCode(keyCode)) {
                return true;
            }
            char keyChar = keyStroke.getKeyChar();
            if (keyChar != KeyEvent.CHAR_UNDEFINED && keyEvent.isKeyChar(keyChar)) {
                return true;
            }
        }

        return false;
    }

    private void fireActionPerformed(KeyEvent e, Object actionIdOrHandler) {
        if (actionIdOrHandler instanceof ActionListener) { // It WORKS with GWT!/?
            ((ActionListener) actionIdOrHandler).actionPerformed(new ActionEvent(e.getSource(), e.getTime(), e));
        } else {
            actionListener.actionPerformed(new ActionEvent(e.getSource(), e.getTime(), e, actionIdOrHandler));
        }
    }

    public KeyBinding bind(KeyStroke keyStroke, Object actionId) {
        keyBindings.put(keyStroke, actionId);
        return this;
    }

    public KeyBinding bind(KeyStroke keyStroke, ActionListener actionListener) {
        keyBindings.put(keyStroke, actionListener);
        return this;
    }
}

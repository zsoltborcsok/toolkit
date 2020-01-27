package org.nting.toolkit.event;

import org.nting.toolkit.Component;

import playn.core.Keyboard;
import playn.core.Keyboard.Listener;

public class KeyDispatcher implements Listener {

    @Override
    public void onKeyDown(Keyboard.Event event) {
        // TBD
    }

    @Override
    public void onKeyTyped(Keyboard.TypedEvent event) {
        // TBD
    }

    @Override
    public void onKeyUp(Keyboard.Event event) {
        // TBD
    }

    public void requestFocus(Component source) {
        // TBD
    }

    public MouseEvent updateModifiers(MouseEvent mouseEvent) {
        // TBD
        return null;
    }

    public MouseMotionEvent updateModifiers(MouseMotionEvent mouseMotionEvent) {
        // TBD
        return null;
    }

    public void checkForcedFocus() {

    }
}

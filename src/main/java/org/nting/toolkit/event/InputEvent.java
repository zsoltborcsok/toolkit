package org.nting.toolkit.event;

import org.nting.toolkit.Component;

public class InputEvent {

    private Component source;
    private final double time;

    private boolean consumed = false;

    InputEvent(Component source, double time) {
        this.source = source;
        this.time = time;
    }

    protected void setSource(Component source) {
        this.source = source;
    }

    public Component getSource() {
        return source;
    }

    public double getTime() {
        return time;
    }

    public void consume() {
        consumed = true;
    }

    public void unConsume() {
        consumed = false;
    }

    public boolean isConsumed() {
        return consumed;
    }
}

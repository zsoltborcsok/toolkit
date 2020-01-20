package org.nting.toolkit.component;

public enum Alignment {
    TOP_LEFT(true, true), TOP_RIGHT(true, false), BOTTOM_RIGHT(false, false), BOTTOM_LEFT(false, true);

    private final boolean top;
    private final boolean left;

    Alignment(boolean top, boolean left) {
        this.top = top;
        this.left = left;
    }

    public boolean isTop() {
        return top;
    }

    public boolean isLeft() {
        return left;
    }
}

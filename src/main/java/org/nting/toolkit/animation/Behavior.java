package org.nting.toolkit.animation;

public interface Behavior {

    boolean update(float elapsedTime);

    void fastForward();

    void rewind();

    boolean isFinished();
}

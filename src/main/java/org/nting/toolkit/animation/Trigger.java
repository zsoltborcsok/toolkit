package org.nting.toolkit.animation;

import static java.lang.Boolean.TRUE;

import java.util.function.Supplier;

/**
 * After the delay expires triggers the operation if condition is true. Note that calling fastForward() triggers
 * immediately only when the condition is true. Also note that the operation is triggered only once.
 */
public class Trigger implements Behavior {

    private final Runnable operation;
    private final int delay;
    private final Supplier<Boolean> condition;

    private float elapsedTime;
    private boolean finished;

    public Trigger(Runnable operation, int delay) {
        this(operation, delay, () -> true);
    }

    public Trigger(Runnable operation, int delay, Supplier<Boolean> condition) {
        this.operation = operation;
        this.delay = delay;
        this.condition = condition;
    }

    @Override
    public boolean update(float delta) {
        return setTime(elapsedTime + delta);
    }

    @Override
    public void fastForward() {
        setTime(delay);
    }

    @Override
    public void rewind() {
        setTime(0);
    }

    private boolean setTime(float newTime) {
        elapsedTime = newTime;
        if (!finished) {
            if (delay <= elapsedTime && TRUE.equals(condition.get())) {
                try {
                    operation.run();
                } finally {
                    finished = true;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}

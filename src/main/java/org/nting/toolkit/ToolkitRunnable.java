package org.nting.toolkit;

import com.google.common.base.MoreObjects;

public abstract class ToolkitRunnable implements Runnable {

    public static ToolkitRunnable createOneTimeRunnable(int delayMillis, Runnable runnable) {
        return new ToolkitRunnable(delayMillis) {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    public static ToolkitRunnable createLoopedRunnable(int loopCount, int loopDelayMillis, Runnable runnable) {
        return new ToolkitRunnable(loopCount, loopDelayMillis) {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    private int delayMillis;
    private int loopCount;
    private int loopDelayMillis;
    private boolean finished = false;
    private float elapsedTime;

    public ToolkitRunnable(int delayMillis) {
        this.delayMillis = delayMillis;
        this.elapsedTime = -delayMillis;
        loopCount = 1;
    }

    public ToolkitRunnable(int loopCount, int loopDelayMillis) {
        this.delayMillis = 0;
        this.loopCount = loopCount;
        this.loopDelayMillis = loopDelayMillis;
        this.elapsedTime = 0;
    }

    public void cancel() {
        finished = true;
    }

    public void update(float delta) {
        if (!finished) {
            delayMillis -= delta;
            elapsedTime += delta;
            if (delayMillis <= 0) {
                try {
                    run();
                } finally {
                    loopCount--;
                    finished = (loopCount == 0);
                    delayMillis = loopDelayMillis;
                }
            }
        }
    }

    public boolean isFinished() {
        return finished;
    }

    /** Returns the elapsed time since the timer became active. */
    public float getElapsedTime() {
        return elapsedTime;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("delayMillis", delayMillis).add("loopCount", loopCount)
                .add("loopDelayMillis", loopDelayMillis).add("finished", finished).add("elapsedTime", elapsedTime)
                .toString();
    }
}

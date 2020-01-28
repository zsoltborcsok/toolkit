package org.nting.toolkit.animation;

import java.util.function.Consumer;

/**
 * An Animation changes state over a specific duration. It is up to subclasses to implement the
 * {@link #updateState(int)} method to change the state as the Animation is updated over time. An Animation is updated
 * over time with its {@link #update(float)} method.
 * <p/>
 * A simple Animation starts immediately:
 * <p/>
 * 
 * <pre>
 * |===================|
 * 0     duration     end
 * 
 * ------- time ------->
 * </pre>
 * <p/>
 * <p/>
 * Animations can delay before they start:
 * <p/>
 * 
 * <pre>
 * |--------------|===================|
 * 0  startDelay        duration     end
 * 
 * ---------------- time --------------->
 * </pre>
 * <p/>
 * <p/>
 * Animations can loop:
 * <p/>
 * 
 * <pre>
 * |--------------|===================|===================|===================|
 * 0  startDelay        duration            duration           duration      end
 * 
 * ----------------------------------- time ------------------------------------>
 * </pre>
 * <p/>
 * <p/>
 * Animations can have a delay between loops:
 * <p/>
 * 
 * <pre>
 * |--------------|================|------|================|------|================|
 * 0  startDelay       duration      loop      duration      loop      duration   end
 *                                   delay                   delay
 * ------------------------------------- time ------------------------------------->
 * </pre>
 * <p/>
 * Also, Animations can have an {@link Easing} to make the Animation look more smooth.
 */
public abstract class Animation implements Behavior {

    public static Animation createAnimation(int duration, Easing easing, int startDelay,
            Consumer<Integer> updateState) {

        return new Animation(duration, easing, startDelay) {

            @Override
            protected void updateState(int animationTime) {
                updateState.accept(animationTime);
            }
        };
    }

    /** Value indicating that the Animation loops forever. */
    public static final int LOOP_FOREVER = -1;

    protected static final int SECTION_START_DELAY = 0;
    protected static final int SECTION_ANIMATION = 1;
    protected static final int SECTION_LOOP_DELAY = 2;

    private final Easing easing;
    private final int startDelay;
    private int duration;
    private int numLoops;
    private int loopDelay;

    private float elapsedTime;

    public Animation(int duration) {
        this(duration, null, 0);
    }

    public Animation(int duration, Easing easing) {
        this(duration, easing, 0);
    }

    public Animation(int duration, Easing easing, int startDelay) {
        this.duration = duration;
        this.easing = easing;
        this.startDelay = startDelay;
        loop(1, 0);
    }

    public final Animation loopForever() {
        loop(LOOP_FOREVER, 0);
        return this;
    }

    public final void loopForever(int loopDelay) {
        loop(LOOP_FOREVER, loopDelay);
    }

    public final void loop(int numLoops) {
        loop(numLoops, 0);
    }

    public final void loop(int numLoops, int loopDelay) {
        if (duration == 0 && numLoops != 1 && loopDelay == 0) {// Can't loop!
            this.numLoops = 1;
        } else {
            this.numLoops = numLoops;
            this.loopDelay = loopDelay;
        }
    }

    public final int getStartDelay() {
        return startDelay;
    }

    public final int getDuration() {
        return duration;
    }

    public final Easing getEasing() {
        return easing;
    }

    protected final void setDuration(int duration) {
        this.duration = duration;
    }

    public final int getNumLoops() {
        return numLoops;
    }

    public final int getLoopDelay() {
        return loopDelay;
    }

    /**
     * Returns the total duration including the start delay and loops.
     */
    public final int getTotalDuration() {
        if (numLoops == LOOP_FOREVER) {
            return LOOP_FOREVER;
        } else {
            return startDelay + duration * numLoops + loopDelay * (numLoops - 1);
        }
    }

    public final float getTime() {
        return elapsedTime;
    }

    private int getAnimationTime() {
        return getAnimationTime((int) elapsedTime);
    }

    private int getAnimationTime(int elapsedTime) {
        int animationTime = elapsedTime - startDelay;
        if (animationTime >= 0 && numLoops != 1) {
            animationTime %= (duration + loopDelay);
        }
        return animationTime;
    }

    protected final int getSection() {
        return getSection((int) elapsedTime);
    }

    protected final int getSection(int elapsedTime) {
        int animationTime = getAnimationTime(elapsedTime);
        if (animationTime < 0) {
            return SECTION_START_DELAY;
        } else if (animationTime < duration) {
            return SECTION_ANIMATION;
        } else {
            return SECTION_LOOP_DELAY;
        }
    }

    @Override
    public final boolean isFinished() {
        return numLoops != LOOP_FOREVER && getTotalDuration() <= elapsedTime;
    }

    /**
     * Sets the current time to the end of this animation. If this animation loop forever, the time is set to the end of
     * the current loop.
     */
    @Override
    public final void fastForward() {
        if (numLoops == LOOP_FOREVER) {
            int loop = 0;
            int animTime = (int) (elapsedTime - startDelay);
            if (animTime >= 0) {
                loop = animTime / (duration + loopDelay);
            }
            this.numLoops = loop;
        }
        setTime(getTotalDuration());
    }

    public final void rewind() {
        setTime(0);
    }

    @Override
    public boolean update(float delta) {
        return setTime(this.elapsedTime + delta);
    }

    private boolean setTime(float newTime) {
        // Takes care of special case where elapsedTime, startDelay and duration are 0
        int oldState = (elapsedTime <= 0) ? SECTION_START_DELAY : getSection();
        this.elapsedTime = newTime;
        int newState = getSection();

        if (newState == SECTION_ANIMATION) {
            int animTime = getAnimationTime();
            if (easing != null) {
                animTime = easing.ease(animTime, duration);
            }
            updateState(animTime);
            return true;
        } else if ((newState == SECTION_LOOP_DELAY && oldState != SECTION_LOOP_DELAY)
                || (newState == SECTION_START_DELAY && oldState == SECTION_ANIMATION)) {
            updateState(duration);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Updates the state based on the animation time, typically from 0 to {@link #getDuration()}. Note that the duration
     * can be zero.
     * 
     * @param animationTime
     *            The animation time, typically from 0 to {@link #getDuration()} , although an {@link Easing} can cause
     *            the value to be outside those bounds.
     */
    protected abstract void updateState(int animationTime);
}

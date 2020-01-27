package org.nting.toolkit.animation;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * A Timeline is a list of Animations.
 */
public class Timeline extends Animation {

    private Timeline parent;
    private final List<Animation> animationList = Lists.newLinkedList();

    private float playSpeed = 1f;
    private int remainderMicros; // Remainder, in microseconds, of the play time. Used when playSpeed != 1.

    public Timeline() {
        this(null, 0);
    }

    public Timeline(int startDelay) {
        this(null, startDelay);
    }

    public Timeline(Easing easing) {
        this(easing, 0);
    }

    public Timeline(Easing easing, int startDelay) {
        super(0, easing, startDelay);
    }

    private void setParent(Timeline parent) {
        this.parent = parent;
    }

    private void calcDuration() {
        int duration = 0;
        for (Animation anim : animationList) {
            int childDuration = anim.getTotalDuration();
            if (childDuration == LOOP_FOREVER) {
                duration = LOOP_FOREVER;
                break;
            } else if (childDuration > duration) {
                duration = childDuration;
            }
        }
        super.setDuration(duration);
        if (parent != null) {
            parent.calcDuration();
        }
    }

    /**
     * Sets the play speed. A speed of '1' is normal, '.5' is half speed, '2' is twice normal speed, and '-1' is reverse
     * speed. Note: play speed only affects top-level Timelines - child Timelines play at their parent's speed.
     */
    public void setPlaySpeed(float speed) {
        playSpeed = speed;
    }

    public float getPlaySpeed() {
        return playSpeed;
    }

    @Override
    public boolean update(float delta) {
        if (playSpeed == 0) {
            delta = 0;
        } else if (playSpeed == -1) {
            delta = -delta;
        } else if (playSpeed != 1) {
            long timeMicros = Math.round(delta * 1000L * playSpeed) + remainderMicros;
            delta = (int) (timeMicros / 1000);
            remainderMicros = (int) (timeMicros % 1000);
        }
        return super.update(delta);
    }

    @Override
    protected void updateState(int animTime) {
        for (Animation animation : animationList) {
            animation.update(animTime - animation.getTime());
        }
    }

    /** Creates a child timeline that starts at the specified time relative to the start of this timeline. */
    public Timeline at(int time) {
        Timeline child = new Timeline(Easing.NONE, time);
        add(child);
        return child;
    }

    /** Creates a child timeline that starts at end of this timeline. */
    public Timeline after() {
        return after(0);
    }

    /** Creates a child timeline that starts at the specified time relative to the end of this timeline. */
    public Timeline after(int time) {
        int t = getDuration();
        if (t == LOOP_FOREVER) {
            return at(time);
        } else {
            return at(time + t);
        }
    }

    public void add(Animation animation) {
        if (animation instanceof Timeline) {
            ((Timeline) animation).setParent(this);
        }
        animationList.add(animation);
        calcDuration();
    }
}

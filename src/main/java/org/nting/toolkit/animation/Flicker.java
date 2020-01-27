package org.nting.toolkit.animation;

import org.nting.data.Property;
import org.nting.data.ValueChangeListener;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.Component;

public class Flicker implements Behavior {

    /** The minimum distance (in pixels) the pointer must have moved to register as a flick. */
    private float minFlickDelta = 20;
    /** The minimum velocity (in pixels per ms) to initiate a flick */
    private float minFlickVelocity = 0.5f;

    /** The deceleration (in pixels per ms per ms) applied to non-zero velocity. */
    private float friction = 0.0015f;
    /** The fraction of flick velocity that is transferred to the entity (a value between 0 and 1). */
    private float flickTransferred = 0.9f;
    /** Returns the maximum flick velocity (in pixels per ms) that will be transferred to the entity. */
    private float maxFlickVelocity = 1.4f;

    private Property<Float> property;
    private float velocity = 0f;

    public Flicker minFlickDelta(float minFlickDelta) {
        this.minFlickDelta = minFlickDelta;
        return this;
    }

    public Flicker minFlickVelocity(float minFlickVelocity) {
        this.minFlickVelocity = minFlickVelocity;
        return this;
    }

    public Flicker friction(float friction) {
        this.friction = friction;
        return this;
    }

    public Flicker flickTransferred(float flickTransferred) {
        this.flickTransferred = flickTransferred;
        return this;
    }

    public Flicker maxFlickVelocity(float maxFlickVelocity) {
        this.maxFlickVelocity = maxFlickVelocity;
        return this;
    }

    @Override
    public boolean update(float elapsedTime) {
        if (velocity == 0f) {
            return false;
        }
        float delta;

        float diffVelocity = friction * elapsedTime * Math.signum(velocity);
        if (Math.abs(diffVelocity) <= Math.abs(velocity)) {
            delta = (velocity + velocity - diffVelocity) * elapsedTime / 2f;
            velocity -= diffVelocity;
        } else {
            float time = Math.abs(velocity / friction);
            delta = velocity * time / 2f;
            velocity = 0f;
        }

        increasePropertyValue(delta);
        return true;
    }

    @Override
    public void fastForward() {
        float delta = velocity * Math.abs(velocity / friction) / 2;
        increasePropertyValue(delta);
    }

    @Override
    public void rewind() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isFinished() {
        return velocity == 0f;
    }

    public boolean isFlicked(float delta, float velocity) {
        return minFlickDelta <= Math.abs(delta) || minFlickVelocity <= Math.abs(velocity);
    }

    public Flicker flick(float velocity, ValueChangeListener<Float> flickChangeListener) {
        this.velocity = Math.min(maxFlickVelocity, flickTransferred * velocity);
        property = new ObjectProperty<>(0f);
        property.addValueChangeListener(flickChangeListener);
        return this;
    }

    public void start(Component component, boolean removeBehaviorOnAnyConsumedEvent) {
        component.addBehavior(this);
        if (removeBehaviorOnAnyConsumedEvent) {
            component.removeBehaviorOnAnyConsumedEvent(this);
        }
    }

    private void increasePropertyValue(float delta) {
        property.setValue(property.getValue() + delta);
    }
}

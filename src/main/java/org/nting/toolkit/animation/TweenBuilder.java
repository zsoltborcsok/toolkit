package org.nting.toolkit.animation;

import org.nting.data.Property;
import org.nting.data.ValueChangeListener;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.Component;
import org.nting.toolkit.animation.Easing.EasingFunction;
import org.nting.toolkit.animation.Easing.EasingType;

public class TweenBuilder<T> {

    public static <T> TweenBuilder<T> tween(T fromValue, T toValue, int duration) {
        return new TweenBuilder<>(fromValue, toValue, duration);
    }

    private Property<T> property;
    private final T fromValue;
    private final T toValue;
    private final int duration;

    private Easing easing = null;
    private int startDelay = 0;

    public TweenBuilder(T fromValue, T toValue, int duration) {
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.duration = duration;
    }

    public TweenBuilder<T> property(Property<T> property) {
        this.property = property;
        return this;
    }

    public TweenBuilder<T> tweenChangeListener(ValueChangeListener<T> tweenChangeListener) {
        property = new ObjectProperty<>(fromValue);
        property.addValueChangeListener(tweenChangeListener);
        return this;
    }

    public TweenBuilder<T> easing(Easing easing) {
        this.easing = easing;
        return this;
    }

    public TweenBuilder<T> easing(EasingType type, EasingFunction function) {
        this.easing = new Easing(type, function);
        return this;
    }

    public TweenBuilder<T> startDelay(int startDelay) {
        this.startDelay = startDelay;
        return this;
    }

    public Tween<T> build() {
        return build(property);
    }

    public Tween<T> build(Component component) {
        return build(component, false);
    }

    public Tween<T> build(Component component, boolean removeBehaviorOnAnyConsumedEvent) {
        Tween<T> tween = build(property);
        component.addBehavior(tween);
        if (removeBehaviorOnAnyConsumedEvent) {
            component.removeBehaviorOnAnyConsumedEvent(tween);
        }
        return tween;
    }

    private Tween<T> build(Property<T> property) {
        return new Tween<>(property, fromValue, toValue, duration, easing, startDelay);
    }
}

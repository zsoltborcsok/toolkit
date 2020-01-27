package org.nting.toolkit.animation;

import static org.nting.toolkit.animation.Easing.EasingFunction.FUNCTION_LINEAR;
import static org.nting.toolkit.animation.Easing.EasingFunction.FUNCTION_QUADRATIC;
import static org.nting.toolkit.animation.Easing.EasingFunction.FUNCTION_QUINTIC;
import static org.nting.toolkit.animation.Easing.EasingType.TYPE_IN;
import static org.nting.toolkit.animation.Easing.EasingType.TYPE_IN_OUT;
import static org.nting.toolkit.animation.Easing.EasingType.TYPE_OUT;

/**
 * The Easing class provides functions to ease property animation in a non-linear way. Most apps will not need to create
 * an instance of this class. See http://easings.net and
 * https://github.com/cinder/Cinder/blob/master/include/cinder/Easing.h.
 */
public class Easing {

    public enum EasingType {
        TYPE_IN, TYPE_OUT, TYPE_IN_OUT
    }

    public enum EasingFunction {
        FUNCTION_LINEAR, FUNCTION_SINUSOIDAL, FUNCTION_QUADRATIC, FUNCTION_CUBIC, FUNCTION_QUARTIC, FUNCTION_QUINTIC, //
        FUNCTION_EXPONENTIAL, FUNCTION_CIRCULAR, FUNCTION_BACK, FUNCTION_ELASTIC, FUNCTION_BOUNCE
    }

    // Aliases
    public static final EasingFunction FUNCTION_REGULAR = FUNCTION_QUADRATIC;
    public static final EasingFunction FUNCTION_STRONG = FUNCTION_QUINTIC;

    // Default Easing-s
    public static final Easing NONE = new Easing(TYPE_IN, FUNCTION_LINEAR);
    public static final Easing REGULAR_IN = new Easing(TYPE_IN, FUNCTION_REGULAR);
    public static final Easing REGULAR_OUT = new Easing(TYPE_OUT, FUNCTION_REGULAR);
    public static final Easing REGULAR_IN_OUT = new Easing(TYPE_IN_OUT, FUNCTION_REGULAR);

    private final EasingType type;
    private final EasingFunction function;
    private final float strength;
    private final BounceFunction bounceFunction = new BounceFunction();

    protected Easing() {
        this(TYPE_IN, FUNCTION_LINEAR);
    }

    public Easing(EasingType type, EasingFunction function) {
        this(type, function, 1);
    }

    public Easing(EasingType type, EasingFunction function, float strength) {
        this.type = type;
        this.function = function;
        this.strength = strength;
    }

    public final int ease(int time, int duration) {
        if (time <= 0 || duration <= 0) {
            return 0;
        } else if (time >= duration) {
            return duration;
        }

        final double t = (double) time / duration;
        double easedT;

        switch (type) {
        default:
            easedT = t;
            break;
        case TYPE_IN:
            easedT = ease(t);
            break;
        case TYPE_OUT:
            easedT = 1 - ease(1 - t);
            break;
        case TYPE_IN_OUT:
            if (t < 0.5) {
                easedT = ease(2 * t) / 2;
            } else {
                easedT = 1 - ease(2 - 2 * t) / 2;
            }
            break;
        }

        if (strength != 1) {
            easedT = strength * easedT + (1 - strength) * t;
        }
        return (int) Math.round(easedT * duration);
    }

    protected double ease(double t) {
        double t2;
        double t3;

        switch (function) {
        default:
        case FUNCTION_LINEAR:
            return t;
        case FUNCTION_QUADRATIC:
            return t * t;
        case FUNCTION_CUBIC:
            return t * t * t;
        case FUNCTION_QUARTIC:
            t2 = t * t;
            return t2 * t2;
        case FUNCTION_QUINTIC:
            t2 = t * t;
            return t2 * t2 * t;
        case FUNCTION_BACK:
            t2 = t * t;
            t3 = t2 * t;
            return t3 + t2 - t;
        case FUNCTION_ELASTIC:
            t2 = t * t;
            t3 = t2 * t;
            double scale = t2 * (2 * t3 + t2 - 4 * t + 2);
            double wave = (float) -Math.sin(t * 3.5 * Math.PI);
            return scale * wave;
        case FUNCTION_SINUSOIDAL:
            return 1 - Math.cos(t * Math.PI / 2.0);
        case FUNCTION_EXPONENTIAL:
            return t == 0 ? 0 : Math.pow(2, 10 * (t - 1));
        case FUNCTION_CIRCULAR:
            return 1 - Math.sqrt(1 - t * t);
        case FUNCTION_BOUNCE:
            return 1 - bounceFunction.apply(1 - t);
        }
    }

    public static class BounceFunction {

        public double apply(double t) {
            if (t == 1) {
                return 1;
            } else if (t < (4 / 11.0f)) {
                return (7.5625f * t * t);
            } else if (t < (8 / 11.0f)) {
                t -= (6 / 11.0f);
                return (7.5625f * t * t) + 0.75f;
            } else if (t < (10 / 11.0f)) {
                t -= (9 / 11.0f);
                return (7.5625f * t * t) + 0.9375f;
            } else {
                t -= (21 / 22.0f);
                return (7.5625f * t * t) + 0.984375f;
            }
        }
    }
}

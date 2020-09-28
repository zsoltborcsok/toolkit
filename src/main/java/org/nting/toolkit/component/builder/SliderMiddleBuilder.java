package org.nting.toolkit.component.builder;

import static org.nting.toolkit.ToolkitServices.unitConverter;

import java.util.function.Function;

import org.nting.data.Property;
import org.nting.toolkit.component.Slider;

public class SliderMiddleBuilder<T extends ContainerBuilder<?, ?>> extends AbstractMiddleBuilder<Slider, T> {

    public SliderMiddleBuilder() {
        this(new ComponentBuilder<>(new Slider()));
    }

    public SliderMiddleBuilder(ComponentBuilder<Slider, T> componentBuilder) {
        super(componentBuilder);
    }

    public SliderMiddleBuilder<T> divisions(int divisions) {
        componentBuilder.set(slider -> slider.divisions, divisions);
        return this;
    }

    public SliderMiddleBuilder<T> padding(int padding) {
        componentBuilder.set(slider -> slider.padding, padding);
        return this;
    }

    public SliderMiddleBuilder<T> paddingDluX(float paddingDlu) {
        return padding(unitConverter().dialogUnitXAsPixel(paddingDlu, null));
    }

    public SliderMiddleBuilder<T> paddingDluY(float paddingDlu) {
        return padding(unitConverter().dialogUnitYAsPixel(paddingDlu, null));
    }

    public SliderMiddleBuilder<T> enabled(boolean enabled) {
        componentBuilder.set(slider -> slider.enabled, enabled);
        return this;
    }

    public SliderMiddleBuilder<T> enabled(Property<Boolean> enabled) {
        componentBuilder.bind(slider -> slider.enabled, enabled);
        return this;
    }

    public SliderMiddleBuilder<T> value(Float value) {
        componentBuilder.set(slider -> slider.value, value);
        return this;
    }

    public SliderMiddleBuilder<T> value(Property<Float> value) {
        componentBuilder.bind(slider -> slider.value, value);
        return this;
    }

    public <F> SliderMiddleBuilder<T> value(Property<F> source, Function<F, Float> transform) {
        componentBuilder.bind(slider -> slider.value, source, transform);
        return this;
    }

    public SliderMiddleBuilder<T> min(Float min) {
        componentBuilder.set(slider -> slider.min, min);
        return this;
    }

    public SliderMiddleBuilder<T> min(Property<Float> minProperty) {
        componentBuilder.bind(slider -> slider.min, minProperty);
        return this;
    }

    public <F> SliderMiddleBuilder<T> min(Property<F> source, Function<F, Float> transform) {
        componentBuilder.bind(slider -> slider.min, source, transform);
        return this;
    }

    public SliderMiddleBuilder<T> max(Float max) {
        componentBuilder.set(slider -> slider.max, max);
        return this;
    }

    public SliderMiddleBuilder<T> max(Property<Float> maxProperty) {
        componentBuilder.bind(slider -> slider.max, maxProperty);
        return this;
    }

    public <F> SliderMiddleBuilder<T> max(Property<F> source, Function<F, Float> transform) {
        componentBuilder.bind(slider -> slider.max, source, transform);
        return this;
    }
}

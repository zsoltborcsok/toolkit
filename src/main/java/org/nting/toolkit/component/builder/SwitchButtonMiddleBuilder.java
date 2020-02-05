package org.nting.toolkit.component.builder;

import static org.nting.toolkit.ToolkitServices.unitConverter;

import java.util.function.Function;

import org.nting.data.Property;
import org.nting.toolkit.component.SwitchButton;

public class SwitchButtonMiddleBuilder<T extends ContainerBuilder<?, ?>>
        extends AbstractMiddleBuilder<SwitchButton, T> {

    public SwitchButtonMiddleBuilder() {
        this(new ComponentBuilder<>(new SwitchButton()));
    }

    public SwitchButtonMiddleBuilder(ComponentBuilder<SwitchButton, T> componentBuilder) {
        super(componentBuilder);
    }

    public SwitchButtonMiddleBuilder<T> color(int color) {
        componentBuilder.set(switchButton -> switchButton.color, color);
        return this;
    }

    public SwitchButtonMiddleBuilder<T> padding(int padding) {
        componentBuilder.set(switchButton -> switchButton.padding, padding);
        return this;
    }

    public SwitchButtonMiddleBuilder<T> paddingDluX(float paddingDlu) {
        return padding(unitConverter().dialogUnitXAsPixel(paddingDlu, null));
    }

    public SwitchButtonMiddleBuilder<T> paddingDluY(float paddingDlu) {
        return padding(unitConverter().dialogUnitYAsPixel(paddingDlu, null));
    }

    public SwitchButtonMiddleBuilder<T> enabled(boolean enabled) {
        componentBuilder.set(switchButton -> switchButton.enabled, enabled);
        return this;
    }

    public SwitchButtonMiddleBuilder<T> enabled(Property<Boolean> enabled) {
        componentBuilder.<Boolean> set(switchButton -> switchButton.enabled, enabled);
        return this;
    }

    public SwitchButtonMiddleBuilder<T> switched(Boolean switched) {
        componentBuilder.set(switchButton -> switchButton.switched, switched);
        return this;
    }

    public SwitchButtonMiddleBuilder<T> switched(Property<Boolean> switched) {
        componentBuilder.<Boolean> set(switchButton -> switchButton.switched, switched);
        return this;
    }

    public SwitchButtonMiddleBuilder<T> captionLeft(String captionLeft) {
        componentBuilder.set(switchButton -> switchButton.captionLeft, captionLeft);
        return this;
    }

    public SwitchButtonMiddleBuilder<T> captionLeft(Property<String> captionLeftProperty) {
        componentBuilder.<String> set(switchButton -> switchButton.captionLeft, captionLeftProperty);
        return this;
    }

    public <F> SwitchButtonMiddleBuilder<T> captionLeft(Property<F> source, Function<F, String> transform) {
        componentBuilder.bind(switchButton -> switchButton.captionLeft, source, transform);
        return this;
    }

    public SwitchButtonMiddleBuilder<T> captionRight(String captionRight) {
        componentBuilder.set(switchButton -> switchButton.captionRight, captionRight);
        return this;
    }

    public SwitchButtonMiddleBuilder<T> captionRight(Property<String> captionRightProperty) {
        componentBuilder.<String> set(switchButton -> switchButton.captionRight, captionRightProperty);
        return this;
    }

    public <F> SwitchButtonMiddleBuilder<T> captionRight(Property<F> source, Function<F, String> transform) {
        componentBuilder.bind(switchButton -> switchButton.captionRight, source, transform);
        return this;
    }
}

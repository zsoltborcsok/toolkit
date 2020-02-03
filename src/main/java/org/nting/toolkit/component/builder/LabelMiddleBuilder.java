package org.nting.toolkit.component.builder;

import java.util.function.Function;

import org.nting.data.Property;
import org.nting.toolkit.component.Label;
import org.nting.toolkit.component.TextAlignment;

public class LabelMiddleBuilder<T extends ContainerBuilder<?, ?>> {

    private final ComponentBuilder<Label, T> componentBuilder;

    public LabelMiddleBuilder(ComponentBuilder<Label, T> componentBuilder) {
        this.componentBuilder = componentBuilder;
    }

    public LabelMiddleBuilder<T> alignment(TextAlignment alignment) {
        componentBuilder.set(label -> label.alignment, alignment);
        return this;
    }

    public LabelMiddleBuilder<T> text(String text) {
        componentBuilder.set(label -> label.text, text);
        return this;
    }

    public LabelMiddleBuilder<T> text(Property<String> textProperty) {
        componentBuilder.<String> set(label -> label.text, textProperty);
        return this;
    }

    public <F> LabelMiddleBuilder<T> text(Property<F> source, Function<F, String> transform) {
        componentBuilder.bind(label -> label.text, source, transform);
        return this;
    }

    public ComponentBuilder<Label, T> pass() {
        return componentBuilder;
    }
}

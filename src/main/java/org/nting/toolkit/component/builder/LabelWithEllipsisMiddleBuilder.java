package org.nting.toolkit.component.builder;

import java.util.function.Function;

import org.nting.data.Property;
import org.nting.toolkit.component.LabelWithEllipsis;

public class LabelWithEllipsisMiddleBuilder<T extends ContainerBuilder<?, ?>>
        extends AbstractMiddleBuilder<LabelWithEllipsis, T> {

    public LabelWithEllipsisMiddleBuilder() {
        this(new ComponentBuilder<>(new LabelWithEllipsis()));
    }

    public LabelWithEllipsisMiddleBuilder(ComponentBuilder<LabelWithEllipsis, T> componentBuilder) {
        super(componentBuilder);
    }

    public LabelWithEllipsisMiddleBuilder<T> text(String text) {
        componentBuilder.set(label -> label.text, text);
        return this;
    }

    public LabelWithEllipsisMiddleBuilder<T> text(Property<String> textProperty) {
        componentBuilder.bind(label -> label.text, textProperty);
        return this;
    }

    public <F> LabelWithEllipsisMiddleBuilder<T> text(Property<F> source, Function<F, String> transform) {
        componentBuilder.bind(label -> label.text, source, transform);
        return this;
    }
}

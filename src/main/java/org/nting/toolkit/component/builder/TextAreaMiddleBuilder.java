package org.nting.toolkit.component.builder;

import java.util.function.Function;

import org.nting.data.Property;
import org.nting.toolkit.component.TextArea;

import com.google.common.base.Converter;

public class TextAreaMiddleBuilder<T extends ContainerBuilder<?, ?>> extends AbstractMiddleBuilder<TextArea, T> {

    public TextAreaMiddleBuilder() {
        this(new ComponentBuilder<>(new TextArea()));
    }

    public TextAreaMiddleBuilder(ComponentBuilder<TextArea, T> componentBuilder) {
        super(componentBuilder);
    }

    public TextAreaMiddleBuilder<T> color(int color) {
        componentBuilder.set(textArea -> textArea.color, color);
        return this;
    }

    public TextAreaMiddleBuilder<T> errorMessage(String errorMessage) {
        componentBuilder.set(textArea -> textArea.errorMessage, errorMessage);
        return this;
    }

    public TextAreaMiddleBuilder<T> errorMessage(Property<String> errorMessage) {
        componentBuilder.bind(textArea -> textArea.errorMessage, errorMessage);
        return this;
    }

    public <F> TextAreaMiddleBuilder<T> errorMessage(Property<F> source, Function<F, String> transform) {
        componentBuilder.bind(textArea -> textArea.errorMessage, source, transform);
        return this;
    }

    public TextAreaMiddleBuilder<T> caption(String caption) {
        componentBuilder.set(textArea -> textArea.caption, caption);
        return this;
    }

    public TextAreaMiddleBuilder<T> caption(Property<String> caption) {
        componentBuilder.bind(textArea -> textArea.caption, caption);
        return this;
    }

    public <F> TextAreaMiddleBuilder<T> caption(Property<F> source, Function<F, String> transform) {
        componentBuilder.bind(textArea -> textArea.caption, source, transform);
        return this;
    }

    public TextAreaMiddleBuilder<T> enabled(boolean enabled) {
        componentBuilder.set(textArea -> textArea.enabled, enabled);
        return this;
    }

    public TextAreaMiddleBuilder<T> enabled(Property<Boolean> enabled) {
        componentBuilder.bind(textArea -> textArea.enabled, enabled);
        return this;
    }

    public TextAreaMiddleBuilder<T> text(String text) {
        componentBuilder.set(textArea -> textArea.text, text);
        return this;
    }

    public TextAreaMiddleBuilder<T> text(Property<String> textProperty) {
        componentBuilder.bind(textArea -> textArea.text, textProperty);
        return this;
    }

    public <F> TextAreaMiddleBuilder<T> text(Property<F> source, Converter<F, String> converter) {
        componentBuilder.bind(textArea -> textArea.text, source, converter);
        return this;
    }

    public TextAreaMiddleBuilder<T> rows(int rows) {
        componentBuilder.set(textArea -> textArea.rows, rows);
        return this;
    }

    public TextAreaMiddleBuilder<T> columns(int columns) {
        componentBuilder.set(textArea -> textArea.columns, columns);
        return this;
    }
}

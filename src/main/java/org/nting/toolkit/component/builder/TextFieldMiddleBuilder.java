package org.nting.toolkit.component.builder;

import java.util.function.Function;

import org.nting.data.Property;
import org.nting.toolkit.component.TextField;

import com.google.common.base.Converter;

public class TextFieldMiddleBuilder<T extends ContainerBuilder<?, ?>> extends AbstractMiddleBuilder<TextField, T> {

    public TextFieldMiddleBuilder() {
        this(new ComponentBuilder<>(new TextField()));
    }

    public TextFieldMiddleBuilder(ComponentBuilder<TextField, T> componentBuilder) {
        super(componentBuilder);
    }

    public TextFieldMiddleBuilder<T> color(int color) {
        componentBuilder.set(textField -> textField.color, color);
        return this;
    }

    public TextFieldMiddleBuilder<T> errorMessage(String errorMessage) {
        componentBuilder.set(textField -> textField.errorMessage, errorMessage);
        return this;
    }

    public TextFieldMiddleBuilder<T> errorMessage(Property<String> errorMessage) {
        componentBuilder.<String> set(textField -> textField.errorMessage, errorMessage);
        return this;
    }

    public <F> TextFieldMiddleBuilder<T> errorMessage(Property<F> source, Function<F, String> transform) {
        componentBuilder.bind(textField -> textField.errorMessage, source, transform);
        return this;
    }

    public TextFieldMiddleBuilder<T> caption(String caption) {
        componentBuilder.set(textField -> textField.caption, caption);
        return this;
    }

    public TextFieldMiddleBuilder<T> caption(Property<String> caption) {
        componentBuilder.<String> set(textField -> textField.caption, caption);
        return this;
    }

    public <F> TextFieldMiddleBuilder<T> caption(Property<F> source, Function<F, String> transform) {
        componentBuilder.bind(textField -> textField.caption, source, transform);
        return this;
    }

    public TextFieldMiddleBuilder<T> enabled(boolean enabled) {
        componentBuilder.set(textField -> textField.enabled, enabled);
        return this;
    }

    public TextFieldMiddleBuilder<T> enabled(Property<Boolean> enabled) {
        componentBuilder.<Boolean> set(textField -> textField.enabled, enabled);
        return this;
    }

    public TextFieldMiddleBuilder<T> text(String text) {
        componentBuilder.set(textField -> textField.text, text);
        return this;
    }

    public TextFieldMiddleBuilder<T> text(Property<String> textProperty) {
        componentBuilder.bind(textField -> textField.text, textProperty);
        return this;
    }

    public <F> TextFieldMiddleBuilder<T> text(Property<F> source, Converter<F, String> converter) {
        componentBuilder.bind(textField -> textField.text, source, converter);
        return this;
    }
}

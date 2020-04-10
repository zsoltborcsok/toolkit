package org.nting.toolkit.component.builder;

import java.util.function.Function;

import org.nting.data.Property;
import org.nting.toolkit.component.PasswordField;

import com.google.common.base.Converter;

public class PasswordFieldMiddleBuilder<T extends ContainerBuilder<?, ?>>
        extends AbstractMiddleBuilder<PasswordField, T> {

    public PasswordFieldMiddleBuilder() {
        this(new ComponentBuilder<>(new PasswordField()));
    }

    public PasswordFieldMiddleBuilder(ComponentBuilder<PasswordField, T> componentBuilder) {
        super(componentBuilder);
    }

    public PasswordFieldMiddleBuilder<T> echoChar(Character echoChar) {
        componentBuilder.set(passwordField -> passwordField.echoChar, echoChar);
        return this;
    }

    public PasswordFieldMiddleBuilder<T> color(int color) {
        componentBuilder.set(passwordField -> passwordField.color, color);
        return this;
    }

    public PasswordFieldMiddleBuilder<T> errorMessage(String errorMessage) {
        componentBuilder.set(passwordField -> passwordField.errorMessage, errorMessage);
        return this;
    }

    public PasswordFieldMiddleBuilder<T> errorMessage(Property<String> errorMessage) {
        componentBuilder.bind(passwordField -> passwordField.errorMessage, errorMessage);
        return this;
    }

    public <F> PasswordFieldMiddleBuilder<T> errorMessage(Property<F> source, Function<F, String> transform) {
        componentBuilder.bind(passwordField -> passwordField.errorMessage, source, transform);
        return this;
    }

    public PasswordFieldMiddleBuilder<T> caption(String caption) {
        componentBuilder.set(passwordField -> passwordField.caption, caption);
        return this;
    }

    public PasswordFieldMiddleBuilder<T> caption(Property<String> caption) {
        componentBuilder.bind(passwordField -> passwordField.caption, caption);
        return this;
    }

    public <F> PasswordFieldMiddleBuilder<T> caption(Property<F> source, Function<F, String> transform) {
        componentBuilder.bind(passwordField -> passwordField.caption, source, transform);
        return this;
    }

    public PasswordFieldMiddleBuilder<T> enabled(boolean enabled) {
        componentBuilder.set(passwordField -> passwordField.enabled, enabled);
        return this;
    }

    public PasswordFieldMiddleBuilder<T> enabled(Property<Boolean> enabled) {
        componentBuilder.bind(passwordField -> passwordField.enabled, enabled);
        return this;
    }

    public PasswordFieldMiddleBuilder<T> text(String text) {
        componentBuilder.set(passwordField -> passwordField.text, text);
        return this;
    }

    public PasswordFieldMiddleBuilder<T> text(Property<String> textProperty) {
        componentBuilder.bind(passwordField -> passwordField.text, textProperty);
        return this;
    }

    public <F> PasswordFieldMiddleBuilder<T> text(Property<F> source, Converter<F, String> converter) {
        componentBuilder.bind(passwordField -> passwordField.text, source, converter);
        return this;
    }
}

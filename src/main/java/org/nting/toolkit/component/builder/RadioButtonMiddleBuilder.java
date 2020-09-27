package org.nting.toolkit.component.builder;

import org.nting.data.Property;
import org.nting.toolkit.component.RadioButton;

public class RadioButtonMiddleBuilder<T extends ContainerBuilder<?, ?>>
        extends AbstractCheckBoxMiddleBuilder<RadioButton, T> {

    public RadioButtonMiddleBuilder() {
        this(new ComponentBuilder<>(new RadioButton()));
    }

    public RadioButtonMiddleBuilder(ComponentBuilder<RadioButton, T> componentBuilder) {
        super(componentBuilder);
    }

    public RadioButtonMiddleBuilder<T> value(Object value) {
        componentBuilder.set(radioButton -> radioButton.value, value);
        return this;
    }

    public RadioButtonMiddleBuilder<T> value(Property<Object> valueProperty) {
        componentBuilder.bind(radioButton -> radioButton.value, valueProperty);
        return this;
    }
}

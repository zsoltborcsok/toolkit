package org.nting.toolkit.component.builder;

import static org.nting.toolkit.ToolkitServices.unitConverter;

import java.util.function.Function;

import org.nting.data.Property;
import org.nting.toolkit.component.CheckBox;

public class CheckBoxMiddleBuilder<T extends ContainerBuilder<?, ?>> extends AbstractMiddleBuilder<CheckBox, T> {

    public CheckBoxMiddleBuilder() {
        this(new ComponentBuilder<>(new CheckBox()));
    }

    public CheckBoxMiddleBuilder(ComponentBuilder<CheckBox, T> componentBuilder) {
        super(componentBuilder);
    }

    public CheckBoxMiddleBuilder<T> color(int color) {
        componentBuilder.set(checkBox -> checkBox.color, color);
        return this;
    }

    public CheckBoxMiddleBuilder<T> padding(int padding) {
        componentBuilder.set(checkBox -> checkBox.padding, padding);
        return this;
    }

    public CheckBoxMiddleBuilder<T> paddingDluX(float paddingDlu) {
        return padding(unitConverter().dialogUnitXAsPixel(paddingDlu, null));
    }

    public CheckBoxMiddleBuilder<T> paddingDluY(float paddingDlu) {
        return padding(unitConverter().dialogUnitYAsPixel(paddingDlu, null));
    }

    public CheckBoxMiddleBuilder<T> enabled(boolean enabled) {
        componentBuilder.set(checkBox -> checkBox.enabled, enabled);
        return this;
    }

    public CheckBoxMiddleBuilder<T> enabled(Property<Boolean> enabled) {
        componentBuilder.<Boolean> set(checkBox -> checkBox.enabled, enabled);
        return this;
    }

    public CheckBoxMiddleBuilder<T> selected(Boolean selected) {
        componentBuilder.set(checkBox -> checkBox.selected, selected);
        return this;
    }

    public CheckBoxMiddleBuilder<T> selected(Property<Boolean> selected) {
        componentBuilder.<Boolean> set(checkBox -> checkBox.selected, selected);
        return this;
    }

    public CheckBoxMiddleBuilder<T> text(String text) {
        componentBuilder.set(checkBox -> checkBox.text, text);
        return this;
    }

    public CheckBoxMiddleBuilder<T> text(Property<String> textProperty) {
        componentBuilder.<String> set(checkBox -> checkBox.text, textProperty);
        return this;
    }

    public <F> CheckBoxMiddleBuilder<T> text(Property<F> source, Function<F, String> transform) {
        componentBuilder.bind(checkBox -> checkBox.text, source, transform);
        return this;
    }
}

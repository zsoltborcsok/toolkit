package org.nting.toolkit.component.builder;

import static org.nting.toolkit.ToolkitServices.unitConverter;

import java.util.function.Function;

import org.nting.data.Property;
import org.nting.toolkit.component.CheckBox;

public abstract class AbstractCheckBoxMiddleBuilder<C extends CheckBox, T extends ContainerBuilder<?, ?>>
        extends AbstractMiddleBuilder<C, T> {

    public AbstractCheckBoxMiddleBuilder(ComponentBuilder<C, T> componentBuilder) {
        super(componentBuilder);
    }

    public AbstractCheckBoxMiddleBuilder<C, T> color(int color) {
        componentBuilder.set(checkBox -> checkBox.color, color);
        return this;
    }

    public AbstractCheckBoxMiddleBuilder<C, T> padding(int padding) {
        componentBuilder.set(checkBox -> checkBox.padding, padding);
        return this;
    }

    public AbstractCheckBoxMiddleBuilder<C, T> paddingDluX(float paddingDlu) {
        return padding(unitConverter().dialogUnitXAsPixel(paddingDlu, null));
    }

    public AbstractCheckBoxMiddleBuilder<C, T> paddingDluY(float paddingDlu) {
        return padding(unitConverter().dialogUnitYAsPixel(paddingDlu, null));
    }

    public AbstractCheckBoxMiddleBuilder<C, T> enabled(boolean enabled) {
        componentBuilder.set(checkBox -> checkBox.enabled, enabled);
        return this;
    }

    public AbstractCheckBoxMiddleBuilder<C, T> enabled(Property<Boolean> enabled) {
        componentBuilder.bind(checkBox -> checkBox.enabled, enabled);
        return this;
    }

    public AbstractCheckBoxMiddleBuilder<C, T> selected(Boolean selected) {
        componentBuilder.set(checkBox -> checkBox.selected, selected);
        return this;
    }

    public AbstractCheckBoxMiddleBuilder<C, T> selected(Property<Boolean> selected) {
        componentBuilder.bind(checkBox -> checkBox.selected, selected);
        return this;
    }

    public AbstractCheckBoxMiddleBuilder<C, T> text(String text) {
        componentBuilder.set(checkBox -> checkBox.text, text);
        return this;
    }

    public AbstractCheckBoxMiddleBuilder<C, T> text(Property<String> textProperty) {
        componentBuilder.bind(checkBox -> checkBox.text, textProperty);
        return this;
    }

    public <F> AbstractCheckBoxMiddleBuilder<C, T> text(Property<F> source, Function<F, String> transform) {
        componentBuilder.bind(checkBox -> checkBox.text, source, transform);
        return this;
    }
}

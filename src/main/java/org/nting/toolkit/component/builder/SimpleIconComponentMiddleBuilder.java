package org.nting.toolkit.component.builder;

import java.util.function.Function;

import org.nting.data.Property;
import org.nting.toolkit.FontManager.FontSize;
import org.nting.toolkit.component.Icon;
import org.nting.toolkit.component.SimpleIconComponent;

public class SimpleIconComponentMiddleBuilder<T extends ContainerBuilder<?, ?>> {

    private final ComponentBuilder<SimpleIconComponent, T> componentBuilder;

    public SimpleIconComponentMiddleBuilder() {
        this(new ComponentBuilder<>(new SimpleIconComponent(null)));
    }

    public SimpleIconComponentMiddleBuilder(ComponentBuilder<SimpleIconComponent, T> componentBuilder) {
        this.componentBuilder = componentBuilder;
    }

    public SimpleIconComponentMiddleBuilder<T> icon(Icon icon) {
        componentBuilder.set(simpleIconComponent -> simpleIconComponent.icon, icon);
        return this;
    }

    public SimpleIconComponentMiddleBuilder<T> icon(Property<Icon> icon) {
        componentBuilder.<Icon> set(simpleIconComponent -> simpleIconComponent.icon, icon);
        return this;
    }

    public <F> SimpleIconComponentMiddleBuilder<T> icon(Property<F> source, Function<F, Icon> transform) {
        componentBuilder.bind(simpleIconComponent -> simpleIconComponent.icon, source, transform);
        return this;
    }

    public SimpleIconComponentMiddleBuilder<T> fontSize(FontSize fontSize) {
        componentBuilder.set(simpleIconComponent -> simpleIconComponent.fontSize, fontSize);
        return this;
    }

    public SimpleIconComponentMiddleBuilder<T> color(int color) {
        componentBuilder.set(simpleIconComponent -> simpleIconComponent.color, color);
        return this;
    }

    public SimpleIconComponentMiddleBuilder<T> disabledColor(int disabledColor) {
        componentBuilder.set(simpleIconComponent -> simpleIconComponent.disabledColor, disabledColor);
        return this;
    }

    public SimpleIconComponentMiddleBuilder<T> hoverColor(int hoverColor) {
        componentBuilder.set(simpleIconComponent -> simpleIconComponent.hoverColor, hoverColor);
        return this;
    }

    public SimpleIconComponentMiddleBuilder<T> enabled(boolean enabled) {
        componentBuilder.set(simpleIconComponent -> simpleIconComponent.enabled, enabled);
        return this;
    }

    public SimpleIconComponentMiddleBuilder<T> enabled(Property<Boolean> enabled) {
        componentBuilder.<Boolean> set(simpleIconComponent -> simpleIconComponent.enabled, enabled);
        return this;
    }

    public SimpleIconComponentMiddleBuilder<T> zoom(float zoom) {
        componentBuilder.set(simpleIconComponent -> simpleIconComponent.zoom, zoom);
        return this;
    }

    public SimpleIconComponentMiddleBuilder<T> zoom(Property<Float> zoom) {
        componentBuilder.<Float> set(simpleIconComponent -> simpleIconComponent.zoom, zoom);
        return this;
    }

    public ComponentBuilder<SimpleIconComponent, T> pass() {
        return componentBuilder;
    }
}

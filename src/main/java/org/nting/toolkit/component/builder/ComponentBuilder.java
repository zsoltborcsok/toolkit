package org.nting.toolkit.component.builder;

import static org.nting.data.binding.Bindings.BindingStrategy.READ;
import static org.nting.data.binding.Bindings.BindingStrategy.READ_WRITE;
import static org.nting.toolkit.ToolkitServices.fontManager;

import java.util.function.Consumer;
import java.util.function.Function;

import org.nting.data.Property;
import org.nting.data.binding.Binding;
import org.nting.data.binding.BindingList;
import org.nting.data.binding.Bindings;
import org.nting.toolkit.FontManager.FontSize;
import org.nting.toolkit.animation.Behavior;
import org.nting.toolkit.component.AbstractComponent;
import org.nting.toolkit.component.Alignment;
import org.nting.toolkit.component.Orientation;
import org.nting.toolkit.event.KeyListener;
import org.nting.toolkit.event.MouseListener;

import com.google.common.base.Converter;

import playn.core.Font;

// https://theagilejedi.wordpress.com/2016/10/21/nested-fluent-builders-with-java-8/
public class ComponentBuilder<COMPONENT extends AbstractComponent, PARENT_BUILDER extends ContainerBuilder<?, ?>>
        implements Binding {

    private final COMPONENT component;
    private final PARENT_BUILDER parentBuilder;

    private final BindingList bindingList = new BindingList();

    public ComponentBuilder(COMPONENT component) {
        this(component, null);
    }

    protected ComponentBuilder(COMPONENT component, PARENT_BUILDER parentBuilder) {
        this.component = component;
        this.parentBuilder = parentBuilder;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> tooltipText(String tooltipText) {
        component.setTooltipText(tooltipText);
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> tooltipText(Property<String> tooltipText) {
        addBinding(Bindings.bind(READ, tooltipText, component.tooltipText));
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> tooltipLocation(Alignment alignment, Orientation orientation) {
        component.setTooltipLocation(alignment, orientation);
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> visible(boolean visible) {
        component.setVisible(visible);
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> visible(Property<Boolean> visible) {
        addBinding(Bindings.bind(READ, visible, component.visible));
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> focusable(boolean focusable) {
        component.setFocusable(focusable);
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> focusNeutral(boolean focusNeutral) {
        component.setFocusNeutral(focusNeutral);
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> adKeyListener(KeyListener keyListener) {
        addBinding(Bindings.asBinding(component.addKeyListener(keyListener)));
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> addMouseListener(MouseListener mouseListener) {
        addBinding(Bindings.asBinding(component.addMouseListener(mouseListener)));
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> id(Object id) {
        component.setId(id);
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> clip(boolean clip) {
        component.setClip(clip);
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> addBehavior(Behavior behavior) {
        component.addBehavior(behavior);
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> font(Font font) {
        component.font.setValue(font);
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> font(FontSize fontSize) {
        return font(fontManager().getFont(fontSize));
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> font(FontSize fontSize, Font.Style style) {
        return font(fontManager().getFont(fontSize, style));
    }

    /** May not be defined for the current component */
    public ComponentBuilder<COMPONENT, PARENT_BUILDER> color(int color) {
        return set("color", color);
    }

    /** May not be defined for the current component */
    public ComponentBuilder<COMPONENT, PARENT_BUILDER> backgroundColor(int color) {
        return set("backgroundColor", color);
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> setValueOfComponentsAt(Object propertyId, Object value,
            Object... constraints) {
        for (Object constraint : constraints) {
            AbstractComponent component = this.component.componentAt(constraint);
            component.getProperty(propertyId).setValue(value);
        }

        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> set(String propertyName, Object value) {
        component.set(propertyName, value);
        return this;
    }

    public <T> ComponentBuilder<COMPONENT, PARENT_BUILDER> set(Function<COMPONENT, Property<T>> propertyFunction,
            T value) {
        propertyFunction.apply(component).setValue(value);
        return this;
    }

    public <T> ComponentBuilder<COMPONENT, PARENT_BUILDER> set(String propertyName, Property<T> source) {
        addBinding(Bindings.bind(READ, source, component.getProperty(propertyName)));
        return this;
    }

    public <T> ComponentBuilder<COMPONENT, PARENT_BUILDER> set(Function<COMPONENT, Property<T>> propertyFunction,
            Property<T> source) {
        addBinding(Bindings.bind(READ, source, propertyFunction.apply(component)));
        return this;
    }

    public <T> ComponentBuilder<COMPONENT, PARENT_BUILDER> bind(String propertyName, Property<T> source) {
        addBinding(Bindings.bind(READ_WRITE, source, component.getProperty(propertyName)));
        return this;
    }

    public <T> ComponentBuilder<COMPONENT, PARENT_BUILDER> bind(Function<COMPONENT, Property<T>> propertyFunction,
            Property<T> source) {
        addBinding(Bindings.bind(READ_WRITE, source, propertyFunction.apply(component)));
        return this;
    }

    public <F, T> ComponentBuilder<COMPONENT, PARENT_BUILDER> bind(String propertyName, Property<F> source,
            Converter<F, T> converter) {
        addBinding(Bindings.bind(READ_WRITE, source, component.getProperty(propertyName), converter));
        return this;
    }

    public <F, T> ComponentBuilder<COMPONENT, PARENT_BUILDER> bind(Function<COMPONENT, Property<T>> propertyFunction,
            Property<F> source, Converter<F, T> converter) {
        addBinding(Bindings.bind(READ_WRITE, source, propertyFunction.apply(component), converter));
        return this;
    }

    public <F, T> ComponentBuilder<COMPONENT, PARENT_BUILDER> bind(String propertyName, Property<F> source,
            Function<F, T> transform) {
        addBinding(Bindings.bind(source, component.getProperty(propertyName), transform));
        return this;
    }

    public <F, T> ComponentBuilder<COMPONENT, PARENT_BUILDER> bind(Function<COMPONENT, Property<T>> propertyFunction,
            Property<F> source, Function<F, T> transform) {
        addBinding(Bindings.bind(source, propertyFunction.apply(component), transform));
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> process(Consumer<COMPONENT> consumer) {
        component.process(consumer);
        return this;
    }

    public void addBinding(Binding binding) {
        bindingList.addBinding(binding);
    }

    public COMPONENT build() {
        return component;
    }

    public PARENT_BUILDER end() {
        return parentBuilder;
    }

    @Override
    public void unbind() {
        bindingList.unbind();
    }

    public COMPONENT getComponent() {
        return component;
    }
}

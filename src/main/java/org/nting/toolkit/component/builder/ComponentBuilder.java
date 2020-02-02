package org.nting.toolkit.component.builder;

import static org.nting.toolkit.ToolkitServices.fontManager;

import java.util.function.Consumer;

import org.nting.data.binding.Binding;
import org.nting.data.binding.BindingList;
import org.nting.toolkit.FontManager.FontSize;
import org.nting.toolkit.animation.Behavior;
import org.nting.toolkit.component.AbstractComponent;
import org.nting.toolkit.component.Alignment;
import org.nting.toolkit.component.Orientation;
import org.nting.toolkit.event.KeyListener;
import org.nting.toolkit.event.MouseListener;

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

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> tooltipText(String text) {
        component.setTooltipText(text);
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

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> focusable(boolean focusable) {
        component.setFocusable(focusable);
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> focusNeutral(boolean focusNeutral) {
        component.setFocusNeutral(focusNeutral);
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> adKeyListener(KeyListener keyListener) {
        component.addKeyListener(keyListener);
        return this;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> addMouseListener(MouseListener mouseListener) {
        component.addMouseListener(mouseListener);
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

    /** May not be defined for the current component */
    public ComponentBuilder<COMPONENT, PARENT_BUILDER> enabled(boolean enabled) {
        return set("enabled", enabled);
    }

    /** May not be defined for the current component */
    public ComponentBuilder<COMPONENT, PARENT_BUILDER> text(String text) {
        return set("text", text);
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> set(String propertyName, Object value) {
        component.set(propertyName, value);
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

    protected COMPONENT getComponent() {
        return component;
    }
}

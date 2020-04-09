package org.nting.toolkit.component.builder;

import static org.nting.toolkit.ToolkitServices.unitConverter;
import static org.nting.toolkit.ui.stone.Content.EMPTY_CONTENT;

import java.util.function.Function;

import org.nting.data.Property;
import org.nting.data.binding.Bindings;
import org.nting.toolkit.FontManager;
import org.nting.toolkit.component.Button;
import org.nting.toolkit.component.Icon;
import org.nting.toolkit.event.ActionListener;
import org.nting.toolkit.event.Actions;
import org.nting.toolkit.ui.stone.Content;

public class ButtonMiddleBuilder<T extends ContainerBuilder<?, ?>> extends AbstractMiddleBuilder<Button, T> {

    public ButtonMiddleBuilder() {
        this(new ComponentBuilder<>(new Button()));
    }

    public ButtonMiddleBuilder(ComponentBuilder<Button, T> componentBuilder) {
        super(componentBuilder);
    }

    public ButtonMiddleBuilder<T> color(int color) {
        componentBuilder.set(button -> button.color, color);
        return this;
    }

    public ButtonMiddleBuilder<T> padding(int padding) {
        componentBuilder.set(button -> button.padding, padding);
        return this;
    }

    public ButtonMiddleBuilder<T> paddingDluX(float paddingDlu) {
        return padding(unitConverter().dialogUnitXAsPixel(paddingDlu, null));
    }

    public ButtonMiddleBuilder<T> paddingDluY(float paddingDlu) {
        return padding(unitConverter().dialogUnitYAsPixel(paddingDlu, null));
    }

    public ButtonMiddleBuilder<T> enabled(boolean enabled) {
        componentBuilder.set(button -> button.enabled, enabled);
        return this;
    }

    public ButtonMiddleBuilder<T> enabled(Property<Boolean> enabled) {
        componentBuilder.bind(button -> button.enabled, enabled);
        return this;
    }

    public ButtonMiddleBuilder<T> actionListener(ActionListener actionListener) {
        Actions actions = componentBuilder.getComponent().actions();
        actions.addActionListener(actionListener);
        componentBuilder.addBinding(Bindings.asBinding(() -> actions.removeActionListener(actionListener)));
        return this;
    }

    public ButtonMiddleBuilder<T> actionListener(ActionListener actionListener, Object actionId) {
        Actions actions = componentBuilder.getComponent().actions();
        actions.addActionListener(actionListener, actionId);
        componentBuilder.addBinding(Bindings.asBinding(() -> actions.removeActionListener(actionListener)));
        return this;
    }

    public ButtonMiddleBuilder<T> image(Content image) {
        componentBuilder.set(button -> button.image, image);
        return this;
    }

    public ButtonMiddleBuilder<T> image(Icon icon, FontManager.FontSize fontSize) {
        return image(icon.getContent(fontSize));
    }

    public ButtonMiddleBuilder<T> image(Property<Icon> icon, FontManager.FontSize fontSize) {
        return image(icon, anIcon -> anIcon != null ? anIcon.getContent(fontSize) : EMPTY_CONTENT);
    }

    public <F> ButtonMiddleBuilder<T> image(Property<F> source, Function<F, Content> transform) {
        componentBuilder.bind(button -> button.image, source, transform);
        return this;
    }

    public ButtonMiddleBuilder<T> text(String text) {
        componentBuilder.set(button -> button.text, text);
        return this;
    }

    public ButtonMiddleBuilder<T> text(Property<String> textProperty) {
        componentBuilder.bind(button -> button.text, textProperty);
        return this;
    }

    public <F> ButtonMiddleBuilder<T> text(Property<F> source, Function<F, String> transform) {
        componentBuilder.bind(button -> button.text, source, transform);
        return this;
    }
}

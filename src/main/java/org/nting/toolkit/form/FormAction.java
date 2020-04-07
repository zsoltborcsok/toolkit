package org.nting.toolkit.form;

import static org.nting.toolkit.ToolkitServices.fontManager;
import static org.nting.toolkit.ToolkitServices.toolkitManager;

import org.nting.data.Property;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.FontManager.FontSize;
import org.nting.toolkit.component.AbstractComponent;
import org.nting.toolkit.component.Icon;
import org.nting.toolkit.component.builder.ButtonMiddleBuilder;
import org.nting.toolkit.event.ActionListener;

import playn.core.Font;

public abstract class FormAction implements ActionListener {

    public final Property<Icon> icon;
    public final Property<String> text;
    public final Property<String> tooltip;

    public final Property<Boolean> enabled = new ObjectProperty<>(true);
    public final Property<Boolean> visible = new ObjectProperty<>(true);

    public FormAction(Icon icon, String text) {
        this(icon, text, null);
    }

    public FormAction(Icon icon, String text, String tooltip) {
        this.icon = new ObjectProperty<>(icon);
        this.text = new ObjectProperty<>(text);
        this.tooltip = new ObjectProperty<>(tooltip);
    }

    public void configureButton(ButtonMiddleBuilder<?> buttonMiddleBuilder) {
        FontSize fontSize = fontManager().getFontSize(
                toolkitManager().getStyleInjector().getPropertyValue(AbstractComponent.class.getName(), "font"));
        buttonMiddleBuilder.image(icon, fontSize.increase()).text(text).enabled(enabled).pass().tooltipText(tooltip)
                .visible(visible).font(fontSize, Font.Style.BOLD);
    }
}

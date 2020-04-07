package org.nting.toolkit.form;

import static org.nting.toolkit.FontManager.FontSize.LARGE_FONT;
import static org.nting.toolkit.FontManager.FontSize.SMALL_FONT;
import static org.nting.toolkit.component.ScrollComponent.ScrollBarPolicy.AS_NEEDED;
import static org.nting.toolkit.component.ScrollComponent.ScrollBarPolicy.NEVER;
import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.layout.FormLayout.xyw;
import static playn.core.Font.Style.ITALIC;

import java.util.Optional;

import org.nting.data.Property;
import org.nting.data.binding.Binding;
import org.nting.toolkit.Component;
import org.nting.toolkit.component.AbstractComponent;
import org.nting.toolkit.component.Orientation;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.ScrollPane;
import org.nting.toolkit.component.Separator;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.component.builder.LabelMiddleBuilder;
import org.nting.toolkit.component.builder.LabelWithEllipsisMiddleBuilder;
import org.nting.toolkit.layout.FormLayout;
import org.nting.toolkit.layout.ZLayout;
import org.nting.toolkit.util.EnumUtils;

import playn.core.Font;

public class ReadOnlyFormBuilder {

    private final ContainerBuilder<Panel, ?> panelBuilder;

    private ContainerBuilder<Panel, ?> zLayoutBuilder;
    private ContainerBuilder<Panel, ?> fieldGroupBuilder;

    public ReadOnlyFormBuilder() {
        panelBuilder = panelBuilder("7dlu, 0px:grow, 7dlu", "4dlu");
    }

    // Show at least 50 chars per column
    public void newZLayout() {
        panelBuilder.formLayout().addRow("pref");
        zLayoutBuilder = panelBuilder.addPanel(xy(1, panelBuilder.formLayout().lastRow()))
                .layoutManager(new ZLayout(200, 8));
    }

    public void addSeparateComponent(String caption, AbstractComponent component, FormAction... actions) {
        panelBuilder.formLayout().addRow("pref");
        if (actions.length == 0) {
            panelBuilder.addLabel(xy(1, panelBuilder.formLayout().lastRow())).text(caption).pass().font(LARGE_FONT,
                    ITALIC);
        } else {
            ContainerBuilder<Panel, ?> panelBuilder = this.panelBuilder
                    .addPanel(xy(1, this.panelBuilder.formLayout().lastRow())).layoutManager("pref, 0px:grow", "pref");
            panelBuilder.addLabel(xy(0, 0)).text(caption).pass().font(LARGE_FONT, ITALIC);
            for (FormAction action : actions) {
                panelBuilder.formLayout().addColumn("3dlu").addColumn("pref");
                action.configureButton(panelBuilder.addButton(xy(panelBuilder.formLayout().lastColumn(), 0)));
            }
        }
        panelBuilder.formLayout().addRow("2dlu").addRow("1px");
        panelBuilder.addComponent(new Separator(Orientation.HORIZONTAL), xy(1, panelBuilder.formLayout().lastRow()));
        panelBuilder.formLayout().addRow("4dlu").addRow("pref");
        panelBuilder.addComponent(component, xy(1, panelBuilder.formLayout().lastRow()));
        panelBuilder.formLayout().addRow("8dlu");
    }

    public Optional<LabelWithEllipsisMiddleBuilder<?>> addSeparateField(String caption, Property<String> property) {
        if (isNullOrEmpty(property.getValue())) {
            return Optional.empty();
        }
        panelBuilder.formLayout().addRow("pref");
        ContainerBuilder<Panel, ?> formBuilder = panelBuilder.addPanel(xy(1, panelBuilder.formLayout().lastRow()))
                .layoutManager("right:max(pref;64dlu), 3dlu, 0px:grow", "pref, 8dlu");
        addFieldCaption(formBuilder, xy(0, 0)).text(caption);
        return Optional.of(formBuilder.addLabelWithEllipsis(xy(2, 0)).text(property));
    }

    public void addFieldGroupCaption(String caption, FormAction... actions) {
        if (fieldGroupBuilder != null) {
            fieldGroupBuilder.formLayout().addRow("8dlu");
        }
        fieldGroupBuilder = zLayoutBuilder.addPanel(null).layoutManager("right:max(pref;64dlu), 3dlu, 0px:grow", "");

        fieldGroupBuilder.formLayout().addRow("pref");
        if (actions.length == 0) {
            fieldGroupBuilder.addLabel(xyw(0, fieldGroupBuilder.formLayout().lastRow(), 3)).text(caption).pass()
                    .font(LARGE_FONT, ITALIC);
        } else {
            ContainerBuilder<Panel, ?> fieldGroupBuilder = this.fieldGroupBuilder
                    .addPanel(xyw(0, this.fieldGroupBuilder.formLayout().lastRow(), 3))
                    .layoutManager("pref, 0px:grow", "pref");
            fieldGroupBuilder.addLabel(xy(0, 0)).text(caption).pass().font(LARGE_FONT, ITALIC);
            for (FormAction action : actions) {
                fieldGroupBuilder.formLayout().addColumn("3dlu").addColumn("pref");
                action.configureButton(fieldGroupBuilder.addButton(xy(fieldGroupBuilder.formLayout().lastColumn(), 0)));
            }
        }
        fieldGroupBuilder.formLayout().addRow("2dlu").addRow("1px");
        fieldGroupBuilder.addComponent(new Separator(Orientation.HORIZONTAL),
                xyw(0, fieldGroupBuilder.formLayout().lastRow(), 3));
    }

    public void checkFieldGroupVisibility() {
        int visibleComponentCount = 0;
        for (Component component : fieldGroupBuilder.getComponent().getComponents()) {
            if (component.isVisible()) {
                visibleComponentCount++;
            }
        }
        if (visibleComponentCount <= 2) {
            zLayoutBuilder.getComponent().removeComponent(fieldGroupBuilder.getComponent());
        }
    }

    public Optional<LabelMiddleBuilder<?>> addField(String caption, Property<String> property) {
        if (isNullOrEmpty(property.getValue())) {
            return Optional.empty();
        }
        fieldGroupBuilder.formLayout().addRow("4dlu").addRow("baseline:pref");
        addFieldCaption(fieldGroupBuilder, xy(0, fieldGroupBuilder.formLayout().lastRow())).text(caption);
        return Optional.of(fieldGroupBuilder.addLabel(xy(2, fieldGroupBuilder.formLayout().lastRow())).text(property));
    }

    @SuppressWarnings("rawtypes")
    public <T extends Enum> Optional<LabelMiddleBuilder<?>> addEnumField(String caption, Property<T> property) {
        return addField(caption, EnumUtils.asHumanReadableName(property));
    }

    public Optional<LabelMiddleBuilder<?>> addFieldWithoutCaption(Property<String> property) {
        if (isNullOrEmpty(property.getValue())) {
            return Optional.empty();
        }
        fieldGroupBuilder.formLayout().addRow("4dlu").addRow("pref");
        return Optional.of(fieldGroupBuilder.addMultiLineLabel(xyw(0, fieldGroupBuilder.formLayout().lastRow(), 3))
                .text(property));
    }

    // Not preferred on ANDROID or ROBOVM platforms
    public Optional<LabelWithEllipsisMiddleBuilder<?>> addFieldWithEllipsis(String caption, Property<String> property) {
        if (isNullOrEmpty(property.getValue())) {
            return Optional.empty();
        }
        fieldGroupBuilder.formLayout().addRow("4dlu").addRow("baseline:pref");
        addFieldCaption(fieldGroupBuilder, xy(0, fieldGroupBuilder.formLayout().lastRow())).text(caption);
        return Optional.of(
                fieldGroupBuilder.addLabelWithEllipsis(xy(2, fieldGroupBuilder.formLayout().lastRow())).text(property));
    }

    public Optional<LabelMiddleBuilder<?>> addMultiLineField(String caption, Property<String> property) {
        if (isNullOrEmpty(property.getValue())) {
            return Optional.empty();
        }
        fieldGroupBuilder.formLayout().addRow("4dlu").addRow("baseline:pref");
        addFieldCaption(fieldGroupBuilder, xy(0, fieldGroupBuilder.formLayout().lastRow())).text(caption);
        return Optional.of(
                fieldGroupBuilder.addMultiLineLabel(xy(2, fieldGroupBuilder.formLayout().lastRow())).text(property));
    }

    public void addComponent(String caption, AbstractComponent component) {
        if (0 <= component.getBaselinePosition()) {
            fieldGroupBuilder.formLayout().addRow("4dlu").addRow("baseline:pref");
        } else {
            fieldGroupBuilder.formLayout().addRow("4dlu").addRow("center:pref");
        }
        addFieldCaption(fieldGroupBuilder, xy(0, fieldGroupBuilder.formLayout().lastRow())).text(caption);
        fieldGroupBuilder.addComponent(component, xy(2, fieldGroupBuilder.formLayout().lastRow()));
    }

    public void addGap() {
        if (fieldGroupBuilder != null) {
            fieldGroupBuilder.formLayout().addRow("8dlu");
        }
    }

    public void clear() {
        panelBuilder.removeAllComponents();
        panelBuilder.formLayout().clearRows().addRow("4dlu");
    }

    public Component component() {
        return panelBuilder.formLayout().addRow("7dlu").done().build();
    }

    public Component componentOnScrollPane() {
        return panelBuilder(new FormLayout("0px:grow", "0px:grow"))
                .addComponent(new ScrollPane(component(), AS_NEEDED, NEVER), xy(0, 0)).build();
    }

    public void addBinding(Binding binding) {
        panelBuilder.addBinding(binding);
    }

    public static boolean isNullOrEmpty(Object object) {
        return object == null || "".equals(object);
    }

    public LabelMiddleBuilder<?> addFieldCaption(ContainerBuilder<Panel, ?> containerBuilder, Object constraints) {
        LabelMiddleBuilder<?> labelMiddleBuilder = containerBuilder.addLabel(constraints);
        labelMiddleBuilder.pass().font(SMALL_FONT, Font.Style.BOLD).color(0xFF5E6977);
        return labelMiddleBuilder;
    }
}

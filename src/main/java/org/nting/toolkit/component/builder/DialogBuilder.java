package org.nting.toolkit.component.builder;

import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;

import org.nting.toolkit.Component;
import org.nting.toolkit.component.Button;
import org.nting.toolkit.component.Dialog;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.layout.FormLayout;
import org.nting.toolkit.layout.LayoutManager;

public class DialogBuilder {

    private final ContainerBuilder<Panel, ?> contentBuilder;
    private final Dialog dialog;

    public DialogBuilder(String title, String columns, String rows) {
        this(title, new FormLayout(columns, rows));
    }

    public DialogBuilder(String title, LayoutManager layoutManager) {
        contentBuilder = panelBuilder(layoutManager);
        dialog = new Dialog(title, contentBuilder.getComponent());
    }

    public ContainerBuilder<Panel, ?> content() {
        return contentBuilder;
    }

    public ButtonMiddleBuilder<? extends ContainerBuilder<Panel, ?>> addDefaultButton(Object constraints) {
        ButtonMiddleBuilder<? extends ContainerBuilder<Panel, ?>> buttonBuilder = contentBuilder.addButton(constraints);
        dialog.setDefaultButton(buttonBuilder.pass().getComponent());
        return buttonBuilder;
    }

    public DialogBuilder defaultButton(Button defaultButton) {
        dialog.setDefaultButton(defaultButton);
        return this;
    }

    public DialogBuilder showCentered() {
        dialog.showCentered();
        return this;
    }

    public DialogBuilder showCentered(Component componentToFocus) {
        dialog.showCentered();
        componentToFocus.requestFocus();
        return this;
    }

    public Dialog getDialog() {
        return dialog;
    }
}

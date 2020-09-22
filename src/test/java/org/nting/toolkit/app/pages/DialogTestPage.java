package org.nting.toolkit.app.pages;

import static org.nting.toolkit.FontManager.FontSize.LARGE_FONT;
import static org.nting.toolkit.component.Orientation.HORIZONTAL;
import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.layout.FormLayout.xyw;
import static playn.core.Font.Style.PLAIN;

import org.nting.toolkit.Component;
import org.nting.toolkit.app.IPageFactory;
import org.nting.toolkit.app.Pages;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.Separator;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.component.builder.DialogBuilder;
import org.nting.toolkit.component.builder.FormLayoutMiddleBuilder;

public class DialogTestPage implements IPageFactory {

    @Override
    public PageSize getPageSize() {
        return PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent(Pages pages) {
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder("7dlu, pref, 7dlu", "7dlu");
        FormLayoutMiddleBuilder<? extends ContainerBuilder<Panel, ?>> layoutBuilder = panelBuilder.formLayout();

        layoutBuilder.addRow("pref").done() //
                .addButton(xy(1, layoutBuilder.lastRow())).text("Simple OK/Cancel Dialog")
                .actionListener(actionEvent -> showSimpleOkCancelDialog());
        return layoutBuilder.addRow("7dlu").done().build();
    }

    private void showSimpleOkCancelDialog() {
        DialogBuilder dialogBuilder = new DialogBuilder("SAMPLE",
                "7dlu, pref, max(pref;40dlu), 4dlu, max(pref;40dlu), 7dlu",
                "7dlu, pref, 4dlu, 1px, 4dlu, pref, 7dlu, pref, 7dlu");
        dialogBuilder.content().addLabel(xyw(1, 1, 4)).text("Title").pass().font(LARGE_FONT, PLAIN).end() //
                .addComponent(new Separator(HORIZONTAL), xyw(1, 3, 4)).end() //
                .addLabel(xyw(1, 5, 4)).text("A very long message which needs much space...");
        dialogBuilder.addDefaultButton(xy(2, 7)).text("OK")
                .actionListener(actionEvent -> dialogBuilder.getDialog().close());
        dialogBuilder.content().addButton(xy(4, 7)).text("Cancel")
                .actionListener(actionEvent -> dialogBuilder.getDialog().close());
        dialogBuilder.showCentered(dialogBuilder.getDialog().getDefaultButton());
    }
}

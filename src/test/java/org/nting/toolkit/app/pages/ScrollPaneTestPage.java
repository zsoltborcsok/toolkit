package org.nting.toolkit.app.pages;

import static org.nting.toolkit.component.Orientation.HORIZONTAL;
import static org.nting.toolkit.component.ScrollComponent.ScrollBarPolicy.ALWAYS;
import static org.nting.toolkit.component.ScrollComponent.ScrollBarPolicy.NEVER;
import static org.nting.toolkit.component.TextAlignment.CENTER;
import static org.nting.toolkit.component.TextAlignment.RIGHT;
import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.layout.FormLayout.xyw;

import org.nting.toolkit.Component;
import org.nting.toolkit.app.IPageFactory;
import org.nting.toolkit.app.Pages;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.ScrollPane;
import org.nting.toolkit.component.Separator;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.component.builder.FormLayoutMiddleBuilder;

public class ScrollPaneTestPage implements IPageFactory {

    @Override
    public PageSize getPageSize() {
        return PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent(Pages pages) {
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder(
                "7dlu, pref, 4dlu, max(pref;120dlu), 10dlu, pref, 4dlu, max(pref;120dlu), 7dlu", "7dlu");
        FormLayoutMiddleBuilder<? extends ContainerBuilder<Panel, ?>> layoutBuilder = panelBuilder.formLayout();

        layoutBuilder.addRow("pref").done() //
                .addComponent(new Separator(HORIZONTAL), xyw(1, layoutBuilder.lastRow(), 7));
        layoutBuilder.addRow("24dlu").addRow("center:pref").done() //
                .addLabel(xy(1, layoutBuilder.lastRow())).text("Company:").alignment(RIGHT).pass().end() //
                .addTextField(xyw(3, layoutBuilder.lastRow(), 5));
        layoutBuilder.addRow("24dlu").addRow("center:pref").done() //
                .addLabel(xy(1, layoutBuilder.lastRow())).text("Contact:").alignment(RIGHT).pass().end() //
                .addTextField(xyw(3, layoutBuilder.lastRow(), 5));
        layoutBuilder.addRow("24dlu").addRow("center:pref").done() //
                .addLabel(xy(1, layoutBuilder.lastRow())).text("Order No:").alignment(RIGHT).pass().end() //
                .addTextField(xy(3, layoutBuilder.lastRow()));

        layoutBuilder.addRow("24dlu").addRow("pref").done() //
                .addComponent(new Separator(HORIZONTAL), xyw(1, layoutBuilder.lastRow(), 7));
        layoutBuilder.addRow("24dlu").addRow("center:pref").done() //
                .addLabel(xy(1, layoutBuilder.lastRow())).text("Name:").alignment(CENTER).pass().end() //
                .addTextField(xyw(3, layoutBuilder.lastRow(), 5));
        layoutBuilder.addRow("24dlu").addRow("center:pref").done() //
                .addLabel(xy(1, layoutBuilder.lastRow())).text("Reference No:").alignment(CENTER).pass().end() //
                .addTextField(xy(3, layoutBuilder.lastRow()));
        layoutBuilder.addRow("24dlu").addRow("center:pref").done() //
                .addLabel(xy(1, layoutBuilder.lastRow())).text("Status:").alignment(CENTER).pass().end();
        // .addComboBox(false, true, xy(3, layoutBuilder.lastRow()));

        layoutBuilder.addRow("24dlu").addRow("pref").done() //
                .addComponent(new Separator(HORIZONTAL), xyw(1, layoutBuilder.lastRow(), 7));
        layoutBuilder.addRow("24dlu").addRow("center:pref").done() //
                .addLabel(xy(1, layoutBuilder.lastRow())).text("Shipyard:").pass().end()
                .addTextField(xyw(3, layoutBuilder.lastRow(), 5));
        layoutBuilder.addRow("24dlu").addRow("center:pref").done() //
                .addLabel(xy(1, layoutBuilder.lastRow())).text("Register No:").pass().end() //
                .addTextField(xy(3, layoutBuilder.lastRow())).pass().end() //
                .addLabel(xy(5, layoutBuilder.lastRow())).text("Hull No:").pass().end() //
                .addTextField(xy(7, layoutBuilder.lastRow()));
        layoutBuilder.addRow("24dlu").addRow("center:pref").done() //
                .addLabel(xy(1, layoutBuilder.lastRow())).text("Project type:");
        // .addComboBox(false, true, xy(3, layoutBuilder.lastRow()));

        layoutBuilder.addRow("7dlu");

        return new ScrollPane(panelBuilder.build(), ALWAYS, NEVER);
    }
}

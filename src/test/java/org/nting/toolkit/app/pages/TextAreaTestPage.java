package org.nting.toolkit.app.pages;

import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.FormLayout.xy;

import org.nting.toolkit.Component;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.layout.FormLayout;

public class TextAreaTestPage implements ITestPage {

    private static final String TEST = "0\n-------------------\nTextAreaTestView\n-------------------\n"
            + "1\n-------------------\nTextAreaTestView\n-------------------\n"
            + "2\n-------------------\nTextAreaTestView\n-------------------\n"
            + "3\n-------------------\nTextAreaTestView\n-------------------\n"
            + "4\n-------------------\nTextAreaTestView\n-------------------\n"
            + "5\n-------------------\nTextAreaTestView\n-------------------\n"
            + "6\n-------------------\nTextAreaTestView\n-------------------\n"
            + "7\n-------------------\nTextAreaTestView\n-------------------\n"
            + "8\n-------------------\nTextAreaTestView\n-------------------\n9";

    @Override
    public PageSize getPageSize() {
        return PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent() {
        FormLayout formLayout = new FormLayout("7dlu, 0px:grow, 7dlu", "7dlu, pref, 4dlu, pref, 7dlu:grow");
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder(formLayout);

        ContainerBuilder<Panel, ?> nextPanelBuilder = panelBuilder.addPanel(xy(1, 1))
                .layoutManager(new FormLayout("pref", "pref"));
        nextPanelBuilder.addTextArea(xy(0, 0)).rows(25).columns(60).text(TEST);

        nextPanelBuilder = panelBuilder.addPanel(xy(1, 3))
                .layoutManager(new FormLayout("0px:grow, 4dlu, 0px:grow", "pref"));
        nextPanelBuilder.addTextArea(xy(0, 0)).rows(8).text("-------------------\nDisabled\n-------------------\n")
                .enabled(false);
        nextPanelBuilder.addTextArea(xy(2, 0)).rows(8).text("-------------------\nEnabled\n-------------------\n");

        return panelBuilder.build();
    }
}

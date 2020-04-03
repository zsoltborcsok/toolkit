package org.nting.toolkit.app.pages;

import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.FormLayout.xy;

import org.nting.toolkit.Component;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.AbstractComponent;
import org.nting.toolkit.component.ScrollPane;

public interface ITestPage {

    PageSize getPageSize();

    Component createContent();

    default Component wrap(AbstractComponent component) {
        return new ScrollPane(panelBuilder("7dlu, pref:grow, 7dlu", "7dlu, pref:grow, 7dlu")
                .addComponent(component, xy(1, 1)).end().build());
    }
}

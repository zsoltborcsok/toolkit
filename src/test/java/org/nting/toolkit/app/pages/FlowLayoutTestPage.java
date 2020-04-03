package org.nting.toolkit.app.pages;

import static org.nting.toolkit.FontManager.FontSize.LARGE_FONT;
import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.ui.style.material.ButtonPropertyIds.RAISED;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.blue_500;

import org.nting.toolkit.Component;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.FontIcon;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.layout.FlowLayout;

public class FlowLayoutTestPage implements ITestPage {

    @Override
    public PageSize getPageSize() {
        return PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent() {
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder(new FlowLayout(4, 4, true));
        for (int i = 0; i < 20; i++) {
            panelBuilder.addButton(null).text("Split button " + i).image(FontIcon.CLOSE, LARGE_FONT).pass()
                    .color(blue_500).set(RAISED.toString(), true);
        }
        return panelBuilder.build();
    }
}

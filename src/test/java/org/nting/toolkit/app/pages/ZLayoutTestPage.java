package org.nting.toolkit.app.pages;

import static org.nting.toolkit.component.ScrollComponent.ScrollBarPolicy.AS_NEEDED;
import static org.nting.toolkit.component.ScrollComponent.ScrollBarPolicy.NEVER;
import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.blue_grey_100;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.blue_grey_200;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.blue_grey_300;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.blue_grey_400;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.blue_grey_50;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.blue_grey_500;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.blue_grey_600;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.blue_grey_700;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.blue_grey_800;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.blue_grey_900;

import org.nting.toolkit.Component;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.ScrollPane;
import org.nting.toolkit.layout.AbsoluteLayout;
import org.nting.toolkit.layout.ZLayout;

public class ZLayoutTestPage implements ITestPage {

    @Override
    public PageSize getPageSize() {
        return PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent() {
        Panel panel = new Panel(new ZLayout(200, 1, false));
        panel.addComponent(newComponent(150, 150, blue_grey_900));
        panel.addComponent(newComponent(150, 135, blue_grey_800));
        panel.addComponent(newComponent(150, 120, blue_grey_700));
        panel.addComponent(newComponent(150, 105, blue_grey_600));
        panel.addComponent(newComponent(150, 90, blue_grey_500));
        panel.addComponent(newComponent(150, 75, blue_grey_400));
        panel.addComponent(newComponent(150, 60, blue_grey_300));
        panel.addComponent(newComponent(150, 45, blue_grey_200));
        panel.addComponent(newComponent(150, 30, blue_grey_100));
        panel.addComponent(newComponent(150, 15, blue_grey_50));

        return new ScrollPane(panelBuilder("7dlu, pref:grow, 7dlu", "7dlu, pref:grow, 7dlu")
                .addComponent(panel, xy(1, 1)).end().build(), AS_NEEDED, NEVER);
    }

    private Component newComponent(int width, int height, int backgroundColor) {
        return new Panel(new AbsoluteLayout(width, height))
                .<Panel> process(p -> p.backgroundColor.setValue(backgroundColor));
    }
}

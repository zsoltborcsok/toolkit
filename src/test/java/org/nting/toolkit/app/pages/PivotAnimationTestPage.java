package org.nting.toolkit.app.pages;

import static org.nting.toolkit.FontManager.FontSize.LARGE_FONT;
import static org.nting.toolkit.FontManager.FontSize.SMALL_FONT;
import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.layout.PivotAnimationLayout.pivotLayout;
import static org.nting.toolkit.util.ToolkitUtils.getComponentById;
import static playn.core.Font.Style.BOLD;

import org.nting.toolkit.Component;
import org.nting.toolkit.animation.Easing;
import org.nting.toolkit.app.IPageFactory;
import org.nting.toolkit.app.Pages;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.layout.FormLayout;
import org.nting.toolkit.layout.PivotAnimationLayout;

public class PivotAnimationTestPage implements IPageFactory {

    @Override
    public Pages.PageSize getPageSize() {
        return Pages.PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent(Pages pages) {
        PivotAnimationLayout pivotAnimationLayout = pivotLayout(new FormLayout("left:pref", "pref, pref, pref"));
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder(pivotAnimationLayout);

        panelBuilder.addLabel(xy(0, 0)).text("Project of the year").pass().font(LARGE_FONT).id("L1");
        panelBuilder.addLabel(xy(0, 1)).text("Finished").pass().font(SMALL_FONT, BOLD).color(0xFF808080).id("L2");
        panelBuilder.addLabel(xy(0, 2)).text("Created on 25-12-2015").pass().font(SMALL_FONT).color(0xFF808080)
                .id("L3");
        pivotAnimationLayout.translateChildX(getComponentById(panelBuilder.build(), "L1"), 200f, 300,
                Easing.REGULAR_IN);
        pivotAnimationLayout.translateChildX(getComponentById(panelBuilder.build(), "L2"), 200f, 350,
                Easing.REGULAR_IN);
        pivotAnimationLayout.translateChildX(getComponentById(panelBuilder.build(), "L3"), 200f, 400,
                Easing.REGULAR_IN);

        return wrap(panelBuilder.build());
    }
}

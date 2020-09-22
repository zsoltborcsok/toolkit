package org.nting.toolkit.app.pages;

import org.nting.toolkit.Component;
import org.nting.toolkit.FontManager.FontSize;
import org.nting.toolkit.app.IPageFactory;
import org.nting.toolkit.app.Pages;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.FontIcon;
import org.nting.toolkit.component.Label;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.ScrollComponent;
import org.nting.toolkit.component.ScrollPane;
import org.nting.toolkit.component.SimpleIconComponent;
import org.nting.toolkit.component.builder.LabelMiddleBuilder;
import org.nting.toolkit.layout.FormLayout;

public class FontIconTestPage implements IPageFactory {

    @Override
    public PageSize getPageSize() {
        return PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent(Pages pages) {
        FormLayout layout = new FormLayout(
                "2dlu, center:0px:grow, 2dlu, center:0px:grow, 2dlu, center:0px:grow, 2dlu, center:0px:grow, 2dlu, "
                        + "center:0px:grow, 2dlu, center:0px:grow, 2dlu, center:0px:grow, 2dlu, center:0px:grow, 2dlu, "
                        + "center:0px:grow, 2dlu, center:0px:grow, 2dlu",
                "2dlu");
        Panel panel = new Panel(layout);

        FontIcon[] values = FontIcon.values();
        for (int i = 0; i < values.length; i++) {
            FontIcon fontIcon = values[i];

            if (i % 10 == 0) {
                layout.addRow("pref");
                layout.addRow("pref");
            }

            int x = (i % 10) * 2 + 1;
            int y = layout.lastRow(1);
            SimpleIconComponent icon = new SimpleIconComponent(fontIcon)
                    .process(s -> s.fontSize.setValue(FontSize.EXTRA_EXTRA_LARGE_FONT));
            icon.setTooltipText(fontIcon.toString());
            panel.addComponent(icon, FormLayout.xy(x, y));
            Label label = new LabelMiddleBuilder<>().text(fontIcon.toString()).pass().font(FontSize.SMALL_FONT).build();
            panel.addComponent(label, FormLayout.xy(x, y + 1));
        }

        ScrollPane scrollPane = new ScrollPane(panel, ScrollComponent.ScrollBarPolicy.AS_NEEDED,
                ScrollComponent.ScrollBarPolicy.NEVER);
        scrollPane.setGridSize(74);
        return scrollPane;
    }
}

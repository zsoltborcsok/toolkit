package org.nting.toolkit.app.pages;

import static org.nting.toolkit.FontManager.FontSize.LARGE_FONT;
import static org.nting.toolkit.FontManager.FontSize.MEDIUM_FONT;
import static org.nting.toolkit.FontManager.FontSize.SMALL_FONT;
import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.ui.style.material.FieldComponentPropertyIds.BACKGROUND_COLOR;
import static playn.core.Font.Style.BOLD;
import static playn.core.Font.Style.BOLD_ITALIC;
import static playn.core.Font.Style.ITALIC;

import org.nting.toolkit.Component;
import org.nting.toolkit.app.IPageFactory;
import org.nting.toolkit.app.Pages;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.builder.ContainerBuilder;

import playn.core.Font;

public class TextFieldTestPage implements IPageFactory {

    @Override
    public PageSize getPageSize() {
        return PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent(Pages pages) {
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder("0px:grow",
                "pref, 14dlu, pref");

        ContainerBuilder<Panel, ?> nextPanelBuilder = panelBuilder.addPanel(xy(0, 0)).layoutManager("max(pref;200dlu)",
                "pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref");
        nextPanelBuilder.addLabel(xy(0, 0)).text("TextFields").pass().font(LARGE_FONT, BOLD).end() //
                .addTextField(xy(0, 2)).text("Text field").end() //
                .addTextField(xy(0, 4)).text("Disabled").enabled(false).end() //
                .addTextField(xy(0, 6)).text("BoldItalic").pass().font(SMALL_FONT, BOLD_ITALIC)
                .set(BACKGROUND_COLOR.toString(), 0xFFFFFFC8).end() //
                .addTextField(xy(0, 8)).text("Medium and italic").pass().font(MEDIUM_FONT, ITALIC).end() //
                .addTextField(xy(0, 10)).text("Large and bold").pass().font(LARGE_FONT, BOLD)
                .set(BACKGROUND_COLOR.toString(), 0xFFFFE0E0);

        nextPanelBuilder = panelBuilder.addPanel(xy(0, 2)).layoutManager("max(pref;200dlu)",
                "pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref");
        nextPanelBuilder.addLabel(xy(0, 0)).text("PasswordFields").pass().font(LARGE_FONT, Font.Style.BOLD);
        nextPanelBuilder.addPasswordField(xy(0, 2)).text("Password field");
        nextPanelBuilder.addPasswordField(xy(0, 4)).text("Disabled").enabled(false);
        nextPanelBuilder.addPasswordField(xy(0, 6)).text("BoldItalic").pass().font(SMALL_FONT, Font.Style.BOLD_ITALIC)
                .set(BACKGROUND_COLOR.toString(), 0xFFFFFFC8);
        nextPanelBuilder.addPasswordField(xy(0, 8)).text("Medium and italic").pass().font(MEDIUM_FONT,
                Font.Style.ITALIC);
        nextPanelBuilder.addPasswordField(xy(0, 10)).text("Large and bold").pass().font(LARGE_FONT, Font.Style.BOLD)
                .set(BACKGROUND_COLOR.toString(), 0xFFFFE0E0);

        return wrap(panelBuilder.build());
    }
}

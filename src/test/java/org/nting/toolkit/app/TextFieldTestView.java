package org.nting.toolkit.app;

import static org.nting.toolkit.FontManager.FontSize.LARGE_FONT;
import static org.nting.toolkit.FontManager.FontSize.MEDIUM_FONT;
import static org.nting.toolkit.FontManager.FontSize.SMALL_FONT;
import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.ui.style.material.FieldComponentPropertyIds.BACKGROUND_COLOR;
import static playn.core.Font.Style.BOLD;
import static playn.core.Font.Style.BOLD_ITALIC;
import static playn.core.Font.Style.ITALIC;

import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.builder.ContainerBuilder;

public class TextFieldTestView {

    public Panel createPane() {
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder("0px:grow",
                "pref, 7dlu, pref, 7dlu, pref, 7dlu, pref, 7dlu, pref, 7dlu");

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

        return panelBuilder.build();
    }
}

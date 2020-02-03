package org.nting.toolkit.app;

import static org.nting.toolkit.FontManager.FontSize.LARGE_FONT;
import static org.nting.toolkit.component.FontIcon.FOLDER_OPEN;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.layout.FormLayout.xyw;
import static org.nting.toolkit.ui.Colors.WHITE;
import static org.nting.toolkit.ui.style.material.ButtonPropertyIds.RAISED;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.blue_500;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.green_500;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.red_500;
import static playn.core.Font.Style.BOLD;

import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.component.Button;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.layout.FormLayout;

public class ButtonTestView {

    public Panel createPane() {
        FormLayout formLayout = new FormLayout("0px:grow, 7dlu, 0px:grow, 7dlu, 0px:grow",
                "pref, 4dlu, pref, 7dlu, pref, 7dlu, pref, 7dlu, pref, 7dlu, pref");
        ContainerBuilder<Panel, ?> panelBuilder = new ContainerBuilder<>(new Panel(formLayout)) //
                .addLabel(xyw(0, 0, 5)).text("Buttons").pass().font(LARGE_FONT, BOLD).end() //
                .addButton(xy(0, 2)).text("NORMAL / DISABLED").enabled(false).pass().end() //
                .addButton(xy(2, 2)).text("NORMAL / ENABLED").pass().end() //
                .addButton(xy(4, 2)).text("NORMAL / FOCUSED").pass().process(this::setFocused).end();
        panelBuilder.addButton(xy(0, 4)).text("GREEN / DISABLED").enabled(false).color(green_500).pass().end() //
                .addButton(xy(2, 4)).text("GREEN / ENABLED").color(green_500).pass().end() //
                .addButton(xy(4, 4)).text("GREEN / FOCUSED").color(green_500).pass().process(this::setFocused).end();
        panelBuilder.addButton(xy(0, 6)).text("BLUE / DISABLED").enabled(false).color(blue_500).pass().end() //
                .addButton(xy(2, 6)).text("BLUE / ENABLED").color(blue_500).pass().end() //
                .addButton(xy(4, 6)).text("BLUE / FOCUSED").color(blue_500).pass().process(this::setFocused).end();
        panelBuilder.addButton(xy(0, 8)).text("RED / DISABLED").enabled(false).color(red_500).pass().end() //
                .addButton(xy(2, 8)).text("RED / ENABLED").color(red_500).pass().end() //
                .addButton(xy(4, 8)).text("RED / FOCUSED").color(red_500).pass().process(this::setFocused).end();

        ContainerBuilder<Panel, ?> nextPanelBuilder = panelBuilder.addPanel(xyw(0, 10, 5))
                .layoutManager(new FormLayout("pref, 7dlu, pref, 7dlu, pref, 7dlu, pref, 7dlu, pref", "pref"));
        nextPanelBuilder.addButton(xy(0, 0)).text("TEXT").pass().end() //
                .addButton(xy(2, 0)).image(FOLDER_OPEN, LARGE_FONT).pass().end() //
                .addButton(xy(4, 0)).image(FOLDER_OPEN, LARGE_FONT).enabled(false).pass().end() //
                .addButton(xy(6, 0)).image(FOLDER_OPEN, LARGE_FONT).text("TEXT SAVE CANCEL").pass().end() //
                .addButton(xy(8, 0)).image(FOLDER_OPEN, LARGE_FONT).text("IMAGE+TEXT / DISABLED").enabled(false);

        panelBuilder.formLayout().addRow("7dlu").addRow("pref");
        nextPanelBuilder = panelBuilder.addPanel(xyw(0, panelBuilder.formLayout().lastRow(), 5))
                .layoutManager(new FormLayout("0px:grow, 7dlu, 0px:grow, 7dlu, 0px:grow",
                        "pref, 4dlu, pref, 7dlu, pref, 7dlu, pref, 7dlu, pref, 7dlu, pref"));
        nextPanelBuilder.addLabel(xyw(0, 0, 5)).text("Raised Buttons").pass().font(LARGE_FONT, BOLD);

        nextPanelBuilder.addButton(xy(0, 2)).text("NORMAL / DISABLED").enabled(false).color(WHITE);
        nextPanelBuilder.addButton(xy(2, 2)).text("NORMAL / ENABLED").color(WHITE);
        nextPanelBuilder.addButton(xy(4, 2)).text("NORMAL / FOCUSED").color(WHITE).pass().process(this::setFocused);

        nextPanelBuilder.addButton(xy(0, 4)).text("GREEN / DISABLED").enabled(false).color(green_500);
        nextPanelBuilder.addButton(xy(2, 4)).text("GREEN / ENABLED").color(green_500);
        nextPanelBuilder.addButton(xy(4, 4)).text("GREEN / FOCUSED").color(green_500).pass().process(this::setFocused);

        nextPanelBuilder.addButton(xy(0, 6)).text("BLUE / DISABLED").enabled(false).color(blue_500);
        nextPanelBuilder.addButton(xy(2, 6)).text("BLUE / ENABLED").color(blue_500);
        nextPanelBuilder.addButton(xy(4, 6)).text("BLUE / FOCUSED").color(blue_500).pass().process(this::setFocused);

        nextPanelBuilder.addButton(xy(0, 8)).text("RED / DISABLED").enabled(false).color(red_500);
        nextPanelBuilder.addButton(xy(2, 8)).text("RED / ENABLED").color(red_500);
        nextPanelBuilder.addButton(xy(4, 8)).text("RED / FOCUSED").color(red_500).pass().process(this::setFocused);
        nextPanelBuilder.setValueOfComponentsAt(RAISED, true, xy(0, 2), xy(2, 2), xy(4, 2), xy(0, 4), xy(2, 4),
                xy(4, 4), xy(0, 6), xy(2, 6), xy(4, 6), xy(0, 8), xy(2, 8), xy(4, 8));

        return panelBuilder.build();
    }

    public void setFocused(Button b) {
        ((ObjectProperty<Boolean>) b.focused).forceValue(true);
    }
}

package org.nting.toolkit.app.pages;

import static org.nting.toolkit.FontManager.FontSize.LARGE_FONT;
import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.layout.FormLayout.xyw;
import static playn.core.Font.Style.BOLD;

import java.util.function.Consumer;

import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.Component;
import org.nting.toolkit.app.IPageFactory;
import org.nting.toolkit.app.Pages;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.Slider;
import org.nting.toolkit.component.builder.ComponentBuilder;
import org.nting.toolkit.component.builder.ContainerBuilder;

public class SliderTestPage implements IPageFactory {

    @Override
    public PageSize getPageSize() {
        return PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent(Pages pages) {
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder(
                "right:pref, 7dlu, pref:grow(2), 7dlu, pref:grow(3), 7dlu, pref:grow(2)", "pref, 4dlu");
        panelBuilder.addLabel(xyw(0, 0, 7)).text("Sliders").pass().font(LARGE_FONT, BOLD);
        addSlidersRow(panelBuilder, "Enabled", cb -> {
        });
        panelBuilder.formLayout().addRow("4dlu");
        addSlidersRow(panelBuilder, "Enabled | Discrete", cb -> cb.set(s -> s.divisions, 5));
        panelBuilder.formLayout().addRow("4dlu");
        addSlidersRow(panelBuilder, "Disabled", cb -> cb.set(s -> s.enabled, false));
        panelBuilder.formLayout().addRow("4dlu");
        addSlidersRow(panelBuilder, "Disabled | Discrete",
                cb -> cb.set(s -> s.divisions, 5).set(s -> s.enabled, false));
        panelBuilder.formLayout().addRow("4dlu");
        addSlidersRow(panelBuilder, "Focused", cb -> setFocused(cb.getComponent()));

        return wrap(panelBuilder.build());
    }

    private void addSlidersRow(ContainerBuilder<Panel, ?> panelBuilder, String caption,
            Consumer<ComponentBuilder<Slider, ?>> rowConfiguration) {
        int lastRow = panelBuilder.formLayout().addRow("center:pref").lastRow();
        panelBuilder.addLabel(xy(0, lastRow)).text(caption).end() //
                .addSlider(xy(2, lastRow)).process(rowConfiguration).end() //
                .addSlider(xy(4, lastRow)).value(0.5f).process(rowConfiguration).end() //
                .addSlider(xy(6, lastRow)).value(1.0f).process(rowConfiguration);
    }

    private void setFocused(Slider slider) {
        ((ObjectProperty<Boolean>) slider.focused).forceValue(true);
    }
}

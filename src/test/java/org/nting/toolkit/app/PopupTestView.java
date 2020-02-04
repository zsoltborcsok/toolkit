package org.nting.toolkit.app;

import static org.nting.toolkit.FontManager.FontSize.LARGE_FONT;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.layout.FormLayout.xyw;
import static playn.core.Font.Style.BOLD;

import org.nting.data.Property;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.Component;
import org.nting.toolkit.component.Alignment;
import org.nting.toolkit.component.Button;
import org.nting.toolkit.component.Orientation;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.StandardPopup;
import org.nting.toolkit.component.TooltipPopup;
import org.nting.toolkit.component.WindowPopup;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.event.ActionEvent;
import org.nting.toolkit.event.ActionListener;
import org.nting.toolkit.layout.FormLayout;

public class PopupTestView {

    private enum PopupType {
        STANDARD, TOOLTIP, WINDOW
    }

    public Component createPane() {
        FormLayout layout = new FormLayout("0px:grow, pref, 20dlu, pref, 0px:grow",
                "100dlu, pref, pref, 20dlu, pref, pref, 0px:grow");
        Panel panel = new Panel(layout);

        Property<PopupType> popupTypeProperty = new ObjectProperty<>(PopupType.TOOLTIP);
        ActionListener popupActionHandler = new PopupActionHandler(popupTypeProperty);

        panel.addComponent(newButton("0", popupActionHandler), xy(1, 2));
        panel.addComponent(newButton("1", popupActionHandler), xy(1, 1));
        panel.addComponent(newButton("2", popupActionHandler), xy(3, 1));
        panel.addComponent(newButton("3", popupActionHandler), xy(3, 2));
        panel.addComponent(newButton("4", popupActionHandler), xy(3, 4));
        panel.addComponent(newButton("5", popupActionHandler), xy(3, 5));
        panel.addComponent(newButton("6", popupActionHandler), xy(1, 5));
        panel.addComponent(newButton("7", popupActionHandler), xy(1, 4));

        ContainerBuilder<Panel, ?> popupTypeBuilder = new ContainerBuilder<>(new Panel())
                .layoutManager("7dlu, pref, 7dlu, pref, 7dlu, pref, 7dlu", "7dlu, pref, 4dlu, pref, 7dlu");
        popupTypeBuilder.addLabel(xyw(1, 1, 5)).text("Select popup type").pass().font(LARGE_FONT, BOLD)
                .process(l -> l.tooltipText.setValue("Label tooltip test..."));

        panel.addComponent(popupTypeBuilder.build(), xyw(0, 0, 5));
        return panel;
    }

    private Button newButton(String text, ActionListener actionListener) {
        return new Button().set("text", text).process(b -> b.actions().addActionListener(actionListener));
    }

    private static class PopupActionHandler implements ActionListener {

        private final Property<PopupType> popupTypeProperty;

        private PopupActionHandler(Property<PopupType> popupTypeProperty) {
            this.popupTypeProperty = popupTypeProperty;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Button source = (Button) actionEvent.getSource();
            String textValue = source.text.getValue();

            StandardPopup popup;
            switch (popupTypeProperty.getValue()) {
            case STANDARD:
                popup = createStandardPopup(Integer.parseInt(textValue));
                break;
            case WINDOW:
                popup = createWindowPopup(Integer.parseInt(textValue));
                break;
            case TOOLTIP:
            default:
                popup = createTooltipPopup(Integer.parseInt(textValue));
                break;
            }
            popup.showRelativeTo(source);
        }

        private TooltipPopup createTooltipPopup(int preferredType) {
            ContainerBuilder<TooltipPopup, ?> tooltipPopupBuilder = new ContainerBuilder<>(
                    new TooltipPopup(Alignment.values()[preferredType / 2], Orientation.values()[preferredType % 2],
                            new FormLayout("2dlu, 80dlu, 2dlu", "2dlu, min(pref;80dlu), 2dlu")));
            return tooltipPopupBuilder.addMultiLineLabel(xy(1, 1)).text("It's a TooltipPopup").pass()
                    .set("color", tooltipPopupBuilder.build().color).end().build();
        }

        private StandardPopup createStandardPopup(int preferredType) {
            ContainerBuilder<StandardPopup, ?> tooltipPopupBuilder = new ContainerBuilder<>(
                    new StandardPopup(Alignment.values()[preferredType / 2], Orientation.values()[preferredType % 2],
                            new FormLayout("2dlu, 80dlu, 2dlu", "2dlu, min(pref;80dlu), 2dlu")));
            return tooltipPopupBuilder.addMultiLineLabel(xy(1, 1)).text("It's a StandardPopup").end().build();
        }

        private WindowPopup createWindowPopup(int preferredType) {
            ContainerBuilder<WindowPopup, ?> tooltipPopupBuilder = new ContainerBuilder<>(
                    new WindowPopup(Alignment.values()[preferredType / 2], Orientation.values()[preferredType % 2],
                            new FormLayout("2dlu, 80dlu, 2dlu", "2dlu, min(pref;80dlu), 2dlu")));
            return tooltipPopupBuilder.addMultiLineLabel(xy(1, 1)).text("It's a WindowPopup").end().build();
        }
    }
}

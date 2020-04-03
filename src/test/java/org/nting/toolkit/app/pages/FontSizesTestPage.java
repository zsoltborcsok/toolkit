package org.nting.toolkit.app.pages;

import static org.nting.toolkit.ToolkitServices.fontManager;

import org.nting.toolkit.Component;
import org.nting.toolkit.FontManager.FontSize;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.Alignment;
import org.nting.toolkit.component.Button;
import org.nting.toolkit.component.Label;
import org.nting.toolkit.component.Orientation;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.StandardPopup;
import org.nting.toolkit.component.TextAlignment;
import org.nting.toolkit.component.TextField;
import org.nting.toolkit.component.builder.ButtonMiddleBuilder;
import org.nting.toolkit.component.builder.LabelMiddleBuilder;
import org.nting.toolkit.component.builder.TextFieldMiddleBuilder;
import org.nting.toolkit.event.ActionEvent;
import org.nting.toolkit.event.ActionListener;
import org.nting.toolkit.layout.FormLayout;

public class FontSizesTestPage implements ITestPage {

    @Override
    public PageSize getPageSize() {
        return PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent() {
        FormLayout layout = new FormLayout("2dlu, 0px:grow, 2dlu", "2dlu");
        Panel panel = new Panel(layout);

        for (FontSize fontSize : FontSize.values()) {
            layout.addRow("pref");
            layout.addRow("2dlu");

            TextField textField = new TextFieldMiddleBuilder<>().text("Click on button...").pass().font(fontSize)
                    .build();
            panel.addComponent(textField, FormLayout.xy(1, layout.lastRow(1)));
        }

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                createNewVocabularyWindow().showRelativeTo(actionEvent.getSource());
            }
        };
        for (FontSize fontSize : FontSize.values()) {
            layout.addRow("pref");
            layout.addRow("2dlu");

            Button button = new ButtonMiddleBuilder<>().text("Create").actionListener(actionListener).pass()
                    .font(fontSize).build();
            panel.addComponent(button, FormLayout.xy(1, layout.lastRow(1)));
        }

        return wrap(panel);
    }

    private StandardPopup createNewVocabularyWindow() {
        FormLayout formLayout = new FormLayout("18px, 324px, 18px", "43px, pref, 36px, 10px, pref, 36px, 10px, pref, "
                + "36px, 10px, pref, 36px, 10px, pref, 36px, 10px, pref, 14px");
        StandardPopup popup = new StandardPopup(Alignment.BOTTOM_LEFT, Orientation.HORIZONTAL, formLayout);

        Panel titlePanel = new Panel(new FormLayout("pref:grow", "pref:grow"));
        titlePanel.backgroundColor.setValue(0xFFF6F1ED);
        Label title = createLabel("NEW VOCABULARY");
        title.alignment.setValue(TextAlignment.CENTER);
        title.font.setValue(fontManager().getFont(FontSize.LARGE_FONT));
        titlePanel.addComponent(title, FormLayout.xy(0, 0));

        popup.addComponent(titlePanel, FormLayout.xyw(0, 0, 3));
        popup.addComponent(createLabel("Name"), FormLayout.xy(1, 1));
        popup.addComponent(createLabel("Language"), FormLayout.xy(1, 4));
        popup.addComponent(createLabel("Translated"), FormLayout.xy(1, 7));
        popup.addComponent(createLabel("Translation Language"), FormLayout.xy(1, 10));
        popup.addComponent(createLabel("Comment"), FormLayout.xy(1, 13));

        popup.addComponent(createTextField(), FormLayout.xy(1, 2));
        popup.addComponent(createTextField(), FormLayout.xy(1, 5));
        popup.addComponent(createTextField(), FormLayout.xy(1, 8));
        popup.addComponent(createTextField(), FormLayout.xy(1, 11));
        popup.addComponent(createTextField(), FormLayout.xy(1, 14));

        return popup;
    }

    private Label createLabel(String text) {
        return new LabelMiddleBuilder<>().text(text).pass().build();
    }

    private TextField createTextField() {
        TextField textField = new TextField();
        textField.hPadding.setValue(0);
        return textField;
    }

}

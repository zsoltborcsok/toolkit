package org.nting.toolkit.app.pages;

import org.nting.toolkit.app.ListPageFactory;

public class TestsPage extends ListPageFactory {

    public TestsPage() {
        registerSubPage("Button | CheckBox | RadioButton | ToggleButton", ButtonTestPage::new);
        registerSubPage("DropDownList", DropDownListTestPage::new);
        registerSubPage("ListComponent", ListComponentTestPage::new);
        registerSubPage("Label | LabelWithEllipsis | Link", LabelTestPage::new);
        registerSubPage("TextField | PasswordField", TextFieldTestPage::new);
        registerSubPage("TextArea", TextAreaTestPage::new);
        registerSubPage("Popup", PopupTestPage::new);
        registerSubPage("ScrollPane", ScrollPaneTestPage::new);
        registerSubPage("SplitPane", SplitPaneTestPage::new);
        registerSubPage("Dialog", DialogTestPage::new);
        registerSubPage("ZLayout (+GWT 2.8)", ZLayoutTestPage::new);
        registerSubPage("FlowLayout", FlowLayoutTestPage::new);
        registerSubPage("Pivot animation", PivotAnimationTestPage::new);
        registerSubPage("Notifications", NotificationsTestPage::new);
        registerSubPage("Shapes", ShapesTestPage::new);
        registerSubPage("FontIcons", FontIconTestPage::new);
        registerSubPage("FontSizes", FontSizesTestPage::new);
        registerSubPage("Read-only form", ReadOnlyFormBuilderTestPage::new);
        registerSubPage("Easing", EasingTestPage::new);
    }
}

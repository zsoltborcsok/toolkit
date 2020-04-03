package org.nting.toolkit.app.pages;

import java.util.List;
import java.util.function.Supplier;

import org.nting.data.bean.BeanDescriptor;
import org.nting.data.query.ListDataProvider;
import org.nting.data.util.Pair;
import org.nting.toolkit.Component;
import org.nting.toolkit.app.Pages;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.ListComponent;
import org.nting.toolkit.component.renderer.BasicItemRenderer;

import com.google.common.collect.Lists;

// Some missing widgets: ComboBox, Table, PasswordField, LabelWithEllipsis
public class TestsPage implements ITestPage {

    private final List<Pair<String, Supplier<ITestPage>>> testPages = Lists.newLinkedList();
    private final Pages pages;

    public TestsPage(Pages pages) {
        this.pages = pages;

        testPages.add(Pair.of("Button | CheckBox | RadioButton | ToggleButton", ButtonTestPage::new));
        testPages.add(Pair.of("DropDownList", DropDownListTestPage::new));
        testPages.add(Pair.of("ListComponent", ListComponentTestPage::new));
        testPages.add(Pair.of("Label", LabelTestPage::new));
        testPages.add(Pair.of("TextField", TextFieldTestPage::new));
        testPages.add(Pair.of("TextArea", TextAreaTestPage::new));
        testPages.add(Pair.of("Popup", PopupTestPage::new));
        testPages.add(Pair.of("ScrollPane", ScrollPaneTestPage::new));
        testPages.add(Pair.of("SplitPane", SplitPaneTestPage::new));
        testPages.add(Pair.of("Dialog", DialogTestPage::new));
        // testPages.add(Pair.of("ZLayout (+GWT 2.8)", ZLayoutTestView::new));
        // testPages.add(Pair.of("FlowLayout", FlowLayoutTestView::new));
        // testPages.add(Pair.of("Pivot animation", PivotAnimationTestView::new));
        // testPages.add(Pair.of("Read-only form", ReadOnlyFormBuilderTestView::new));
        // testPages.add(Pair.of("Notifications", NotificationsTestView::new));
        // testPages.add(Pair.of("Easing", EasingTestView::new));
        // testPages.add(Pair.of("Shapes", ShapesTestView::new));
        // testPages.add(Pair.of("FontIcons", FontIconTestView::new));
        // testPages.add(Pair.of("FontSizes", FontSizesTestView::new));
        // testPages.add(Pair.of("Curves", CurvesTestView::new));
        // testPages.add(Pair.of("Triangles", TrianglesTestView::new));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Component createContent() {
        ListComponent<Pair<String, Supplier<ITestPage>>> listComponent = new ListComponent<>(
                new BasicItemRenderer<>(pair -> pair.first));
        listComponent.setDataProvider(new ListDataProvider(testPages, new BeanDescriptor(Pair.class), t -> t));
        listComponent.selectedItems.addValueChangeListener(event -> {
            if (event.getValue().size() == 1) {
                Pair<String, Supplier<ITestPage>> selectedItem = event.getValue().get(0);
                pages.removeNextPages(listComponent);
                ITestPage testPage = selectedItem.second.get();
                pages.addPage(testPage.createContent(), testPage.getPageSize());
            }
        });
        // Trigger the selection of the first item when this Page is already added, as it also add a page to the pages.
        listComponent.attached.addValueChangeListener(event -> {
            if (event.getValue()) {
                listComponent.selection.setValue(ListComponent.Selection.NEEDS_ONE);
            }
        });
        return listComponent;
    }

    @Override
    public PageSize getPageSize() {
        return PageSize.SINGLE_COLUMN;
    }
}
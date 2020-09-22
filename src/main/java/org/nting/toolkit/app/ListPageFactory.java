package org.nting.toolkit.app;

import java.util.List;
import java.util.function.Supplier;

import org.nting.data.bean.BeanDescriptor;
import org.nting.data.query.ListDataProvider;
import org.nting.data.util.Pair;
import org.nting.toolkit.Component;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.ListComponent;
import org.nting.toolkit.component.Selection;
import org.nting.toolkit.component.renderer.BasicItemRenderer;

import com.google.common.collect.Lists;

/**
 * Shows a list of selectable sub pages in one column.
 */
public class ListPageFactory implements IPageFactory {

    private final List<Pair<String, Supplier<IPageFactory>>> subPages = Lists.newLinkedList();

    public void registerSubPage(String pageName, Supplier<IPageFactory> pageFactorySupplier) {
        subPages.add(Pair.of(pageName, pageFactorySupplier));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Component createContent(Pages pages) {
        ListComponent<Pair<String, Supplier<IPageFactory>>> listComponent = new ListComponent<>(
                new BasicItemRenderer<>(pair -> pair.first));
        listComponent.setDataProvider(new ListDataProvider(subPages, new BeanDescriptor(Pair.class), t -> t));
        listComponent.selectedItems.addValueChangeListener(event -> {
            if (event.getValue().size() == 1) {
                Pair<String, Supplier<IPageFactory>> selectedItem = event.getValue().get(0);
                pages.removeNextPages(listComponent);
                pages.addPage(selectedItem.second.get());
            }
        });
        // Trigger the selection of the first item when this Page is already added, as it also add a page to the pages.
        listComponent.attached.addValueChangeListener(event -> {
            if (event.getValue()) {
                listComponent.selection.setValue(Selection.NEEDS_ONE);
            }
        });
        return listComponent;
    }

    @Override
    public PageSize getPageSize() {
        return PageSize.SINGLE_COLUMN;
    }
}

package org.nting.toolkit.app.pages;

import static org.nting.toolkit.component.Selection.SINGLE;
import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.FormLayout.xy;

import java.util.List;
import java.util.function.Consumer;

import org.nting.data.Property;
import org.nting.data.ValueChangeEvent;
import org.nting.data.ValueChangeListener;
import org.nting.data.bean.BeanDescriptor;
import org.nting.data.property.ObjectProperty;
import org.nting.data.query.ListDataProvider;
import org.nting.toolkit.Component;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.FontIcon;
import org.nting.toolkit.component.Icon;
import org.nting.toolkit.component.ListComponent;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.component.renderer.BasicItemRenderer;
import org.nting.toolkit.component.renderer.ItemRenderer;
import org.nting.toolkit.component.renderer.OneRowItemRenderer;
import org.nting.toolkit.component.renderer.PrimaryInfoItemRenderer;
import org.nting.toolkit.component.renderer.TwoRowsItemRenderer;
import org.nting.toolkit.data.Properties;
import org.nting.toolkit.layout.FormLayout;
import org.nting.toolkit.util.UUID;

import com.google.common.collect.Lists;

public class ListComponentTestPage implements ITestPage, ValueChangeListener<List<ListComponentTestPage.Data>> {

    public static class Data extends Properties {

        public final ObjectProperty<String> id = addObjectProperty("id", "");
        public final Property<String> name = addObjectProperty("name", "");
        public final Property<String> caption = addObjectProperty("caption", "");
        public final Property<String> description = addObjectProperty("description", "");
        public final Property<Icon> icon = addObjectProperty("icon", FontIcon.AUTOMATION);
        public final Property<Boolean> selection = addObjectProperty("selection", false);
        public final Property<ItemRenderer<Data>> itemRenderer = addObjectProperty("itemRenderer", null);

        public Data(String id) {
            this.id.setValue(id);
            this.id.setReadOnly(true);
        }

        public Data process(Consumer<Data> consumer) {
            consumer.accept(this);
            return this;
        }
    }

    private ListComponent<Data> listComponent;

    @Override
    public PageSize getPageSize() {
        return PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent() {
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder(new FormLayout("0px:grow", "0px:grow"));
        listComponent = panelBuilder.<Data> addListComponent(xy(0, 0), item -> item.name.getValue()).selection(SINGLE)
                .dataProvider(new ListDataProvider<>(createBackendCollection(), new BeanDescriptor<>(Data.class),
                        data -> data.id))
                .addSelectionChangeListener(this).build();

        return panelBuilder.build();
    }

    @Override
    public void valueChange(ValueChangeEvent<List<Data>> event) {
        List<Data> selections = event.getValue();
        if (0 < selections.size()) {
            Data selectedItem = selections.get(0);
            ItemRenderer<Data> itemRenderer = selectedItem.itemRenderer.getValue();
            if (listComponent != null && itemRenderer != null) {
                listComponent.setItemRenderer(itemRenderer);
            }
        }
    }

    private List<Data> createBackendCollection() {
        List<Data> backendCollection = Lists.newLinkedList();

        Data item = new Data(UUID.uuid(5)).process(backendCollection::add);
        item.name.setValue("BasicItemRenderer");
        item.itemRenderer.setValue(new BasicItemRenderer<>(i -> i.name.getValue()));

        item = new Data(UUID.uuid(5)).process(backendCollection::add);
        item.name.setValue("OneRowItemRenderer");
        item.itemRenderer.setValue(new OneRowItemRenderer<>(null, i -> i.name.getValue(), i -> i.caption.getValue(),
                i -> i.description.getValue()));

        item = new Data(UUID.uuid(5)).process(backendCollection::add);
        item.name.setValue("TwoRowsItemRenderer");
        item.itemRenderer.setValue(new TwoRowsItemRenderer<>(null, i -> i.name.getValue(), i -> i.caption.getValue(),
                i -> i.description.getValue()));

        item = new Data(UUID.uuid(5)).process(backendCollection::add);
        item.name.setValue("PrimaryInfoItemRenderer {1}");
        item.itemRenderer.setValue(new PrimaryInfoItemRenderer<>(i -> i.icon.getValue(), i -> i.name.getValue(),
                i -> i.caption.getValue(), i -> i.description.getValue()));

        item = new Data(UUID.uuid(5)).process(backendCollection::add);
        item.name.setValue("PrimaryInfoItemRenderer {2}");
        item.itemRenderer.setValue(new PrimaryInfoItemRenderer<Data>(i -> i.icon.getValue(), i -> i.name.getValue(),
                i -> i.caption.getValue(), i -> i.description.getValue()).lineCount(2));

        item = new Data(UUID.uuid(5)).process(backendCollection::add);
        item.name.setValue("PrimaryInfoItemRenderer {3}");
        item.itemRenderer.setValue(new PrimaryInfoItemRenderer<Data>(i -> i.icon.getValue(), i -> i.name.getValue(),
                i -> i.caption.getValue(), i -> i.description.getValue()).lineCount(3));

        FontIcon[] fontIcons = FontIcon.values();
        for (int i = 0; i < 100; i++) {
            item = new Data(UUID.uuid(5)).process(backendCollection::add);
            item.name.setValue(UUID.uuid(25));
            item.caption.setValue(UUID.uuid(12));
            item.description.setValue(UUID.uuid(40));
            item.icon.setValue(fontIcons[i]);
        }

        return backendCollection;
    }

}

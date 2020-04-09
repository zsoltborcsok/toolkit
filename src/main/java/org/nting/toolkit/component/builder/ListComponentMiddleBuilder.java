package org.nting.toolkit.component.builder;

import java.util.List;
import java.util.function.Function;

import org.nting.data.Property;
import org.nting.data.ValueChangeListener;
import org.nting.data.binding.Bindings;
import org.nting.data.query.DataProvider;
import org.nting.toolkit.component.ListComponent;
import org.nting.toolkit.component.ListComponent.Selection;
import org.nting.toolkit.component.renderer.ItemRenderer;

public class ListComponentMiddleBuilder<T extends ContainerBuilder<?, ?>, I>
        extends AbstractMiddleBuilder<ListComponent<I>, T> {

    public ListComponentMiddleBuilder(ItemRenderer<I> itemRenderer) {
        this(new ComponentBuilder<>(new ListComponent<>(itemRenderer)));
    }

    public ListComponentMiddleBuilder(ComponentBuilder<ListComponent<I>, T> componentBuilder) {
        super(componentBuilder);
    }

    public ListComponentMiddleBuilder<T, I> color(int color) {
        componentBuilder.set(listComponent -> listComponent.color, color);
        return this;
    }

    public ListComponentMiddleBuilder<T, I> secondaryColor(int secondaryColor) {
        componentBuilder.set(listComponent -> listComponent.secondaryColor, secondaryColor);
        return this;
    }

    public ListComponentMiddleBuilder<T, I> enabled(boolean enabled) {
        componentBuilder.set(listComponent -> listComponent.enabled, enabled);
        return this;
    }

    public ListComponentMiddleBuilder<T, I> enabled(Property<Boolean> enabled) {
        componentBuilder.bind(listComponent -> listComponent.enabled, enabled);
        return this;
    }

    public ListComponentMiddleBuilder<T, I> selection(Selection selection) {
        componentBuilder.set(listComponent -> listComponent.selection, selection);
        return this;
    }

    public ListComponentMiddleBuilder<T, I> selection(Property<Selection> selection) {
        componentBuilder.bind(listComponent -> listComponent.selection, selection);
        return this;
    }

    public ListComponentMiddleBuilder<T, I> noItemsInfo(String noItemsInfo) {
        componentBuilder.set(listComponent -> listComponent.noItemsInfo, noItemsInfo);
        return this;
    }

    public ListComponentMiddleBuilder<T, I> noItemsInfo(Property<String> noItemsInfo) {
        componentBuilder.bind(listComponent -> listComponent.noItemsInfo, noItemsInfo);
        return this;
    }

    public <F> ListComponentMiddleBuilder<T, I> noItemsInfo(Property<F> source, Function<F, String> transform) {
        componentBuilder.bind(listComponent -> listComponent.noItemsInfo, source, transform);
        return this;
    }

    public ListComponentMiddleBuilder<T, I> pageLength(Integer pageLength) {
        componentBuilder.set(listComponent -> listComponent.pageLength, pageLength);
        return this;
    }

    public ListComponentMiddleBuilder<T, I> pageLength(Property<Integer> pageLength) {
        componentBuilder.bind(listComponent -> listComponent.pageLength, pageLength);
        return this;
    }

    public <F> ListComponentMiddleBuilder<T, I> pageLength(Property<F> source, Function<F, Integer> transform) {
        componentBuilder.bind(listComponent -> listComponent.pageLength, source, transform);
        return this;
    }

    public ListComponentMiddleBuilder<T, I> selectedItems(List<I> selectedItems) {
        componentBuilder.set(listComponent -> listComponent.selectedItems, selectedItems);
        return this;
    }

    public ListComponentMiddleBuilder<T, I> dataProvider(DataProvider<I> dataProvider) {
        componentBuilder.process(listComponent -> listComponent.setDataProvider(dataProvider));
        return this;
    }

    public ListComponentMiddleBuilder<T, I> addSelectionChangeListener(
            ValueChangeListener<List<I>> valueChangeListener) {
        componentBuilder.addBinding(Bindings.bind(componentBuilder.getComponent().selectedItems, valueChangeListener));
        return this;
    }
}

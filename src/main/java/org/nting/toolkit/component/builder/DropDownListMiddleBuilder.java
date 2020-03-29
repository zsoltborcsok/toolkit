package org.nting.toolkit.component.builder;

import java.util.function.Function;

import org.nting.data.Property;
import org.nting.data.query.DataProvider;
import org.nting.toolkit.component.DropDownList;

public class DropDownListMiddleBuilder<T extends ContainerBuilder<?, ?>, S>
        extends AbstractMiddleBuilder<DropDownList<S>, T> {

    public DropDownListMiddleBuilder() {
        this(new ComponentBuilder<>(new DropDownList<>()));
    }

    public DropDownListMiddleBuilder(ComponentBuilder<DropDownList<S>, T> componentBuilder) {
        super(componentBuilder);
    }

    public DropDownListMiddleBuilder<T, S> color(int color) {
        componentBuilder.set(dropDownList -> dropDownList.color, color);
        return this;
    }

    public DropDownListMiddleBuilder<T, S> caption(String caption) {
        componentBuilder.set(dropDownList -> dropDownList.caption, caption);
        return this;
    }

    public DropDownListMiddleBuilder<T, S> caption(Property<String> caption) {
        componentBuilder.<String> set(dropDownList -> dropDownList.caption, caption);
        return this;
    }

    public <F> DropDownListMiddleBuilder<T, S> caption(Property<F> source, Function<F, String> transform) {
        componentBuilder.bind(dropDownList -> dropDownList.caption, source, transform);
        return this;
    }

    public DropDownListMiddleBuilder<T, S> enabled(boolean enabled) {
        componentBuilder.set(dropDownList -> dropDownList.enabled, enabled);
        return this;
    }

    public DropDownListMiddleBuilder<T, S> enabled(Property<Boolean> enabled) {
        componentBuilder.<Boolean> set(dropDownList -> dropDownList.enabled, enabled);
        return this;
    }

    public DropDownListMiddleBuilder<T, S> emptySelectionAllowed(boolean emptySelectionAllowed) {
        componentBuilder.set(dropDownList -> dropDownList.emptySelectionAllowed, emptySelectionAllowed);
        return this;
    }

    public DropDownListMiddleBuilder<T, S> emptySelectionAllowed(Property<Boolean> emptySelectionAllowed) {
        componentBuilder.<Boolean> set(dropDownList -> dropDownList.emptySelectionAllowed, emptySelectionAllowed);
        return this;
    }

    public DropDownListMiddleBuilder<T, S> emptySelectionCaption(String emptySelectionCaption) {
        componentBuilder.set(dropDownList -> dropDownList.emptySelectionCaption, emptySelectionCaption);
        return this;
    }

    public DropDownListMiddleBuilder<T, S> emptySelectionCaption(Property<String> emptySelectionCaption) {
        componentBuilder.<String> set(dropDownList -> dropDownList.emptySelectionCaption, emptySelectionCaption);
        return this;
    }

    public <F> DropDownListMiddleBuilder<T, S> emptySelectionCaption(Property<F> source,
            Function<F, String> transform) {
        componentBuilder.bind(dropDownList -> dropDownList.emptySelectionCaption, source, transform);
        return this;
    }

    public DropDownListMiddleBuilder<T, S> visibleItemCount(Integer visibleItemCount) {
        componentBuilder.set(dropDownList -> dropDownList.visibleItemCount, visibleItemCount);
        return this;
    }

    public DropDownListMiddleBuilder<T, S> visibleItemCount(Property<Integer> visibleItemCount) {
        componentBuilder.<Integer> set(dropDownList -> dropDownList.visibleItemCount, visibleItemCount);
        return this;
    }

    public <F> DropDownListMiddleBuilder<T, S> visibleItemCount(Property<F> source, Function<F, Integer> transform) {
        componentBuilder.bind(dropDownList -> dropDownList.visibleItemCount, source, transform);
        return this;
    }

    public DropDownListMiddleBuilder<T, S> selectedItem(S selectedItem) {
        componentBuilder.set(dropDownList -> dropDownList.selectedItem, selectedItem);
        return this;
    }

    public DropDownListMiddleBuilder<T, S> selectedItem(Property<S> selectedItem) {
        componentBuilder.<S> set(dropDownList -> dropDownList.selectedItem, selectedItem);
        return this;
    }

    public DropDownListMiddleBuilder<T, S> dataProvider(DataProvider<S> dataProvider) {
        componentBuilder.process(dropDownList -> dropDownList.setDataProvider(dataProvider));
        return this;
    }

    public DropDownListMiddleBuilder<T, S> itemCaptionGenerator(Function<S, String> itemCaptionGenerator) {
        componentBuilder.process(dropDownList -> dropDownList.setItemCaptionGenerator(itemCaptionGenerator));
        return this;
    }
}

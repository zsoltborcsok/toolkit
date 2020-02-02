package org.nting.toolkit.component.builder;

import java.util.List;

import org.nting.toolkit.FontManager.FontSize;
import org.nting.toolkit.component.AbstractComponent;
import org.nting.toolkit.component.Button;
import org.nting.toolkit.component.Icon;
import org.nting.toolkit.component.Label;
import org.nting.toolkit.component.MultiLineLabel;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.SimpleIconComponent;
import org.nting.toolkit.component.TextAlignment;
import org.nting.toolkit.component.TextField;
import org.nting.toolkit.layout.LayoutManager;
import org.nting.toolkit.ui.stone.Content;

import com.google.common.collect.Lists;

public class ContainerBuilder<CONTAINER extends AbstractComponent, PARENT_BUILDER extends ContainerBuilder<?, ?>>
        extends ComponentBuilder<CONTAINER, PARENT_BUILDER> {

    private final List<ComponentBuilder<?, ContainerBuilder<CONTAINER, PARENT_BUILDER>>> componentBuilders = Lists
            .newLinkedList();

    public ContainerBuilder(CONTAINER component) {
        super(component);
    }

    protected ContainerBuilder(CONTAINER component, PARENT_BUILDER parentBuilder) {
        super(component, parentBuilder);
    }

    public ContainerBuilder<CONTAINER, PARENT_BUILDER> layoutManager(LayoutManager layoutManager) {
        getComponent().setLayoutManager(layoutManager);
        return this;
    }

    public <T extends AbstractComponent> ComponentBuilder<T, ContainerBuilder<CONTAINER, PARENT_BUILDER>> addComponent(
            T child, Object constraints) {
        getComponent().addComponent(child, constraints);
        ComponentBuilder<T, ContainerBuilder<CONTAINER, PARENT_BUILDER>> componentBuilder = new ComponentBuilder<>(
                child, this);
        componentBuilders.add(componentBuilder);
        return componentBuilder;
    }

    public ComponentBuilder<Label, ContainerBuilder<CONTAINER, PARENT_BUILDER>> addLabel(String text,
            Object constraints) {
        return addComponent(new Label(), constraints).text(text);
    }

    public ComponentBuilder<Label, ContainerBuilder<CONTAINER, PARENT_BUILDER>> addLabel(String text,
            TextAlignment alignment, Object constraints) {
        return addComponent(new Label(), constraints).text(text).process(l -> l.alignment.setValue(alignment));
    }

    public ComponentBuilder<MultiLineLabel, ContainerBuilder<CONTAINER, PARENT_BUILDER>> addMultiLineLabel(String text,
            Object constraints) {
        return addComponent(new MultiLineLabel(), constraints).text(text);
    }

    public ComponentBuilder<Button, ContainerBuilder<CONTAINER, PARENT_BUILDER>> addButton(String text,
            Object constraints) {
        return addComponent(new Button(), constraints).text(text);
    }

    public ComponentBuilder<Button, ContainerBuilder<CONTAINER, PARENT_BUILDER>> addButton(Content image,
            Object constraints) {
        return addComponent(new Button(), constraints).process(b -> b.image.setValue(image));
    }

    public ComponentBuilder<Button, ContainerBuilder<CONTAINER, PARENT_BUILDER>> addButton(String text, Content image,
            Object constraints) {
        return addComponent(new Button(), constraints).text(text).process(b -> b.image.setValue(image));
    }

    public ComponentBuilder<TextField, ContainerBuilder<CONTAINER, PARENT_BUILDER>> addTextField(String caption,
            Object constraints) {
        return addComponent(new TextField(), constraints).process(tf -> tf.caption.setValue(caption));
    }

    public ComponentBuilder<SimpleIconComponent, ContainerBuilder<CONTAINER, PARENT_BUILDER>> addIcon(Icon icon,
            Object constraints) {
        return addComponent(new SimpleIconComponent(icon), constraints);
    }

    public ComponentBuilder<SimpleIconComponent, ContainerBuilder<CONTAINER, PARENT_BUILDER>> addIcon(Icon icon,
            FontSize fontSize, Object constraints) {
        return addComponent(new SimpleIconComponent(icon), constraints).process(sic -> sic.fontSize.setValue(fontSize));
    }

    public ContainerBuilder<Panel, ContainerBuilder<CONTAINER, PARENT_BUILDER>> addPanel(Object constraints) {
        Panel childPanel = new Panel();
        getComponent().addComponent(childPanel, constraints);
        ContainerBuilder<Panel, ContainerBuilder<CONTAINER, PARENT_BUILDER>> containerBuilder = new ContainerBuilder<>(
                childPanel, this);
        componentBuilders.add(containerBuilder);
        return containerBuilder;
    }

    @Override
    public void unbind() {
        super.unbind();
        componentBuilders.forEach(ComponentBuilder::unbind);
    }
}

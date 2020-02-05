package org.nting.toolkit.component.builder;

import java.util.List;

import org.nting.toolkit.component.AbstractComponent;
import org.nting.toolkit.component.Button;
import org.nting.toolkit.component.CheckBox;
import org.nting.toolkit.component.Label;
import org.nting.toolkit.component.MultiLineLabel;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.RadioButton;
import org.nting.toolkit.component.RadioButtonGroup;
import org.nting.toolkit.component.SimpleIconComponent;
import org.nting.toolkit.component.TextField;
import org.nting.toolkit.layout.FormLayout;
import org.nting.toolkit.layout.LayoutManager;

import com.google.common.collect.Lists;

public class ContainerBuilder<CONTAINER extends AbstractComponent, PARENT_BUILDER extends ContainerBuilder<?, ?>>
        extends ComponentBuilder<CONTAINER, PARENT_BUILDER> {

    public static ContainerBuilder<Panel, ?> panelBuilder(String columns, String rows) {
        return new ContainerBuilder<>(new Panel()).layoutManager(columns, rows);
    }

    public static ContainerBuilder<Panel, ?> panelBuilder(LayoutManager layoutManager) {
        return new ContainerBuilder<>(new Panel()).layoutManager(layoutManager);
    }

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

    public ContainerBuilder<CONTAINER, PARENT_BUILDER> layoutManager(String columns, String rows) {
        getComponent().setLayoutManager(new FormLayout(columns, rows));
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

    public LabelMiddleBuilder<ContainerBuilder<CONTAINER, PARENT_BUILDER>> addLabel(Object constraints) {
        return new LabelMiddleBuilder<>(addComponent(new Label(), constraints));
    }

    public LabelMiddleBuilder<ContainerBuilder<CONTAINER, PARENT_BUILDER>> addMultiLineLabel(Object constraints) {
        return new LabelMiddleBuilder<>(addComponent(new MultiLineLabel(), constraints));
    }

    public ButtonMiddleBuilder<ContainerBuilder<CONTAINER, PARENT_BUILDER>> addButton(Object constraints) {
        return new ButtonMiddleBuilder<>(addComponent(new Button(), constraints));
    }

    public TextFieldMiddleBuilder<ContainerBuilder<CONTAINER, PARENT_BUILDER>> addTextField(Object constraints) {
        return new TextFieldMiddleBuilder<>(addComponent(new TextField(), constraints));
    }

    public SimpleIconComponentMiddleBuilder<ContainerBuilder<CONTAINER, PARENT_BUILDER>> addIcon(Object constraints) {
        return new SimpleIconComponentMiddleBuilder<>(addComponent(new SimpleIconComponent(null), constraints));
    }

    public CheckBoxMiddleBuilder<ContainerBuilder<CONTAINER, PARENT_BUILDER>> addCheckBox(Object constraints) {
        return new CheckBoxMiddleBuilder<>(addComponent(new CheckBox(), constraints));
    }

    public CheckBoxMiddleBuilder<ContainerBuilder<CONTAINER, PARENT_BUILDER>> addRadioButton(Object constraints,
            RadioButtonGroup radioButtonGroup) {
        RadioButton radioButton = new RadioButton();
        radioButtonGroup.add(radioButton);
        return new CheckBoxMiddleBuilder<>(addComponent(radioButton, constraints));
    }

    public ContainerBuilder<Panel, ContainerBuilder<CONTAINER, PARENT_BUILDER>> addPanel(Object constraints) {
        Panel childPanel = new Panel();
        getComponent().addComponent(childPanel, constraints);
        ContainerBuilder<Panel, ContainerBuilder<CONTAINER, PARENT_BUILDER>> containerBuilder = new ContainerBuilder<>(
                childPanel, this);
        componentBuilders.add(containerBuilder);
        return containerBuilder;
    }

    public FormLayoutMiddleBuilder<ContainerBuilder<CONTAINER, PARENT_BUILDER>> formLayout() {
        return new FormLayoutMiddleBuilder<>(this, (FormLayout) getComponent().getLayoutManager());
    }

    @Override
    public void unbind() {
        super.unbind();
        componentBuilders.forEach(ComponentBuilder::unbind);
    }
}

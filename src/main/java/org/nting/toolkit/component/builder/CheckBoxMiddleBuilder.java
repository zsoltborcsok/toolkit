package org.nting.toolkit.component.builder;

import org.nting.toolkit.component.CheckBox;

public class CheckBoxMiddleBuilder<T extends ContainerBuilder<?, ?>>
        extends AbstractCheckBoxMiddleBuilder<CheckBox, T> {

    public CheckBoxMiddleBuilder() {
        this(new ComponentBuilder<>(new CheckBox()));
    }

    public CheckBoxMiddleBuilder(ComponentBuilder<CheckBox, T> componentBuilder) {
        super(componentBuilder);
    }
}

package org.nting.toolkit.component.builder;

import org.nting.toolkit.component.AbstractComponent;

public class AbstractMiddleBuilder<COMPONENT extends AbstractComponent, PARENT_BUILDER extends ContainerBuilder<?, ?>> {

    protected final ComponentBuilder<COMPONENT, PARENT_BUILDER> componentBuilder;

    public AbstractMiddleBuilder(ComponentBuilder<COMPONENT, PARENT_BUILDER> componentBuilder) {
        this.componentBuilder = componentBuilder;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> pass() {
        return componentBuilder;
    }

    public COMPONENT build() {
        return pass().build();
    }

    public PARENT_BUILDER end() {
        return pass().end();
    }
}

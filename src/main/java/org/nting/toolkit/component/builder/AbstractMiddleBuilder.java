package org.nting.toolkit.component.builder;

import java.util.function.Consumer;

import org.nting.toolkit.component.AbstractComponent;

public class AbstractMiddleBuilder<COMPONENT extends AbstractComponent, PARENT_BUILDER extends ContainerBuilder<?, ?>> {

    protected final ComponentBuilder<COMPONENT, PARENT_BUILDER> componentBuilder;

    public AbstractMiddleBuilder(ComponentBuilder<COMPONENT, PARENT_BUILDER> componentBuilder) {
        this.componentBuilder = componentBuilder;
    }

    public ComponentBuilder<COMPONENT, PARENT_BUILDER> pass() {
        return componentBuilder;
    }

    public AbstractMiddleBuilder<COMPONENT, PARENT_BUILDER> process(Consumer<ComponentBuilder<COMPONENT, ?>> consumer) {
        consumer.accept(componentBuilder);
        return this;
    }

    public COMPONENT build() {
        return pass().build();
    }

    public PARENT_BUILDER end() {
        return pass().end();
    }
}

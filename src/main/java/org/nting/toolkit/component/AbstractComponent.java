package org.nting.toolkit.component;

import java.util.Set;

import org.nting.data.Property;
import org.nting.data.bean.RuntimeBean;
import org.nting.data.property.ListProperty;
import org.nting.data.property.MapProperty;
import org.nting.data.property.SetProperty;
import org.nting.toolkit.Component;
import org.nting.toolkit.data.Properties;

import com.google.common.collect.Lists;

public abstract class AbstractComponent implements Component, RuntimeBean {

    private final Properties properties = new Properties();

    public final <T> Property<T> createProperty(Object propertyId, T initialValue) {
        return properties.addObjectProperty(propertyId, initialValue);
    }

    @SafeVarargs
    public final <T> ListProperty<T> createListProperty(Object propertyId, T... initialElements) {
        return properties.addListProperty(propertyId, Lists.newArrayList(initialElements));
    }

    @SafeVarargs
    public final <T> SetProperty<T> createSetProperty(Object propertyId, T... initialElements) {
        return properties.addSetProperty(propertyId, Lists.newArrayList(initialElements));
    }

    public final <K, V> MapProperty<K, V> addMapProperty(Object propertyId) {
        return properties.addMapProperty(propertyId);
    }

    public final boolean removeProperty(Object propertyId) {
        return properties.removeProperty(propertyId);
    }

    public final boolean hasProperty(Object propertyId) {
        return properties.getPropertyNames().contains(propertyId.toString());
    }

    @Override
    public final Set<String> getPropertyNames() {
        return properties.getPropertyNames();
    }

    @Override
    public final <T> Property<T> getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}

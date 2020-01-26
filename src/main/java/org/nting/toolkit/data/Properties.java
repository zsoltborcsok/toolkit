package org.nting.toolkit.data;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.nting.data.Property;
import org.nting.data.property.ListProperty;
import org.nting.data.property.MapProperty;
import org.nting.data.property.PropertySet;
import org.nting.data.property.SetProperty;

/**
 * Allows adding the properties multiple times, but the stored value (and its type) won't change. Except the case when
 * custom properties are added; in that case a property is already created outside (exception is thrown).
 */
public class Properties extends PropertySet {

    @Override
    public <T> Property<T> addCustomProperty(Object propertyId, Property<T> property) {
        Property<Object> existingProperty = getProperty(propertyId);
        if (existingProperty == property) { // property is already added with the given propertyId
            return property;
        } else {
            return super.addCustomProperty(propertyId, property);
        }
    }

    @Override
    public <T> Property<T> addObjectProperty(Object propertyId, T initialValue) {
        return firstNonNull(getProperty(propertyId), () -> super.addObjectProperty(propertyId, initialValue));
    }

    @Override
    public <T> ListProperty<T> addListProperty(Object propertyId, Iterable<? extends T> initialElements) {
        return firstNonNull((ListProperty<T>) this.<T> getProperty(propertyId),
                () -> super.addListProperty(propertyId, initialElements));
    }

    @Override
    public <T> SetProperty<T> addSetProperty(Object propertyId, Iterable<? extends T> initialElements) {
        return firstNonNull((SetProperty<T>) this.<T> getProperty(propertyId),
                () -> super.addSetProperty(propertyId, initialElements));
    }

    @Override
    public <K, V> MapProperty<K, V> addMapProperty(Object propertyId) {
        return firstNonNull((MapProperty<K, V>) this.<Map<K, V>> getProperty(propertyId),
                () -> super.addMapProperty(propertyId));
    }

    private <T> T firstNonNull(T first, Supplier<T> second) {
        return Optional.ofNullable(first).orElseGet(second);
    }
}

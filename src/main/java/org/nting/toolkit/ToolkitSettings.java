package org.nting.toolkit;

import java.util.Optional;

import org.nting.data.ValueChangeEvent;

import playn.core.PlayN;

public interface ToolkitSettings {

    // region Settings
    default boolean renderingOptimisation() {
        return true;
    }

    default boolean loggingPropertyChanges() {
        return false;
    }
    // endregion

    // region Utilities
    default void logPropertyChange(ValueChangeEvent<Object> event, Object id) {
        if (loggingPropertyChanges()) {
            PlayN.log(getClass()).info("Change@{} [{}: {}]", new Object[] {
                    Optional.ofNullable(id).orElseGet(this::hashCode), event.getPropertyName(), event.getValue() });
        }
    }
    // endregion
}

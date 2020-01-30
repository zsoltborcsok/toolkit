package org.nting.toolkit.component;

import org.nting.data.Property;

public abstract class FieldComponent extends AbstractComponent {

    public final Property<String> errorMessage = createProperty("errorMessage", null);
    public final Property<String> caption = createProperty("caption", null);
    public final Property<Boolean> enabled = createProperty("enabled", true);

    public FieldComponent(String... reLayoutPropertyNames) {
        super(reLayoutPropertyNames);
    }

    // TODO
}

package org.nting.toolkit.component;

import org.nting.data.Property;

public class RadioButton extends CheckBox {

    public final Property<Object> value = createProperty("value", null);

    @Override
    public void setIndeterminate() {
        throw new UnsupportedOperationException();
    }

    protected void changeSelection() {
        if (!selected.getValue()) {
            selected.setValue(true);
        }
    }
}

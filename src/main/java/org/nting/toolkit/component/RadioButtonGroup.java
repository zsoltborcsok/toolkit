package org.nting.toolkit.component;

import java.util.Collections;
import java.util.List;

import org.nting.data.Property;
import org.nting.data.Property.ValueChangeListenerSupport;
import org.nting.data.ValueChangeEvent;
import org.nting.data.ValueChangeListener;
import org.nting.data.property.ObjectProperty;

import com.google.common.collect.Lists;

public class RadioButtonGroup implements ValueChangeListener<Boolean> {

    public final Property<Object> value;

    private final List<RadioButton> radioButtons = Lists.newLinkedList();
    private final Property<RadioButton> selection = new ObjectProperty<>(null);

    public RadioButtonGroup() {
        value = selection.transform(radioButton -> radioButton == null ? null : radioButton.value.getValue());
    }

    public void add(RadioButton radioButton) {
        if (radioButton == null || radioButtons.contains(radioButton)) {
            return;
        }

        radioButtons.add(radioButton);

        if (radioButton.selected.getValue()) {
            if (!selection.hasValue()) {
                selection.setValue(radioButton);
            } else {
                radioButton.selected.setValue(false);
            }
        }
        radioButton.selected.addValueChangeListener(this);
    }

    @SuppressWarnings("unchecked")
    public void remove(RadioButton radioButton) {
        if (radioButton == null || !radioButtons.contains(radioButton)) {
            return;
        }

        radioButtons.remove(radioButton);

        ((ValueChangeListenerSupport<Boolean>) radioButton.selected).removeValueChangeListener(this);
        if (radioButton == selection.getValue()) {
            selection.setValue(null);
        }
    }

    public List<RadioButton> getRadioButtons() {
        return Collections.unmodifiableList(radioButtons);
    }

    public RadioButton getSelection() {
        return selection.getValue();
    }

    public void clearSelection() {
        if (selection.hasValue()) {
            RadioButton oldSelection = selection.getValue();
            selection.setValue(null);
            oldSelection.selected.setValue(false);
        }
    }

    @Override
    public void valueChange(ValueChangeEvent<Boolean> event) {
        Property<Boolean> property = event.getProperty();
        if (property.getValue()) {
            for (RadioButton radioButton : radioButtons) {
                if (radioButton.selected == property) {
                    selection.setValue(radioButton);
                } else {
                    radioButton.selected.setValue(false);
                }
            }
        }
    }
}

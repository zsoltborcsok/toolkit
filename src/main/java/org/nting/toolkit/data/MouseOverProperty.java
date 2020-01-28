package org.nting.toolkit.data;

import org.nting.data.Property;
import org.nting.data.Registration;
import org.nting.data.ValueChangeListener;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.component.AbstractComponent;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.event.MouseMotionEvent;

import playn.core.PlayN;

public class MouseOverProperty extends ObjectProperty<Boolean> {

    private final MouseHandler mouseHandler = new MouseHandler();
    private final AbstractComponent component;

    private Registration registrationOnAttachedProperty;
    private float buttonWidth = 0;

    public MouseOverProperty(AbstractComponent component) {
        super(false);
        this.component = component;
    }

    public void setButtonWidth(Property<Float> buttonWidthProperty) {
        buttonWidthProperty.addValueChangeListener(event -> buttonWidth = event.getValue());

    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<Boolean> listener) {
        if (valueChangeListeners.size() == 0 && PlayN.mouse().hasMouse()) {
            component.addMouseListener(mouseHandler);
            registrationOnAttachedProperty = component.attached.addValueChangeListener(event -> {
                if (!event.getValue()) {
                    setValue(false);
                }
            });
        }

        return super.addValueChangeListener(listener);
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener<Boolean> listener) {
        super.removeValueChangeListener(listener);

        if (valueChangeListeners.size() == 0 && PlayN.mouse().hasMouse()) {
            component.addMouseListener(mouseHandler).remove(); // removeMouseListener
            registrationOnAttachedProperty.remove();
            registrationOnAttachedProperty = null;
        }
    }

    private class MouseHandler implements MouseListener {

        @Override
        public void mouseEntered(MouseEvent e) {
            if (buttonWidth == 0) {
                setValue(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setValue(false);
        }

        @Override
        public void mouseMoved(MouseMotionEvent e) {
            if (0 < buttonWidth) {
                setValue(e.getX() + buttonWidth < component.width.getValue());
            }
        }
    }
}

package org.nting.toolkit.data;

import org.nting.data.Registration;
import org.nting.data.ValueChangeListener;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.component.AbstractComponent;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.event.MouseMotionEvent;

import playn.core.PlayN;
import pythagoras.f.Point;

public class MousePositionProperty extends ObjectProperty<Point> {

    private final MouseHandler mouseHandler = new MouseHandler();
    private final AbstractComponent component;

    private Registration registrationOnAttachedProperty;
    private boolean updateOnMouseDragged = true;

    public MousePositionProperty(AbstractComponent component) {
        super(null);
        this.component = component;
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<Point> listener) {
        if (valueChangeListeners.size() == 0 && PlayN.mouse().hasMouse()) {
            component.addMouseListener(mouseHandler);
            component.attached.addValueChangeListener(event -> {
                if (!event.getValue()) {
                    setValue(null);
                }
            });
        }

        return super.addValueChangeListener(listener);
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener<Point> listener) {
        super.removeValueChangeListener(listener);

        if (valueChangeListeners.size() == 0 && PlayN.mouse().hasMouse()) {
            component.addMouseListener(mouseHandler).remove(); // removeMouseListener
            registrationOnAttachedProperty.remove();
            registrationOnAttachedProperty = null;
        }
    }

    public void setUpdateOnMouseDragged(boolean updateOnMouseDragged) {
        this.updateOnMouseDragged = updateOnMouseDragged;
    }

    private class MouseHandler implements MouseListener {

        @Override
        public void mouseEntered(MouseEvent e) {
            setValue(new Point(e.getX(), e.getY()));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setValue(null);
        }

        @Override
        public void mouseMoved(MouseMotionEvent e) {
            setValue(new Point(e.getX(), e.getY()));
        }

        @Override
        public void mouseDragged(MouseMotionEvent e) {
            if (updateOnMouseDragged) {
                setValue(new Point(e.getX(), e.getY()));
            }
        }
    }
}

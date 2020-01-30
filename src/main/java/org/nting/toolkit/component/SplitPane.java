package org.nting.toolkit.component;

import org.nting.data.Property;
import org.nting.data.ValueChangeEvent;
import org.nting.data.ValueChangeListener;
import org.nting.data.condition.Conditions;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.Component;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.event.MouseMotionEvent;

import playn.core.Canvas;
import playn.core.Platform;
import playn.core.PlayN;

public class SplitPane extends AbstractComponent {

    public enum Type {
        DYNAMIC_RESIZE, FIXED_LEFT, FIXED_RIGHT
    }

    public final Property<Orientation> orientation = createProperty("orientation", Orientation.HORIZONTAL);
    public final Property<Double> resizeWeight = createProperty("resizeWeight", 0.5);
    public final Property<Type> type = createProperty("type", Type.DYNAMIC_RESIZE);
    public final Property<Integer> mouseOverSize = createProperty("mouseOverSize", 8);
    public final Property<Boolean> mouseOverSplit = createReadOnlyProperty("mouseOverSplit", false);

    private Component leftComponent;
    private Component rightComponent;

    public SplitPane() {
        this(Orientation.HORIZONTAL);
    }

    public SplitPane(Orientation orientation) {
        this(orientation, null, null);
    }

    @SuppressWarnings("unchecked")
    public SplitPane(Orientation orientation, Component leftComponent, Component rightComponent) {
        this.orientation.setValue(orientation);

        if (leftComponent != null) {
            setLeftComponent(leftComponent);
        }
        if (rightComponent != null) {
            setRightComponent(rightComponent);
        }

        ValueChangeListener<Float> resizeHandler = new ResizeHandler();
        width.addValueChangeListener(resizeHandler);
        height.addValueChangeListener(resizeHandler);
        Conditions.convert(mouseOverSplit).and(attached).addValueChangeListener(event -> {
            if (event.getValue()) {
                if (SplitPane.this.orientation.getValue() == Orientation.HORIZONTAL) {
                    PlayN.setCursor(Platform.Cursor.COL_RESIZE);
                } else {
                    PlayN.setCursor(Platform.Cursor.ROW_RESIZE);
                }
            } else {
                PlayN.setCursor(Platform.Cursor.DEFAULT);
            }
        });

        setFocusable(false);
        addMouseListener(new MouseHandler());
    }

    public void setLeftComponent(Component leftComponent) {
        if (getComponents().contains(this.leftComponent)) {
            removeComponent(this.leftComponent);
        }

        this.leftComponent = leftComponent;
        super.addComponent(leftComponent);
    }

    public Component getLeftComponent() {
        return leftComponent;
    }

    public void setRightComponent(Component rightComponent) {
        if (getComponents().contains(this.rightComponent)) {
            removeComponent(this.rightComponent);
        }

        this.rightComponent = rightComponent;
        super.addComponent(rightComponent);
    }

    public Component getRightComponent() {
        return rightComponent;
    }

    @Override
    public void addComponent(Component child) {
        super.addComponent(child);

        if (leftComponent == null) {
            leftComponent = child;
        } else if (rightComponent == null) {
            rightComponent = child;
        } else {
            throw new IllegalStateException("SplitPane can only handle two children");
        }
    }

    @Override
    public void removeComponent(Component child) {
        super.removeComponent(child);

        if (child == leftComponent) {
            leftComponent.setClip(false);
            leftComponent = null;
        } else if (child == rightComponent) {
            rightComponent.setClip(false);
            rightComponent = null;
        }
    }

    public int getDividerLocation() {
        if (orientation.getValue() == Orientation.HORIZONTAL) {
            return (int) Math.round(width.getValue() * resizeWeight.getValue());
        } else {
            return (int) Math.round(height.getValue() * resizeWeight.getValue());
        }
    }

    public void setDividerLocation(int dividerLocation) {
        dividerLocation = Math.max(0, dividerLocation);

        if (orientation.getValue() == Orientation.HORIZONTAL) {
            dividerLocation = (int) Math.min(width.getValue() - mouseOverSize.getValue(), dividerLocation);
            resizeWeight.setValue(dividerLocation / (double) width.getValue());
        } else {
            dividerLocation = (int) Math.min(height.getValue() - mouseOverSize.getValue(), dividerLocation);
            resizeWeight.setValue(dividerLocation / (double) height.getValue());
        }
    }

    @Override
    public void doPaintChildren(Canvas canvas) {
        if (leftComponent != null) {
            leftComponent.setClip(true);
        }
        if (rightComponent != null) {
            rightComponent.setClip(true);
        }
        super.doPaintChildren(canvas);
    }

    private class ResizeHandler implements ValueChangeListener<Float> {

        @Override
        public void valueChange(ValueChangeEvent<Float> event) {
            if (orientation.getValue() == Orientation.HORIZONTAL && event.getProperty() != width) {
                return;
            } else if (orientation.getValue() == Orientation.VERTICAL && event.getProperty() != height) {
                return;
            } else if (event.getPrevValue() == 0) {
                return;
            }

            int oldDividerLocation = (int) Math.round(event.getPrevValue() * resizeWeight.getValue());
            if (type.getValue() == Type.FIXED_LEFT) {
                if (event.getValue() < oldDividerLocation) {
                    resizeWeight.setValue(1.0);
                } else {
                    resizeWeight.setValue(oldDividerLocation / (double) event.getValue());
                }
            } else if (type.getValue() == Type.FIXED_RIGHT) {
                if (event.getValue() < event.getPrevValue() - oldDividerLocation) {
                    resizeWeight.setValue(0.0);
                } else {
                    resizeWeight
                            .setValue(1.0 - (event.getPrevValue() - oldDividerLocation) / (double) event.getValue());
                }
            }
        }
    }

    private class MouseHandler implements MouseListener {

        private float mouseStart = 0;
        private float delta = 0;

        @Override
        public void mouseDragged(MouseMotionEvent e) {
            if (mouseOverSplit.getValue()) {
                delta += orientation.getValue() == Orientation.HORIZONTAL ? e.getDx() : e.getDy();
                setDividerLocation(Math.round(mouseStart + delta));
                e.consume();
            } else {
                mouseMoved(e);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            ((ObjectProperty<Boolean>) mouseOverSplit).forceValue(false);
        }

        @Override
        public void mouseMoved(MouseMotionEvent e) {
            int dividerLocation = getDividerLocation();
            float mousePosition = orientation.getValue() == Orientation.HORIZONTAL ? e.getX() : e.getY();
            if (dividerLocation <= mousePosition && mousePosition <= dividerLocation + mouseOverSize.getValue()) {
                mouseStart = mousePosition;
                delta = dividerLocation - mousePosition;
                ((ObjectProperty<Boolean>) mouseOverSplit).forceValue(true);
                e.consume();
            } else {
                ((ObjectProperty<Boolean>) mouseOverSplit).forceValue(false);
            }
        }
    }
}

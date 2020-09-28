package org.nting.toolkit.component;

import static org.nting.toolkit.component.Alignment.TOP_LEFT;
import static org.nting.toolkit.component.Orientation.VERTICAL;
import static org.nting.toolkit.event.MouseEvent.MouseButton.BUTTON_LEFT;
import static org.nting.toolkit.layout.FormLayout.xy;
import static pythagoras.f.MathUtil.clamp;
import static pythagoras.f.MathUtil.round;

import java.util.function.Function;

import org.nting.data.Property;
import org.nting.data.binding.Bindings;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.event.MouseMotionEvent;
import org.nting.toolkit.layout.FormLayout;

import pythagoras.f.MathUtil;

public class Slider extends AbstractComponent {

    public final Property<Float> value = createProperty("value", 0.0f);
    public final Property<Float> min = createProperty("min", 0.0f);
    public final Property<Float> max = createProperty("max", 1.0f);
    public final Property<Integer> divisions = createProperty("divisions", 0);
    public final Property<Boolean> enabled = createProperty("enabled", true);
    public final Property<Integer> padding = createProperty("padding", 20);
    public final Property<Function<Float, String>> valueFormatter = createProperty("valueFormatter", Object::toString);

    public final Property<Boolean> pressed = createReadOnlyProperty("pressed", false);

    private TooltipPopup tooltipPopup;

    public Slider() {
        super("padding");

        addMouseListener(new MouseHandler());

        // Keep the min, max and value properties consistent (min <= value <= max).
        min.addValueChangeListener(event -> {
            if (max.getValue() < event.getValue()) {
                max.setValue(event.getValue());
            }
            if (value.getValue() < event.getValue()) {
                value.setValue(event.getValue());
            }
        });
        max.addValueChangeListener(event -> {
            if (event.getValue() < min.getValue()) {
                min.setValue(event.getValue());
            }
            if (event.getValue() < value.getValue()) {
                value.setValue(event.getValue());
            }
        });
        value.addValueChangeListener(event -> {
            if (event.getValue() < min.getValue()) {
                min.setValue(event.getValue());
            }
            if (max.getValue() < event.getValue()) {
                max.setValue(event.getValue());
            }
        });

        Bindings.transformToPair(pressed, value).addValueChangeListener(pressedAvdValue -> {
            if (pressedAvdValue.getValue().first && valueFormatter.hasValue()) {
                String text = valueFormatter.getValue().apply(pressedAvdValue.getValue().second);
                if (tooltipPopup == null) {
                    new ContainerBuilder<>(tooltipPopup = new TooltipPopup(TOP_LEFT, VERTICAL,
                            new FormLayout("3dlu, pref, 3dlu", "1dlu, pref, 1dlu"))).addLabel(xy(1, 1)).text(text)
                                    .pass().font(font.getValue()).set("color", tooltipPopup.color).end().build()
                                    .showRelativeTo(this);
                } else {
                    ((Label) tooltipPopup.componentAt(xy(1, 1))).text.setValue(text);
                    tooltipPopup.showRelativeTo(this);
                }
                // Align the tooltipPopup to the thumb if there is a space for it.
                if (tooltipPopup.width.getValue() < width.getValue()) {
                    float thumbPosition = padding.getValue()
                            + (width.getValue() - padding.getValue() * 2) * sliderPercentage();
                    float dx = thumbPosition - (tooltipPopup.width.getValue() / 2) - 2; // Magic 2: some extra border?
                    float adjustment = clamp(dx, 0, width.getValue() - tooltipPopup.width.getValue());
                    tooltipPopup.x.adjustValue(x -> x + adjustment);
                }
            } else {
                if (tooltipPopup != null) {
                    tooltipPopup.close();
                    tooltipPopup = null;
                }
            }
        });
    }

    @Override
    public boolean isFocusable() {
        return super.isFocusable() && enabled.getValue();
    }

    private void updateValue(float percentage) {
        if (0 < divisions.getValue()) {
            float dPercentage = round(percentage * divisions.getValue()) / (float) divisions.getValue();
            value.setValue(min.getValue() + dPercentage * (max.getValue() - min.getValue()));
        } else {
            value.setValue(min.getValue() + percentage * (max.getValue() - min.getValue()));
        }
    }

    private float sliderPercentage() {
        return min.valueEquals(max) ? 0 : (value.getValue() - min.getValue()) / (max.getValue() - min.getValue());
    }

    private class MouseHandler implements MouseListener {

        @Override
        public void mousePressed(MouseEvent e) {
            if (enabled.getValue()) {
                ((ObjectProperty<Boolean>) pressed).forceValue(true);
                float sliderWidth = width.getValue() - padding.getValue() * 2;
                float position = e.getX() - padding.getValue();
                updateValue(MathUtil.clamp(position / sliderWidth, 0, 1));
                e.consume();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (enabled.getValue()) {
                ((ObjectProperty<Boolean>) pressed).forceValue(false);
                e.consume();
            }
        }

        @Override
        public void mouseDragged(MouseMotionEvent e) {
            if (e.getButton() == BUTTON_LEFT && enabled.getValue()) {
                float sliderWidth = width.getValue() - padding.getValue() * 2;
                float position = e.getX() - padding.getValue();
                updateValue(MathUtil.clamp(position / sliderWidth, 0, 1));
                e.consume();
            }
        }

    }
}

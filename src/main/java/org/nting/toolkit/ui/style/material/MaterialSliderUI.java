package org.nting.toolkit.ui.style.material;

import static org.nting.toolkit.ui.stone.ContentBuilder.builderOnContent;
import static org.nting.toolkit.ui.stone.ContentBuilder.builderOnFixShape;
import static org.nting.toolkit.ui.stone.ContentBuilder.builderOnShape;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.DISABLED_TEXT_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.DIVIDER_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.PRIMARY_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.disabledColor;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.dividerColor;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.secondaryColor;
import static org.nting.toolkit.ui.style.material.SliderPropertyIds.DISABLED_COLOR;
import static org.nting.toolkit.ui.style.material.SliderPropertyIds.SELECTED_COLOR;
import static pythagoras.f.MathUtil.round;

import java.util.Map;
import java.util.Optional;

import org.nting.data.Property;
import org.nting.data.Registration;
import org.nting.toolkit.Component;
import org.nting.toolkit.animation.Tween;
import org.nting.toolkit.component.Slider;
import org.nting.toolkit.ui.ComponentUI;
import org.nting.toolkit.ui.shape.CircleShape;
import org.nting.toolkit.ui.shape.RoundedRectangleShape;
import org.nting.toolkit.ui.stone.ContentBuilder;
import org.nting.toolkit.ui.stone.FixShapeContent;

import com.google.common.collect.Maps;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class MaterialSliderUI implements ComponentUI<Slider> {

    private static final Map<Slider, Registration> registrations = Maps.newHashMap();
    private static final String SLIDER_PERCENT = "sliderPercent";

    @Override
    public void initialize(Slider slider) {
        slider.createProperty(SELECTED_COLOR, PRIMARY_COLOR);
        slider.createProperty(DISABLED_COLOR, DISABLED_TEXT_COLOR);
        animateSliderPercent(slider, slider.createProperty(SLIDER_PERCENT,
                calculatePercentage(slider.value.getValue(), slider.min.getValue(), slider.max.getValue())));
        slider.activateMouseOver();
    }

    private void animateSliderPercent(Slider slider, Property<Float> sliderPercent) {
        registrations.put(slider, slider.value.addValueChangeListener(event -> {
            float toPercent = calculatePercentage(event.getValue(), slider.min.getValue(), slider.max.getValue());
            if (0 < slider.divisions.getValue()) {
                float fromPercent = calculatePercentage(event.getPrevValue(), slider.min.getValue(),
                        slider.max.getValue());
                slider.addBehavior(new Tween<>(sliderPercent, fromPercent, toPercent, 80));
            } else {
                sliderPercent.setValue(toPercent);
            }
        }));
    }

    @Override
    public void terminate(Slider slider) {
        slider.removeProperty(SELECTED_COLOR);
        slider.removeProperty(SLIDER_PERCENT);
        Optional.ofNullable(registrations.remove(slider)).ifPresent(Registration::remove);
    }

    @Override
    public void paintComponent(Slider slider, Canvas canvas) {
        int activeTrackColor = getActiveTrackColor(slider);
        int inactiveTrackColor = getInactiveTrackColor(slider);
        float sliderPercent = slider.<Float> getValue(SLIDER_PERCENT);

        int padding = slider.padding.getValue();
        int thumbRadius = padding / 2;
        int activeTrackHeight = round(padding * 0.3f);
        int inactiveTrackHeight = round(padding * 0.2f);
        int indicatorRadius = round(padding * 0.05f);

        Dimension size = slider.getSize();
        float innerWidth = size.width - 2 * padding;
        int sliderPosition = round(innerWidth * sliderPercent);

        // Painting little bit bigger track to be able to show the first and last indicators on it.
        ContentBuilder inactiveTrackBuilder = builderOnShape(
                new RoundedRectangleShape(indicatorRadius).fillColor(inactiveTrackColor),
                innerWidth + 2 * indicatorRadius, inactiveTrackHeight);
        inactiveTrackBuilder.center().paint(canvas, size);

        if (0 < sliderPercent) { // Painting little bit bigger track here as well.
            ContentBuilder activeTrackBuilder = builderOnShape(
                    new RoundedRectangleShape(2 * indicatorRadius).fillColor(activeTrackColor),
                    sliderPosition + 2 * indicatorRadius, activeTrackHeight);
            activeTrackBuilder.leftBorder(padding - indicatorRadius).alignLeft().paint(canvas, size);
        }

        if (1 < slider.divisions.getValue()) {
            for (int i = 0; i <= slider.divisions.getValue(); i++) {
                int indicatorPosition = round(innerWidth * i / slider.divisions.getValue());
                int indicatorColor = (indicatorPosition < sliderPosition) ? DIVIDER_COLOR : activeTrackColor;
                builderOnFixShape(new CircleShape(padding + indicatorPosition, indicatorRadius, indicatorRadius)
                        .fillColor(indicatorColor), size.width, indicatorRadius * 2).centerVertically().paint(canvas,
                                size);
            }
        }

        if (slider.enabled.getValue() && (slider.focused.getValue() || slider.mouseOver.getValue())) {
            int hoverColor = slider.focused.getValue() ? disabledColor(activeTrackColor)
                    : dividerColor(activeTrackColor);
            new CircleShape(padding + sliderPosition - 1, size.height / 2, padding).fillColor(hoverColor).paint(canvas);
        }

        ContentBuilder thumbBuilder = builderOnContent(
                new FixShapeContent(new Dimension(2 * thumbRadius, 2 * thumbRadius),
                        new CircleShape(thumbRadius, thumbRadius, thumbRadius).fillColor(activeTrackColor)));
        thumbBuilder.emptyBorder(0, padding, 0, thumbRadius + sliderPosition).alignLeft().paint(canvas, size);
    }

    @Override
    public Dimension getPreferredSize(Slider slider) {
        float thePadding = slider.padding.getValue();
        return new Dimension(4 * thePadding, 2 * thePadding);
    }

    @Override
    public void paintForeground(Slider component, Canvas canvas) {
    }

    private float calculatePercentage(float value, float min, float max) {
        return min < max ? (value - min) / (max - min) : 0;
    }

    private int getActiveTrackColor(Slider slider) {
        if (slider.enabled.getValue()) {
            return SELECTED_COLOR.<Integer> getValueOf(slider);
        } else {
            return DISABLED_COLOR.<Integer> getValueOf(slider);
        }
    }

    private int getInactiveTrackColor(Slider slider) {
        if (slider.enabled.getValue()) {
            return secondaryColor(SELECTED_COLOR.<Integer> getValueOf(slider));
        } else {
            return secondaryColor(DISABLED_COLOR.<Integer> getValueOf(slider));
        }
    }

    @Override
    public boolean isComponentSupported(Component c) {
        return c instanceof Slider;
    }
}

package org.nting.toolkit.ui.style.material;

import static java.lang.Boolean.TRUE;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.PRIMARY_BACKGROUND_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.PRIMARY_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.disabledColor;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.dividerColor;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.secondaryColor;
import static org.nting.toolkit.ui.style.material.SwitchButtonPropertyIds.SELECTED_COLOR;
import static org.nting.toolkit.ui.style.material.SwitchButtonPropertyIds.UNSELECTED_COLOR;

import java.util.Map;
import java.util.Optional;

import org.nting.data.Property;
import org.nting.data.Registration;
import org.nting.toolkit.Component;
import org.nting.toolkit.animation.Tween;
import org.nting.toolkit.component.SwitchButton;
import org.nting.toolkit.ui.ComponentUI;
import org.nting.toolkit.ui.shape.CircleShadowShape;
import org.nting.toolkit.ui.shape.CircleShape;
import org.nting.toolkit.ui.shape.RoundedRectangleShape;
import org.nting.toolkit.ui.stone.BasicShapeContent;
import org.nting.toolkit.ui.stone.ContentBuilder;
import org.nting.toolkit.ui.stone.FixShapeContent;
import org.nting.toolkit.ui.stone.TextContent;
import org.nting.toolkit.util.ColorUtils;

import com.google.common.collect.Maps;

import playn.core.Canvas;
import pythagoras.f.Dimension;
import pythagoras.f.MathUtil;

public class MaterialSwitchButtonUI implements ComponentUI<SwitchButton> {

    private static final Map<SwitchButton, Registration> registrations = Maps.newHashMap();
    private static final String SWITCH_PERCENT = "switchPercent";

    @Override
    public void initialize(SwitchButton switchButton) {
        switchButton.createProperty(SELECTED_COLOR, PRIMARY_COLOR);
        switchButton.createProperty(UNSELECTED_COLOR, PRIMARY_BACKGROUND_COLOR);
        animateSwitchPercent(switchButton,
                switchButton.createProperty(SWITCH_PERCENT, switchButton.switched.getValue() ? 100f : 0f));
        switchButton.activateMouseOver();
    }

    private void animateSwitchPercent(SwitchButton switchButton, Property<Float> switchPercent) {
        registrations.put(switchButton, switchButton.switched.addValueChangeListener(event -> {
            if (event.getValue()) {
                switchButton.addBehavior(new Tween<>(switchPercent, 0f, 100f, 80));
            } else {
                switchButton.addBehavior(new Tween<>(switchPercent, 100f, 0f, 80));
            }
        }));
    }

    @Override
    public void terminate(SwitchButton switchButton) {
        switchButton.removeProperty(SELECTED_COLOR);
        switchButton.removeProperty(UNSELECTED_COLOR);
        switchButton.removeProperty(SWITCH_PERCENT);
        Optional.ofNullable(registrations.remove(switchButton)).ifPresent(Registration::remove);
    }

    @Override
    public void paintComponent(SwitchButton switchButton, Canvas canvas) {
        int sliderColor = getSliderColor(switchButton);
        int knobColor = getKnobColor(switchButton);
        float thePadding = switchButton.padding.getValue();

        float preferredTextHeight = switchButton.getRightContent().getPreferredHeight();
        ContentBuilder contentBuilder = ContentBuilder
                .builderOnContent(new BasicShapeContent(new Dimension(thePadding * 4, preferredTextHeight / 2),
                        new RoundedRectangleShape(MathUtil.ifloor(preferredTextHeight / 4)).fillColor(sliderColor)));
        contentBuilder.paddedContent(1, 1f * thePadding, 1, 0);

        TextContent textContent = switchButton.switched.getValue() ? switchButton.getLeftContent()
                : switchButton.getRightContent();
        textContent.setColor(switchButton.enabled.getValue() ? switchButton.color : disabledColor(switchButton.color));
        contentBuilder.horizontalContents(textContent).paddedContent(thePadding, 0, thePadding, 1f * thePadding);
        contentBuilder.paint(canvas);

        float position = thePadding - 1 + (thePadding * 2) * switchButton.<Float> getValue(SWITCH_PERCENT) / 100;
        float radius = thePadding * 1.2f;
        if (switchButton.enabled.getValue() && (switchButton.focused.getValue() || switchButton.mouseOver.getValue())) {
            int hoverColor = switchButton.switched.getValue() == TRUE ? SELECTED_COLOR.getValueOf(switchButton)
                    : switchButton.color.getValue();
            hoverColor = dividerColor(hoverColor);
            new CircleShape(radius + position, switchButton.height.getValue() / 2, switchButton.height.getValue() / 2)
                    .fillColor(hoverColor).paint(canvas);
        }

        circleWithShadow(radius, knobColor).paddedContentWithoutClipping(0, 0, 0, position)
                .paddedContentWithoutClipping(1, 0, 1, 0).paintUsingHeight(canvas, switchButton.height.getValue());
    }

    @Override
    public Dimension getPreferredSize(SwitchButton switchButton) {
        float thePadding = switchButton.padding.getValue();
        Dimension preferredLeftContentSize = switchButton.getLeftContent().getPreferredSize();
        Dimension preferredRightContentSize = switchButton.getRightContent().getPreferredSize();
        float maxTextWidth = Math.max(preferredLeftContentSize.width, preferredRightContentSize.width);
        float maxTextHeight = Math.max(preferredLeftContentSize.height, preferredRightContentSize.height);
        return new Dimension(maxTextWidth + 7 * thePadding, maxTextHeight + 2 * thePadding);
    }

    @Override
    public void paintForeground(SwitchButton component, Canvas canvas) {
    }

    private int getSliderColor(SwitchButton switchButton) {
        if (switchButton.enabled.getValue()) {
            if (switchButton.switched.getValue()) {
                return secondaryColor(SELECTED_COLOR.<Integer> getValueOf(switchButton));
            } else {
                return secondaryColor(switchButton.color.getValue());
            }
        } else {
            return dividerColor(switchButton.color.getValue());
        }
    }

    private int getKnobColor(SwitchButton switchButton) {
        if (switchButton.enabled.getValue()) {
            if (switchButton.switched.getValue()) {
                return SELECTED_COLOR.getValueOf(switchButton);
            } else {
                return UNSELECTED_COLOR.getValueOf(switchButton);
            }
        } else {
            return ColorUtils.drawOver(0xffFFFFFF, disabledColor(switchButton.color.getValue()));
        }
    }

    private ContentBuilder circleWithShadow(float radius, int background) {
        ContentBuilder contentBuilder = new ContentBuilder();
        contentBuilder.fixShapeContent(new CircleShadowShape(0, 0, 1, 5, 0x99000000).circle(radius, radius, radius),
                new Dimension(2 * radius, 2 * radius));
        return contentBuilder.addOverContent(new FixShapeContent(new Dimension(2 * radius, 2 * radius),
                new CircleShape(radius, radius, radius).fillColor(background)));
    }

    @Override
    public boolean isComponentSupported(Component c) {
        return c instanceof SwitchButton;
    }
}

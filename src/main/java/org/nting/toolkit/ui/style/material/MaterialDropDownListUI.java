package org.nting.toolkit.ui.style.material;

import static org.nting.toolkit.ToolkitServices.fontManager;
import static org.nting.toolkit.ui.Colors.TRANSPARENT;
import static org.nting.toolkit.ui.style.material.DropDownListPropertyIds.BACKGROUND_COLOR;
import static org.nting.toolkit.ui.style.material.DropDownListPropertyIds.DIVIDER_COLOR;
import static org.nting.toolkit.ui.style.material.DropDownListPropertyIds.FOCUSED_COLOR;
import static org.nting.toolkit.ui.style.material.DropDownListPropertyIds.SECONDARY_COLOR;
import static org.nting.toolkit.ui.style.material.DropDownListPropertyIds.SELECTION_BACKGROUND;
import static org.nting.toolkit.ui.style.material.MaterialContentUtil.paintFocusedLine;
import static org.nting.toolkit.ui.style.material.MaterialFieldUI.FOCUS_PERCENT;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.PRIMARY_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.SECONDARY_TEXT_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.disabledColor;

import java.util.Map;
import java.util.Optional;

import org.nting.data.Property;
import org.nting.data.Registration;
import org.nting.toolkit.Component;
import org.nting.toolkit.animation.Tween;
import org.nting.toolkit.component.DropDownList;
import org.nting.toolkit.component.FontIcon;
import org.nting.toolkit.ui.ComponentUI;
import org.nting.toolkit.ui.shape.RectangleShape;
import org.nting.toolkit.ui.stone.Content;
import org.nting.toolkit.ui.stone.ContentBuilder;
import org.nting.toolkit.ui.stone.TextContent;

import com.google.common.collect.Maps;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class MaterialDropDownListUI implements ComponentUI<DropDownList<?>> {

    private static final Map<DropDownList<?>, Registration> registrations = Maps.newHashMap();
    private static final String BUTTON_WIDTH = "BUTTON_WIDTH";

    @Override
    public void initialize(DropDownList<?> component) {
        component.createProperty(BACKGROUND_COLOR, TRANSPARENT);
        component.createProperty(SECONDARY_COLOR, SECONDARY_TEXT_COLOR); // caption, icon
        component.createProperty(DIVIDER_COLOR, MaterialStyleColors.DIVIDER_COLOR); // line
        component.createProperty(FOCUSED_COLOR, PRIMARY_COLOR); // focused line
        component.createProperty(SELECTION_BACKGROUND, MaterialStyleColors.SELECTION_BACKGROUND);
        component.createProperty(BUTTON_WIDTH, 0f);
        animateFocusPercent(component, component.createProperty(FOCUS_PERCENT, 0f));
    }

    private void animateFocusPercent(DropDownList<?> dropDownList, Property<Float> focusPercent) {
        registrations.put(dropDownList, dropDownList.focused.addValueChangeListener(event -> {
            if (event.getValue()) {
                dropDownList.addBehavior(new Tween<>(focusPercent, 0f, 100f, 150));
            } else {
                focusPercent.setValue(0f);
            }
        }));
    }

    @Override
    public void terminate(DropDownList<?> component) {
        component.removeProperty(SECONDARY_COLOR);
        component.removeProperty(DIVIDER_COLOR);
        component.removeProperty(FOCUSED_COLOR);
        component.removeProperty(SELECTION_BACKGROUND);
        component.removeProperty(FOCUS_PERCENT);
        component.removeProperty(BUTTON_WIDTH);
        Optional.ofNullable(registrations.remove(component)).ifPresent(Registration::remove);
    }

    @Override
    public void paintComponent(DropDownList<?> dropDownList, Canvas canvas) {
        paintLine(dropDownList).topPaddedContent(1).paint(canvas, dropDownList.getSize());

        int theHPadding = dropDownList.hPadding.getValue();
        int theVPadding = dropDownList.vPadding.getValue();
        TextContent textContent = dropDownList.getTextContent();
        if (0 < dropDownList.selectionLength.getValue()) {
            float selectionX1 = Math.round(textContent.getCaretPosition(0, dropDownList.selectionStart.getValue()));
            float selectionX2 = Math.round(textContent.getCaretPosition(0,
                    dropDownList.selectionStart.getValue() + dropDownList.selectionLength.getValue()));
            selectionX1 = Math.max(0, Math.round(selectionX1 + textContent.getTranslateX()));
            selectionX2 = Math.min(dropDownList.getSize().width /*- dropDownList.getTextContentPadding()*/,
                    Math.round(selectionX2 + textContent.getTranslateX()));
            new RectangleShape(selectionX1 + theHPadding, theVPadding - 1, selectionX2 - selectionX1 + 1,
                    dropDownList.getSize().height - 2 * (theVPadding - 1))
                            .fillColor(SELECTION_BACKGROUND.getValueOf(dropDownList)).paint(canvas);
        }

        ContentBuilder buttonContentBuilder = new ContentBuilder().content(getExpandIcon(dropDownList))
                .paddedContent(1, theVPadding, 1, theVPadding).measureContent();
        Property<Integer> textColor = dropDownList.enabled.getValue() ? dropDownList.color
                : disabledColor(dropDownList.color);
        new ContentBuilder().content(textContent, textColor)
                .paddedContent(theVPadding - 1, 0, theVPadding - 1, theHPadding).rightPaddedContent(1)
                .horizontalContentsRightAligned(buttonContentBuilder.getContent()).emptyBorder(1, 0, 1, 0)
                .paint(canvas, dropDownList.getSize());
        dropDownList.setValue(BUTTON_WIDTH, buttonContentBuilder.getMeasuredSize().width);
    }

    private Content getExpandIcon(DropDownList<?> dropDownList) {
        int iconColor = dropDownList.enabled.getValue() ? SECONDARY_COLOR.getValueOf(dropDownList)
                : disabledColor(SECONDARY_COLOR.<Integer> getValueOf(dropDownList));
        FontIcon icon = dropDownList.isPopupVisible() ? FontIcon.CARET_UP : FontIcon.CARET_DOWN;
        return icon.getContent(fontManager().getFontSize(dropDownList.font.getValue()), iconColor);
    }

    protected ContentBuilder paintLine(DropDownList<?> component) {
        return paintFocusedLine(component.enabled.getValue(), false, component.getValue(FOCUS_PERCENT),
                component.width.getValue(), DIVIDER_COLOR.getValueOf(component), 0,
                FOCUSED_COLOR.getValueOf(component));
    }

    @Override
    public Dimension getPreferredSize(DropDownList<?> dropDownList) {
        int theHPadding = dropDownList.hPadding.getValue();
        int theVPadding = dropDownList.vPadding.getValue();
        Dimension preferredContentSize = dropDownList.getTextContent().getPreferredSize();
        return new Dimension(preferredContentSize.width + dropDownList.<Float> getValue(BUTTON_WIDTH) + 2 * theHPadding,
                preferredContentSize.height + 2 * theVPadding);
    }

    @Override
    public void paintForeground(DropDownList<?> component, Canvas canvas) {
    }

    @Override
    public boolean isComponentSupported(Component c) {
        return c instanceof DropDownList;
    }
}

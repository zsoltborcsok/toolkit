package org.nting.toolkit.ui.style.material;

import static java.lang.Boolean.TRUE;
import static org.nting.toolkit.ui.style.material.CheckBoxPropertyIds.CHECK_BOX_CHECKED_COLOR;
import static org.nting.toolkit.ui.style.material.CheckBoxPropertyIds.CHECK_BOX_SIZE;
import static org.nting.toolkit.ui.style.material.CheckBoxPropertyIds.CHECK_MARK_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.PRIMARY_BACKGROUND_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.PRIMARY_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.disabledColor;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.dividerColor;

import org.nting.data.Property;
import org.nting.toolkit.Component;
import org.nting.toolkit.component.CheckBox;
import org.nting.toolkit.ui.ComponentUI;
import org.nting.toolkit.ui.shape.CircleShape;
import org.nting.toolkit.ui.shape.RoundedRectangleShape;
import org.nting.toolkit.ui.stone.Content;
import org.nting.toolkit.ui.stone.ContentBuilder;

import com.google.common.primitives.Floats;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class MaterialCheckBoxUI<T extends CheckBox> implements ComponentUI<T> {

    @Override
    public void initialize(T checkBox) {
        checkBox.createProperty(CHECK_BOX_SIZE, 3);
        checkBox.createProperty(CHECK_BOX_CHECKED_COLOR, PRIMARY_COLOR);
        checkBox.createProperty(CHECK_MARK_COLOR, PRIMARY_BACKGROUND_COLOR);
        checkBox.activateMouseOver();
    }

    @Override
    public void terminate(T checkBox) {
        checkBox.removeProperty(CHECK_BOX_SIZE);
        checkBox.removeProperty(CHECK_BOX_CHECKED_COLOR);
        checkBox.removeProperty(CHECK_MARK_COLOR);
    }

    @Override
    public void paintComponent(T checkBox, Canvas canvas) {
        float thePadding = checkBox.padding.getValue();

        if (checkBox.enabled.getValue() && (checkBox.focused.getValue() || checkBox.mouseOver.getValue())) {
            int hoverColor = checkBox.selected.getValue() == TRUE ? CHECK_BOX_CHECKED_COLOR.getValueOf(checkBox)
                    : checkBox.color.getValue();
            hoverColor = dividerColor(hoverColor);
            new CircleShape(thePadding * 2 + 2, checkBox.height.getValue() / 2,
                    (int) CHECK_BOX_SIZE.getValueOf(checkBox) * 1.15f).fillColor(hoverColor).paint(canvas);
        }

        ContentBuilder contentBuilder = new ContentBuilder();
        Property<Integer> checkBoxColor = checkBox.enabled.getValue() ? checkBox.color : disabledColor(checkBox.color);
        contentBuilder.content(checkBox.getTextContent(), checkBoxColor).leftPaddedContent(thePadding * 2 - 2)
                .horizontalContentsLeft(
                        boxContent(checkBox, (int) CHECK_BOX_SIZE.getValueOf(checkBox), checkBoxColor.getValue()))
                .paddedContent(thePadding - 1, thePadding - 2, thePadding - 1, thePadding - 1).rightPaddedContent(1)
                .contentAndBackground().paint(canvas, checkBox.getSize());
    }

    @Override
    public Dimension getPreferredSize(T checkBox) {
        float thePadding = checkBox.padding.getValue();
        Dimension preferredTextContentSize = checkBox.getTextContent().getPreferredSize();
        float hPadding = preferredTextContentSize.width == 0 ? 2 * thePadding : 6 * thePadding + 1;
        return new Dimension(preferredTextContentSize.width + (int) CHECK_BOX_SIZE.getValueOf(checkBox) + hPadding,
                Floats.max(preferredTextContentSize.height, (int) CHECK_BOX_SIZE.getValueOf(checkBox) + 2)
                        + 2 * thePadding);
    }

    @Override
    public void paintForeground(CheckBox component, Canvas canvas) {
    }

    protected Content boxContent(T checkBox, float boxSize, int checkBoxColor) {
        Boolean selected = checkBox.selected.getValue();
        RoundedRectangleShape boxShape = new RoundedRectangleShape(3);
        if (selected == TRUE) {
            boxShape.fillColor(CHECK_BOX_CHECKED_COLOR.getValueOf(checkBox));
        } else {
            boxShape.strokeColor(checkBoxColor).strokeWidth(2);
        }

        ContentBuilder contentBuilder = new ContentBuilder().basicShapeContent(boxShape,
                new Dimension(boxSize, boxSize));
        if (selected == null) {
            contentBuilder.addScalableContent(checkBoxColor, 2).addLine(0.25f, 0.5f, 0.75f, 0.5f);
        } else if (selected) {
            contentBuilder.addScalableContent(CHECK_MARK_COLOR.getValueOf(checkBox), 2)
                    .addLine(0.25f, 0.55f, 0.4f, 0.75f).addLine(0.4f, 0.75f, 0.8f, 0.25f);
        }
        return contentBuilder.paddedContent(1, 0, 1, 1).getContent();
    }

    @Override
    public boolean isComponentSupported(Component c) {
        return c instanceof CheckBox;
    }
}

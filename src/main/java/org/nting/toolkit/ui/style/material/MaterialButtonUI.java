package org.nting.toolkit.ui.style.material;

import static org.nting.toolkit.ui.style.material.ButtonPropertyIds.RAISED;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.SECONDARY_OPACITY_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.disabledColor;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.dividerColor;
import static org.nting.toolkit.util.ColorUtils.adjustAlpha;
import static org.nting.toolkit.util.ColorUtils.drawOver;
import static org.nting.toolkit.util.MaterialShadows.paintShadow2;
import static org.nting.toolkit.util.MaterialShadows.paintShadow8;

import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.Component;
import org.nting.toolkit.component.Button;
import org.nting.toolkit.ui.ComponentUI;
import org.nting.toolkit.ui.shape.RoundedRectangleShape;
import org.nting.toolkit.ui.stone.Content;
import org.nting.toolkit.ui.stone.ContentBuilder;
import org.nting.toolkit.ui.stone.PaddedContent;
import org.nting.toolkit.ui.stone.TextContent;

import com.google.common.base.Strings;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class MaterialButtonUI implements ComponentUI<Button> {

    @Override
    public void initialize(Button button) {
        button.createProperty(RAISED, false);
        button.activateMouseOver();
    }

    @Override
    public void terminate(Button button) {
        button.removeProperty(RAISED);
    }

    @Override
    public void paintComponent(Button button, Canvas canvas) {
        if (RAISED.getValueOf(button)) {
            paintRaisedButton(button, canvas);
        } else {
            paintFlatButton(button, canvas);
        }
    }

    private void paintFlatButton(Button button, Canvas canvas) {
        ContentBuilder contentBuilder = new ContentBuilder();

        if (button.pressed.getValue()) {
            int backgroundColor = adjustAlpha(102, button.color.getValue()); // 40%
            contentBuilder.background(new RoundedRectangleShape(2), backgroundColor);
        } else if (button.enabled.getValue() && (button.focused.getValue() || button.mouseOver.getValue())) {
            int backgroundColor = dividerColor(button.color.getValue());
            contentBuilder.background(new RoundedRectangleShape(2), backgroundColor);
        }

        int color = button.color.getValue();
        if (!button.enabled.getValue()) {
            color = disabledColor(color);
        }

        paintContent(button, canvas, contentBuilder, color, button.getSize());
    }

    private void paintRaisedButton(Button button, Canvas canvas) {
        ContentBuilder contentBuilder = new ContentBuilder();

        int backgroundColor = button.color.getValue();
        if (button.pressed.getValue()) {
            backgroundColor = drawOver(backgroundColor, 0x66000000); // 40%
        } else if (button.enabled.getValue() && (button.focused.getValue() || button.mouseOver.getValue())) {
            backgroundColor = drawOver(backgroundColor, 0x1E000000); // DarkTheme.DARK_DIVIDER_OPACITY
        }
        contentBuilder.background(new RoundedRectangleShape(2), backgroundColor);

        int color = SECONDARY_OPACITY_COLOR;
        if (!button.enabled.getValue()) {
            color = disabledColor(color);
        }

        canvas.translate(2, 1);
        Dimension size = button.getSize();
        size.width -= 4;
        size.height -= 4;
        if (button.pressed.getValue()) {
            paintShadow8(canvas, size);
        } else {
            paintShadow2(canvas, size);
        }
        paintContent(button, canvas, contentBuilder, color, size);
        canvas.translate(-2, -1);
    }

    private void paintContent(Button button, Canvas canvas, ContentBuilder contentBuilder, int color, Dimension size) {
        ObjectProperty<Integer> colorProperty = new ObjectProperty<>(color);
        if (button.image.getValue() != null && Strings.isNullOrEmpty(button.text.getValue())) {
            contentBuilder.content(button.image.getValue(), colorProperty).paddedContent(1, 1, 1, 1)
                    .contentAndBackground().paint(canvas, size);
        } else {
            contentBuilder.content(button.getTextContent(), colorProperty);
            float thePadding = button.padding.getValue();
            if (button.image.getValue() != null) {
                Content imageContent = button.image.getValue();
                if (imageContent instanceof TextContent) {
                    ((TextContent) imageContent).setColor(colorProperty);
                }
                if (button.getTextContent().getPreferredSize().height < imageContent.getPreferredSize().height) {
                    contentBuilder.paddedContent(1, 0, 1, thePadding).horizontalContentsLeft(imageContent);
                } else {
                    contentBuilder.paddedContent(0, 0, 0, thePadding)
                            .horizontalContentsLeft(new PaddedContent(imageContent, 1, 0, 1, 0));
                }
                contentBuilder.paddedContent(thePadding - 1, 2 * thePadding - 1, thePadding - 1, thePadding - 1);
            } else {
                contentBuilder.paddedContent(thePadding - 1, 2 * thePadding - 1, thePadding - 1, 2 * thePadding - 1);
            }
            contentBuilder.contentAndBackground().paint(canvas, size);
        }
    }

    @Override
    public Dimension getPreferredSize(Button button) {
        Dimension preferredSize = getPreferredSizeOfFlat(button);
        if (RAISED.getValueOf(button)) {
            preferredSize.width += 4; // 2 + 2
            preferredSize.height += 4; // 1 + 3
        }

        return preferredSize;
    }

    private Dimension getPreferredSizeOfFlat(Button button) {
        float thePadding = button.padding.getValue();
        if (button.image.getValue() == null) {
            Dimension preferredTextContentSize = button.getTextContent().getPreferredSize();
            return new Dimension(preferredTextContentSize.width + 4 * thePadding,
                    preferredTextContentSize.height + 2 * thePadding);
        } else if (Strings.isNullOrEmpty(button.text.getValue())) {
            Dimension preferredImageContentSize = button.image.getValue().getPreferredSize();
            preferredImageContentSize.width += 2 * thePadding;
            preferredImageContentSize.height += 2 * thePadding;
            return preferredImageContentSize;
        } else {
            Dimension preferredTextContentSize = button.getTextContent().getPreferredSize();
            Dimension preferredImageContentSize = button.image.getValue().getPreferredSize();
            return new Dimension(preferredTextContentSize.width + preferredImageContentSize.width + 4 * thePadding,
                    Math.max(preferredTextContentSize.height, preferredImageContentSize.height) + 2 * thePadding);
        }
    }

    @Override
    public void paintForeground(Button component, Canvas canvas) {
    }

    @Override
    public boolean isComponentSupported(Component c) {
        return c instanceof Button;
    }
}

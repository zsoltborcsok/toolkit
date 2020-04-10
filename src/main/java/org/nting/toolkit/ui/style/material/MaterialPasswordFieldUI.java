package org.nting.toolkit.ui.style.material;

import static org.nting.toolkit.ui.stone.ContentBuilder.builderOnContent;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.disabledColor;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.secondaryColor;

import org.nting.data.Property;
import org.nting.toolkit.Component;
import org.nting.toolkit.component.PasswordField;
import org.nting.toolkit.ui.stone.ContentBuilder;

import playn.core.Canvas;

public class MaterialPasswordFieldUI extends MaterialTextFieldUI<PasswordField> {

    @Override
    protected void doPaintContent(PasswordField passwordField, Canvas canvas) {
        int theHPadding = passwordField.hPadding.getValue();
        int theVPadding = passwordField.vPadding.getValue();

        Property<Integer> theColor = passwordField.enabled.getValue() ? passwordField.color
                : disabledColor(passwordField.color);
        ContentBuilder contentBuilder = builderOnContent(passwordField.getTextContent(), theColor)
                .emptyBorder(theVPadding, theHPadding, theVPadding, theHPadding).rightPaddedContent(1);
        if (passwordField.icon() != null) {
            Property<Integer> theIconColor = passwordField.enabled.getValue()
                    ? (passwordField.isButtonPressed() ? passwordField.color : secondaryColor(passwordField.color))
                    : disabledColor(passwordField.color);
            if (passwordField.isButtonPressed()) {
                contentBuilder = builderOnContent(passwordField.getNormalTextContent(), theColor)
                        .emptyBorder(theVPadding, theHPadding, theVPadding, theHPadding).rightPaddedContent(1);
                passwordField.setCaretVisible(false);
            }
            ContentBuilder buttonContentBuilder = new ContentBuilder()
                    .iconContent(passwordField.icon(), passwordField.font, theIconColor)
                    .paddedContent(1, theVPadding, 1, theVPadding).measureContent();
            contentBuilder.horizontalContentsRightAligned(buttonContentBuilder.getContent()).paint(canvas,
                    passwordField.getSize());
            passwordField.setButtonWidth(buttonContentBuilder.getMeasuredSize().width);
        } else {
            contentBuilder.paint(canvas, passwordField.getSize());
            passwordField.setButtonWidth(0f);
        }
    }

    @Override
    public boolean isComponentSupported(Component c) {
        return c instanceof PasswordField;
    }
}

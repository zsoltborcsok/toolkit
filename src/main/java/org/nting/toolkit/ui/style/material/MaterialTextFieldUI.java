package org.nting.toolkit.ui.style.material;

import static org.nting.toolkit.ui.stone.ContentBuilder.builderOnContent;
import static org.nting.toolkit.ui.style.material.FieldComponentPropertyIds.BACKGROUND_COLOR;
import static org.nting.toolkit.ui.style.material.FieldComponentPropertyIds.SELECTION_BACKGROUND;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.disabledColor;

import org.nting.toolkit.Component;
import org.nting.toolkit.component.TextField;
import org.nting.toolkit.ui.shape.LineShape;
import org.nting.toolkit.ui.shape.RectangleShape;
import org.nting.toolkit.ui.stone.TextContent;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class MaterialTextFieldUI<T extends TextField> extends MaterialFieldUI<T> {

    @Override
    public void paintComponent(T textField, Canvas canvas) {
        new RectangleShape().fillColor(BACKGROUND_COLOR.getValueOf(textField)).size(textField.getSize()).paint(canvas);
        paintLine(textField).topPaddedContent(1).paint(canvas, textField.getSize());

        int theHPadding = textField.hPadding.getValue();
        int theVPadding = textField.vPadding.getValue();
        TextContent textContent = textField.getTextContent();
        if (0 < textField.selectionLength.getValue()) {
            float selectionX1 = Math.round(textContent.getCaretPosition(0, textField.selectionStart.getValue()));
            float selectionX2 = Math.round(textContent.getCaretPosition(0,
                    textField.selectionStart.getValue() + textField.selectionLength.getValue()));
            selectionX1 = Math.max(0, Math.round(selectionX1 + textContent.getTranslateX()));
            selectionX2 = Math.min(textField.getSize().width - textField.getTextContentPadding(),
                    Math.round(selectionX2 + textContent.getTranslateX()));
            new RectangleShape(selectionX1 + theHPadding, theVPadding - 1, selectionX2 - selectionX1 + 1,
                    textField.getSize().height - 2 * (theVPadding - 1))
                            .fillColor(SELECTION_BACKGROUND.getValueOf(textField)).paint(canvas);
        }

        doPaintContent(textField, canvas);

        if (textField.isCaretVisible()) {
            float caretX = Math.round(textContent.getCaretPosition(0, textField.caretPosition.getValue()));
            float x = Math.round(caretX + textContent.getTranslateX() + theHPadding);
            new LineShape(x, theVPadding - 1, x, textField.getSize().height - theVPadding)
                    .strokeColor(getCaretColor(textField)).paint(canvas);
        }
    }

    @Override
    public Dimension getPreferredSize(T textField) {
        int theHPadding = textField.hPadding.getValue();
        int theVPadding = textField.vPadding.getValue();
        return new Dimension(2 * theHPadding, textField.getTextContent().getPreferredHeight() + 2 * theVPadding);
    }

    protected void doPaintContent(T textField, Canvas canvas) {
        int theHPadding = textField.hPadding.getValue();
        int theVPadding = textField.vPadding.getValue();
        builderOnContent(textField.getTextContent(),
                textField.enabled.getValue() ? textField.color : disabledColor(textField.color))
                        .emptyBorder(theVPadding, theHPadding, theVPadding, theHPadding).paddedContent(1, 1, 1, 0)
                        .paint(canvas, textField.getSize());
    }

    @Override
    public boolean isComponentSupported(Component c) {
        return c instanceof TextField;
    }
}

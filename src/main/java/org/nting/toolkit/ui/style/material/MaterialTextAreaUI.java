package org.nting.toolkit.ui.style.material;

import static org.nting.toolkit.ToolkitServices.unitConverter;
import static org.nting.toolkit.ui.stone.ContentBuilder.builderOnContent;
import static org.nting.toolkit.ui.style.material.FieldComponentPropertyIds.BACKGROUND_COLOR;
import static org.nting.toolkit.ui.style.material.FieldComponentPropertyIds.SELECTION_BACKGROUND;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.disabledColor;

import java.util.List;

import org.nting.data.util.Pair;
import org.nting.toolkit.Component;
import org.nting.toolkit.component.TextArea;
import org.nting.toolkit.ui.shape.LineShape;
import org.nting.toolkit.ui.shape.RectangleShape;
import org.nting.toolkit.ui.stone.TextContent;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class MaterialTextAreaUI<T extends TextArea> extends MaterialFieldUI<T> {

    @Override
    public void paintComponent(T textArea, Canvas canvas) {
        new RectangleShape().fillColor(BACKGROUND_COLOR.getValueOf(textArea)).size(textArea.getSize()).paint(canvas);
        paintLine(textArea).topPaddedContent(1).paint(canvas, textArea.getSize());

        List<Integer> lineLengths = textArea.getTextContent().getLineLengths();
        float lineHeight = textArea.lineHeight(lineLengths.size());
        textArea.getTextContent().setTranslateY(linePosition(textArea, lineLengths, 0));

        int theHPadding = textArea.hPadding.getValue();
        int theVPadding = textArea.vPadding.getValue();
        if (0 < textArea.selectionLength.getValue()) {
            TextContent textContent = textArea.getTextContent();
            Pair<Integer, Integer> selectionStartCoordinate = textContent
                    .getCaretCoordinate(textArea.selectionStart.getValue());
            Pair<Integer, Integer> selectionEndCoordinate = textContent
                    .getCaretCoordinate(textArea.selectionStart.getValue() + textArea.selectionLength.getValue());

            int selectionRowFrom = Math.max(-textArea.getTranslateY(), selectionStartCoordinate.second);
            int selectionRowTwo = Math.min(textArea.rows.getValue() - textArea.getTranslateY(),
                    selectionEndCoordinate.second);
            for (int i = selectionRowFrom; i <= selectionRowTwo; i++) {
                float selectionX1;
                float selectionX2;
                if (i == selectionStartCoordinate.second) {
                    selectionX1 = Math.round(textContent.getCaretPosition(i, selectionStartCoordinate.first));
                } else {
                    selectionX1 = 0;
                }
                if (i == selectionEndCoordinate.second) {
                    selectionX2 = Math.round(textContent.getCaretPosition(i, selectionEndCoordinate.first));
                } else {
                    selectionX2 = Math.round(textContent.getCaretPosition(i, lineLengths.get(i)));
                }

                selectionX1 = Math.max(0, Math.round(selectionX1 + textContent.getTranslateX()));
                selectionX2 = Math.min(textArea.width.getValue() - 2 * theHPadding,
                        Math.round(selectionX2 + textContent.getTranslateX()));
                float selectionY1 = Math.max(0, linePosition(textArea, lineLengths, i));
                float selectionY2 = Math.min(textArea.height.getValue() - 2 * theVPadding, selectionY1 + lineHeight);
                new RectangleShape(selectionX1 + theHPadding, selectionY1 + theVPadding, selectionX2 - selectionX1 + 1,
                        selectionY2 - selectionY1).fillColor(SELECTION_BACKGROUND.getValueOf(textArea)).paint(canvas);
            }
        }

        builderOnContent(textArea.getTextContent(),
                textArea.enabled.getValue() ? textArea.color : disabledColor(textArea.color))
                        .paddedContent(theVPadding, theHPadding, theVPadding, theHPadding).rightPaddedContent(1)
                        .paint(canvas, textArea.getSize());

        if (textArea.isCaretVisible()) {
            Integer caretLine = textArea.getCaretCoordinate().second;
            if (-textArea.getTranslateY() <= caretLine
                    && caretLine < textArea.rows.getValue() - textArea.getTranslateY()) {
                Integer caretX = textArea.getCaretCoordinate().first;
                float x = Math.round(textArea.getTextContent().getCaretPosition(caretLine, caretX)) + theHPadding
                        + textArea.getTextContent().getTranslateX();
                if (theHPadding <= x && x < textArea.width.getValue() - theHPadding) {
                    float y = linePosition(textArea, lineLengths, caretLine) + theVPadding;
                    new LineShape(x, y, x, y + lineHeight - 1).strokeColor(getCaretColor(textArea)).paint(canvas);
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize(T textArea) {
        int width = unitConverter().dialogUnitXAsPixel(textArea.columns.getValue() * 4, textArea);
        int rows = Math.min(textArea.rows.getValue(), Math.max(1, textArea.getTextContent().getLineCount()));
        int height = unitConverter().dialogUnitYAsPixel(rows * 8, textArea)
                + Math.round(textArea.getTextContent().getLeading() * (rows - 1));

        return new Dimension(width + 2 * textArea.hPadding.getValue(), height + 2 * textArea.vPadding.getValue());
    }

    @Override
    public boolean isComponentSupported(Component c) {
        return c instanceof TextArea;
    }

    private float linePosition(T textArea, List<Integer> lineLengths, int line) {
        return Math.round((line + textArea.getTranslateY()) * textArea.getTextContent().getPreferredSize().height
                / lineLengths.size());
    }
}

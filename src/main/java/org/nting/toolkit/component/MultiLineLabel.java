package org.nting.toolkit.component;

import static org.nting.toolkit.ui.stone.ContentBuilder.builderOnContent;

import java.util.List;

import org.nting.data.util.Pair;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.ui.shape.RectangleShape;
import org.nting.toolkit.ui.stone.ContentBuilder;
import org.nting.toolkit.ui.stone.TextContent;

import playn.core.Canvas;
import playn.core.PlayN;
import pythagoras.f.MathUtil;

public class MultiLineLabel extends Label {

    public MultiLineLabel() {
        width.addValueChangeListener(event -> PlayN.invokeLater(this::reLayout));
    }

    @Override
    protected TextContent createTextContent() {
        return new TextContent(font, color, text, width);
    }

    @Override
    protected MouseListener createMouseHandler() {
        return new MouseHandler();
    }

    @Override
    public void doPaintComponent(Canvas canvas) {
        List<Integer> lineLengths = textContent.getLineLengths();
        float lineHeight = lineHeight(lineLengths);
        if (0 < selectionLength.getValue()) {
            Pair<Integer, Integer> selectionStartCoordinate = textContent.getCaretCoordinate(selectionStart.getValue());
            Pair<Integer, Integer> selectionEndCoordinate = textContent
                    .getCaretCoordinate(selectionStart.getValue() + selectionLength.getValue());

            int selectionRowFrom = Math.max(0, selectionStartCoordinate.second);
            int selectionRowTwo = Math.min(textContent.getLineCount(), selectionEndCoordinate.second);
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
                selectionX2 = Math.min(width.getValue(), Math.round(selectionX2 + textContent.getTranslateX()));
                float selectionY1 = Math.max(0, linePosition(lineLengths, i));
                float selectionY2 = Math.min(height.getValue(), selectionY1 + lineHeight);
                new RectangleShape(selectionX1, selectionY1, selectionX2 - selectionX1 + 1, selectionY2 - selectionY1)
                        .fillColor(selectionBackground.getValue()).paint(canvas);
            }
        }

        ContentBuilder contentBuilder = builderOnContent(textContent);
        if (alignment.getValue() == TextAlignment.LEFT) {
            contentBuilder.rightPaddedContent(1);
        } else if (alignment.getValue() == TextAlignment.CENTER) {
            contentBuilder.paddedContent(0, 1, 0, 1);
        } else if (alignment.getValue() == TextAlignment.RIGHT) {
            contentBuilder.leftPaddedContent(1);
        }
        contentBuilder.paint(canvas, getSize());
    }

    private float lineHeight(List<Integer> lineLengths) {
        return Math.round(textContent.getPreferredSize().height / lineLengths.size());
    }

    private float linePosition(List<Integer> lineLengths, int line) {
        return Math.round(line * textContent.getPreferredSize().height / lineLengths.size());
    }

    private class MouseHandler extends SelectionMouseHandler {

        @Override
        public void mousePressed(MouseEvent e) {
            if (selectionSupported.getValue()) {
                super.mousePressed(e);
            }
        }

        @Override
        public void mouseLongPressed(MouseEvent e) {
            if (selectionSupported.getValue()) {
                super.mouseLongPressed(e);
            }
        }

        @Override
        protected int getCaretIndex(float x, float y) {
            float lineHeight = textContent.getPreferredSize().height / textContent.getLineCount();
            int currentLine = MathUtil.ifloor(y / lineHeight);
            if (textContent.getLineCount() <= currentLine) {
                return text.getValue().length();
            } else if (currentLine < 0 || (currentLine == 0 && x <= 0)) {
                return 0;
            }

            return textContent.getCaretIndex(currentLine, x);
        }
    }
}

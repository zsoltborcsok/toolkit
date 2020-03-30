package org.nting.toolkit.component;

import static org.nting.toolkit.event.MouseEvent.MouseButton.BUTTON_LEFT;

import java.util.List;

import org.nting.data.Property;
import org.nting.data.binding.Bindings;
import org.nting.data.util.Pair;
import org.nting.toolkit.event.KeyEvent;
import org.nting.toolkit.event.KeyListener;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.event.MouseMotionEvent;
import org.nting.toolkit.ui.stone.TextContent;
import org.nting.toolkit.util.ToolkitUtils;

import playn.core.Key;
import playn.core.Platform;
import playn.core.PlayN;
import pythagoras.f.MathUtil;

public class TextArea extends FieldComponent {

    public final Property<Integer> rows = createProperty("rows", 0);
    public final Property<Integer> columns = createProperty("columns", 0);
    public final Property<Integer> hPadding = createProperty("hPadding", 3);
    public final Property<Integer> vPadding = createProperty("vPadding", 3);

    private final Property<Float> innerWidth = Bindings.transformProperties(width, hPadding,
            (width, padding) -> (4 * padding <= width) ? width - 2 * padding : 2 * padding);
    private final TextContent textContent = createTextContent();
    private final Property<Pair<Integer, Integer>> caretCoordinate = createProperty("caretCoordinate", Pair.of(0, 0));
    private final Property<Integer> translateY = createProperty("translateY", 0);

    public TextArea() {
        super("rows", "columns");

        caretPosition.addValueChangeListener(event -> {
            caretCoordinate.setValue(getCaretCoordinate(caretPosition.getValue()));
            scrollToLineVisible(caretCoordinate.getValue().second);
        });
        text.addValueChangeListener(event -> {
            if (textContent.getLineCount() < rows.getValue() - translateY.getValue()) {
                scrollToLine(textContent.getLineCount() - 1);
            }
            if (textContent.getLineCount() <= rows.getValue()) {
                reLayout();
            }
        });

        innerWidth.addValueChangeListener(
                event -> caretCoordinate.setValue(getCaretCoordinate(caretPosition.getValue())));
    }

    public TextContent getTextContent() {
        return textContent;
    }

    protected TextContent createTextContent() {
        return new TextContent(font, color, text, innerWidth);
    }

    protected void selectLine(int line) {
        List<Integer> lineLengths = textContent.getLineLengths();
        int start = 0;
        for (int i = 0; i < line; i++) {
            start += lineLengths.get(i);
        }

        int length = lineLengths.get(line);
        if (0 < line) {
            start++;
            length--;
        }

        selectionStart.setValue(start);
        selectionLength.setValue(length);
    }

    public Pair<Integer, Integer> getCaretCoordinate() {
        return caretCoordinate.getValue();
    }

    private Pair<Integer, Integer> getCaretCoordinate(int caretPosition) {
        return textContent.getCaretCoordinate(caretPosition);
    }

    @Override
    public int search(String searchText) {
        return textContent.search(searchText);
    }

    @Override
    public void highlightMatch(int index) {
        textContent.highlightMatch(index);
        repaint();
        ToolkitUtils.scrollComponentToVisible(this);

        if (0 <= index) {
            scrollToLineVisible(textContent.lineOfMatch(index));
        }
    }

    public int getTranslateY() {
        return translateY.getValue();
    }

    protected void scrollToLine(int line) {
        int maxLine = textContent.getLineCount() - rows.getValue();
        line = Math.max(0, Math.min(maxLine, line));
        translateY.setValue(-line);
    }

    protected void scrollToLineVisible(int line) {
        if (0 <= line) {
            if (line + translateY.getValue() < 0) {
                translateY.setValue(-line);
            } else if (rows.getValue() <= line + translateY.getValue()) {
                translateY.setValue(rows.getValue() - line - 1);
            }
        }
    }

    public float lineHeight(int lineCount) {
        return Math.round(textContent.getPreferredSize().height / lineCount);
    }

    protected KeyListener createKeyHandler() {
        return new TextFieldKeyHandler();
    }

    private class TextFieldKeyHandler extends KeyHandler {

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.isKeyCode(Key.ENTER)) {
                if (PlayN.platformType() != Platform.Type.ANDROID) {
                    keyTyped(new KeyEvent(keyEvent.getSource(), keyEvent.getTime(), '\n'));
                }
                return;
            }

            String textValue = text.getValue();
            int caretPositionValue = caretPosition.getValue();
            int oldCaretPosition = caretPositionValue;

            if (keyEvent.isKeyCode(Key.UP)) {
                int newCaretPosition = 0;

                List<Integer> lineLengths = textContent.getLineLengths();
                int caretX = caretCoordinate.getValue().first;
                int caretY = caretCoordinate.getValue().second;
                if (0 < caretY) {
                    int lengthOfPreviousLine = lineLengths.get(caretY - 1);
                    if (caretY == 1) {// First line doesn't have '\n'
                        lengthOfPreviousLine++;
                    }
                    newCaretPosition = caretPositionValue - Math.max(lengthOfPreviousLine, caretX);
                }

                if (keyEvent.isShiftPressed()) {
                    extendSelection(newCaretPosition);
                } else {
                    selectionLength.setValue(0);
                }
                caretPositionValue = newCaretPosition;
            } else if (keyEvent.isKeyCode(Key.DOWN)) {
                int newCaretPosition = textValue.length();

                List<Integer> lineLengths = textContent.getLineLengths();
                int caretX = caretCoordinate.getValue().first;
                int caretY = caretCoordinate.getValue().second;
                if (caretY < lineLengths.size() - 1) {
                    int lengthOfCurrentLine = lineLengths.get(caretY);
                    int lengthOfNextLine = lineLengths.get(caretY + 1);
                    if (caretX < lengthOfNextLine) {
                        newCaretPosition = caretPositionValue + lengthOfCurrentLine;
                        if (caretY == 0) {// First line doesn't have '\n'
                            newCaretPosition++;
                        }
                    } else {
                        newCaretPosition = caretPositionValue + lengthOfCurrentLine - caretX + lengthOfNextLine;
                    }
                }

                if (keyEvent.isShiftPressed()) {
                    extendSelection(newCaretPosition);
                } else {
                    selectionLength.setValue(0);
                }
                caretPositionValue = newCaretPosition;
            } else if (keyEvent.isKeyCode(Key.HOME)) {
                if (keyEvent.isControlPressed()) {
                    if (keyEvent.isShiftPressed()) {
                        extendSelection(0);
                    } else {
                        selectionLength.setValue(0);
                    }

                    caretPositionValue = 0;
                } else {
                    int caretX = caretCoordinate.getValue().first;
                    int caretY = caretCoordinate.getValue().second;

                    caretPositionValue -= caretX;
                    if (caretY != 0) {
                        caretPositionValue++;
                    }

                    if (keyEvent.isShiftPressed()) {
                        extendSelection(caretPositionValue);
                    } else {
                        selectionLength.setValue(0);
                    }
                }
            } else if (keyEvent.isKeyCode(Key.END)) {
                if (keyEvent.isControlPressed()) {
                    if (keyEvent.isShiftPressed()) {
                        extendSelection(textValue.length());
                    } else {
                        selectionLength.setValue(0);
                    }

                    caretPositionValue = textValue.length();
                } else {
                    List<Integer> lineLengths = textContent.getLineLengths();
                    int caretX = caretCoordinate.getValue().first;
                    int caretY = caretCoordinate.getValue().second;

                    caretPositionValue += lineLengths.get(caretY) - caretX;

                    if (keyEvent.isShiftPressed()) {
                        extendSelection(caretPositionValue);
                    } else {
                        selectionLength.setValue(0);
                    }
                }
            } else if (keyEvent.isKeyCode(Key.PAGE_UP)) {
                int newCaretPosition = 0;

                List<Integer> lineLengths = textContent.getLineLengths();
                int caretX = caretCoordinate.getValue().first;
                int caretY = caretCoordinate.getValue().second;
                if (rows.getValue() - 1 <= caretY) {
                    int upperY = caretY - rows.getValue() + 1;
                    int lengthOfUpperLine = lineLengths.get(upperY);
                    if (upperY != 0) {// First line doesn't have '\n'
                        lengthOfUpperLine--;
                    }
                    newCaretPosition = textContent.getCaretIndex(upperY, 0) + Math.min(lengthOfUpperLine, caretX - 1);
                }

                if (keyEvent.isShiftPressed()) {
                    extendSelection(newCaretPosition);
                } else {
                    selectionLength.setValue(0);
                }
                caretPositionValue = newCaretPosition;
            } else if (keyEvent.isKeyCode(Key.PAGE_DOWN)) {
                int newCaretPosition = textValue.length();

                List<Integer> lineLengths = textContent.getLineLengths();
                int caretX = caretCoordinate.getValue().first;
                int caretY = caretCoordinate.getValue().second;
                if (caretY + rows.getValue() - 1 < lineLengths.size()) {
                    int lowerY = caretY + rows.getValue() - 1;
                    int lengthOfLowerLine = lineLengths.get(lowerY) - 1;
                    if (caretY != 0) {// First line doesn't have '\n'
                        caretX--;
                    }
                    newCaretPosition = textContent.getCaretIndex(lowerY, 0) + Math.min(lengthOfLowerLine, caretX);
                }

                if (keyEvent.isShiftPressed()) {
                    extendSelection(newCaretPosition);
                } else {
                    selectionLength.setValue(0);
                }
                caretPositionValue = newCaretPosition;
            } else {
                super.keyPressed(keyEvent);
            }

            if (caretPositionValue != oldCaretPosition) {
                blinkingAnimation.rewind();
                caretPosition.setValue(caretPositionValue);
            }
        }
    }

    protected MouseListener createMouseHandler() {
        return new MouseHandler();
    }

    private class MouseHandler implements MouseListener {

        private double lastPressTime = -1000.0;
        private int lastCaretPosition = -1;
        private int pressCount = 1;

        @Override
        public void mousePressed(MouseEvent e) {
            if (text.getValue().length() == 0 || e.getButton() != BUTTON_LEFT || !enabled.getValue()) {
                return;
            }

            int newCaretPosition = getCaretIndex(e.getX(), e.getY());

            if (e.isShiftPressed()) {
                e.consume();
                extendSelection(newCaretPosition);
            } else {
                if (newCaretPosition == lastCaretPosition && pressCount < 3 && e.getTime() <= lastPressTime + 500) {
                    pressCount++;
                    if (pressCount == 2) {
                        e.consume();
                        newCaretPosition = selectWord(newCaretPosition);
                    } else if (pressCount == 3) {
                        e.consume();
                        selectLine(getCaretCoordinate(newCaretPosition).second);
                    }
                } else {
                    pressCount = 1;
                    lastCaretPosition = newCaretPosition;

                    if (0 < selectionLength.getValue()) {
                        e.consume();
                        selectionLength.setValue(0);
                    }
                }
                lastPressTime = e.getTime();
            }

            if (newCaretPosition != caretPosition.getValue()) {
                e.consume();
                caretPosition.setValue(newCaretPosition);
            }
        }

        @Override
        public void mouseLongPressed(MouseEvent e) {
            if (e.getButton() != BUTTON_LEFT) {
                return;
            }

            int caretIndex = getCaretIndex(e.getX(), e.getY());
            if (caretIndex < selectionStart.getValue()
                    || selectionStart.getValue() + selectionLength.getValue() < caretIndex) {
                e.consume();
                caretPosition.setValue(selectWord(caretIndex));
            }
        }

        @Override
        public void mouseWheelScroll(MouseEvent e) {
            if (rows.getValue() < textContent.getLineCount()) {
                if (e.getVelocity() < 0) {
                    if (translateY.getValue() < 0) {
                        translateY.setValue(translateY.getValue() + 1);
                    } else {
                        e.unConsume();
                    }
                } else {
                    if (rows.getValue() - textContent.getLineCount() < translateY.getValue()) {
                        translateY.setValue(translateY.getValue() - 1);
                    } else {
                        e.unConsume();
                    }
                }
            }
        }

        @Override
        public void mouseDragged(MouseMotionEvent e) {
            if (e.getButton() != BUTTON_LEFT || !enabled.getValue()) {
                return;
            } else {
                e.consume();
            }

            Integer caretPositionValue = caretPosition.getValue();
            int newCaretPosition = getCaretIndex(e.getX(), e.getY());
            while (caretPositionValue != newCaretPosition) {
                if (caretPositionValue < newCaretPosition) {
                    extendSelection(++caretPositionValue);
                } else if (newCaretPosition < caretPositionValue) {
                    extendSelection(--caretPositionValue);
                }
                caretPosition.setValue(caretPositionValue);
            }
        }

        private int getCaretIndex(float x, float y) {
            x -= hPadding.getValue();
            y -= vPadding.getValue();

            float lineHeight = Math.round(textContent.getPreferredSize().height / textContent.getLineCount());
            int currentLine = MathUtil.ifloor(y / lineHeight) - translateY.getValue();
            if (textContent.getLineCount() <= currentLine) {
                return text.getValue().length();
            } else if (currentLine < 0 || (currentLine == 0 && x <= 0)) {
                return 0;
            }

            return textContent.getCaretIndex(currentLine, x);
        }
    }
}

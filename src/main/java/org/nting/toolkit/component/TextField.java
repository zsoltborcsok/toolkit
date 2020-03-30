package org.nting.toolkit.component;

import static org.nting.toolkit.event.MouseEvent.MouseButton.BUTTON_LEFT;
import static org.nting.toolkit.ui.stone.TextContentSingleLine.textContent;

import org.nting.data.Property;
import org.nting.toolkit.event.KeyEvent;
import org.nting.toolkit.event.KeyListener;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.event.MouseMotionEvent;
import org.nting.toolkit.ui.stone.TextContent;
import org.nting.toolkit.util.ToolkitUtils;

public class TextField extends FieldComponent {

    public final Property<Integer> hPadding = createProperty("hPadding", 3);
    public final Property<Integer> vPadding = createProperty("vPadding", 3);

    private final TextContent textContent;

    public TextField() {
        super("text");

        textContent = createTextContent();

        caretPosition.addValueChangeListener(event -> {
            if (getSize().width == 0) {
                return;
            }
            float caretX = Math.round(textContent.getCaretPosition(0, caretPosition.getValue()));
            float width = getSize().width - getTextContentPadding();
            if (width <= textContent.getTranslateX() + caretX) {
                textContent.setTranslateX(width - caretX - 1);
            } else if (textContent.getTranslateX() + caretX < 0) {
                textContent.setTranslateX(-caretX);
            }
        });
        text.addValueChangeListener(event -> {
            if (event.getProperty().hasValue()) {
                float maxCaretX = Math.round(textContent.getCaretPosition(0, event.getValue().length()));
                float width = getSize().width - getTextContentPadding();
                if (textContent.getTranslateX() < 0 && maxCaretX < width - textContent.getTranslateX()) {
                    textContent.setTranslateX(Math.min(0, width - maxCaretX - 1));
                }
            } else {
                textContent.setTranslateX(0);
            }
        });
    }

    public TextContent getTextContent() {
        return textContent;
    }

    protected TextContent createTextContent() {
        return textContent(font, color, text);
    }

    @Override
    public void paste(String newText) {
        super.paste(newText.replace("\n", ""));
    }

    public String getText() {
        String value = text.getValue();
        return value == null ? "" : value;
    }

    public float getTextContentPadding() {
        return 2 * hPadding.getValue();
    }

    public float getBaselinePosition() {
        return textContent.getPreferredSize().height - font.getValue().size() + vPadding.getValue();
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
    }

    protected KeyListener createKeyHandler() {
        return new TextFieldKeyHandler();
    }

    private class TextFieldKeyHandler extends KeyHandler {
        @Override
        public void keyTyped(KeyEvent keyEvent) {
            if (((byte) keyEvent.getKeyChar(false)) != 10) {
                super.keyTyped(keyEvent);
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
            if (e.getButton() != BUTTON_LEFT || !enabled.getValue()) {
                return;
            }

            int newCaretPosition = getCaretIndex(e.getX());

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
                        selectAll();
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

            int caretIndex = getCaretIndex(e.getX());
            if (caretIndex < selectionStart.getValue()
                    || selectionStart.getValue() + selectionLength.getValue() < caretIndex) {
                e.consume();
                caretPosition.setValue(selectWord(caretIndex));
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
            int newCaretPosition = getCaretIndex(e.getX());
            while (caretPositionValue != newCaretPosition) {
                if (caretPositionValue < newCaretPosition) {
                    extendSelection(++caretPositionValue);
                } else if (newCaretPosition < caretPositionValue) {
                    extendSelection(--caretPositionValue);
                }
                caretPosition.setValue(caretPositionValue);
            }
        }

        protected int getCaretIndex(float x) {
            return textContent.getCaretIndex(0, x - hPadding.getValue());
        }
    }
}

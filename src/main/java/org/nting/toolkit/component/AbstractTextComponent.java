package org.nting.toolkit.component;

import static org.nting.data.binding.Bindings.transformToPair;
import static org.nting.toolkit.event.MouseEvent.MouseButton.BUTTON_LEFT;
import static org.nting.toolkit.ui.Colors.DARK_GREY;
import static org.nting.toolkit.util.WordUtils.relativePositionOfWordEnd;
import static org.nting.toolkit.util.WordUtils.relativePositionOfWordStart;

import java.util.function.BiFunction;

import org.nting.data.Property;
import org.nting.data.binding.Bindings;
import org.nting.data.util.Pair;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;

public abstract class AbstractTextComponent extends AbstractComponent {

    public final Property<Integer> color = createProperty("color", DARK_GREY);
    public final Property<String> text = createProperty("text", "");
    public final Property<String> selectedText;

    public final Property<Integer> selectionStart = createProperty("selectionStart", 0);
    public final Property<Integer> selectionLength = createProperty("selectionLength", 0);

    public AbstractTextComponent(String... reLayoutPropertyNames) {
        super(reLayoutPropertyNames);

        selectedText = Bindings.transformProperties(text, transformToPair(selectionStart, selectionLength),
                new SelectedTextConverter());
    }

    public void selectAll() {
        selectionStart.setValue(0);
        selectionLength.setValue(text.getValue().length());
    }

    protected int selectWord(int caretIndex) {
        int from = relativePositionOfWordStart(text.getValue(), caretIndex);
        int to = relativePositionOfWordEnd(text.getValue(), caretIndex);
        if (0 <= from && 0 <= to) {
            selectionStart.setValue(caretIndex - from);
            selectionLength.setValue(from + to);
            return caretIndex + to;
        } else {
            selectionLength.setValue(0);
            return caretIndex;
        }
    }

    private static class SelectedTextConverter implements BiFunction<String, Pair<Integer, Integer>, String> {

        @Override
        public String apply(String text, Pair<Integer, Integer> selectionStartAndLength) {
            if (0 < selectionStartAndLength.first) {
                int selectionStart = Math.max(0, selectionStartAndLength.first);
                int selectionLength = Math.min(text.length() - selectionStart, selectionStartAndLength.second);
                return text.substring(selectionStart, selectionStart + selectionLength);
            } else {
                return "";
            }
        }
    }

    protected abstract class SelectionMouseHandler implements MouseListener {

        private double lastPressTime = -1000.0;
        private int lastCaretPosition = -1;
        private int pressCount = 1;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() != BUTTON_LEFT || e.isShiftPressed()) {
                return;
            }

            int newCaretPosition = getCaretIndex(e.getX(), e.getY());
            if (newCaretPosition == lastCaretPosition && pressCount < 3 && e.getTime() <= lastPressTime + 500) {
                pressCount++;
                if (pressCount == 2) {
                    e.consume();
                    selectWord(newCaretPosition);
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

        @Override
        public void mouseLongPressed(MouseEvent e) {
            if (e.getButton() == BUTTON_LEFT && !e.isShiftPressed()) {
                e.consume();
                selectWord(getCaretIndex(e.getX(), e.getY()));
            }
        }

        protected abstract int getCaretIndex(float x, float y);
    }
}

package org.nting.toolkit.component;

import static org.nting.toolkit.util.WordUtils.relativePositionOfNextWord;
import static org.nting.toolkit.util.WordUtils.relativePositionOfPreviousWord;

import org.nting.data.Property;
import org.nting.data.binding.Bindings;
import org.nting.data.condition.Conditions;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.animation.Animation;
import org.nting.toolkit.animation.Tween;
import org.nting.toolkit.event.KeyEvent;
import org.nting.toolkit.event.KeyListener;
import org.nting.toolkit.event.MouseListener;

import playn.core.Key;
import playn.core.Platform;
import playn.core.PlayN;

public abstract class FieldComponent extends AbstractTextComponent {

    public final Property<String> errorMessage = createProperty("errorMessage", null);
    public final Property<String> caption = createProperty("caption", null);
    public final Property<Boolean> enabled = createProperty("enabled", true);

    public final Property<Integer> caretPosition = createProperty("caretPosition", 0);
    private final Property<Boolean> caretVisible = createProperty("caretVisible", false);

    protected final Animation blinkingAnimation;
    protected final KeyListener keyHandler;

    @SuppressWarnings("unchecked")
    public FieldComponent(String... reLayoutPropertyNames) {
        super(reLayoutPropertyNames);

        Property<Boolean> blinking = new ObjectProperty<>(true);
        blinkingAnimation = new Tween<>(blinking, true, false, 1000).loopForever();
        addBehavior(blinkingAnimation);

        Bindings.bindCondition(Conditions.convert(focused).and(blinking), caretVisible);

        text.addValueChangeListener(event -> {
            String text = event.getValue();
            if (text == null) {
                caretPosition.setValue(0);
            } else if (caretPosition.getValue() > text.length()) {
                caretPosition.setValue(text.length());
            }
        });
        mouseOver.addValueChangeListener(event -> {
            if (event.getValue() && enabled.getValue()) {
                PlayN.setCursor(Platform.Cursor.TEXT);
            } else {
                PlayN.setCursor(Platform.Cursor.DEFAULT);
            }
        });

        addKeyListener(keyHandler = createKeyHandler());
        addMouseListener(createMouseHandler());
    }

    @Override
    public boolean isFocusable() {
        return super.isFocusable() && enabled.getValue();
    }

    public boolean isCaretVisible() {
        return caretVisible.getValue();
    }

    public void setCaretVisible(boolean visible) {
        caretVisible.setValue(visible);
    }

    public void paste(String newText) {
        newText = newText.replace("\r", "");
        String textValue = text.getValue();
        if (0 < selectionLength.getValue()) {
            Integer selectionStartValue = selectionStart.getValue();
            int selectionEndValue = selectionStartValue + selectionLength.getValue();
            setText(textValue.substring(0, selectionStartValue) + newText + textValue.substring(selectionEndValue));

            caretPosition.setValue(selectionStartValue + newText.length());
            selectionLength.setValue(0);
        } else {
            int caretPositionValue = caretPosition.getValue();
            setText(textValue.substring(0, caretPositionValue) + newText + textValue.substring(caretPositionValue));
            caretPosition.setValue(caretPositionValue + newText.length());
            selectionLength.setValue(0);
        }
    }

    protected void setText(String text) {
        this.text.setValue(text);
    }

    protected KeyListener createKeyHandler() {
        return new KeyHandler();
    }

    protected abstract MouseListener createMouseHandler();

    protected void extendSelection(int newSelectionLimit) {
        if (newSelectionLimit < 0 || text.getValue().length() < newSelectionLimit) {
            return;
        }

        int caretPositionValue = caretPosition.getValue();
        int selectionStartValue = selectionStart.getValue();
        int selectionLengthValue = selectionLength.getValue();

        if (0 < selectionLengthValue) {
            if (newSelectionLimit < selectionStartValue) {
                selectionStart.setValue(newSelectionLimit);
                if (selectionStartValue < caretPositionValue) {
                    selectionLength.setValue(selectionStartValue - newSelectionLimit);
                } else {
                    selectionLength.setValue(selectionStartValue + selectionLengthValue - newSelectionLimit);
                }
            } else if (newSelectionLimit == selectionStartValue
                    && newSelectionLimit + selectionLengthValue != caretPositionValue) {
                // Nothing to do
            } else if (newSelectionLimit < selectionStartValue + selectionLengthValue) {
                if (caretPositionValue < newSelectionLimit) {
                    selectionStart.setValue(newSelectionLimit);
                    selectionLength.setValue(selectionStartValue + selectionLengthValue - newSelectionLimit);
                } else {
                    selectionLength.setValue(newSelectionLimit - selectionStartValue);
                }
            } else if (newSelectionLimit == selectionStartValue + selectionLengthValue
                    && caretPositionValue + selectionLengthValue != newSelectionLimit) {
                // Nothing to do
            } else {// selectionStartValue + selectionLengthValue < newSelectionLimit
                if (caretPositionValue < selectionStartValue + selectionLengthValue) {
                    selectionLength.setValue(newSelectionLimit - (selectionStartValue + selectionLengthValue));
                    selectionStart.setValue(selectionStartValue + selectionLengthValue);
                } else {
                    selectionLength.setValue(newSelectionLimit - (selectionStartValue));
                    selectionStart.setValue(selectionStartValue);
                }
            }
        } else {
            if (caretPositionValue < newSelectionLimit) {
                selectionStart.setValue(caretPositionValue);
                selectionLength.setValue(newSelectionLimit - caretPositionValue);
            } else {
                selectionStart.setValue(newSelectionLimit);
                selectionLength.setValue(caretPositionValue - newSelectionLimit);
            }
        }
    }

    protected class KeyHandler implements KeyListener {

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            String textValue = text.getValue();
            int caretPositionValue = caretPosition.getValue();
            int oldCaretPosition = caretPositionValue;
            int selectionStartValue = selectionStart.getValue();
            int selectionLengthValue = selectionLength.getValue();

            if (keyEvent.isKeyCode(Key.BACKSPACE)) {
                if (textValue.length() > 0 && caretPositionValue > 0) {
                    if (0 < selectionLength.getValue()) {
                        setText(textValue.substring(0, selectionStartValue)
                                + textValue.substring(selectionStartValue + selectionLengthValue));
                        caretPositionValue = selectionStartValue;
                        selectionLength.setValue(0);
                    } else {
                        setText(textValue.substring(0, caretPositionValue - 1)
                                + textValue.substring(caretPositionValue));
                        caretPositionValue = caretPositionValue - 1;
                    }
                }
            } else if (!keyEvent.isAltPressed() && keyEvent.isKeyCode(Key.LEFT)) {
                int positionChange = keyEvent.isControlPressed()
                        ? relativePositionOfPreviousWord(textValue, caretPositionValue)
                        : 1;

                if (keyEvent.isShiftPressed()) {
                    extendSelection(caretPositionValue - positionChange);
                } else {
                    selectionLength.setValue(0);
                }

                caretPositionValue = Math.max(0, caretPositionValue - positionChange);
            } else if (keyEvent.isKeyCode(Key.RIGHT)) {
                int positionChange = keyEvent.isControlPressed()
                        ? relativePositionOfNextWord(textValue, caretPositionValue)
                        : 1;

                if (keyEvent.isShiftPressed()) {
                    extendSelection(caretPositionValue + positionChange);
                } else {
                    selectionLength.setValue(0);
                }

                caretPositionValue = Math.min(textValue.length(), caretPositionValue + positionChange);
            } else if (keyEvent.isKeyCode(Key.DELETE)) {
                if (keyEvent.isShiftPressed()) {
                    keyEvent.unConsume();
                } else if (0 < selectionLength.getValue()) {
                    setText(textValue.substring(0, selectionStartValue)
                            + textValue.substring(selectionStartValue + selectionLengthValue));
                    caretPositionValue = selectionStartValue;
                    selectionLength.setValue(0);
                } else {
                    if (textValue.length() > 0 && caretPositionValue < textValue.length()) {
                        setText(textValue.substring(0, caretPositionValue)
                                + textValue.substring(caretPositionValue + 1));
                    }
                }
            } else if (keyEvent.isKeyCode(Key.HOME)) {
                if (keyEvent.isShiftPressed()) {
                    extendSelection(0);
                } else {
                    selectionLength.setValue(0);
                }

                caretPositionValue = 0;
            } else if (keyEvent.isKeyCode(Key.END)) {
                if (keyEvent.isShiftPressed()) {
                    extendSelection(textValue.length());
                } else {
                    selectionLength.setValue(0);
                }

                caretPositionValue = textValue.length();
            } else if (keyEvent.isControlPressed() && keyEvent.isKeyCode(Key.A)) {
                selectAll();
            } else if (0 < selectionLength.getValue() && keyEvent.isKeyCode(Key.ESCAPE)) {
                selectionLength.setValue(0);
            }

            if (caretPositionValue != oldCaretPosition) {
                blinkingAnimation.rewind();
                caretPosition.setValue(caretPositionValue);
            }
        }

        @Override
        public void keyTyped(KeyEvent keyEvent) {
            byte key = (byte) keyEvent.getKeyChar(false);
            if (!(key == 0 || key == 13)) {
                String textValue = text.getValue();
                int caretPositionValue = caretPosition.getValue();
                int selectionStartValue = selectionStart.getValue();
                int selectionLengthValue = selectionLength.getValue();

                if (0 < selectionLengthValue) {
                    setText(textValue.substring(0, selectionStartValue) + keyEvent.getKeyChar(true)
                            + textValue.substring(selectionStartValue + selectionLengthValue));
                    caretPositionValue = selectionStartValue + 1;
                    selectionLength.setValue(0);
                } else {
                    setText(textValue.substring(0, caretPositionValue) + keyEvent.getKeyChar(true)
                            + textValue.substring(caretPositionValue));
                    caretPositionValue++;
                }

                blinkingAnimation.rewind();
                caretPosition.setValue(caretPositionValue);
            }
        }
    }
}

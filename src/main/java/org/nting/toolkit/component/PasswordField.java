package org.nting.toolkit.component;

import static org.nting.toolkit.event.MouseEvent.MouseButton.BUTTON_LEFT;
import static org.nting.toolkit.ui.stone.TextContentSingleLine.textContent;

import java.util.Arrays;
import java.util.function.Function;

import org.nting.data.Property;
import org.nting.data.binding.Bindings;
import org.nting.data.condition.Conditions;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.data.MouseOverProperty;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.event.MouseMotionEvent;
import org.nting.toolkit.ui.stone.TextContent;

public class PasswordField extends TextField {

    public final Property<Character> echoChar;

    private final Property<Icon> icon = createProperty("icon", null);
    private final Property<Boolean> mouseOverButton = createProperty("mouseOverButton", false);
    private final Property<Boolean> buttonPressed = createProperty("buttonPressed", false);
    private final Property<Float> buttonWidth = new ObjectProperty<>(0f);

    private final TextContent normalTextContent;

    public PasswordField() {
        // The 'echoChar' property is already created in createTextContent() method, called from super constructor.
        echoChar = getProperty("echoChar");
        normalTextContent = super.createTextContent();

        Bindings.bind(Conditions.textOf(text).isEmpty(), icon,
                isTextEmpty -> isTextEmpty == Boolean.TRUE ? null : FontIcon.EYE);
        ((MouseOverProperty) mouseOver).setButtonWidth(buttonWidth);
    }

    public TextContent getNormalTextContent() {
        return normalTextContent;
    }

    @Override
    protected TextContent createTextContent() {
        Property<String> echoText = Bindings.transformProperties(text, createProperty("echoChar", '*'),
                (text, echoChar) -> {
                    if (text == null) {
                        return null;
                    } else {
                        char[] buffer = new char[text.length()];
                        Arrays.fill(buffer, echoChar);
                        return new String(buffer);
                    }
                });
        return textContent(font, color, echoText);
    }

    public float getTextContentPadding() {
        return buttonWidth.getValue() + super.getTextContentPadding();
    }

    public void setButtonWidth(float buttonWidth) {
        this.buttonWidth.setValue(buttonWidth);
    }

    public boolean isMouseOverButton() {
        return mouseOverButton.getValue();
    }

    public boolean isButtonPressed() {
        return buttonPressed.getValue();
    }

    public Property<Icon> icon() {
        return icon.transform(Function.identity());
    }

    protected MouseListener createMouseHandler() {
        return new MouseHandler();
    }

    @Override
    public void selectAll() {
        // Selection not supported
    }

    @Override
    protected void extendSelection(int newSelectionLimit) {
        // Selection not supported
    }

    private class MouseHandler extends TextField.MouseHandler {

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getX() + buttonWidth.getValue() < width.getValue()) { // Just move the caret
                e.consume();
                caretPosition.setValue(getCaretIndex(e.getX()));
            } else if (enabled.getValue()) {
                e.consume();
                buttonPressed.setValue(true);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            e.consume();
            buttonPressed.setValue(false);
        }

        @Override
        public void mouseLongPressed(MouseEvent e) {
            // Selection not supported
        }

        @Override
        public void mouseDragged(MouseMotionEvent e) {
            // Selection not supported
            if (buttonPressed.getValue() && e.getButton() == BUTTON_LEFT) {
                e.consume();
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            e.consume();
            mouseOverButton.setValue(false);
        }

        @Override
        public void mouseMoved(MouseMotionEvent e) {
            mouseOverButton.setValue(width.getValue() - buttonWidth.getValue() < e.getX());
        }
    }
}

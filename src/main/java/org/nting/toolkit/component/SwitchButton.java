package org.nting.toolkit.component;

import static org.nting.toolkit.ui.Colors.DARK_GREY;
import static org.nting.toolkit.ui.stone.TextContentSingleLine.textContent;

import org.nting.data.Property;
import org.nting.toolkit.event.KeyEvent;
import org.nting.toolkit.event.KeyListener;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.ui.stone.TextContent;

import com.google.common.base.Strings;

import playn.core.Key;

public class SwitchButton extends AbstractComponent {

    public final Property<Integer> color = createProperty("color", DARK_GREY);
    public final Property<String> captionLeft = createProperty("captionLeft", "");
    public final Property<String> captionRight = createProperty("captionRight", "");
    public final Property<Boolean> switched = createProperty("switched", false);
    public final Property<Boolean> enabled = createProperty("enabled", true);
    public final Property<Integer> padding = createProperty("padding", 3);

    private final TextContent leftContent = textContent(font, color, captionLeft);
    private final TextContent rightContent = textContent(font, color, captionRight);

    public SwitchButton() {
        addMouseListener(new MouseHandler());
        addKeyListener(new KeyHandler());
    }

    public TextContent getLeftContent() {
        return leftContent;
    }

    public TextContent getRightContent() {
        return rightContent;
    }

    public float getBaselinePosition() {
        String text = switched.getValue() ? captionLeft.getValue() : captionRight.getValue();
        TextContent textContent = switched.getValue() ? leftContent : rightContent;

        if (Strings.isNullOrEmpty(text)) {
            return -1;
        } else {
            return textContent.getPreferredSize().height - font.getValue().size() + padding.getValue();
        }
    }

    @Override
    public boolean isFocusable() {
        return super.isFocusable() && enabled.getValue();
    }

    private void doSwitch() {
        switched.setValue(!switched.getValue());
    }

    private class MouseHandler implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (enabled.getValue()) {
                doSwitch();
                e.consume();
            }
        }
    }

    private class KeyHandler implements KeyListener {

        @Override
        public void keyReleased(KeyEvent e) {
            if (enabled.getValue() && e.isKeyCode(Key.SPACE)) {
                doSwitch();
            }
        }
    }
}

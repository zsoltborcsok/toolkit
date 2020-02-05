package org.nting.toolkit.component;

import static org.nting.toolkit.ui.Colors.DARK_GREY;
import static org.nting.toolkit.ui.stone.TextContentSingleLine.textContent;

import org.nting.data.Property;
import org.nting.toolkit.event.KeyEvent;
import org.nting.toolkit.event.KeyListener;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.ui.stone.TextContent;
import org.nting.toolkit.util.ToolkitUtils;

import playn.core.Key;

public class CheckBox extends AbstractComponent {

    public final Property<Integer> color = createProperty("color", DARK_GREY);
    public final Property<String> text = createProperty("text", "");
    public final Property<Boolean> pressed = createProperty("pressed", false);
    public final Property<Boolean> enabled = createProperty("enabled", true);
    public final Property<Boolean> selected = createProperty("selected", false);
    public final Property<Integer> padding = createProperty("padding", 3);

    private final TextContent textContent = textContent(font, color, text);

    public CheckBox() {
        super("text", "focused", "selected", "mouseOver", "pressed");

        addMouseListener(new MouseHandler());
        addKeyListener(new KeyHandler());
    }

    public TextContent getTextContent() {
        return textContent;
    }

    public float getBaselinePosition() {
        return textContent.getPreferredSize().height - font.getValue().size() + padding.getValue();
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

    public void setIndeterminate() {
        selected.setValue(null);
    }

    protected void changeSelection() {
        selected.setValue(!selected.hasValue() || !selected.getValue());
    }

    @Override
    public boolean isFocusable() {
        return super.isFocusable() && enabled.getValue();
    }

    private class MouseHandler implements MouseListener {

        @Override
        public void mousePressed(MouseEvent e) {
            if (enabled.getValue()) {
                pressed.setValue(true);
                e.consume();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (enabled.getValue()) {
                pressed.setValue(false);
                e.consume();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (enabled.getValue()) {
                changeSelection();
                e.consume();
            }
        }
    }

    private class KeyHandler implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e) {
            if (enabled.getValue() && e.isKeyCode(Key.SPACE)) {
                pressed.setValue(true);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (enabled.getValue() && e.isKeyCode(Key.SPACE)) {
                pressed.setValue(false);
                changeSelection();
            }
        }
    }
}

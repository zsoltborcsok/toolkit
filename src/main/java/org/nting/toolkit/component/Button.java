package org.nting.toolkit.component;

import org.nting.data.Property;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.event.ActionEvent;
import org.nting.toolkit.event.Actions;
import org.nting.toolkit.event.KeyEvent;
import org.nting.toolkit.event.KeyListener;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;

import playn.core.Key;

public class Button extends AbstractComponent {

    public final Property<String> text = createProperty("text", "");
    // TODO public final Property<Content> image = createProperty("image", null);
    public final Property<Boolean> pressed = createReadOnlyProperty("pressed", false);
    public final Property<Boolean> enabled = createProperty("enabled", true);

    private final Actions actions = new Actions();
    // TODO private final TextContent textContent = textContent(font, color, text);

    public Button() {
        super("text", "image", "pressed");

        addMouseListener(new MouseHandler());
        addKeyListener(new KeyHandler());
    }

    public Actions actions() {
        return actions;
    }

    // TODO
    // public TextContent getTextContent() {
    // return textContent;
    // }
    //
    // public float getBaselinePosition() {
    // return textContent.getPreferredSize().height - font.getValue().size() + padding.getValue();
    // }
    //
    // @Override
    // public int search(String searchText) {
    // return textContent.search(searchText);
    // }
    //
    // @Override
    // public void highlightMatch(int index) {
    // textContent.highlightMatch(index);
    // repaint();
    // ToolkitUtilities.scrollComponentToVisible(this);
    // }

    @Override
    public boolean isFocusable() {
        return super.isFocusable() && enabled.getValue();
    }

    private class MouseHandler implements MouseListener {

        @Override
        public void mousePressed(MouseEvent e) {
            if (enabled.getValue()) {
                ((ObjectProperty<Boolean>) pressed).forceValue(true);
                e.consume();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (enabled.getValue()) {
                ((ObjectProperty<Boolean>) pressed).forceValue(false);
                e.consume();
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (enabled.getValue()) {
                ActionEvent actionEvent = new ActionEvent(Button.this, e.getTime(), e);
                actions().fireActionPerformed(actionEvent);
                e.consume();
            }
        }
    }

    private class KeyHandler implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.isKeyCode(Key.SPACE)) {
                ((ObjectProperty<Boolean>) pressed).forceValue(true);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.isKeyCode(Key.SPACE)) {
                ((ObjectProperty<Boolean>) pressed).forceValue(false);

                ActionEvent actionEvent = new ActionEvent(Button.this, e.getTime(), e);
                actions().fireActionPerformed(actionEvent);
            }
        }
    }
}

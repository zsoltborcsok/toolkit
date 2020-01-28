package org.nting.toolkit.component;

import static org.nting.toolkit.ToolkitServices.toolkitManager;

import java.util.List;

import org.nting.data.Property;
import org.nting.data.Registration;
import org.nting.toolkit.Component;
import org.nting.toolkit.event.ActionEvent;
import org.nting.toolkit.event.KeyEvent;
import org.nting.toolkit.event.KeyListener;

import com.google.common.collect.Lists;

import playn.core.Key;
import playn.core.PlayN;

public abstract class Popup extends AbstractComponent {

    public final Property<Integer> shadowSize = createProperty("shadowSize", 0);

    private final List<Runnable> closeListeners = Lists.newLinkedList();
    private Button defaultButton;

    public Popup() {
        addKeyListener(new KeyHandler());
    }

    @Override
    public void addComponent(Component child) {
        super.addComponent(child);

        if (defaultButton == null && child instanceof Button) {
            setDefaultButton((Button) child);
        }
    }

    public Button getDefaultButton() {
        return defaultButton;
    }

    public void setDefaultButton(Button defaultButton) {
        this.defaultButton = defaultButton;
    }

    public boolean isPopupVisible() {
        return getParent() != null;
    }

    public void close() {
        toolkitManager().root().setPopupVisible(this, false);
    }

    public void closed() {
        setParent(null);

        closeListeners.forEach(Runnable::run);
    }

    public Registration addCloseListener(Runnable closeListener) {
        if (closeListener != null && !closeListeners.contains(closeListener)) {
            closeListeners.add(closeListener);
        }

        return () -> closeListeners.remove(closeListener);
    }

    private class KeyHandler implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.isKeyCode(Key.ESCAPE)) {
                close();
            } else if (e.isKeyCode(Key.BACK)) { // Different thread (android)!
                PlayN.invokeLater(Popup.this::close);
            } else if (defaultButton != null && e.isKeyCode(Key.ENTER) && defaultButton.enabled.getValue()) {
                defaultButton.actions().fireActionPerformed(new ActionEvent(defaultButton, e.getTime(), e));
            }
        }
    }
}

package org.nting.toolkit.component;

import static org.nting.toolkit.ui.Colors.TRANSPARENT;

import org.nting.data.Property;
import org.nting.toolkit.event.ActionEvent;
import org.nting.toolkit.event.KeyEvent;
import org.nting.toolkit.event.KeyListener;
import org.nting.toolkit.layout.AbsoluteLayout;
import org.nting.toolkit.layout.LayoutManager;
import org.nting.toolkit.ui.ComponentUI;

import playn.core.Canvas;
import playn.core.Image;
import playn.core.Key;

public class Panel extends AbstractComponent {

    /** If you define backgroundImage, then backgroundColor is ignored */
    public final Property<Image> backgroundImage = createProperty("backgroundImage", null);
    public final Property<Integer> backgroundColor = createProperty("backgroundColor", TRANSPARENT);

    private Button defaultButton;

    public Panel(String... reLayoutPropertyNames) {
        this(new AbsoluteLayout(), reLayoutPropertyNames);
    }

    public Panel(LayoutManager layoutManager, String... reLayoutPropertyNames) {
        super(reLayoutPropertyNames);
        setLayoutManager(layoutManager);

        addKeyListener(new KeyHandler());

        setFocusable(false);
    }

    public Button getDefaultButton() {
        return defaultButton;
    }

    public void setDefaultButton(Button defaultButton) {
        this.defaultButton = defaultButton;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setComponentUI(ComponentUI componentUI) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void doPaintComponent(Canvas canvas) {
        Image backgroundImage = this.backgroundImage.getValue();
        if (backgroundImage != null) {
            canvas.drawImage(backgroundImage, 0, 0, width.getValue(), height.getValue());
        } else {
            canvas.setFillColor(backgroundColor.getValue());
            canvas.fillRect(0, 0, width.getValue(), height.getValue());
        }
    }

    private class KeyHandler implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e) {
            if (defaultButton != null && e.isKeyCode(Key.ENTER) && defaultButton.enabled.getValue()) {
                defaultButton.actions().fireActionPerformed(new ActionEvent(defaultButton, e.getTime(), e));
            }
        }
    }
}

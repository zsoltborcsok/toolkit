package org.nting.toolkit.component;

import static org.nting.toolkit.ToolkitServices.fontManager;
import static org.nting.toolkit.ToolkitServices.toolkitManager;
import static org.nting.toolkit.ui.Colors.TRN_BLACK;
import static playn.core.util.SimpleMessageFormat.format;

import org.nting.data.Property;
import org.nting.toolkit.Component;
import org.nting.toolkit.FontManager;

import playn.core.PlayN;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

public class Dialog extends Popup {

    public final Property<Integer> modalityCurtain = createProperty("modalityCurtain", TRN_BLACK(0x40));
    public final Property<String> title = createProperty("title", "");

    private final Component content;

    public Dialog(String title, Component content) {
        this(content);
        this.title.setValue(title);
    }

    public Dialog(Component content) {
        this.content = content;
        super.addComponent(content);
    }

    @Override
    public void addComponent(Component child, Object constraints) {
        content.addComponent(child, constraints);
    }

    @Override
    public void addComponent(Component child) {
        content.addComponent(child);
    }

    @Override
    public void removeComponent(Component child) {
        content.removeComponent(child);
    }

    @Override
    public void removeAllComponents() {
        content.removeAllComponents();
    }

    @Override
    public Point getPosition() {
        // In order to achieve modality!
        return new Point(0, 0);
    }

    @Override
    public Dimension getSize() {
        // In order to achieve modality!
        return getParent() != null ? getParent().getSize() : new Dimension(0, 0);
    }

    @Override
    public void doLayout() {
        super.doLayout();
        for (Component component : getComponents()) {
            Point position = component.getPosition();
            component.setPosition(position.x + x.getValue(), position.y + y.getValue());
        }
    }

    public Dialog showCentered() {
        Dimension rootSize = toolkitManager().root().getSize();
        Dimension preferredSize = checkSize(getPreferredSize(), rootSize);
        float xPos = (float) Math.round((rootSize.width - preferredSize.width) / 2);
        float yPos = (float) Math.round((rootSize.height - preferredSize.height) / 2);
        return showAt(xPos, yPos, preferredSize.width, preferredSize.height);
    }

    public Dialog showAt(float xPos, float yPos) {
        Dimension preferredSize = checkSize(getPreferredSize(), toolkitManager().root().getSize());
        return showAt(xPos, yPos, preferredSize.width, preferredSize.height);
    }

    public Dialog showAt(float xPos, float yPos, float w, float h) {
        x.setValue(xPos);
        y.setValue(yPos);
        width.setValue(w);
        height.setValue(h);
        toolkitManager().root().setPopupVisible(this, true);
        requestFocus();
        return this;
    }

    public Component getContent() {
        return content;
    }

    private Dimension checkSize(Dimension size, Dimension limit) {
        if (limit.width < size.width || limit.height < size.height) {
            PlayN.log(Dialog.class)
                    .error(format("Screen size [{}, {}] is smaller than dialog size [{}, {}]. Font Size: {}",
                            limit.width, limit.height, size.width, size.height,
                            fontManager().getFont(FontManager.FontSize.SMALL_FONT).size()),
                            new IllegalStateException());
        }
        return size;
    }
}

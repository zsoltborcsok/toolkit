package org.nting.toolkit.internal;

import static org.nting.toolkit.FontManager.FontSize.LARGE_FONT;
import static org.nting.toolkit.ToolkitServices.fontManager;
import static org.nting.toolkit.ToolkitServices.unitConverter;
import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.event.MouseListener.onClick;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.layout.FormLayout.xyw;
import static playn.core.Font.Style.BOLD;
import static playn.core.Font.Style.ITALIC;
import static playn.core.Platform.Type.HTML;

import java.util.List;

import org.nting.data.Property;
import org.nting.data.Registration;
import org.nting.data.condition.Conditions;
import org.nting.toolkit.Component;
import org.nting.toolkit.DeveloperTools;
import org.nting.toolkit.animation.Trigger;
import org.nting.toolkit.component.AbstractComponent;
import org.nting.toolkit.component.FontIcon;
import org.nting.toolkit.component.Icon;
import org.nting.toolkit.component.Label;
import org.nting.toolkit.component.Orientation;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.ScrollPane;
import org.nting.toolkit.component.Separator;
import org.nting.toolkit.component.SimpleIconComponent;
import org.nting.toolkit.component.TextAlignment;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.event.KeyEvent;
import org.nting.toolkit.event.KeyListener;
import org.nting.toolkit.layout.AbsoluteLayout;
import org.nting.toolkit.layout.VerticalLayout;

import playn.core.Key;
import playn.core.PlayN;
import pythagoras.f.Dimension;

public class DeveloperToolsImpl extends AbstractComponent implements DeveloperTools {

    private final Property<Boolean> isActivated = createProperty("isActivated", false);

    private final Panel content = new Panel(new VerticalLayout());
    private Registration keyListenerRegistration;

    public DeveloperToolsImpl() {
        setLayoutManager(new AbsoluteLayout());
        addComponent(new ScrollPane(content), xy(0, 0));

        setFocusable(false);
        addBehavior(new Trigger(this::init, 0));
    }

    // DeveloperTools is registered statically in the ToolkitServices together with ToolkitManager (used often),
    // but these services should be instantiated without referring each other.
    private void init() {
        content.backgroundColor.setValue(0xFFefebe9); // Transparency works as well: 0x7F9e9e9e
        List<Component> alreadyRegisteredTools = content.getComponents();
        content.removeAllComponents();
        content.addComponent(new Label().set("text", "Developer Tools")
                .set("font", fontManager().getFont(LARGE_FONT, BOLD)).set("alignment", TextAlignment.CENTER));
        alreadyRegisteredTools.forEach(content::addComponent);
    }

    @Override
    public boolean isVisible() {
        return isActivated.getValue();
    }

    @Override
    public void setParent(Component parent) {
        super.setParent(parent);

        if (keyListenerRegistration != null) {
            keyListenerRegistration.remove();
        }
        if (parent != null) {
            keyListenerRegistration = parent.addKeyListener(new KeyListener() {

                @Override
                public void keyPressed(KeyEvent e) {
                    if ((PlayN.platformType() != HTML || e.isShiftPressed()) && e.isKeyCode(Key.F12)) {
                        isActivated.adjustValue(b -> !b);
                        parent.repaint();
                    }
                }
            });
        }
    }

    @Override
    public void doLayout() {
        // Position and size not controlled by the Root's LayoutManager, so let's configure it.
        Dimension parentSize = getParent().getSize();
        float width = Math.min(unitConverter().dialogUnitXAsPixel(150, this), parentSize.width / 3);
        setPosition(parentSize.width - width, 0);
        setSize(width, parentSize.height);

        super.doLayout();
    }

    @Override
    public Property<Boolean> addTool(String name, AbstractComponent tool) {
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder("4dlu, 0px:grow, pref, 4dlu", "");
        panelBuilder.formLayout().addRow("4dlu").addRow("pref");
        panelBuilder.addLabel(xy(1, panelBuilder.formLayout().lastRow())).text(name).pass().font(LARGE_FONT, ITALIC);
        panelBuilder.addComponent(new SimpleIconComponent(null), xy(2, panelBuilder.formLayout().lastRow()))
                .<Boolean, Icon> bind("icon", tool.visible, visible -> visible ? FontIcon.EYE : FontIcon.EYE_SLASH)
                .getComponent().addMouseListener(onClick(e -> tool.visible.adjustValue(b -> !b)));
        panelBuilder.formLayout().addRow("pref");
        panelBuilder.addComponent(new Separator(Orientation.HORIZONTAL),
                xyw(1, panelBuilder.formLayout().lastRow(), 2));
        panelBuilder.formLayout().addRow("pref");
        panelBuilder.addComponent(tool, xyw(1, panelBuilder.formLayout().lastRow(), 2));
        content.addComponent(panelBuilder.build());

        return Conditions.convert(isActivated).and(tool.visible);
    }
}

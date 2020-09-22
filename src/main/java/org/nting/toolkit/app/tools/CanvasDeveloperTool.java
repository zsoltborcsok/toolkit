package org.nting.toolkit.app.tools;

import static org.nting.data.binding.Bindings.BindingStrategy.READ;
import static org.nting.toolkit.ToolkitServices.developerTools;
import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.FormLayout.xy;

import org.nting.data.Property;
import org.nting.data.binding.Bindings;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.ToolkitManager;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.StoneComponent;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.ui.stone.ImageContent;
import org.nting.toolkit.ui.stone.LineBorder;
import org.nting.toolkit.ui.stone.ScaledContent;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.PlayN;
import pythagoras.f.MathUtil;
import pythagoras.f.Point;

public class CanvasDeveloperTool {

    private Property<Boolean> isActive = new ObjectProperty<>(false);
    private Property<Image> snapshot = new ObjectProperty<>(null);

    private final ToolkitManager toolkitManager;

    public CanvasDeveloperTool(ToolkitManager toolkitManager) {
        this.toolkitManager = toolkitManager;
        register();
    }

    public void update() {
        if (!isActive.getValue()) {
            return;
        }

        Point position = toolkitManager.mouseDispatcher().getLastMousePosition(toolkitManager.root());
        Image rootImage = ((ImageLayer) PlayN.graphics().rootLayer().get(0)).image();

        float x = MathUtil.clamp(position.x, 12, rootImage.width() - 12);
        float y = MathUtil.clamp(position.y, 12, rootImage.height() - 12);
        snapshot.setValue(rootImage.subImage(x - 12, y - 12, 24, 24));
    }

    private void register() {
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder("center:pref:grow", "4dlu, pref");

        panelBuilder.addComponent(new StoneComponent(null), xy(0, 1)).bind("stone", snapshot,
                image -> new LineBorder(new ScaledContent(new ImageContent(image), 8, 8), 1, 1, 1, 1, 0xFF7f7f7f));

        Property<Boolean> isToolActive = developerTools().addTool("Canvas", panelBuilder.build());
        Bindings.bind(READ, isToolActive, isActive);
    }
}

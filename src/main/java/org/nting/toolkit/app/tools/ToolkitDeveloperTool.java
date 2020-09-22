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
import org.nting.toolkit.ui.shape.RectangleShape;
import org.nting.toolkit.ui.stone.Background;

import playn.core.Color;
import playn.core.ImageLayer;
import playn.core.PlayN;
import pythagoras.f.Point;

public class ToolkitDeveloperTool {

    private Property<String> currentFps = new ObjectProperty<>("N/A");
    private Property<String> mousePosition = new ObjectProperty<>("N/A");
    private Property<Integer> color = new ObjectProperty<>(0);
    private Property<String> argbColor = new ObjectProperty<>("N/A");
    private Property<String> htmlColor = new ObjectProperty<>("N/A");
    private Property<Boolean> isActive = new ObjectProperty<>(false);

    private final ToolkitManager toolkitManager;

    public ToolkitDeveloperTool(ToolkitManager toolkitManager) {
        this.toolkitManager = toolkitManager;
        register();
    }

    public void update(float fps) {
        if (!isActive.getValue()) {
            return;
        }

        currentFps.setValue(String.valueOf(fps));
        Point position = toolkitManager.mouseDispatcher().getLastMousePosition(toolkitManager.root());
        mousePosition.setValue("(" + position.x + ", " + position.y + ")");
        int[][] imageData = ((ImageLayer) PlayN.graphics().rootLayer().get(0)).image().getImageData((int) position.x,
                (int) position.y, 1, 1);
        argbColor.setValue("ARGB(" + imageData[0][0] + ", " + imageData[0][1] + ", " + imageData[0][2] + ", "
                + imageData[0][3] + ")");
        color.setValue(Color.argb(imageData[0][0], imageData[0][1], imageData[0][2], imageData[0][3]));
        htmlColor.setValue("HTML(#" + Integer.toHexString(color.getValue()) + ")");
    }

    private void register() {
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder("right:max(pref;64dlu), 3dlu, 0px:grow", "");

        panelBuilder.formLayout().addRow("4dlu").addRow("baseline:pref");
        panelBuilder.addLabel(xy(0, panelBuilder.formLayout().lastRow())).text("Frames Per Second").pass()
                .color(0xFF999999);
        panelBuilder.addLabel(xy(2, panelBuilder.formLayout().lastRow())).text(currentFps);

        panelBuilder.formLayout().addRow("4dlu").addRow("baseline:pref");
        panelBuilder.addLabel(xy(0, panelBuilder.formLayout().lastRow())).text("Mouse position").pass()
                .color(0xFF999999);
        panelBuilder.addLabel(xy(2, panelBuilder.formLayout().lastRow())).text(mousePosition);

        panelBuilder.formLayout().addRow("4dlu").addRow("baseline:pref").addRow("2dlu").addRow("pref");
        panelBuilder.addLabel(xy(0, panelBuilder.formLayout().lastRow(2))).text("Color at mouse").pass()
                .color(0xFF999999);
        panelBuilder.addLabel(xy(2, panelBuilder.formLayout().lastRow(2))).text(argbColor);
        panelBuilder.addLabel(xy(2, panelBuilder.formLayout().lastRow())).text(htmlColor);
        panelBuilder.formLayout().addRow("2dlu").addRow("8dlu").done()
                .addComponent(new StoneComponent(null), xy(2, panelBuilder.formLayout().lastRow()))
                .bind("stone", color, c -> new Background(new RectangleShape().strokeColor(0xFF7f7f7f).fillColor(c)));

        Property<Boolean> isToolActive = developerTools().addTool("Toolkit", panelBuilder.build());
        Bindings.bind(READ, isToolActive, isActive);
    }
}

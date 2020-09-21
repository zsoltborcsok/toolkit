package org.nting.toolkit.app;

import static org.nting.data.binding.Bindings.BindingStrategy.READ;
import static org.nting.toolkit.ToolkitRunnable.createOneTimeRunnable;
import static org.nting.toolkit.ToolkitServices.developerTools;
import static org.nting.toolkit.ToolkitServices.toolkitManager;
import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.FormLayout.xy;

import java.util.function.Consumer;

import org.nting.data.Property;
import org.nting.data.binding.Bindings;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.ToolkitManager;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.ui.style.AbstractStyleModule;
import org.nting.toolkit.ui.style.material.MaterialStyleModule;

import playn.core.Color;
import playn.core.Game;
import playn.core.ImageLayer;
import playn.core.PlayN;
import pythagoras.f.Point;

public class ToolkitApp extends Game.Default {

    @FunctionalInterface
    public interface InitPromise {

        void then(Consumer<ToolkitManager> initConsumer);
    }

    public static InitPromise startApp() {
        return startApp(new MaterialStyleModule());
    }

    public static InitPromise startApp(AbstractStyleModule styleModule) {
        ToolkitManager toolkitManager = toolkitManager();
        toolkitManager.setStyleModule(styleModule);
        PlayN.run(new ToolkitApp(33, toolkitManager));
        return initConsumer -> toolkitManager
                .schedule(createOneTimeRunnable(0, () -> initConsumer.accept(toolkitManager)));
    }

    private final ToolkitManager toolkitManager;
    private int paintCount = 0;
    private int time = 0;

    private Property<String> currentFps = new ObjectProperty<>("N/A");
    private Property<String> mousePosition = new ObjectProperty<>("N/A");
    private Property<String> argbColor = new ObjectProperty<>("N/A");
    private Property<String> htmlColor = new ObjectProperty<>("N/A");
    private Property<Boolean> isToolkitDeveloperToolActive = new ObjectProperty<>(false);

    public ToolkitApp(int updateRate, ToolkitManager toolkitManager) {
        super(updateRate);
        this.toolkitManager = toolkitManager;

        registerToolkitDeveloperTool();
    }

    @Override
    public void init() {
    }

    @Override
    public boolean paint(float alpha) {
        paintCount++;

        try {
            return toolkitManager.paint();
        } catch (RuntimeException e) {
            PlayN.log(getClass()).error(e.getMessage(), e);
            return true;
        }
    }

    @Override
    public void update(int delta) {
        try {
            toolkitManager.update(delta);
        } catch (RuntimeException e) {
            PlayN.log(getClass()).error(e.getMessage(), e);
        }

        time += delta;
        if (time > 5000) {
            if (5 <= paintCount && paintCount < 150) {
                PlayN.log(getClass()).info("FPS: {}", paintCount / 5);
            }

            paintCount = 0;
            time -= 5000;
        }

        if (isToolkitDeveloperToolActive.getValue()) {
            updateToolkitDeveloperTool();
        }
    }

    private void updateToolkitDeveloperTool() {
        currentFps.setValue(String.valueOf(paintCount * 1000f / time));
        Point position = toolkitManager.mouseDispatcher().getLastMousePosition(toolkitManager.root());
        mousePosition.setValue("(" + position.x + ", " + position.y + ")");
        int[][] imageData = ((ImageLayer) PlayN.graphics().rootLayer().get(0)).image().getImageData((int) position.x,
                (int) position.y, 1, 1);
        argbColor.setValue("ARGB(" + imageData[0][0] + ", " + imageData[0][1] + ", " + imageData[0][2] + ", "
                + imageData[0][3] + ")");
        htmlColor.setValue("HTML(#"
                + Integer.toHexString(Color.argb(imageData[0][0], imageData[0][1], imageData[0][2], imageData[0][3]))
                + ")");
    }

    private void registerToolkitDeveloperTool() {
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder("right:max(pref;64dlu), 3dlu, 0px:grow", "");

        panelBuilder.formLayout().addRow("4dlu").addRow("baseline:pref");
        panelBuilder.addLabel(xy(0, panelBuilder.formLayout().lastRow())).text("Frames Per Second").pass()
                .color(0xFF999999);
        panelBuilder.addLabel(xy(2, panelBuilder.formLayout().lastRow())).text(currentFps);

        panelBuilder.formLayout().addRow("4dlu").addRow("baseline:pref");
        panelBuilder.addLabel(xy(0, panelBuilder.formLayout().lastRow())).text("Mouse position").pass()
                .color(0xFF999999);
        panelBuilder.addLabel(xy(2, panelBuilder.formLayout().lastRow())).text(mousePosition);

        panelBuilder.formLayout().addRow("4dlu").addRow("baseline:pref").addRow("4dlu").addRow("pref");
        panelBuilder.addLabel(xy(0, panelBuilder.formLayout().lastRow(2))).text("Color at mouse").pass()
                .color(0xFF999999);
        panelBuilder.addLabel(xy(2, panelBuilder.formLayout().lastRow(2))).text(argbColor);
        panelBuilder.addLabel(xy(2, panelBuilder.formLayout().lastRow())).text(htmlColor);

        Property<Boolean> isToolActive = developerTools().addTool("Toolkit", panelBuilder.build());
        Bindings.bind(READ, isToolActive, isToolkitDeveloperToolActive);
    }
}

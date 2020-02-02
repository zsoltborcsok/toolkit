package org.nting.toolkit.app;

import static org.nting.toolkit.ToolkitServices.toolkitManager;

import javax.annotation.Nullable;

import org.nting.toolkit.Component;
import org.nting.toolkit.ToolkitManager;
import org.nting.toolkit.layout.AbsoluteLayout;
import org.nting.toolkit.ui.style.AbstractStyleModule;
import org.nting.toolkit.ui.style.material.MaterialStyleModule;

import playn.core.Game;
import playn.core.PlayN;

public class ToolkitApp extends Game.Default {

    public static void startApp(@Nullable Component content) {
        startApp(new MaterialStyleModule(), content);
    }

    public static void startApp(AbstractStyleModule styleModule, @Nullable Component content) {
        ToolkitManager toolkitManager = toolkitManager();
        toolkitManager.setStyleModule(styleModule);
        if (content != null) {
            toolkitManager.root().addComponent(content, AbsoluteLayout.fillParentConstraint());
        }
        PlayN.run(new ToolkitApp(33, toolkitManager));
    }

    private final ToolkitManager toolkitManager;
    private int paintCount = 0;
    private int time = 0;

    public ToolkitApp(int updateRate, ToolkitManager toolkitManager) {
        super(updateRate);
        this.toolkitManager = toolkitManager;
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
    }
}

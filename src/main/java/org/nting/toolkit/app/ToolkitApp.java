package org.nting.toolkit.app;

import static org.nting.toolkit.ToolkitRunnable.createOneTimeRunnable;
import static org.nting.toolkit.ToolkitServices.toolkitManager;

import java.util.function.Consumer;

import org.nting.toolkit.ToolkitManager;
import org.nting.toolkit.ui.style.AbstractStyleModule;
import org.nting.toolkit.ui.style.material.MaterialStyleModule;

import playn.core.Game;
import playn.core.PlayN;

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

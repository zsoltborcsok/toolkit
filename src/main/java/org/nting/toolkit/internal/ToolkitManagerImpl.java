package org.nting.toolkit.internal;

import java.util.List;

import org.nting.toolkit.ToolkitManager;
import org.nting.toolkit.ToolkitRunnable;
import org.nting.toolkit.event.ClipboardDispatcher;
import org.nting.toolkit.event.KeyDispatcher;
import org.nting.toolkit.event.MouseDispatcher;
import org.nting.toolkit.event.PointerDispatcher;

import com.google.common.collect.Lists;

import playn.core.PlayN;
import playn.core.Pointer;

public class ToolkitManagerImpl implements ToolkitManager {

    private final CanvasManager canvasManager;

    public final MouseDispatcher mouseDispatcher;
    public final KeyDispatcher keyDispatcher;
    public final ClipboardDispatcher clipboardDispatcher;

    private final List<ToolkitRunnable> scheduledRunnables = Lists.newLinkedList();
    private final List<Runnable> afterPaintRunnables = Lists.newLinkedList();

    private Root root;
    private int dluSizeX = -1;
    private int dluSizeY = -1;

    public ToolkitManagerImpl() {
        keyDispatcher = new KeyDispatcher();
        if (PlayN.mouse().hasMouse()) {
            mouseDispatcher = new MouseDispatcher();
            PlayN.mouse().setListener(mouseDispatcher);
        } else {
            mouseDispatcher = new PointerDispatcher();
            PlayN.pointer().setListener((Pointer.Listener) mouseDispatcher);
        }
        PlayN.keyboard().setListener(keyDispatcher);

        clipboardDispatcher = new ClipboardDispatcher();
        PlayN.clipboard().addPasteListener(clipboardDispatcher);

        canvasManager = new CanvasManager();
        updateDluSize(false);

        PlayN.setResizeListener((width, height) -> {
            canvasManager.updateCanvas();
            updateDluSize(false);
        });
    }

    @Override
    public boolean paint() {
        if (/* PlayN.assets().isDone() && */root != null) {
            canvasManager.getCanvas().save();
            root.paint(canvasManager.getCanvas());
            canvasManager.getCanvas().restore();

            for (int i = 0; i < afterPaintRunnables.size(); i++) {
                afterPaintRunnables.get(i).run();
                afterPaintRunnables.remove(i--);
            }

            return root.isCanvasDirty();
        } else {
            return false;
        }
    }

    @Override
    public void update(float delta) {
        if (root != null) {
            root.update(delta);
        }

        for (int i = 0; i < scheduledRunnables.size(); i++) {
            ToolkitRunnable runnable = scheduledRunnables.get(i);
            if (runnable.isFinished()) {
                scheduledRunnables.remove(i--);
            } else {
                runnable.update(delta);
            }
        }
    }

    @Override
    public MouseDispatcher mouseDispatcher() {
        return mouseDispatcher;
    }

    @Override
    public KeyDispatcher keyDispatcher() {
        return keyDispatcher;
    }

    @Override
    public ClipboardDispatcher clipboardDispatcher() {
        return clipboardDispatcher;
    }

    @Override
    public Root root() {
        return root;
    }

    @Override
    public int getDluSizeX() {
        return dluSizeX;
    }

    @Override
    public int getDluSizeY() {
        return dluSizeY;
    }

    @Override
    public void schedule(ToolkitRunnable runnable) {
        if (!scheduledRunnables.contains(runnable)) {
            scheduledRunnables.add(runnable);
        }
    }

    @Override
    public void invokeAfterRepaint(Runnable runnable) {
        if (!afterPaintRunnables.contains(runnable)) {
            afterPaintRunnables.add(runnable);
        }
    }

    private void updateDluSize(boolean force) {
        // TODO
    }
}

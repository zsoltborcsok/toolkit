package org.nting.toolkit.internal;

import static org.nting.toolkit.ToolkitServices.tooltipManager;
import static org.nting.toolkit.ToolkitServices.unitConverter;

import java.util.List;
import java.util.function.BiConsumer;

import org.nting.data.Registration;
import org.nting.toolkit.Component;
import org.nting.toolkit.ToolkitManager;
import org.nting.toolkit.ToolkitRunnable;
import org.nting.toolkit.component.Dialog;
import org.nting.toolkit.component.Popup;
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
    private final List<BiConsumer<Integer, Integer>> dluChangeListeners = Lists.newLinkedList();

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
        updateDluSize();

        PlayN.setResizeListener((width, height) -> {
            canvasManager.updateCanvas();
            updateDluSize();
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

        tooltipManager().update(delta);
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

    @Override
    public Registration addDluChangeListener(BiConsumer<Integer, Integer> dluChangeListener) {
        if (dluChangeListener != null && !dluChangeListeners.contains(dluChangeListener)) {
            dluChangeListeners.add(dluChangeListener);
        }

        return () -> dluChangeListeners.remove(dluChangeListener);
    }

    private void updateDluSize() {
        if (canvasManager.getCanvas() == null) {
            return;
        }

        float width = canvasManager.getCanvas().width();
        float height = canvasManager.getCanvas().height();

        float xUnit = unitConverter().dialogUnitXAsPixel((Component) null);
        float yUnit = unitConverter().dialogUnitYAsPixel((Component) null);

        int dluSizeX = Math.round(width / xUnit);
        int dluSizeY = Math.round(height / yUnit);

        if (this.dluSizeX != dluSizeX || this.dluSizeY != dluSizeY) {
            this.dluSizeX = dluSizeX;
            this.dluSizeY = dluSizeY;

            List<Popup> popups = Lists.newArrayList();
            Component focusOwner = null;
            if (root == null) {
                root = new Root(canvasManager.getCanvas().width(), canvasManager.getCanvas().height());
                tooltipManager().registerRoot(root);
            } else {
                focusOwner = keyDispatcher.getFocusOwner().getValue();
                popups.addAll(root.popups());
                root.popups().clear();
                root.removeAllComponents();
                root.setSize(width, height);
            }
            dluChangeListeners.forEach(listener -> listener.accept(dluSizeX, dluSizeY));
            popups.stream().filter(popup -> popup instanceof Dialog).forEach(popup -> root.popups().add(popup));
            keyDispatcher.requestFocus(focusOwner);
        }
    }
}

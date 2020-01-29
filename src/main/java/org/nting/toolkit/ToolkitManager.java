package org.nting.toolkit;

import java.util.function.BiConsumer;

import org.nting.data.Registration;
import org.nting.toolkit.event.ClipboardDispatcher;
import org.nting.toolkit.event.KeyDispatcher;
import org.nting.toolkit.event.MouseDispatcher;
import org.nting.toolkit.internal.Root;

public interface ToolkitManager {

    // region Called by the the PlayN framework
    boolean paint();

    void update(float delta);
    // endregion

    // region Used related to the Toolkit
    MouseDispatcher mouseDispatcher();

    KeyDispatcher keyDispatcher();

    ClipboardDispatcher clipboardDispatcher();

    Root root();

    int getDluSizeX();

    int getDluSizeY();

    void schedule(ToolkitRunnable runnable);

    void invokeAfterRepaint(Runnable runnable);

    Registration addDluChangeListener(BiConsumer<Integer, Integer> dluChangeListener);
    // endregion
}

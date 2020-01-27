package org.nting.toolkit;

import org.nting.toolkit.event.ClipboardDispatcher;
import org.nting.toolkit.event.KeyDispatcher;
import org.nting.toolkit.event.MouseDispatcher;

public interface ToolkitManager {

    // region Called by the the PlayN framework
    boolean paint();

    void update(float delta);
    // endregion

    // region Used internally by the Toolkit
    MouseDispatcher mouseDispatcher();

    KeyDispatcher keyDispatcher();

    ClipboardDispatcher clipboardDispatcher();

    Component root();

    int getDluSizeX();

    int getDluSizeY();

    void schedule(ToolkitRunnable runnable);

    void invokeAfterRepaint(Runnable runnable);
    // endregion
}

package org.nting.toolkit;

import java.util.function.BiConsumer;

import org.nting.data.Registration;
import org.nting.data.inject.Injector;
import org.nting.toolkit.event.ClipboardDispatcher;
import org.nting.toolkit.event.KeyDispatcher;
import org.nting.toolkit.event.MouseDispatcher;
import org.nting.toolkit.internal.Root;
import org.nting.toolkit.ui.style.AbstractStyleModule;

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

    void setStyleModule(AbstractStyleModule styleModule);

    Injector getStyleInjector();

    int getDluSizeX();

    int getDluSizeY();

    Registration addDluChangeListener(BiConsumer<Integer, Integer> dluChangeListener);

    void schedule(ToolkitRunnable runnable);

    void invokeAfterRepaint(Runnable runnable);
    // endregion
}

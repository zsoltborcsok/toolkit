package org.nting.toolkit.ui.style;

import static org.nting.toolkit.ToolkitServices.fontManager;
import static org.nting.toolkit.ToolkitServices.unitConverter;

import org.nting.data.inject.AbstractModule;
import org.nting.toolkit.FontManager;

import playn.core.Font;

public abstract class AbstractStyleModule extends AbstractModule {

    protected Font font;

    @Override
    protected final void configure() {
        font = fontManager().getFont(FontManager.FontSize.SMALL_FONT);

        doConfigureUIs();
        doConfigureComponents();
        doConfigureStyles();
    }

    protected abstract void doConfigureComponents();

    protected abstract void doConfigureStyles();

    protected abstract void doConfigureUIs();

    protected final int dluX(int dluX) {
        float oneDlu = unitConverter().dialogUnitXAsPixel(font);
        return Math.round(dluX * oneDlu);
    }

    protected final int dluY(int dluY) {
        float oneDlu = unitConverter().dialogUnitYAsPixel(font);
        return Math.round(dluY * oneDlu);
    }
}

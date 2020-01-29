package org.nting.toolkit.internal;

import static org.nting.toolkit.ToolkitServices.fontManager;

import java.util.Map;

import org.nting.toolkit.Component;
import org.nting.toolkit.FontManager;
import org.nting.toolkit.UnitConverter;
import org.nting.toolkit.component.AbstractComponent;

import com.google.common.collect.Maps;

import playn.core.Font;
import playn.core.PlayN;
import playn.core.TextFormat;
import playn.core.TextLayout;
import pythagoras.f.Dimension;

/**
 * Note that Aero(on Vista) uses horizontal dialog unit conversion ratio 1/3 [0.3333] and not 1/4 [0.25]
 */
public class UnitConverterImpl implements UnitConverter {

    public static final String OLD_AVERAGE_CHARACTER_TEST_STRING = "x";
    public static final String MODERN_AVERAGE_CHARACTER_TEST_STRING = "abcdefghijklmnopqrstuvwxyz0123456789";
    public static final String BALANCED_AVERAGE_CHARACTER_TEST_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private String averageCharWidthTestString = BALANCED_AVERAGE_CHARACTER_TEST_STRING;
    private final Map<Font, Dimension> dialogBaseUnitsCache = Maps.newHashMap();

    public void setAverageCharWidthTestString(String averageCharWidthTestString) {
        this.averageCharWidthTestString = averageCharWidthTestString;
        clearCache();
    }

    public int dialogUnitXAsPixel(float dluX, Component component) {
        return dluX == 0 ? 0 : Math.round(dluX * dialogUnitXAsPixel(component));
    }

    public float dialogUnitXAsPixel(Component component) {
        return getDialogBaseUnits(component).width / 4;
    }

    public float dialogUnitXAsPixel(Font font) {
        return getDialogBaseUnits(font).width / 4;
    }

    public int dialogUnitYAsPixel(float dluY, Component component) {
        return dluY == 0 ? 0 : Math.round(dluY * dialogUnitYAsPixel(component));
    }

    public float dialogUnitYAsPixel(Component component) {
        return getDialogBaseUnits(component).height / 8;
    }

    public float dialogUnitYAsPixel(Font font) {
        return getDialogBaseUnits(font).height / 8;
    }

    private Dimension getDialogBaseUnits(Component component) {
        Font font = null;
        if (component instanceof AbstractComponent) {
            font = ((AbstractComponent) component).getValue("FONT");
        }
        if (font == null) {
            font = getDefaultFont();
        }

        return getDialogBaseUnits(font);
    }

    private Font getDefaultFont() {
        return fontManager().getFont(FontManager.FontSize.SMALL_FONT);
    }

    private Dimension getDialogBaseUnits(Font font) {
        if (dialogBaseUnitsCache.get(font) == null) {
            TextLayout textLayout = PlayN.graphics().layoutText(averageCharWidthTestString,
                    new TextFormat(font, Float.MAX_VALUE, TextFormat.Alignment.LEFT));

            float averageCharWidth = textLayout.width() / averageCharWidthTestString.length();
            if (averageCharWidthTestString.equals(BALANCED_AVERAGE_CHARACTER_TEST_STRING)) {
                averageCharWidth += 0.5f;
            }
            Dimension dimension = new Dimension(averageCharWidth, textLayout.height());
            dialogBaseUnitsCache.put(font, dimension);
            // PlayN.log().debug(font.toString() + "[DLU]: " + dimension.width + ", " + dimension.height);
        }

        return dialogBaseUnitsCache.get(font);
    }

    public void clearCache() {
        dialogBaseUnitsCache.clear();
    }
}

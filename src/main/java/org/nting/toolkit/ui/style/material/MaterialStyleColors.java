package org.nting.toolkit.ui.style.material;

import static org.nting.toolkit.ui.style.material.MaterialColorPalette.deep_orange_a700;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.grey_700;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.indigo_100;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.indigo_500;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.indigo_700;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.pink_a100;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.pink_a200;
import static org.nting.toolkit.ui.style.material.MaterialColorPalette.pink_a400;
import static org.nting.toolkit.ui.style.material.MaterialDarkTheme.DARK_DISABLED_OPACITY;
import static org.nting.toolkit.ui.style.material.MaterialDarkTheme.DARK_DIVIDER_OPACITY;
import static org.nting.toolkit.ui.style.material.MaterialDarkTheme.DARK_PRIMARY_OPACITY;
import static org.nting.toolkit.ui.style.material.MaterialDarkTheme.DARK_SECONDARY_OPACITY;
import static org.nting.toolkit.ui.style.material.MaterialDarkTheme.DARK_THEME_BACKGROUND_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialDarkTheme.DARK_THEME_BASE_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialDarkTheme.DARK_THEME_DISABLED_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialDarkTheme.DARK_THEME_DIVIDER_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialDarkTheme.DARK_THEME_SECONDARY_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialDarkTheme.DARK_THEME_TEXT_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialLightTheme.LIGHT_DISABLED_OPACITY;
import static org.nting.toolkit.ui.style.material.MaterialLightTheme.LIGHT_DIVIDER_OPACITY;
import static org.nting.toolkit.ui.style.material.MaterialLightTheme.LIGHT_PRIMARY_OPACITY;
import static org.nting.toolkit.ui.style.material.MaterialLightTheme.LIGHT_SECONDARY_OPACITY;
import static org.nting.toolkit.ui.style.material.MaterialLightTheme.LIGHT_THEME_BACKGROUND_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialLightTheme.LIGHT_THEME_BASE_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialLightTheme.LIGHT_THEME_DISABLED_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialLightTheme.LIGHT_THEME_DIVIDER_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialLightTheme.LIGHT_THEME_SECONDARY_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialLightTheme.LIGHT_THEME_TEXT_COLOR;

import org.nting.data.Property;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.util.ColorUtils;

// https://material.io/resources/color/#!/
public class MaterialStyleColors {

    public static final boolean IS_LIGHT = true;

    public static final int BASE_COLOR = IS_LIGHT ? LIGHT_THEME_BASE_COLOR : DARK_THEME_BASE_COLOR;
    public static final int PRIMARY_TEXT_COLOR = IS_LIGHT ? LIGHT_THEME_TEXT_COLOR : DARK_THEME_TEXT_COLOR;
    public static final int PRIMARY_BACKGROUND_COLOR = IS_LIGHT ? LIGHT_THEME_BACKGROUND_COLOR
            : DARK_THEME_BACKGROUND_COLOR;
    public static final int SECONDARY_TEXT_COLOR = IS_LIGHT ? LIGHT_THEME_SECONDARY_COLOR : DARK_THEME_SECONDARY_COLOR;
    public static final int DISABLED_TEXT_COLOR = IS_LIGHT ? LIGHT_THEME_DISABLED_COLOR : DARK_THEME_DISABLED_COLOR;
    public static final int DIVIDER_COLOR = IS_LIGHT ? LIGHT_THEME_DIVIDER_COLOR : DARK_THEME_DIVIDER_COLOR;
    public static final int ERROR_COLOR = deep_orange_a700;

    public static final int PRIMARY_COLOR = indigo_500;
    public static final int LIGHT_PRIMARY_COLOR = indigo_100;
    public static final int DARK_PRIMARY_COLOR = indigo_700;
    public static final int ACCENT_COLOR = pink_a200;
    public static final int LIGHT_ACCENT_COLOR = pink_a100;
    public static final int DARK_ACCENT_COLOR = pink_a400;

    public static final int DIVIDER_OPACITY = IS_LIGHT ? LIGHT_DIVIDER_OPACITY : DARK_DIVIDER_OPACITY;
    public static final int DISABLED_OPACITY = IS_LIGHT ? LIGHT_DISABLED_OPACITY : DARK_DISABLED_OPACITY;
    public static final int SECONDARY_OPACITY = IS_LIGHT ? LIGHT_SECONDARY_OPACITY : DARK_SECONDARY_OPACITY;
    public static final int PRIMARY_OPACITY = IS_LIGHT ? LIGHT_PRIMARY_OPACITY : DARK_PRIMARY_OPACITY;

    public static final int DIVIDER_OPACITY_COLOR = ColorUtils.adjustAlpha(DIVIDER_OPACITY, BASE_COLOR);
    public static final int DISABLED_OPACITY_COLOR = ColorUtils.adjustAlpha(DISABLED_OPACITY, BASE_COLOR);
    public static final int SECONDARY_OPACITY_COLOR = ColorUtils.adjustAlpha(SECONDARY_OPACITY, BASE_COLOR);
    public static final int PRIMARY_OPACITY_COLOR = ColorUtils.adjustAlpha(PRIMARY_OPACITY, BASE_COLOR);

    public static final int DIVIDER_OPACITY_PRIMARY = ColorUtils.adjustAlpha(DIVIDER_OPACITY, PRIMARY_COLOR);
    public static final int DISABLED_OPACITY_PRIMARY = ColorUtils.adjustAlpha(DISABLED_OPACITY, PRIMARY_COLOR);

    public static final int DIALOG_TITLE_COLOR = ColorUtils.adjustAlpha(222, BASE_COLOR);
    public static final int TOOLTIP_BACKGROUND = ColorUtils.adjustAlpha(230, grey_700);
    public static final int TOOLTIP_COLOR = PRIMARY_BACKGROUND_COLOR;

    public static final int SELECTION_BACKGROUND = 0xFF81C7EB;
    public static final int BORDER_COLOR = 0xFFCCCCCC;


    public static int disabledColor(int color) {
        int opacity = IS_LIGHT ? MaterialLightTheme.LIGHT_DISABLED_OPACITY : MaterialDarkTheme.DARK_DISABLED_OPACITY;
        return ColorUtils.adjustAlpha(opacity, color);
    }

    public static int secondaryColor(int color) {
        int opacity = IS_LIGHT ? MaterialLightTheme.LIGHT_SECONDARY_OPACITY : MaterialDarkTheme.DARK_SECONDARY_OPACITY;
        return ColorUtils.adjustAlpha(opacity, color);
    }

    public static int dividerColor(int color) {
        int opacity = IS_LIGHT ? MaterialLightTheme.LIGHT_DIVIDER_OPACITY : MaterialDarkTheme.DARK_DIVIDER_OPACITY;
        return ColorUtils.adjustAlpha(opacity, color);
    }

    public static Property<Integer> disabledColor(Property<Integer> color) {
        return new ObjectProperty<>(disabledColor(color.getValue()));
    }

    public static Property<Integer> secondaryColor(Property<Integer> color) {
        return new ObjectProperty<>(secondaryColor(color.getValue()));
    }

    public static Property<Integer> dividerColor(Property<Integer> color) {
        return new ObjectProperty<>(dividerColor(color.getValue()));
    }
}

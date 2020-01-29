package org.nting.toolkit;

import playn.core.Font;

public interface FontManager {

    enum FontSize {
        EXTRA_SMALL_FONT, SMALL_FONT, MEDIUM_FONT, LARGE_FONT, EXTRA_LARGE_FONT, EXTRA_EXTRA_LARGE_FONT;

        public FontSize increase() {
            switch (this) {
            case EXTRA_SMALL_FONT:
                return SMALL_FONT;
            case SMALL_FONT:
                return MEDIUM_FONT;
            case MEDIUM_FONT:
                return LARGE_FONT;
            case LARGE_FONT:
                return EXTRA_LARGE_FONT;
            default:
                return EXTRA_EXTRA_LARGE_FONT;
            }
        }

        public FontSize reduce() {
            switch (this) {
            case MEDIUM_FONT:
                return SMALL_FONT;
            case LARGE_FONT:
                return MEDIUM_FONT;
            case EXTRA_LARGE_FONT:
                return LARGE_FONT;
            case EXTRA_EXTRA_LARGE_FONT:
                return EXTRA_LARGE_FONT;
            default:
                return EXTRA_SMALL_FONT;
            }
        }
    }

    void setSize(float defaultSize);

    Font getFont(FontSize fontSize);

    Font getIconFont(FontSize fontSize);

    Font getIconFont(float size);

    Font getFont(FontSize fontSize, Font.Style style);

    FontSize getFontSize(Font font);
}

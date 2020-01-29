package org.nting.toolkit.internal;

import org.nting.toolkit.FontManager;
import org.nting.toolkit.util.SimpleMap;
import org.nting.toolkit.util.StorageUtils;

import playn.core.Font;
import playn.core.PlayN;
import pythagoras.f.MathUtil;

// Use 4:5 proportion, while somewhere it is advised to use 3:4.
// http://lifehacker.com/stick-to-these-font-sizes-to-simplify-designs-and-more-1531615325
public class FontManagerImpl implements FontManager {

    private static final String DEFAULT_FONT_SIZE_KEY = "FontManager.defaultFontSize";

    private Font extraSmallFont;
    private Font smallFont;
    private Font mediumFont;
    private Font largeFont;
    private Font extraLargeFont;
    private Font xxLargeFont;

    private Font extraSmallIconFont;
    private Font smallIconFont;
    private Font mediumIconFont;
    private Font largeIconFont;
    private Font extraLargeIconFont;
    private Font xxLargeIconFont;

    private final String textFontName;
    private final String iconFontName;
    private final SimpleMap<Float, Font> customIconFonts = new SimpleMap<>();

    public FontManagerImpl() {
        this("SourceSansPro-Regular", "IconFont");
    }

    public FontManagerImpl(String textFontName, String iconFontName) {
        this.textFontName = textFontName;
        this.iconFontName = iconFontName;

        float defaultFontSize = (float) StorageUtils.getItem(DEFAULT_FONT_SIZE_KEY, PlayN.graphics().defaultFontSize());
        applySize(defaultFontSize);
    }

    @Override
    public void setSize(float defaultFontSize) {
        StorageUtils.setItem(DEFAULT_FONT_SIZE_KEY, defaultFontSize);
        applySize(defaultFontSize);
    }

    private void applySize(float defaultFontSize) {
        int sizeXS = MathUtil.round(defaultFontSize * 0.8f);
        int sizeS = MathUtil.round(defaultFontSize);
        int sizeM = MathUtil.round(defaultFontSize * 1.25f);
        int sizeL = MathUtil.round(defaultFontSize * 1.56f);
        int sizeXL = MathUtil.round(defaultFontSize * 1.95f);
        int sizeXXL = MathUtil.round(defaultFontSize * 2.34f);
        initializeFonts(sizeXS, sizeS, sizeM, sizeL, sizeXL, sizeXXL);
    }

    private void initializeFonts(int sizeXS, int sizeS, int sizeM, int sizeL, int sizeXL, int sizeXXL) {
        customIconFonts.clear();

        extraSmallFont = PlayN.graphics().createFont(textFontName, Font.Style.PLAIN, sizeXS);
        smallFont = PlayN.graphics().createFont(textFontName, Font.Style.PLAIN, sizeS);
        mediumFont = PlayN.graphics().createFont(textFontName, Font.Style.PLAIN, sizeM);
        largeFont = PlayN.graphics().createFont(textFontName, Font.Style.PLAIN, sizeL);
        extraLargeFont = PlayN.graphics().createFont(textFontName, Font.Style.PLAIN, sizeXL);
        xxLargeFont = PlayN.graphics().createFont(textFontName, Font.Style.PLAIN, sizeXXL);

        extraSmallIconFont = PlayN.graphics().createFont(iconFontName, Font.Style.PLAIN, sizeXS);
        smallIconFont = PlayN.graphics().createFont(iconFontName, Font.Style.PLAIN, sizeS);
        mediumIconFont = PlayN.graphics().createFont(iconFontName, Font.Style.PLAIN, sizeM);
        largeIconFont = PlayN.graphics().createFont(iconFontName, Font.Style.PLAIN, sizeL);
        extraLargeIconFont = PlayN.graphics().createFont(iconFontName, Font.Style.PLAIN, sizeXL);
        xxLargeIconFont = PlayN.graphics().createFont(iconFontName, Font.Style.PLAIN, sizeXXL);
    }

    @Override
    public Font getFont(FontSize fontSize) {
        switch (fontSize) {
        case EXTRA_SMALL_FONT:
            return extraSmallFont;
        case SMALL_FONT:
            return smallFont;
        case MEDIUM_FONT:
            return mediumFont;
        case LARGE_FONT:
            return largeFont;
        case EXTRA_LARGE_FONT:
            return extraLargeFont;
        case EXTRA_EXTRA_LARGE_FONT:
            return xxLargeFont;
        }

        return smallFont;
    }

    @Override
    public Font getIconFont(FontSize fontSize) {
        switch (fontSize) {
        case EXTRA_SMALL_FONT:
            return extraSmallIconFont;
        case SMALL_FONT:
            return smallIconFont;
        case MEDIUM_FONT:
            return mediumIconFont;
        case LARGE_FONT:
            return largeIconFont;
        case EXTRA_LARGE_FONT:
            return extraLargeIconFont;
        case EXTRA_EXTRA_LARGE_FONT:
            return xxLargeIconFont;
        }

        return smallIconFont;
    }

    @Override
    public Font getIconFont(float size) {
        Font font = customIconFonts.get(size);
        if (font == null) {
            font = PlayN.graphics().createFont(iconFontName, Font.Style.PLAIN, size);
            customIconFonts.put(size, font);
        }

        return font;
    }

    @Override
    public Font getFont(FontSize fontSize, Font.Style style) {
        String fontName = null;
        switch (style) {
        case PLAIN:
            return getFont(fontSize);
        case BOLD:
            fontName = "SourceSansPro-Bold";
            break;
        case BOLD_ITALIC:
            fontName = "SourceSansPro-BoldItalic";
            break;
        case ITALIC:
            fontName = "SourceSansPro-Italic";
            break;
        }

        switch (fontSize) {
        case EXTRA_SMALL_FONT:
            return PlayN.graphics().createFont(fontName, Font.Style.PLAIN, extraSmallFont.size());
        case SMALL_FONT:
            return PlayN.graphics().createFont(fontName, Font.Style.PLAIN, smallFont.size());
        case MEDIUM_FONT:
            return PlayN.graphics().createFont(fontName, Font.Style.PLAIN, mediumFont.size());
        case LARGE_FONT:
            return PlayN.graphics().createFont(fontName, Font.Style.PLAIN, largeFont.size());
        case EXTRA_LARGE_FONT:
            return PlayN.graphics().createFont(fontName, Font.Style.PLAIN, extraLargeFont.size());
        case EXTRA_EXTRA_LARGE_FONT:
            return PlayN.graphics().createFont(fontName, Font.Style.PLAIN, xxLargeFont.size());
        }

        return smallFont;
    }

    @Override
    public FontSize getFontSize(Font font) {
        float fontSize = font.size();
        if (fontSize == extraSmallFont.size()) {
            return FontSize.EXTRA_SMALL_FONT;
        } else if (fontSize == smallFont.size()) {
            return FontSize.SMALL_FONT;
        } else if (fontSize == mediumFont.size()) {
            return FontSize.MEDIUM_FONT;
        } else if (fontSize == largeFont.size()) {
            return FontSize.LARGE_FONT;
        } else if (fontSize == extraLargeFont.size()) {
            return FontSize.EXTRA_LARGE_FONT;
        } else if (fontSize == xxLargeFont.size()) {
            return FontSize.EXTRA_EXTRA_LARGE_FONT;
        }

        return FontSize.SMALL_FONT;
    }
}

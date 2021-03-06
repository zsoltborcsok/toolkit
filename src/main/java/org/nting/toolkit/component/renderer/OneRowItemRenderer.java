package org.nting.toolkit.component.renderer;

import static org.nting.toolkit.ToolkitServices.fontManager;
import static org.nting.toolkit.ToolkitServices.unitConverter;
import static org.nting.toolkit.ui.stone.ContentBuilder.builderOnContent;
import static org.nting.toolkit.ui.stone.TextContentSingleLine.textContent;

import java.util.function.Function;

import org.nting.data.util.Pair;
import org.nting.toolkit.component.ListComponent;
import org.nting.toolkit.ui.stone.TextContent;

import com.google.common.collect.ObjectArrays;

import playn.core.Canvas;
import playn.core.Font;
import pythagoras.f.Dimension;

public class OneRowItemRenderer<T> extends AbstractItemRenderer<T> {

    private final String defaultText;
    private final Function<T, String> mainPropertyGetter;
    private final Function<T, String>[] altPropertyGetters;
    private Pair<Font, Font> mainAndAltFonts; // Optimization

    @SafeVarargs
    public OneRowItemRenderer(String defaultText, Function<T, String> mainPropertyGetter,
            Function<T, String>... altPropertyGetters) {
        super(ObjectArrays.concat(mainPropertyGetter, altPropertyGetters));

        this.defaultText = defaultText;
        this.mainPropertyGetter = mainPropertyGetter;
        this.altPropertyGetters = altPropertyGetters;
    }

    @Override
    public void paint(ListComponent<T> listComponent, T item, Canvas canvas, Dimension size, boolean selected) {
        TextContent textContentMain = textContent(mainAndAltFonts.first, listComponent.color.getValue(),
                getValueOrDefault(mainPropertyGetter.apply(item), defaultText));
        TextContent textContentAlt = textContent(mainAndAltFonts.second, listComponent.secondaryColor.getValue(),
                getConcatenatedValue(item, altPropertyGetters));
        textContentAlt.setTranslateY(mainAndAltFonts.first.size() - mainAndAltFonts.second.size());
        paintSearch(item, textContentMain, textContentAlt);

        int paddingX = unitConverter().dialogUnitXAsPixel(4, listComponent);
        builderOnContent(textContentMain).rightPaddedContent(paddingX).horizontalContents(textContentAlt)
                .paddedContent(0, paddingX, 0, paddingX).rightPaddedContent(1).paddedContent(1, 0, 1, 0)
                .paint(canvas, size);
    }

    @Override
    public Dimension getPreferredSize(ListComponent<T> listComponent, boolean selected) {
        if (mainAndAltFonts == null) {
            Font font = listComponent.font.getValue();
            if (font == null) {
                return new Dimension();
            }
            Font mainFont = fontManager().getFont(fontManager().getFontSize(font).increase(), font.style());
            mainAndAltFonts = Pair.of(mainFont, font);
        }

        return new Dimension(Math.round(32 * 4 * unitConverter().dialogUnitXAsPixel(mainAndAltFonts.second)),
                Math.round(12 * unitConverter().dialogUnitYAsPixel(mainAndAltFonts.first)));
    }
}

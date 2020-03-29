package org.nting.toolkit.component.renderer;

import static org.nting.toolkit.ToolkitServices.fontManager;
import static org.nting.toolkit.ToolkitServices.unitConverter;
import static org.nting.toolkit.ui.stone.Content.EMPTY_CONTENT;
import static org.nting.toolkit.ui.stone.ContentBuilder.builderOnContent;
import static org.nting.toolkit.ui.stone.TextContentSingleLine.textContent;

import java.util.function.Function;

import org.nting.data.util.Pair;
import org.nting.toolkit.FontManager.FontSize;
import org.nting.toolkit.component.Icon;
import org.nting.toolkit.component.ListComponent;
import org.nting.toolkit.ui.stone.ContentBuilder;
import org.nting.toolkit.ui.stone.TextContent;
import org.nting.toolkit.util.GwtCompatibleUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.ObjectArrays;

import playn.core.Canvas;
import playn.core.Font;
import pythagoras.f.Dimension;

public class PrimaryInfoItemRenderer<T> extends AbstractItemRenderer<T> {

    protected final String defaultText;
    protected final Function<T, Icon> primaryInfoPropertyGetter;
    protected Function<T, String> primaryTextPropertyGetter;
    protected Function<T, String>[] secondaryTextPropertyGetters;
    protected int lineCount = 1;

    protected Pair<Font, Font> primaryAndSecondaryFonts; // Optimization
    protected float dluXSecondary; // Optimization

    @SafeVarargs
    public PrimaryInfoItemRenderer(Function<T, Icon> primaryInfoPropertyGetter,
            Function<T, String> primaryTextPropertyGetter, Function<T, String>... secondaryTextPropertyGetters) {
        this(null, primaryInfoPropertyGetter, primaryTextPropertyGetter, secondaryTextPropertyGetters);
    }

    @SafeVarargs
    public PrimaryInfoItemRenderer(String defaultText, Function<T, Icon> primaryInfoPropertyGetter,
            Function<T, String> primaryTextPropertyGetter, Function<T, String>... secondaryTextPropertyGetters) {
        super(ObjectArrays.concat(primaryTextPropertyGetter, secondaryTextPropertyGetters));
        this.defaultText = defaultText;
        this.primaryInfoPropertyGetter = primaryInfoPropertyGetter;
        this.primaryTextPropertyGetter = primaryTextPropertyGetter;
        this.secondaryTextPropertyGetters = secondaryTextPropertyGetters;
    }

    public PrimaryInfoItemRenderer<T> lineCount(int lineCount) {
        Preconditions.checkArgument(0 < lineCount && lineCount <= secondaryTextPropertyGetters.length + 1);
        this.lineCount = lineCount;
        return this;
    }

    @SafeVarargs
    public final PrimaryInfoItemRenderer<T> textPropertyIds(Function<T, String> primaryTextPropertyId,
            Function<T, String>... secondaryTextPropertyIds) {
        this.primaryTextPropertyGetter = primaryTextPropertyId;
        this.secondaryTextPropertyGetters = secondaryTextPropertyIds;
        return this;
    }

    @Override
    public void paint(ListComponent<T> listComponent, T item, Canvas canvas, Dimension size, boolean selected) {
        ContentBuilder contentBuilder;
        if (lineCount == 1) {
            contentBuilder = paintOneRow(listComponent, item, selected);
        } else {
            contentBuilder = paintMultipleRows(listComponent, item, selected);
        }

        if (primaryInfoPropertyGetter != null) {
            ContentBuilder primaryInfoContentBuilder = paintPrimaryInfo(listComponent, item, selected);
            primaryInfoContentBuilder.paddedContentWithoutClipping(1, 1, 1, 3).fixedSizeContent(
                    new Dimension(Math.round(dluXSecondary * (8 + 10 * Math.pow(1.25, lineCount))), size.height));
            contentBuilder.horizontalContentsLeft(primaryInfoContentBuilder.getContent());
        }
        ContentBuilder secondaryContentBuilder = paintSecondaryInfo(listComponent, item, selected);
        if (secondaryContentBuilder != null) {
            contentBuilder.horizontalContents(secondaryContentBuilder.getContent());
        }

        contentBuilder.paint(canvas, size);
    }

    protected ContentBuilder paintSecondaryInfo(ListComponent<T> listComponent, T item, boolean selected) {
        return null;
    }

    protected ContentBuilder paintPrimaryInfo(ListComponent<T> listComponent, T item, boolean selected) {
        Icon icon = primaryInfoPropertyGetter.apply(item);
        if (icon != null) {
            FontSize iconFontSize = fontManager().getFontSize(primaryAndSecondaryFonts.first);
            for (int i = 1; i < lineCount; i++) {
                iconFontSize = iconFontSize.increase();
            }
            return new ContentBuilder().content(icon.getContent(iconFontSize, listComponent.secondaryColor.getValue()));
        } else {
            return builderOnContent(EMPTY_CONTENT);
        }
    }

    protected ContentBuilder paintOneRow(ListComponent<T> listComponent, T item, boolean selected) {
        TextContent primaryTextContent = textContent(primaryAndSecondaryFonts.first, listComponent.color.getValue(),
                getValueOrDefault(primaryTextPropertyGetter.apply(item), defaultText));
        TextContent secondaryTextContent = textContent(primaryAndSecondaryFonts.second,
                listComponent.secondaryColor.getValue(), getConcatenatedValue(item, secondaryTextPropertyGetters));
        secondaryTextContent
                .setTranslateY(primaryAndSecondaryFonts.first.size() - primaryAndSecondaryFonts.second.size());
        paintSearch(item, primaryTextContent, secondaryTextContent);

        float paddingX = Math.round(dluXSecondary * 7);
        float paddingY = Math.round(dluXSecondary * 4);
        return builderOnContent(primaryTextContent).rightPaddedContent(paddingX)
                .horizontalContents(secondaryTextContent).paddedContent(paddingY, paddingX, paddingY, paddingX)
                .rightPaddedContent(1);
    }

    protected ContentBuilder paintMultipleRows(ListComponent<T> listComponent, T item, boolean selected) {
        TextContent[] textContents = new TextContent[lineCount];

        textContents[0] = textContent(primaryAndSecondaryFonts.first, listComponent.color.getValue(),
                getValueOrDefault(primaryTextPropertyGetter.apply(item), defaultText));
        for (int i = 0; i < lineCount - 1; i++) {
            String value;
            if (i == lineCount - 2 && lineCount <= secondaryTextPropertyGetters.length) {
                Function<T, String>[] propertyIds = GwtCompatibleUtils.copyOfRange(secondaryTextPropertyGetters, i,
                        secondaryTextPropertyGetters.length);
                value = getConcatenatedValue(item, propertyIds);
            } else {
                value = getAbbreviatedValue(secondaryTextPropertyGetters[i].apply(item));
            }
            int color = (i < lineCount - 2) ? listComponent.color.getValue() : listComponent.secondaryColor.getValue();
            textContents[i + 1] = textContent(primaryAndSecondaryFonts.second, color, value);
        }

        paintSearch(item, textContents);

        float paddingX = Math.round(dluXSecondary * 7);
        ContentBuilder contentBuilder = builderOnContent(textContents[0]);
        for (int i = 1; i < textContents.length; i++) {
            contentBuilder.verticalContents(textContents[i]);
        }
        return contentBuilder.paddedContent(0, paddingX, 0, paddingX).rightPaddedContent(1).paddedContent(1, 0, 1, 0);
    }

    @Override
    public Dimension getPreferredSize(ListComponent<T> listComponent, boolean selected) {
        if (primaryAndSecondaryFonts == null) {
            Font font = listComponent.font.getValue();
            if (font == null) {
                return new Dimension();
            }
            Font primaryFont = fontManager().getFont(fontManager().getFontSize(font).increase(), font.style());
            primaryAndSecondaryFonts = Pair.of(primaryFont, font);
        }

        dluXSecondary = unitConverter().dialogUnitXAsPixel(primaryAndSecondaryFonts.second);
        float dluYSecondary = unitConverter().dialogUnitYAsPixel(primaryAndSecondaryFonts.second);
        float dluYPrimary = unitConverter().dialogUnitYAsPixel(primaryAndSecondaryFonts.first);

        if (lineCount == 1) {
            return new Dimension(Math.round(32 * 4 * dluXSecondary),
                    Math.round(dluYPrimary * 8 + dluYSecondary * 8 + 2));
        } else {
            return new Dimension(Math.round(32 * 4 * dluXSecondary),
                    Math.round(dluYPrimary * 8 + dluYSecondary * 8 * (lineCount - 1) + dluYSecondary * 4 + 2));
        }
    }
}

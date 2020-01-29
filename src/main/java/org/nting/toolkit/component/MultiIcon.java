package org.nting.toolkit.component;

import org.nting.data.Property;
import org.nting.toolkit.FontManager.FontSize;
import org.nting.toolkit.ui.stone.Content;
import org.nting.toolkit.ui.stone.OverlappingHorizontalContents;

public class MultiIcon implements Icon {

    private final Icon iconFirst;
    private final Icon iconSecond;
    private final float overlapPercent;
    private final float sizePercent;

    public MultiIcon(Icon iconFirst, Icon iconSecond) {
        this(iconFirst, iconSecond, 0, 1);
    }

    public MultiIcon(Icon iconFirst, Icon iconSecond, float overlapPercent, float sizePercent) {
        this.iconFirst = iconFirst;
        this.iconSecond = iconSecond;
        this.overlapPercent = overlapPercent;
        this.sizePercent = sizePercent;
    }

    @Override
    public Content getContent(FontSize fontSize, int color) {
        return createContent(iconFirst.getContent(fontSize, color), iconSecond.getContent(fontSize, color));
    }

    @Override
    public Content getContent(FontSize fontSize, Property<Integer> color) {
        return createContent(iconFirst.getContent(fontSize, color), iconSecond.getContent(fontSize, color));
    }

    @Override
    public Content getContent(FontSize fontSize) {
        return createContent(iconFirst.getContent(fontSize), iconSecond.getContent(fontSize));
    }

    @Override
    public Content getContent(float size, int color) {
        return createContent(iconFirst.getContent(size, color), iconSecond.getContent(size, color));
    }

    @Override
    public Content getContent(float size) {
        return createContent(iconFirst.getContent(size), iconSecond.getContent(size));
    }

    private Content createContent(Content iconFirstContent, Content iconSecondContent) {
        return new OverlappingHorizontalContents(iconFirstContent, iconSecondContent, overlapPercent, sizePercent);
    }
}

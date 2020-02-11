package org.nting.toolkit.ui.style.material;

import org.nting.toolkit.ui.shape.DashLineShape;
import org.nting.toolkit.ui.shape.LineShape;
import org.nting.toolkit.ui.stone.Content;
import org.nting.toolkit.ui.stone.ContentBuilder;
import org.nting.toolkit.ui.stone.FixShapeContent;

import pythagoras.f.Dimension;
import pythagoras.f.MathUtil;

public class MaterialContentUtil {

    public static ContentBuilder paintFocusedLine(boolean enabled, boolean hasErrorMessage, float focusPercent,
            float width, int dividerColor, int errorColor, int focusedColor) {
        if (!enabled) {
            Content content = new FixShapeContent(new Dimension(width, 1),
                    new DashLineShape(0, 0, width, 0).strokeColor(dividerColor));
            return ContentBuilder.builderOnContent(content).topPaddedContent(1);
        } else if (focusPercent == 0) {
            int color = hasErrorMessage ? errorColor : dividerColor;
            Content content = new FixShapeContent(new Dimension(width, 1),
                    new LineShape(0, 0, width, 0).strokeColor(color));
            return ContentBuilder.builderOnContent(content).topPaddedContent(1);
        } else if (focusPercent == 100) {
            int color = hasErrorMessage ? errorColor : focusedColor;
            Content content = new FixShapeContent(new Dimension(width, 2),
                    new LineShape(0, 1, width, 1).strokeWidth(2).strokeColor(color));
            return ContentBuilder.builderOnContent(content);
        } else {
            int color = hasErrorMessage ? errorColor : dividerColor;
            Content content = new FixShapeContent(new Dimension(width, 1),
                    new LineShape(0, 0, width, 0).strokeColor(color));
            ContentBuilder contentBuilder = ContentBuilder.builderOnContent(content).topPaddedContent(1);

            color = hasErrorMessage ? errorColor : focusedColor;
            width = MathUtil.round(width * focusPercent / 100f);
            Content overContent = new FixShapeContent(new Dimension(width, 2),
                    new LineShape(0, 1, width, 1).strokeWidth(2).strokeColor(color));
            return contentBuilder.addOverContent(overContent, 0, 1, 0, 1);
        }
    }
}

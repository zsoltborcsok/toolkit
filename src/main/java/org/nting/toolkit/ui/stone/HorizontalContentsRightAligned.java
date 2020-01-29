package org.nting.toolkit.ui.stone;

import java.util.List;

import com.google.common.collect.Lists;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class HorizontalContentsRightAligned extends Content {

    private final Content leftContent;
    private final Content rightContent;

    public HorizontalContentsRightAligned(Content leftContent, Content rightContent) {
        this.leftContent = leftContent;
        this.rightContent = rightContent;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        Dimension rightPrefSize = rightContent.getPreferredSize();
        if (rightPrefSize.width < size.width) {
            float leftWidth = size.width - rightPrefSize.width;
            leftContent.paint(canvas, new Dimension(leftWidth, size.height));
            canvas.translate(leftWidth, 0);
            rightContent.paint(canvas, new Dimension(rightPrefSize.width, size.height));
            canvas.translate(-leftWidth, 0);
        } else {
            rightContent.paint(canvas, new Dimension(size.width, size.height));
        }

    }

    @Override
    public Dimension getPreferredSize() {
        Dimension leftPrefSize = leftContent.getPreferredSize();
        Dimension rightPrefSize = rightContent.getPreferredSize();

        return new Dimension(leftPrefSize.width + rightPrefSize.width,
                Math.max(leftPrefSize.height, rightPrefSize.height));
    }

    @Override
    public List<Stone> children() {
        return Lists.<Stone> newArrayList(leftContent, rightContent);
    }
}

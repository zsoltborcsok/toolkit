package org.nting.toolkit.ui.stone;

import java.util.List;

import com.google.common.collect.Lists;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class HorizontalContents extends Content {

    private final Content leftContent;
    private final Content rightContent;

    public HorizontalContents(Content leftContent, Content rightContent) {
        this.leftContent = leftContent;
        this.rightContent = rightContent;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        Dimension leftPrefSize = leftContent.getPreferredSize();
        if (leftPrefSize.width < size.width) {
            leftContent.paint(canvas, new Dimension(leftPrefSize.width, size.height));
            canvas.translate(leftPrefSize.width, 0);
            rightContent.paint(canvas, new Dimension(size.width - leftPrefSize.width, size.height));
            canvas.translate(-leftPrefSize.width, 0);
        } else {
            leftContent.paint(canvas, new Dimension(size.width, size.height));
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

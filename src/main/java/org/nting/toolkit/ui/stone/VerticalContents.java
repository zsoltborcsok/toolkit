package org.nting.toolkit.ui.stone;

import java.util.List;

import com.google.common.collect.Lists;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class VerticalContents extends Content {

    private final Content topContent;
    private final Content bottomContent;

    public VerticalContents(Content topContent, Content bottomContent) {
        this.topContent = topContent;
        this.bottomContent = bottomContent;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        Dimension topPrefSize = topContent.getPreferredSize();
        if (topPrefSize.height < size.height) {
            topContent.paint(canvas, new Dimension(size.width, topPrefSize.height));
            canvas.translate(0, topPrefSize.height);
            bottomContent.paint(canvas, new Dimension(size.width, size.height - topPrefSize.height));
            canvas.translate(0, -topPrefSize.height);
        } else {
            topContent.paint(canvas, new Dimension(size.width, size.height));
        }

    }

    @Override
    public Dimension getPreferredSize() {
        Dimension leftPrefSize = topContent.getPreferredSize();
        Dimension rightPrefSize = bottomContent.getPreferredSize();

        return new Dimension(Math.max(leftPrefSize.width, rightPrefSize.width),
                leftPrefSize.height + rightPrefSize.height);
    }

    @Override
    public List<Stone> children() {
        return Lists.<Stone> newArrayList(topContent, bottomContent);
    }
}

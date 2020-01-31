package org.nting.toolkit.ui.stone;

import java.util.List;

import com.google.common.collect.Lists;

import playn.core.Canvas;
import pythagoras.f.Dimension;
import pythagoras.f.MathUtil;

public class OverlappingHorizontalContents extends Content {

    private final Content contentA;
    private final Content contentB;
    private final float overlapPercent;// e.g. 0.4f
    private final float sizePercent;// e.g. 0.6f

    public OverlappingHorizontalContents(Content contentA, Content contentB, float overlapPercent, float sizePercent) {
        this.contentA = contentA;
        this.contentB = contentB;
        this.overlapPercent = overlapPercent;
        this.sizePercent = sizePercent;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        contentA.paint(canvas, new Dimension(size.width, size.height));

        Dimension aPrefSize = contentA.getPreferredSize();
        Dimension bPrefSize = contentB.getPreferredSize();
        float paddingB = MathUtil.ifloor(aPrefSize.width - bPrefSize.width * (overlapPercent));
        canvas.translate(paddingB, 0);
        contentB.paint(canvas, new Dimension(bPrefSize.width, size.height));
        canvas.translate(-paddingB, 0);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension aPrefSize = contentA.getPreferredSize();
        Dimension bPrefSize = contentB.getPreferredSize();

        return new Dimension(aPrefSize.width + bPrefSize.width * sizePercent,
                Math.max(aPrefSize.height, bPrefSize.height));
    }

    @Override
    public List<Stone> children() {
        return Lists.newArrayList(contentA, contentB);
    }
}

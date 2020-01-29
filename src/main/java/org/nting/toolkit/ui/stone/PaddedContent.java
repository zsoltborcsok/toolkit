package org.nting.toolkit.ui.stone;

import org.nting.toolkit.util.ToolkitUtils;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class PaddedContent extends Content {

    private final float topPadding;
    private final float rightPadding;
    private final float bottomPadding;
    private final float leftPadding;
    private boolean useClipping = true;

    public PaddedContent(Content content, float topPadding, float rightPadding, float bottomPadding,
            float leftPadding) {
        super(content);

        this.topPadding = topPadding;
        this.rightPadding = rightPadding;
        this.bottomPadding = bottomPadding;
        this.leftPadding = leftPadding;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        // NOTE: Change the padding for changing the alignment.

        Dimension preferredSize = stone.getPreferredSize();
        // If size is smaller then preferred size, then clipping is needed.
        if (preferredSize.width + (leftPadding + rightPadding) > size.width) {
            preferredSize.width = size.width - (leftPadding + rightPadding);
        }
        if (preferredSize.height + (topPadding + bottomPadding) > size.height) {
            preferredSize.height = size.height - (topPadding + bottomPadding);
        }

        float xPadding = leftPadding == 0 ? 0
                : (size.width - preferredSize.width) * leftPadding / (leftPadding + rightPadding);
        float yPadding = topPadding == 0 ? 0
                : (size.height - preferredSize.height) * topPadding / (topPadding + bottomPadding);

        canvas.translate(xPadding, yPadding);
        if (useClipping) {
            canvas.save();
            canvas.clipRect(0, 0, preferredSize.width, preferredSize.height);
            stone.paint(canvas, preferredSize);
            canvas.restore();
        } else {
            stone.paint(canvas, preferredSize);
        }
        canvas.translate(-xPadding, -yPadding);
    }

    public PaddedContent withoutClipping() {
        useClipping = false;
        return this;
    }

    @Override
    public Dimension getPreferredSize() {
        return ToolkitUtils.growDimension(stone.getPreferredSize(), leftPadding + rightPadding,
                topPadding + bottomPadding);
    }

}

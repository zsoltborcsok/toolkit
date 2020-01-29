package org.nting.toolkit.ui.stone;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class HorizontalContentsSameSize extends Content {

    private final List<Content> contents = Lists.newLinkedList();
    private float xPadding = 0;
    private float yPadding = 0;

    public HorizontalContentsSameSize(Content... contents) {
        Collections.addAll(this.contents, contents);
    }

    public HorizontalContentsSameSize(Collection<Content> contents) {
        this.contents.addAll(contents);
    }

    public HorizontalContentsSameSize xPadding(float xPadding) {
        this.xPadding = xPadding;
        return this;
    }

    public HorizontalContentsSameSize yPadding(float yPadding) {
        this.yPadding = yPadding;
        return this;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        for (int i = 0; i < contents.size(); i++) {
            Content content = contents.get(i);
            float from = size.width * i / contents.size();
            float to = size.width * (i + 1) / contents.size();
            canvas.translate(from, 0);
            content.paint(canvas, new Dimension(to - from, size.height));
            canvas.translate(-from, 0);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        float width = 0;
        float height = 0;
        for (int i = 0; i < contents.size(); i++) {
            Content content = contents.get(i);
            Dimension preferredSize = content.getPreferredSize();
            width += preferredSize.width + xPadding;
            if (i == 0) {
                height = preferredSize.height;
            } else {
                height = Math.max(height, preferredSize.height + yPadding);
            }
        }
        return new Dimension(width, height);
    }
}

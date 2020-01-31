package org.nting.toolkit.ui.stone;

import java.util.List;

import com.google.common.collect.Lists;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class ContentAndBackground extends Content {

    private Background background;

    public ContentAndBackground(Content content, Background background) {
        super(content);

        this.background = background;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        if (background != null) {
            background.paint(canvas, size);
        }
        stone.paint(canvas, size);
    }

    @Override
    public Dimension getPreferredSize() {
        return stone.getPreferredSize();
    }

    @Override
    public List<Stone> children() {
        return Lists.newArrayList(background, stone);
    }
}

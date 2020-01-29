package org.nting.toolkit.ui.stone;

import java.util.List;

import com.google.common.collect.Lists;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class Contents extends Content {

    private final List<Content> contents = Lists.newLinkedList();

    public Contents(Content mainContent) {
        contents.add(mainContent);
    }

    public Contents addContent(Content content) {
        contents.add(content);
        return this;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        for (Content content : contents) {
            content.paint(canvas, size);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return contents.get(0).getPreferredSize();
    }

    @Override
    public List<Stone> children() {
        return Lists.<Stone> newArrayList(contents);
    }
}

package org.nting.toolkit.ui.stone;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public abstract class Stone {

    public static Stone EMPTY_STONE = new Stone(null) {
        @Override
        public void paint(Canvas canvas, Dimension size) {
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(0, 0);
        }
    };

    protected Stone stone;

    public Stone(Stone stone) {
        this.stone = stone;
    }

    public abstract void paint(Canvas canvas, Dimension size);

    public abstract Dimension getPreferredSize();

    public List<Stone> children() {
        return stone == null ? Collections.emptyList() : Lists.newArrayList(stone);
    }
}

package org.nting.toolkit.ui.stone;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public abstract class Content extends Stone {

    public static Content EMPTY_CONTENT = new Content() {
        @Override
        public void paint(Canvas canvas, Dimension size) {
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(0, 0);
        }
    };

    public Content() {
        this(null);
    }

    public Content(Content content) {
        super(content);
    }

    protected Content(Stone stone) {
        super(stone);
    }
}

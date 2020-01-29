package org.nting.toolkit.ui.stone;

import org.nting.data.Property;

import com.google.common.base.Strings;

import playn.core.Canvas;
import playn.core.Font;
import playn.core.PlayN;
import playn.core.TextLayout;
import pythagoras.f.Dimension;

public class TextContentWithEllipsis extends TextContentSingleLine {

    public TextContentWithEllipsis(Font font, Property<Integer> color, String text) {
        super(font, color, text);
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        if (!Strings.isNullOrEmpty(getText())) {
            canvas.setFillColor(getColor().getValue());

            float x = translateX, y = translateY;
            for (TextLayout layout : getTextLayouts()) {
                if (x + layout.width() < size.width + 1) { // The layout width is not an int, while the size is rounded!
                    canvas.fillText(layout, x, y);
                } else {
                    // Approach the with of '...' by font size! Don't want to use expensive DLU calculation (~6DLU)
                    int splitPosition = getCaretIndex(0, size.width - getFont().size());
                    String text = getText().substring(0, splitPosition) + "...";
                    canvas.fillText(PlayN.graphics().layoutText(text, layout.format()), x, y);
                }
                y += layout.leading() + layout.ascent() + layout.descent();
            }
        }
    }
}

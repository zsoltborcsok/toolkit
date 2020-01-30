package org.nting.toolkit.ui.stone;

import static org.nting.toolkit.util.GwtCompatibleUtils.countMatches;
import static org.nting.toolkit.util.GwtCompatibleUtils.findMatches;

import java.util.List;

import org.nting.data.Property;
import org.nting.data.ValueChangeListener;
import org.nting.data.binding.Binding;
import org.nting.data.binding.Bindings;
import org.nting.data.property.ObjectProperty;
import org.nting.data.util.Pair;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import playn.core.Canvas;
import playn.core.Font;
import playn.core.PlayN;
import playn.core.TextFormat;
import playn.core.TextFormat.Alignment;
import playn.core.TextLayout;
import playn.core.TextWrap;
import pythagoras.f.Dimension;

public class TextContent extends Content {

    protected final List<Binding> bindings = Lists.newLinkedList();
    protected TextLayouts textLayouts;
    protected Property<Integer> color;
    protected float translateX = 0;
    protected float translateY = 0;

    private String searchText;
    private int highlightMatchIndex = -1;

    protected TextContent() {
    }

    public TextContent(Font font, int color, String text, float wrapWidth) {
        this.color = new ObjectProperty<>(color);
        textLayouts = createTextLayouts(font, text, wrapWidth);
    }

    public TextContent(Font font, Property<Integer> color, String text, float wrapWidth) {
        this.color = color;
        textLayouts = createTextLayouts(font, text, wrapWidth);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public TextContent(final Property<Font> font, Property<Integer> color, final Property<String> text,
            final float wrapWidth) {
        this.color = color;
        textLayouts = createTextLayouts(font.getValue(), text.getValue(), wrapWidth);

        ValueChangeListener valueChangeListener = ((ValueChangeListener) event -> textLayouts = createTextLayouts(
                font.getValue(), text.getValue(), wrapWidth)).withPriority(1);
        bindings.add(Bindings.asBinding(font.addValueChangeListener(valueChangeListener)));
        bindings.add(Bindings.asBinding(text.addValueChangeListener(valueChangeListener)));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public TextContent(final Property<Font> font, Property<Integer> color, final Property<String> text,
            final Property<Float> wrapWidth) {
        this.color = color;
        textLayouts = createTextLayouts(font.getValue(), text.getValue(), wrapWidth.getValue());

        ValueChangeListener valueChangeListener = ((ValueChangeListener) event -> textLayouts = createTextLayouts(
                font.getValue(), text.getValue(), wrapWidth.getValue())).withPriority(1);
        bindings.add(Bindings.asBinding(font.addValueChangeListener(valueChangeListener)));
        bindings.add(Bindings.asBinding(text.addValueChangeListener(valueChangeListener)));
        bindings.add(Bindings.asBinding(wrapWidth.addValueChangeListener(valueChangeListener)));
    }

    public int search(String searchText) {
        if (!Strings.isNullOrEmpty(searchText) && textLayouts.text.toLowerCase().contains(searchText)) {
            this.searchText = searchText;
            return countMatches(textLayouts.text.toLowerCase(), searchText);
        } else {
            this.searchText = null;
            highlightMatch(-1);
            return 0;
        }
    }

    public void highlightMatch(int index) {
        highlightMatchIndex = index;
    }

    public int lineOfMatch(int index) {
        int matches = 0;
        for (int i = 0; i < textLayouts.layouts.length; i++) {
            TextLayout layout = textLayouts.layouts[i];
            matches += findMatches(layout.lowerCaseText(), searchText).size();
            if (index < matches) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        if (!Strings.isNullOrEmpty(textLayouts.text)) {
            canvas.setFillColor(color.getValue());

            boolean searchIsActive = searchText != null;
            int currentMatchIndex = 0;
            float x = translateX, y = translateY;
            for (TextLayout layout : textLayouts.layouts) {
                if (size.height < y) {
                    break;
                }
                float rowHeight = layout.leading() + layout.ascent() + layout.descent();
                if (y + rowHeight < 0) {
                    y += rowHeight;
                    continue;
                }

                if (searchIsActive) {
                    currentMatchIndex = paintSearch(canvas, layout, x, y, currentMatchIndex);
                    canvas.setFillColor(color.getValue());
                }
                canvas.fillText(layout, x, y);
                y += rowHeight;
            }
        }
    }

    private int paintSearch(Canvas canvas, TextLayout layout, float x, float y, int currentMatchIndex) {
        int searchTextLength = searchText.length();
        for (Integer match : findMatches(layout.lowerCaseText(), searchText)) {
            float x1 = layout.getCaretPosition(match);
            float x2 = layout.getCaretPosition(match + searchTextLength);
            if (currentMatchIndex != highlightMatchIndex) {
                canvas.setFillColor(0x80ffff00);
            } else {
                canvas.setFillColor(0x800000ff);
            }
            canvas.fillRect(x + x1, y, x2 - x1, layout.leading() + layout.ascent() + layout.descent());
            currentMatchIndex++;
        }
        return currentMatchIndex;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(textLayouts.width, textLayouts.height);
    }

    public float getPreferredWidth() {
        return textLayouts.width;
    }

    public float getPreferredHeight() {
        return textLayouts.height;
    }

    public int getLineCount() {
        return textLayouts.layouts.length;
    }

    public List<Integer> getLineLengths() {
        List<Integer> lines = Lists.newLinkedList();
        for (TextLayout layout : textLayouts.layouts) {
            lines.add(layout.text().length());
        }
        return lines;
    }

    public float getCaretPosition(int line, int index) {
        return textLayouts.layouts[line].getCaretPosition(index);
    }

    public int getCaretIndex(int line, float x) {
        if (Strings.isNullOrEmpty(textLayouts.text)) {
            return 0;
        }

        x -= translateX;
        TextLayout layout = textLayouts.layouts[line];
        int length = layout.text().length();

        int caretIndex = 0;
        for (int i = 0; i < line; i++) {
            caretIndex += textLayouts.layouts[i].text().length();
        }

        float previousWidth = 0;
        for (int i = 1; i <= length; i++) {
            float width = Math.round(layout.getCaretPosition(i));
            if (x < width) {
                if (x - previousWidth < width - x) {
                    return caretIndex + i - 1;
                } else {
                    return caretIndex + i;
                }
            }

            previousWidth = width;
        }
        return caretIndex + textLayouts.layouts[line].text().length();
    }

    public Pair<Integer, Integer> getCaretCoordinate(int caretPosition) {
        int caretX = caretPosition;
        List<Integer> lineLengths = getLineLengths();
        int caretY = 0;
        for (; caretY < lineLengths.size(); caretY++) {
            int currentLineLength = lineLengths.get(caretY);
            if (caretX <= currentLineLength) {
                break;
            } else {
                caretX -= currentLineLength;
            }
        }

        return Pair.of(caretX, caretY);
    }

    public Font getFont() {
        return textLayouts.font;
    }

    public String getText() {
        return textLayouts.text;
    }

    public float getTranslateX() {
        return translateX;
    }

    public void setTranslateX(float translateX) {
        this.translateX = translateX;
    }

    public float getTranslateY() {
        return translateY;
    }

    public void setTranslateY(float translateY) {
        this.translateY = translateY;
    }

    public Property<Integer> getColor() {
        return color;
    }

    public void setColor(Property<Integer> color) {
        this.color = color;
    }

    public List<Binding> getBindings() {
        return bindings;
    }

    public float getLeading() {
        return textLayouts.layouts[0].leading();
    }

    protected TextLayout[] getTextLayouts() {
        return textLayouts.layouts;
    }

    private TextLayouts createTextLayouts(Font font, String text, float wrapWidth) {
        if (!Strings.isNullOrEmpty(text)) {
            return new TextLayouts(text, font, PlayN.graphics().layoutText(text,
                    new TextFormat(font, Math.max(0, wrapWidth), Alignment.LEFT), new TextWrap(wrapWidth)));

        } else {
            return new TextLayouts(text, font, PlayN.graphics().layoutText("ABC",
                    new TextFormat(font, Float.MAX_VALUE, Alignment.LEFT), TextWrap.MANUAL));
        }
    }

    protected static class TextLayouts {
        private final String text;
        private final Font font;
        private final TextLayout[] layouts;
        private final float width;
        private final float height;

        protected TextLayouts(String text, Font font, TextLayout... layouts) {
            this.text = text;
            this.font = font;
            this.layouts = layouts;

            // compute total width and height
            float width = 0, height = 0;
            for (int i = 0; i < layouts.length; i++) {
                TextLayout layout = layouts[i];
                width = Math.max(width, layout.width());
                if (i != 0) {
                    height += layout.leading();
                }
                height += (layout.ascent() + layout.descent());
            }
            this.width = Strings.isNullOrEmpty(text) ? 0 : Math.round(width);
            this.height = Math.round(height);
        }
    }
}

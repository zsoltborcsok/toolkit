package org.nting.toolkit.ui.stone;

import org.nting.data.Property;
import org.nting.data.ValueChangeListener;
import org.nting.data.binding.Bindings;
import org.nting.data.property.ObjectProperty;

import com.google.common.base.Strings;

import playn.core.Font;
import playn.core.PlayN;
import playn.core.TextFormat;

public class TextContentSingleLine extends TextContent {

    public static TextContent textContent(Font font, int color, String text) {
        return new TextContentSingleLine(font, color, text);
    }

    public static TextContent textContent(Font font, Property<Integer> color, String text) {
        return new TextContentSingleLine(font, color, text);
    }

    public static TextContent textContent(Font font, int color, Property<String> text) {
        return new TextContentSingleLine(font, color, text);
    }

    public static TextContent textContent(Property<Font> font, Property<Integer> color, Property<String> text) {
        return new TextContentSingleLine(font, color, text);
    }

    protected TextContentSingleLine(Font font, int color, String text) {
        this.color = new ObjectProperty<>(color);
        textLayouts = createSingleTextLayouts(font, text);
    }

    protected TextContentSingleLine(Font font, Property<Integer> color, String text) {
        this.color = color;
        textLayouts = createSingleTextLayouts(font, text);
    }

    public TextContentSingleLine(final Font font, int color, final Property<String> text) {
        this.color = new ObjectProperty<>(color);
        textLayouts = createSingleTextLayouts(font, text.getValue());

        ValueChangeListener<String> valueChangeListener = ((ValueChangeListener<String>) event -> textLayouts = createSingleTextLayouts(
                font, text.getValue())).withPriority(1);
        bindings.add(Bindings.asBinding(text.addValueChangeListener(valueChangeListener)));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected TextContentSingleLine(final Property<Font> font, Property<Integer> color, final Property<String> text) {
        this.color = color;
        textLayouts = createSingleTextLayouts(font.getValue(), text.getValue());

        ValueChangeListener valueChangeListener = ((ValueChangeListener) event -> textLayouts = createSingleTextLayouts(
                font.getValue(), text.getValue())).withPriority(1);
        bindings.add(Bindings.asBinding(font.addValueChangeListener(valueChangeListener)));
        bindings.add(Bindings.asBinding(text.addValueChangeListener(valueChangeListener)));
    }

    private static TextLayouts createSingleTextLayouts(Font font, String text) {
        if (!Strings.isNullOrEmpty(text)) {
            return new TextLayouts(text, font, PlayN.graphics().layoutText(text,
                    new TextFormat(font, Float.MAX_VALUE, TextFormat.Alignment.LEFT)));

        } else {
            return new TextLayouts(text, font, PlayN.graphics().layoutText("ABC",
                    new TextFormat(font, Float.MAX_VALUE, TextFormat.Alignment.LEFT)));
        }
    }
}

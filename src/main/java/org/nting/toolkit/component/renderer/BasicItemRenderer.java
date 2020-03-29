package org.nting.toolkit.component.renderer;

import static org.nting.toolkit.ToolkitServices.unitConverter;
import static org.nting.toolkit.ui.stone.ContentBuilder.builderOnContent;
import static org.nting.toolkit.ui.stone.TextContentSingleLine.textContent;

import java.util.function.Function;

import org.nting.data.Property;
import org.nting.data.property.ObjectProperty;
import org.nting.data.util.Pair;
import org.nting.toolkit.component.ListComponent;
import org.nting.toolkit.ui.stone.Content;
import org.nting.toolkit.ui.stone.TextContent;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class BasicItemRenderer<T> extends AbstractItemRenderer<T> {

    private final Function<T, String> textPropertyGetter;
    private final Property<String> text = new ObjectProperty<>("");
    private Pair<TextContent, Content> contents; // Optimization

    @SuppressWarnings("unchecked")
    public BasicItemRenderer(Function<T, String> textPropertyGetter) {
        super(textPropertyGetter);
        this.textPropertyGetter = textPropertyGetter;
    }

    @Override
    public void paint(ListComponent<T> listComponent, T item, Canvas canvas, Dimension size, boolean selected) {
        if (contents == null) {
            TextContent textContent = textContent(listComponent.font, listComponent.color, text);
            int leftPadding = unitConverter().dialogUnitXAsPixel(4, listComponent);
            Content content = builderOnContent(textContent).leftPaddedContent(leftPadding).paddedContent(1, 1, 1, 0)
                    .getContent();
            contents = Pair.of(textContent, content);
        }

        text.setValue(textPropertyGetter.apply(item));
        paintSearch(item, contents.first);
        contents.second.paint(canvas, size);
    }

    @Override
    public Dimension getPreferredSize(ListComponent<T> listComponent, boolean selected) {
        return new Dimension(unitConverter().dialogUnitXAsPixel(32 * 4, listComponent),
                unitConverter().dialogUnitYAsPixel(12, listComponent));
    }
}

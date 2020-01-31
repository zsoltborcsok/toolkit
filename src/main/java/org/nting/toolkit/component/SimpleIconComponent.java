package org.nting.toolkit.component;

import static org.nting.toolkit.ToolkitServices.fontManager;
import static org.nting.toolkit.ui.Colors.DARK_GREY;
import static org.nting.toolkit.ui.Colors.TRANSPARENT;
import static org.nting.toolkit.ui.Colors.TRN_BLACK;
import static org.nting.toolkit.util.ColorUtils.isNotTransparent;

import java.util.Set;

import org.nting.data.Property;
import org.nting.data.ValueChangeListener;
import org.nting.toolkit.FontManager.FontSize;
import org.nting.toolkit.ui.ComponentUI;
import org.nting.toolkit.ui.shape.RectangleShape;
import org.nting.toolkit.ui.stone.Content;
import org.nting.toolkit.ui.stone.ContentBuilder;

import com.google.common.collect.ImmutableSet;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class SimpleIconComponent extends AbstractComponent {

    public final Property<Icon> icon = createProperty("icon", null);
    public final Property<FontSize> fontSize = createProperty("fontSize", FontSize.LARGE_FONT);
    public final Property<Integer> color = createProperty("color", DARK_GREY);
    public final Property<Integer> disabledColor = createProperty("disabledColor", TRN_BLACK(0x30));
    public final Property<Integer> hoverColor = createProperty("hoverColor", DARK_GREY);
    public final Property<Float> zoom = createProperty("zoom", 1f); // To enable bigger icons
    public final Property<Boolean> enabled = createProperty("enabled", true);
    public final Property<Integer> backgroundColor = createProperty("backgroundColor", TRANSPARENT);

    private final Property<Content> iconContent = createProperty("iconContent", null);
    private final Set<String> contentBaseProperties = ImmutableSet.of("icon", "fontSize", "color", "disabledColor",
            "hoverColor", "enabled", "mouseOver");

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public SimpleIconComponent(Icon anIcon) {
        addValueChangeListener((ValueChangeListener) event -> {
            if (contentBaseProperties.contains(event.getPropertyName())) {
                updateIconContent();
            }
        });

        icon.setValue(anIcon);
        setFocusable(false);
        activateMouseOver();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setComponentUI(ComponentUI componentUI) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void doPaintComponent(Canvas canvas) {
        Content iconContentValue = iconContent.getValue();
        if (iconContentValue != null) {
            Dimension size = getSize();
            int backgroundColorValue = backgroundColor.getValue();
            if (isNotTransparent(backgroundColorValue)) {
                new RectangleShape().size(size.width, size.height).fillColor(backgroundColorValue).paint(canvas);
            }
            new ContentBuilder().content(iconContentValue).paddedContent(1).paint(canvas, size);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Content iconContentValue = iconContent.getValue();
        if (iconContentValue == null) {
            return new Dimension();
        } else {
            Dimension iconPreferredSize = iconContentValue.getPreferredSize();
            return new Dimension(iconPreferredSize.width + 2, iconPreferredSize.height + 2);
        }
    }

    @Override
    public boolean isFocusable() {
        return super.isFocusable() && enabled.getValue();
    }

    private void updateIconContent() {
        Icon iconValue = icon.getValue();
        if (iconValue == null) {
            iconContent.setValue(null);
        } else {
            int theColor = enabled.getValue() ? (mouseOver.getValue() ? hoverColor.getValue() : color.getValue())
                    : disabledColor.getValue();
            iconContent.setValue(createIconContent(iconValue, theColor));
        }
    }

    private Content createIconContent(Icon iconValue, int theColor) {
        if (iconValue instanceof FontIcon && 1f < zoom.getValue()) {
            float size = fontManager().getIconFont(fontSize.getValue()).size() * zoom.getValue();
            return ((FontIcon) iconValue).getContent(size, theColor);
        } else {
            return iconValue.getContent(fontSize.getValue(), theColor);
        }
    }
}

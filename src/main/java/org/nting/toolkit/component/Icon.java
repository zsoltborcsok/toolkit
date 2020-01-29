package org.nting.toolkit.component;

import org.nting.data.Property;
import org.nting.toolkit.FontManager.FontSize;
import org.nting.toolkit.ui.stone.Content;

public interface Icon {

    Content getContent(FontSize fontSize, int color);

    Content getContent(FontSize fontSize, Property<Integer> color);

    Content getContent(FontSize fontSize);
}

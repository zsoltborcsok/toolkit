package org.nting.toolkit.component;

import org.nting.data.Property;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;

import com.google.common.base.Strings;

import playn.core.Platform;
import playn.core.PlayN;

public class Link extends Label {

    public final Property<String> url = createProperty("url", "");

    public Link() {
        this("", "");
    }

    public Link(String text, String url) {
        this.text.setValue(text);
        this.url.setValue(url);

        activateMouseOver();
        mouseOver.addValueChangeListener(event -> {
            if (event.getValue()) {
                PlayN.setCursor(Platform.Cursor.HAND);
            } else {
                PlayN.setCursor(Platform.Cursor.DEFAULT);
            }
        });

        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!Strings.isNullOrEmpty(Link.this.url.getValue())) {
                    e.consume();
                    PlayN.openURL(Link.this.url.getValue());
                }
            }
        });
    }
}

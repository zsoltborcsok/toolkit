package org.nting.toolkit.ui.stone;

import playn.core.Canvas;
import playn.core.Image;
import pythagoras.f.Dimension;

public class ImageContent extends Content {

    private Image image;

    public ImageContent(Image image) {
        this.image = image;
    }

    @Override
    public void paint(Canvas canvas, Dimension size) {
        if (image != null) {
            if (image.width() <= size.width && image.height() <= size.height) {
                canvas.drawImage(image, 0, 0);
            } else {
                float dividerX = image.width() / size.width;
                float dividerY = image.height() / size.height;
                float divider = Math.max(dividerX, dividerY);
                canvas.drawImage(image, 0, 0, image.width() / divider, image.height() / divider);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (image != null) {
            return new Dimension(image.width(), image.height());
        } else {
            return new Dimension(0, 0);
        }
    }
}

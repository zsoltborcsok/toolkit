package org.nting.toolkit.ui.shape;

import com.google.common.base.MoreObjects;

import playn.core.Canvas;
import playn.core.Image;

public class ImageShape extends BasicShape<ImageShape> {

    private float x;
    private float y;
    private Image image;
    private float alpha = 1.0f;

    public ImageShape(float x, float y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public ImageShape alpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    @Override
    public void paint(Canvas canvas) {
        float width = this.width;
        float height = this.height;

        if (width == 0) {
            width = image.width();
        }
        if (height == 0) {
            height = image.height();
        }

        canvas.setAlpha(alpha);
        canvas.drawImage(image, x, y, width, height);
        canvas.setAlpha(1.0f);
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper().add("x", x).add("y", y).add("image", image).add("alpha", alpha);
    }
}

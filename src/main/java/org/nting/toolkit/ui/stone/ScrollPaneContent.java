package org.nting.toolkit.ui.stone;

import org.nting.toolkit.ui.shape.RectangleShape;

import playn.core.Canvas;
import pythagoras.f.Dimension;
import pythagoras.f.MathUtil;
import pythagoras.f.Point;

public class ScrollPaneContent extends Content {

    private Dimension preferredSize;
    private Point viewPosition;
    private int scrollBarWidth;
    private boolean vsbVisible;
    private boolean hsbVisible;

    private int scrollBarColor;
    private int sliderColor;
    private int hoverBackground;
    private Point mousePosition;

    public ScrollPaneContent update(Dimension preferredSize, Point viewPosition, int scrollBarWidth, boolean vsbVisible,
            boolean hsbVisible, int scrollBarColor, int sliderColor, int hoverBackground, Point mousePosition) {
        this.preferredSize = preferredSize;
        this.viewPosition = viewPosition;
        this.scrollBarWidth = scrollBarWidth;
        this.vsbVisible = vsbVisible;
        this.hsbVisible = hsbVisible;
        this.scrollBarColor = scrollBarColor;
        this.sliderColor = sliderColor;
        this.hoverBackground = hoverBackground;
        this.mousePosition = mousePosition;

        return this;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void paint(Canvas canvas, Dimension size) {
        if (vsbVisible) {
            float vScrollBarHeight = vScrollBarHeight(size.height);
            new RectangleShape(size.width - scrollBarWidth, 0, scrollBarWidth, size.height).fillColor(scrollBarColor)
                    .paint(canvas);

            int theSliderColor = !isVSlideBarHover(size) ? sliderColor : hoverBackground;
            new RectangleShape(size.width - scrollBarWidth, vSliderPosition(vScrollBarHeight), scrollBarWidth,
                    vSliderHeight(size.height, vScrollBarHeight)).fillColor(theSliderColor).paint(canvas);
        }
        if (hsbVisible) {
            float hScrollBarWidth = hScrollBarWidth(size.width);
            new RectangleShape(0, size.height - scrollBarWidth, size.width, scrollBarWidth).fillColor(scrollBarColor)
                    .paint(canvas);

            int theSliderColor = !isHSlideBarHover(size) ? sliderColor : hoverBackground;
            new RectangleShape(hSliderPosition(hScrollBarWidth), size.height - scrollBarWidth,
                    hSliderWidth(size.width, hScrollBarWidth), scrollBarWidth).fillColor(theSliderColor).paint(canvas);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        throw new UnsupportedOperationException();
    }

    public float vScrollBarHeight(float height) {
        return hsbVisible ? height - scrollBarWidth : height;
    }

    public int vSliderPosition(float height) {
        return MathUtil.ifloor(viewPosition.y * height / preferredSize.height);
    }

    public int vSliderHeight(float height, float vScrollBarHeight) {
        return MathUtil.round(height * vScrollBarHeight / preferredSize.height);
    }

    public float hScrollBarWidth(float width) {
        return vsbVisible ? width - scrollBarWidth : width;
    }

    public int hSliderPosition(float width) {
        return MathUtil.ifloor(viewPosition.x * width / preferredSize.width);
    }

    public int hSliderWidth(float width, float hScrollBarWidth) {
        return MathUtil.ifloor(width * hScrollBarWidth / preferredSize.width);
    }

    public boolean isVsbVisible() {
        return vsbVisible;
    }

    public boolean isHsbVisible() {
        return hsbVisible;
    }

    private boolean isVSlideBarHover(Dimension size) {
        return mousePosition != null && size.width - scrollBarWidth <= mousePosition.x;
    }

    private boolean isHSlideBarHover(Dimension size) {
        return mousePosition != null && size.height - scrollBarWidth <= mousePosition.y;
    }
}

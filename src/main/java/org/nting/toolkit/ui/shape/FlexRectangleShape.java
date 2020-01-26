package org.nting.toolkit.ui.shape;

import com.google.common.base.MoreObjects;

import playn.core.Canvas;
import playn.core.Path;
import playn.core.Platform;
import playn.core.PlayN;

public class FlexRectangleShape extends BasicShape<FlexRectangleShape> {

    private final float x;
    private final float y;
    private final float topLeftRadius;
    private final float topRightRadius;
    private final float bottomRightRadius;
    private final float bottomLeftRadius;

    public FlexRectangleShape(float topLeftRadius, float topRightRadius, float bottomRightRadius,
            float bottomLeftRadius) {
        this(0, 0, 0, 0, topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius);
    }

    public FlexRectangleShape(float x, float y, float width, float height, float topLeftRadius, float topRightRadius,
            float bottomRightRadius, float bottomLeftRadius) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.topLeftRadius = topLeftRadius;
        this.topRightRadius = topRightRadius;
        this.bottomRightRadius = bottomRightRadius;
        this.bottomLeftRadius = bottomLeftRadius;
    }

    @Override
    public void paint(Canvas canvas) {
        if (fillGradient != null) {
            canvas.setFillGradient(fillGradient);
            fillRoundRect(canvas);
        } else if (fillPattern != null) {
            canvas.setFillPattern(fillPattern);
            fillRoundRect(canvas);
        } else if (fillColor != 0) {
            canvas.setFillColor(fillColor);
            fillRoundRect(canvas);
        }

        if (strokeColor != 0) {
            canvas.setStrokeColor(strokeColor);
            canvas.setStrokeWidth(strokeWidth);
            strokeRoundRect(canvas);
        }
    }

    private void fillRoundRect(Canvas canvas) {
        Path path = canvas.createPath();

        path.moveTo(topLeftRadius, 0);
        path.lineTo(width - topRightRadius, 0);
        if (0 < topRightRadius)
            path.quadraticCurveTo(width, 0, width, topRightRadius);
        path.lineTo(width, height - bottomRightRadius);
        if (0 < bottomRightRadius)
            path.quadraticCurveTo(width, height, width - bottomRightRadius, height);
        path.lineTo(bottomLeftRadius, height);
        if (0 < bottomLeftRadius)
            path.quadraticCurveTo(0, height, 0, height - bottomLeftRadius);
        path.lineTo(0, topLeftRadius);
        if (0 < topLeftRadius)
            path.quadraticCurveTo(0, 0, topLeftRadius, 0);
        path.close();

        canvas.translate(x, y);
        canvas.fillPath(path);
        canvas.translate(-x, -y);
    }

    private void strokeRoundRect(Canvas canvas) {
        Path path = canvas.createPath();

        if (PlayN.platformType() == Platform.Type.HTML) {
            width -= 0.5f;
            height -= 0.5f;

            path.moveTo(topLeftRadius, 0.5f);
            path.lineTo(width - topRightRadius, 0.5f);
            if (0 < topRightRadius)
                path.quadraticCurveTo(width, 0.5f, width, topRightRadius);
            path.lineTo(width, height - bottomRightRadius);
            if (0 < bottomRightRadius)
                path.quadraticCurveTo(width, height, width - bottomRightRadius, height);
            path.lineTo(bottomLeftRadius, height);
            if (0 < bottomLeftRadius)
                path.quadraticCurveTo(0.5f, height, 0.5f, height - bottomLeftRadius);
            path.lineTo(0.5f, topLeftRadius);
            if (0 < topLeftRadius)
                path.quadraticCurveTo(0.5f, 0.5f, topLeftRadius, 0.5f);
            path.close();
        } else {
            width -= 1;
            height -= 1;

            path.moveTo(topLeftRadius, 0);
            path.lineTo(width - topRightRadius, 0);
            if (0 < topRightRadius)
                path.quadraticCurveTo(width, 0, width, topRightRadius);
            path.lineTo(width, height - bottomRightRadius);
            if (0 < bottomRightRadius)
                path.quadraticCurveTo(width, height, width - bottomRightRadius, height);
            path.lineTo(bottomLeftRadius, height);
            if (0 < bottomLeftRadius)
                path.quadraticCurveTo(0, height, 0, height - bottomLeftRadius);
            path.lineTo(0, topLeftRadius);
            if (0 < topLeftRadius)
                path.quadraticCurveTo(0, 0, topLeftRadius, 0);
            path.close();
        }

        canvas.translate(x, y);
        canvas.strokePath(path);
        canvas.translate(-x, -y);
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper().add("x", x).add("y", y).add("topLeftRadius", topLeftRadius)
                .add("topRightRadius", topRightRadius).add("bottomRightRadius", bottomRightRadius)
                .add("bottomLeftRadius", bottomLeftRadius);
    }
}

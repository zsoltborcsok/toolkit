package org.nting.toolkit.ui.shape;

import org.nting.toolkit.util.ColorUtilities;

import com.google.common.base.MoreObjects;

import playn.core.Canvas;

public class CircleShadowShape extends BasicShape<CircleShadowShape> {

    private final int spread; // size
    private final int offsetX;
    private final int offsetY;
    private final int blur;
    private final int kernelSize; // multiple of two

    // Alpha adjusted colors to paint blur.
    private final int[] colors;

    private float x;
    private float y;
    private float radius;

    public CircleShadowShape(int spread) {
        this(0, 0, 0, spread);
    }

    public CircleShadowShape(int spread, int offsetX, int offsetY, int blur) {
        this(spread, offsetX, offsetY, blur, 0xFF000000);
    }

    public CircleShadowShape(int spread, int offsetX, int offsetY, int blur, int fillColor) {
        this.spread = spread;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.blur = blur;

        kernelSize = ((blur + 1) / 2) * 2;
        colors = new int[kernelSize];
        fillColor(fillColor);
    }

    @Override
    public CircleShadowShape fillColor(int fillColor) {
        super.fillColor(fillColor);

        if (0 < kernelSize) {
            int[] kernel = createMeanBlurKernel();
            int[] sumKernel = createSumKernel(kernel);
            int sum = 2 * sumKernel[kernelSize - 1] - sumKernel[kernelSize - 2];

            for (int i = 0; i < kernelSize; i++) {
                float percent = 1 - sumKernel[i] / (float) sum;
                colors[i] = ColorUtilities.moreTransparent(fillColor, percent * 100);
            }
        }

        return this;
    }

    public CircleShadowShape circle(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        return this;
    }

    @Override
    public CircleShadowShape size(float width, float height) {
        float halfWidth = width / 2;
        float halfHeight = height / 2;
        float radius = 0f < this.radius && this.radius < halfWidth && this.radius < halfHeight ? this.radius
                : Math.min(halfWidth, halfHeight);
        circle(halfWidth, halfHeight, radius);
        return this;
    }

    @Override
    public void paint(Canvas canvas) {
        int kernelHalfSize = kernelSize / 2;
        canvas.translate(offsetX, offsetY);

        canvas.setFillColor(fillColor);
        canvas.fillCircle(x, y, radius + spread - kernelHalfSize);

        for (int i = 0; i < kernelSize; i++) {
            canvas.setStrokeColor(colors[kernelSize - 1 - i]);
            canvas.strokeCircle(x, y, radius + spread - kernelHalfSize + i);
        }
        canvas.translate(-offsetX, -offsetY);
    }

    private int[] createSumKernel(int[] array) {
        int length = array.length;
        int[] sumArray = new int[length];
        for (int x = 0; x < length; x++) {
            sumArray[x] = array[x];
            if (0 < x) {
                sumArray[x] += sumArray[x - 1];
            }
        }
        return sumArray;
    }

    private int[] createMeanBlurKernel() {
        int[] kernel = new int[kernelSize];
        for (int i = 0; i < kernelSize; i++) {
            kernel[i] = 2;
        }
        if (blur < kernelSize) {
            kernel[0] = 1;
            kernel[kernelSize - 1] = 1;
        }

        return kernel;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper().add("spread", spread).add("offsetX", offsetX).add("offsetY", offsetY)
                .add("blur", blur).add("kernelSize", kernelSize).add("colors", colors).add("x", x).add("y", y)
                .add("radius", radius);
    }
}

package org.nting.toolkit.ui.shape;

import org.nting.toolkit.util.ColorUtils;

import com.google.common.base.MoreObjects;

import playn.core.Canvas;
import pythagoras.f.MathUtil;

// See: How Blurs & Filters Work - Computerphile
public class RectangleShadowShape extends BasicShape<RectangleShadowShape> {

    private final int offsetX;
    private final int offsetY;

    private final int blur;
    private final int spread; // size

    private final int kernelSize; // multiple of two

    // Alpha adjusted colors to paint blur.
    private final int[][] colors;

    public RectangleShadowShape(int spread) {
        this(0, 0, 0, spread);
    }

    public RectangleShadowShape(int offsetX, int offsetY, int blur, int spread) {
        this(offsetX, offsetY, blur, spread, 0xFF000000);
    }

    public RectangleShadowShape(int offsetX, int offsetY, int blur, int spread, int fillColor) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.blur = blur;
        this.spread = spread;

        kernelSize = ((blur + 1) / 2) * 2;
        colors = new int[kernelSize + 1][kernelSize + 1];
        fillColor(fillColor);
    }

    @Override
    public RectangleShadowShape fillColor(int fillColor) {
        super.fillColor(fillColor);

        if (0 < kernelSize) {
            int[][] kernel = createMeanBlurKernel();
            int[][] sumKernel = createSumKernel(kernel);
            int sum = 2 * sumKernel[kernelSize - 1][kernelSize - 1] - sumKernel[kernelSize - 1][kernelSize - 2];

            for (int x = 0; x < kernelSize; x++) {
                for (int y = 0; y < kernelSize; y++) {
                    // Support some 'rounding' by virtually leaving out one pixel at the corner
                    int k = sumKernel[x][y] - kernel[x][y];
                    float percent = 1 - k / (float) sum;
                    colors[x][y] = ColorUtils.moreTransparent(fillColor, percent * 100);
                }
            }
            for (int i = 0; i < kernelSize; i++) {
                float percent = 1 - sumKernel[i][kernelSize - 1] / (float) sum;
                colors[i][kernelSize] = colors[kernelSize][i] = ColorUtils.moreTransparent(fillColor,
                        percent * 100);
            }
            colors[kernelSize][kernelSize] = fillColor;
        }

        return this;
    }

    @Override
    public void paint(Canvas canvas) {
        int kernelHalfSize = kernelSize / 2;
        canvas.translate(offsetX - spread - kernelHalfSize, offsetY - spread - kernelHalfSize);

        int width = MathUtil.round(this.width) + 2 * spread + kernelSize;
        int height = MathUtil.round(this.height) + 2 * spread + kernelSize;

        canvas.setFillColor(fillColor);
        canvas.fillRect(kernelSize, kernelSize, width - 2 * kernelSize, height - 2 * kernelSize);

        width--;
        height--;
        for (int x = 0; x <= width; x++) {
            for (int y = 0; y < kernelSize; y++) {
                if (x < kernelSize) {
                    canvas.setStrokeColor(colors[x][y]);
                } else if (width - kernelSize < x) {
                    canvas.setStrokeColor(colors[width - x][y]);
                } else {
                    continue; // canvas.setStrokeColor(colors[kernelSize][y]);
                }
                canvas.drawPoint(x, y);
                canvas.drawPoint(width - x, height - y);
            }
        }

        for (int y = 0; y < kernelSize; y++) {
            canvas.setStrokeColor(colors[kernelSize][y]);
            canvas.drawLine(kernelSize, y, width - kernelSize, y);
            canvas.drawLine(kernelSize, height - y, width - kernelSize, height - y);
        }
        for (int x = 0; x < kernelSize; x++) {
            canvas.setStrokeColor(colors[x][kernelSize]);
            canvas.drawLine(x, kernelSize, x, height - kernelSize);
            canvas.drawLine(width - x, kernelSize, width - x, height - kernelSize);
        }

        canvas.translate(spread + kernelHalfSize - offsetX, spread + kernelHalfSize - offsetY);
    }

    private int[][] createSumKernel(int[][] array) {
        int length = array.length;
        int[][] sumArray = new int[length][length];
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                sumArray[x][y] = array[x][y];
                if (0 < x) {
                    sumArray[x][y] += sumArray[x - 1][y];
                }
                if (0 < y) {
                    sumArray[x][y] += sumArray[x][y - 1];
                }
                if (0 < x && 0 < y) {
                    sumArray[x][y] -= sumArray[x - 1][y - 1];
                }
            }
        }
        return sumArray;
    }

    private int[][] createMeanBlurKernel() {
        int[][] kernel = new int[kernelSize][kernelSize];
        for (int i = 0; i < kernelSize; i++) {
            for (int j = 0; j < kernelSize; j++) {
                kernel[i][j] = 2;
            }
        }
        if (blur < kernelSize) {
            for (int i = 0; i < kernelSize; i++) {
                kernel[i][0] = 1;
                kernel[i][kernelSize - 1] = 1;
            }
            for (int j = 1; j < kernelSize - 1; j++) {
                kernel[0][j] = 1;
                kernel[kernelSize - 1][j] = 1;
            }
        }

        return kernel;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper().add("offsetX", offsetX).add("offsetY", offsetY).add("blur", blur)
                .add("spread", spread).add("kernelSize", kernelSize).add("colors", colors);
    }
}

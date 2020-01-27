package org.nting.toolkit.internal;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.ImageLayer;
import playn.core.PlayN;

public class CanvasManager {

    private Canvas canvas;

    public CanvasManager() {
        canvas = createCanvas(PlayN.graphics().width(), PlayN.graphics().height());
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void updateCanvas() {
        canvas = createCanvas(PlayN.graphics().width(), PlayN.graphics().height());
    }

    private Canvas createCanvas(int width, int height) {
        PlayN.graphics().rootLayer().removeAll();

        CanvasImage canvasImage = PlayN.graphics().createImage(width, height);
        ImageLayer imageLayer = PlayN.graphics().createImageLayer(canvasImage);
        PlayN.graphics().rootLayer().add(imageLayer);
        return canvasImage.canvas();
    }
}

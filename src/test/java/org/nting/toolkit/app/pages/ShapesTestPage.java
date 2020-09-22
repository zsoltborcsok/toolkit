package org.nting.toolkit.app.pages;

import static playn.core.PlayN.graphics;

import org.nting.toolkit.Component;
import org.nting.toolkit.app.IPageFactory;
import org.nting.toolkit.app.Pages;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.StoneComponent;
import org.nting.toolkit.ui.shape.CircleShape;
import org.nting.toolkit.ui.shape.EllipsisShape;
import org.nting.toolkit.ui.shape.LineShape;
import org.nting.toolkit.ui.shape.RectangleShape;
import org.nting.toolkit.ui.shape.RoundedRectangleShape;
import org.nting.toolkit.ui.stone.Stone;

import playn.core.Canvas;
import playn.core.Font;
import playn.core.TextFormat;
import playn.core.TextLayout;
import pythagoras.f.Dimension;

public class ShapesTestPage implements IPageFactory {

    @Override
    public PageSize getPageSize() {
        return PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent(Pages pages) {
        StoneComponent stoneComponent = new StoneComponent(new Stone(null) {

            @Override
            public void paint(Canvas canvas, Dimension size) {
                for (int x = 0; x <= 500; x += 100) {
                    new LineShape(x, 0, x, 500).strokeColor(0x88000000).paint(canvas);
                }
                for (int y = 0; y <= 500; y += 100) {
                    new LineShape(0, y, 500, y).strokeColor(0x88000000).paint(canvas);
                }
                new CircleShape(100.5f, 100.5f, 99).strokeColor(0xff880000).paint(canvas);
                new CircleShape(50.5f, 250.5f, 49).fillColor(0xff880000).paint(canvas);
                new EllipsisShape(201, 1, 299, 99).strokeColor(0xff880000).paint(canvas);
                new EllipsisShape(201, 101, 199, 99).fillColor(0xff880000).paint(canvas);
                new EllipsisShape(401, 1, 99, 199).strokeColor(0xff880000).fillColor(0x40008800).paint(canvas);
                new RectangleShape(101, 201, 199, 199).strokeColor(0xff880000).paint(canvas);
                new RectangleShape(1, 401, 99, 99).fillColor(0xff880000).paint(canvas);
                new RoundedRectangleShape(301, 301, 199, 199, 10).strokeColor(0xff880000).paint(canvas);
                new RoundedRectangleShape(401, 201, 99, 99, 10).fillColor(0xff880000).paint(canvas);

                TextFormat format = new TextFormat()
                        .withFont(graphics().createFont("SourceSansPro-Regular", Font.Style.PLAIN, 16));
                TextLayout layout = graphics().layoutText("\u200Bzero width space character", format);
                canvas.fillText(layout, 50, 520);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(510, 510);
            }
        });

        return wrap(stoneComponent);
    }
}

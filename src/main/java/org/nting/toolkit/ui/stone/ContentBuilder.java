package org.nting.toolkit.ui.stone;

import static org.nting.toolkit.ToolkitServices.fontManager;

import org.nting.data.Property;
import org.nting.toolkit.FontManager.FontSize;
import org.nting.toolkit.component.Icon;
import org.nting.toolkit.component.StoneComponent;
import org.nting.toolkit.ui.shape.BasicShape;
import org.nting.toolkit.ui.shape.CircleShadowShape;
import org.nting.toolkit.ui.shape.CircleShape;
import org.nting.toolkit.ui.shape.Shape;

import playn.core.Canvas;
import playn.core.Font;
import playn.core.Image;
import pythagoras.f.Dimension;

public class ContentBuilder {

    public static ContentBuilder builderOnContent(Content content) {
        return new ContentBuilder().content(content);
    }

    public static ContentBuilder builderOnContent(TextContent content, Property<Integer> color) {
        return new ContentBuilder().content(content, color);
    }

    public static ContentBuilder builderOnContent(Content content, Property<Integer> color) {
        return new ContentBuilder().content(content, color);
    }

    public static ContentBuilder builderOnBackground(BasicShape<?> shape) {
        return new ContentBuilder().background(shape);
    }

    public static ContentBuilder builderOnShape(BasicShape<?> shape) {
        return builderOnContent(new BasicShapeContent(shape));
    }

    public static ContentBuilder builderOnShape(BasicShape<?> shape, Dimension size) {
        return builderOnContent(new BasicShapeContent(size, shape));
    }

    public static ContentBuilder builderOnShape(BasicShape<?> shape, float width, float height) {
        return builderOnShape(shape, new Dimension(width, height));
    }

    public static ContentBuilder builderOnFixShape(Shape<?> shape, Dimension size) {
        return builderOnContent(new FixShapeContent(size, shape));
    }

    public static ContentBuilder builderOnFixShape(Shape<?> shape, float width, float height) {
        return builderOnFixShape(shape, new Dimension(width, height));
    }

    public static ContentBuilder circleWithElevation(float radius, int background) {
        ContentBuilder contentBuilder = new ContentBuilder();

        // .paper-fab-0[elevation="1"], but double dark
        contentBuilder.fixShapeContent(new CircleShadowShape(0, 0, 2, 2, 0x48000000).circle(radius, radius, radius),
                new Dimension(2 * radius, 2 * radius));
        contentBuilder.fixShapeContent(new CircleShadowShape(0, 0, 1, 5, 0x3C000000).circle(radius, radius, radius),
                new Dimension(2 * radius, 2 * radius));
        contentBuilder.fixShapeContent(new CircleShadowShape(-2, 0, 3, 1, 0x66000000).circle(radius, radius, radius),
                new Dimension(2 * radius, 2 * radius));

        return contentBuilder.addOverContent(new FixShapeContent(new Dimension(2 * radius, 2 * radius),
                new CircleShape(radius, radius, radius).fillColor(background)));
    }

    private Content content;
    private MeasureContent measureContent;
    private Background background;

    public ContentBuilder background(BasicShape<?> shape) {
        background = new Background(background, shape);
        return this;
    }

    public ContentBuilder background(BasicShape<? extends BasicShape<?>> shape, int color) {
        background = new Background(background, shape.fillColor(color));
        return this;
    }

    public ContentBuilder background(BasicShape<? extends BasicShape<?>> shape, Property<Integer> color) {
        background = new Background(background, shape.fillColor(color.getValue()));
        return this;
    }

    public ContentBuilder background(BasicShape<? extends BasicShape<?>> shape, Property<Integer> color1,
            Property<Integer> color2, Property<Boolean> colorOrder) {
        if (color1 == color2) {
            background = new Background(background, shape.fillColor(color1.getValue()));
        } else if (colorOrder.getValue()) {
            background = new GradientBackground(background, shape, color1.getValue(), color2.getValue());
        } else {
            background = new GradientBackground(background, shape, color2.getValue(), color1.getValue());
        }
        return this;
    }

    public ContentBuilder gradientBackground(BasicShape<?> shape, int color1, int color2) {
        background = new GradientBackground(background, shape, color1, color2);
        return this;
    }

    public ContentBuilder gradientBackground(BasicShape<?> shape, Property<Integer> color1, Property<Integer> color2) {
        background = new GradientBackground(background, shape, color1.getValue(), color2.getValue());
        return this;
    }

    public ContentBuilder gradientBackgroundVertical(BasicShape<?> shape, int color1, int color2) {
        background = new GradientBackground(background, shape, color1, color2, false);
        return this;
    }

    public ContentBuilder gradientBackgroundVertical(BasicShape<?> shape, Property<Integer> color1,
            Property<Integer> color2) {
        background = new GradientBackground(background, shape, color1.getValue(), color2.getValue(), false);
        return this;
    }

    public ContentBuilder gradientsBackground(BasicShape<?> shape, boolean horizontal, int... colors) {
        background = new GradientsBackground(background, shape, horizontal, colors);
        return this;
    }

    public ContentBuilder gradientsBackground(BasicShape<?> shape, boolean horizontal, int[] colors,
            float[] positions) {
        background = new GradientsBackground(background, shape, horizontal, colors, positions);
        return this;
    }

    public ContentBuilder gradientsBackgroundVertical(BasicShape<?> shape, int... colors) {
        background = new GradientsBackground(background, shape, false, colors);
        return this;
    }

    public ContentBuilder gradientsBackgroundVertical(BasicShape<?> shape, int[] colors, float[] positions) {
        background = new GradientsBackground(background, shape, false, colors, positions);
        return this;
    }

    public ContentBuilder content(Content content) {
        this.content = content;
        return this;
    }

    public ContentBuilder content(TextContent content, Property<Integer> color) {
        content.setColor(color);
        this.content = content;
        return this;
    }

    public ContentBuilder content(Content content, Property<Integer> color) {
        return content instanceof TextContent ? content((TextContent) content, color) : content(content);
    }

    public ContentBuilder iconContent(Property<Icon> icon, FontSize fontSize) {
        this.content = icon.getValue().getContent(fontSize);
        return this;
    }

    public ContentBuilder iconContent(Property<Icon> icon, Property<Font> font) {
        return iconContent(icon, fontManager().getFontSize(font.getValue()));
    }

    public ContentBuilder iconContent(Property<Icon> icon, FontSize fontSize, int color) {
        this.content = icon.getValue().getContent(fontSize, color);
        return this;
    }

    public ContentBuilder iconContent(Property<Icon> icon, Property<Font> font, Property<Integer> color) {
        return iconContent(icon, fontManager().getFontSize(font.getValue()), color.getValue());
    }

    public ContentBuilder basicShapeContent(BasicShape<?> shape, Dimension size) {
        content = new BasicShapeContent(size, shape);
        return this;
    }

    public ContentBuilder fixShapeContent(Shape<?> shape, Dimension size) {
        content = new FixShapeContent(size, shape);
        return this;
    }

    public ContentBuilder fixedSizeContent(Dimension size) {
        content = new FixedSizeContent(content, size);
        return this;
    }

    public ContentBuilder colorContent(Dimension size) {
        content = new ColorContent(size, background);
        return this;
    }

    public ContentBuilder imageContent(Image image) {
        content = new ImageContent(image);
        return this;
    }

    public ContentBuilder measureContent() {
        content = measureContent = new MeasureContent(content);
        return this;
    }

    public Dimension getMeasuredSize() {
        return measureContent.getMeasuredSize();
    }

    public ScalableContent addScalableContent(int color, int thickness) {
        ScalableContent scalableContent = new ScalableContent(color, thickness);
        if (content instanceof Contents) {
            ((Contents) content).addContent(scalableContent);
        } else {
            content = new Contents(content).addContent(scalableContent);
        }
        return scalableContent;
    }

    public ContentBuilder addOverContent(Content content) {
        if (this.content instanceof Contents) {
            ((Contents) this.content).addContent(content);
        } else {
            this.content = new Contents(this.content).addContent(content);
        }
        return this;
    }

    public ContentBuilder addOverContent(Content content, float topPadding, float rightPadding, float bottomPadding,
            float leftPadding) {
        content = new PaddedContent(content, topPadding, rightPadding, bottomPadding, leftPadding);
        if (this.content instanceof Contents) {
            ((Contents) this.content).addContent(content);
        } else {
            this.content = new Contents(this.content).addContent(content);
        }
        return this;
    }

    public ContentBuilder paddedContent(float topPadding, float rightPadding, float bottomPadding, float leftPadding) {
        content = new PaddedContent(content, topPadding, rightPadding, bottomPadding, leftPadding);
        return this;
    }

    public ContentBuilder paddedContent(float padding) {
        content = new PaddedContent(content, padding, padding, padding, padding);
        return this;
    }

    public ContentBuilder topPaddedContent(float padding) {
        content = new PaddedContent(content, padding, 0, 0, 0);
        return this;
    }

    public ContentBuilder rightPaddedContent(float padding) {
        content = new PaddedContent(content, 0, padding, 0, 0);
        return this;
    }

    public ContentBuilder bottomPaddedContent(float padding) {
        content = new PaddedContent(content, 0, 0, padding, 0);
        return this;
    }

    public ContentBuilder leftPaddedContent(float padding) {
        content = new PaddedContent(content, 0, 0, 0, padding);
        return this;
    }

    public ContentBuilder alignTop() {
        content = new PaddedContent(content, 0, 1, 1, 1).withoutClipping();
        return this;
    }

    public ContentBuilder alignRight() {
        content = new PaddedContent(content, 1, 0, 1, 1).withoutClipping();
        return this;
    }

    public ContentBuilder alignBottom() {
        content = new PaddedContent(content, 1, 1, 0, 1).withoutClipping();
        return this;
    }

    public ContentBuilder alignLeft() {
        content = new PaddedContent(content, 1, 1, 1, 0).withoutClipping();
        return this;
    }

    public ContentBuilder center() {
        content = new PaddedContent(content, 1, 1, 1, 1).withoutClipping();
        return this;
    }

    public ContentBuilder centerHorizontally() {
        content = new PaddedContent(content, 0, 1, 0, 1).withoutClipping();
        return this;
    }

    public ContentBuilder centerVertically() {
        content = new PaddedContent(content, 1, 0, 1, 0).withoutClipping();
        return this;
    }

    // Note that emptyBorder doesn't do padding.
    public ContentBuilder paddedContentWithoutClipping(float topPadding, float rightPadding, float bottomPadding,
            float leftPadding) {
        content = new PaddedContent(content, topPadding, rightPadding, bottomPadding, leftPadding).withoutClipping();
        return this;
    }

    public ContentBuilder paddedContentWithoutClipping(float padding) {
        content = new PaddedContent(content, padding, padding, padding, padding).withoutClipping();
        return this;
    }

    public ContentBuilder contentAndBackground() {
        content = new ContentAndBackground(content, background);
        background = null;
        return this;
    }

    public ContentBuilder shapeBorder(int thickness, BasicShape<?> shape) {
        content = new ShapeBorder(content, thickness, shape);
        return this;
    }

    public ContentBuilder shapeBorderOnBackground(int thickness, BasicShape<?> shape) {
        content = new ShapeBorder(background, thickness, shape);
        background = null;
        return this;
    }

    public ContentBuilder lineBorder(int topThickness, int rightThickness, int bottomThickness, int leftThickness,
            int color) {
        content = new LineBorder(content, topThickness, rightThickness, bottomThickness, leftThickness, color);
        return this;
    }

    public ContentBuilder lineBorder(int topThickness, int rightThickness, int bottomThickness, int leftThickness,
            Property<Integer> color) {
        content = new LineBorder(content, topThickness, rightThickness, bottomThickness, leftThickness,
                color.getValue());
        return this;
    }

    public ContentBuilder emptyBorder(int topThickness, int rightThickness, int bottomThickness, int leftThickness) {
        content = new Border(content, topThickness, rightThickness, bottomThickness, leftThickness);
        return this;
    }

    public ContentBuilder horizontalContents(Content rightContent) {
        content = new HorizontalContents(content, rightContent);
        return this;
    }

    public ContentBuilder horizontalContentsLeft(Content leftContent) {
        content = new HorizontalContents(leftContent, content);
        return this;
    }

    public ContentBuilder horizontalContentsRightAligned(Content rightContent) {
        content = new HorizontalContentsRightAligned(content, rightContent);
        return this;
    }

    public ContentBuilder horizontalContentsRightAlignedLeft(Content leftContent) {
        content = new HorizontalContentsRightAligned(leftContent, content);
        return this;
    }

    public ContentBuilder verticalContents(Content bottomContent) {
        content = new VerticalContents(content, bottomContent);
        return this;
    }

    public ContentBuilder verticalContentsTop(Content topContent) {
        content = new VerticalContents(topContent, content);
        return this;
    }

    public ContentBuilder horizontalOverlap(Content otherContent, float overlapPercent, float sizePercent) {
        content = new OverlappingHorizontalContents(content, otherContent, overlapPercent, sizePercent);
        return this;
    }

    public ContentBuilder scaledContent(float scale) {
        content = new ScaledContent(content, scale);
        return this;
    }

    public ContentBuilder scaledContent(float scaleX, float scaleY) {
        content = new ScaledContent(content, scaleX, scaleY);
        return this;
    }

    public void paint(Canvas canvas, Dimension size) {
        content.paint(canvas, size);
    }

    public void paint(Canvas canvas) {
        content.paint(canvas, new Dimension(content.getPreferredSize()));
    }

    public void paintUsingWidth(Canvas canvas, float width) {
        content.paint(canvas, new Dimension(width, content.getPreferredSize().height));
    }

    public void paintUsingHeight(Canvas canvas, float height) {
        content.paint(canvas, new Dimension(content.getPreferredSize().width, height));
    }

    public void paintBackground(Canvas canvas, Dimension size) {
        background.paint(canvas, size);
    }

    public Content getContent() {
        return content;
    }

    public Background getBackground() {
        return background;
    }

    public StoneComponent asComponent() {
        return new StoneComponent(content);
    }
}

package org.nting.toolkit.component;

import static org.nting.toolkit.component.ScrollComponent.ScrollBarPolicy.ALWAYS;
import static org.nting.toolkit.component.ScrollComponent.ScrollBarPolicy.AS_NEEDED;
import static org.nting.toolkit.component.ScrollComponent.ScrollBarPolicy.NEVER;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.util.ToolkitUtils.getAllComponents;

import java.util.List;

import org.nting.data.Property;
import org.nting.toolkit.Component;
import org.nting.toolkit.animation.Animation;
import org.nting.toolkit.animation.Flicker;
import org.nting.toolkit.animation.Tween;
import org.nting.toolkit.event.KeyEvent;
import org.nting.toolkit.event.KeyListener;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.event.MouseMotionEvent;
import org.nting.toolkit.layout.FormLayout;
import org.nting.toolkit.layout.LayoutManager;

import playn.core.Canvas;
import playn.core.Font;
import playn.core.Key;
import pythagoras.f.Dimension;
import pythagoras.f.MathUtil;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;

public class ScrollPane extends ScrollComponent {

    public final Property<ScrollBarPolicy> vsbPolicy = createProperty("vsbPolicy", null);
    public final Property<ScrollBarPolicy> hsbPolicy = createProperty("hsbPolicy", null);
    public final Property<Integer> gridSize = createProperty("gridSize", 1);
    public final Property<Integer> scrollBarWidth = createProperty("scrollBarWidth", 10);
    public final Property<Boolean> usePaddingForAntialias = createProperty("usePaddingForAntialias", true);

    private final Property<Boolean> vsbVisible = createProperty("vsbVisible", false);
    private final Property<Boolean> hsbVisible = createProperty("hsbVisible", false);

    private final Point viewPosition = new Point(0, 0);
    private Dimension viewPrefSize = new Dimension(0, 0);

    // +1/-1: vSlider/hSlider
    private final Property<Integer> hoverIndex = createProperty("hoverIndex", 0);
    private final Animation vsbVisibleAnimation;
    private final Animation hsbVisibleAnimation;

    public ScrollPane() {
        this(null);
    }

    public ScrollPane(Component view) {
        this(view, AS_NEEDED, AS_NEEDED);
    }

    public ScrollPane(Component view, ScrollBarPolicy vsbPolicy, ScrollBarPolicy hsbPolicy) {
        this(view, vsbPolicy, hsbPolicy, true);
    }

    public ScrollPane(Component view, ScrollBarPolicy vsbPolicy, ScrollBarPolicy hsbPolicy,
            boolean usePaddingForAntialias) {
        super("vsbVisible", "hsbVisible");
        this.vsbPolicy.setValue(vsbPolicy);
        this.hsbPolicy.setValue(hsbPolicy);
        this.usePaddingForAntialias.setValue(usePaddingForAntialias);

        vsbVisibleAnimation = new Tween<>(vsbVisible, true, false, 6000);
        vsbVisibleAnimation.fastForward();
        hsbVisibleAnimation = new Tween<>(hsbVisible, true, false, 6000);
        hsbVisibleAnimation.fastForward();
        rewindScrollBarVisibilityAnimations();

        setView(view);
        setLayoutManager(new ScrollLayout());
        addMouseListener(new MouseHandler(font));
        addKeyListener(new KeyHandler());
        setFocusable(false);
    }

    @Override
    public boolean isDirty() {
        if (!super.isDirty() && (vsbVisible.getValue() || hsbVisible.getValue())) {
            Component view = getView();
            if (view != null && getAllComponents(view).stream().anyMatch(Component::isDirty)) {
                repaint(); // Repaint the whole ScrollPane if any child component is dirty when showing scrollbars.
            }
        }

        return super.isDirty();
    }

    @Override
    public void doPaintChildren(Canvas canvas) {
        canvas.save();
        canvas.clipRect(0, 0, width.getValue(), height.getValue());
        super.doPaintChildren(canvas);
        canvas.restore();
    }

    public void setView(Component view) {
        removeAllComponents();
        if (view != null) {
            if (usePaddingForAntialias.getValue()) { // Make some place for the antialiasing!
                Panel panel = new Panel(new FormLayout("1px, pref:grow, 1px", "1px, pref:grow, 1px"));
                panel.addComponent(view, xy(1, 1));
                view = panel;
            }
            addComponent(view);
        }
    }

    @Override
    public Component getView() {
        List<Component> components = getComponents();
        if (components.size() > 0) {
            return components.get(0);
        } else {
            return null;
        }

    }

    public Dimension getViewSize() {
        Dimension viewSize = new Dimension(getSize());
        if (isVerticalScrollSupported()) {
            viewSize.width -= scrollBarWidth.getValue();
        }
        if (isHorizontalScrollSupported()) {
            viewSize.height -= scrollBarWidth.getValue();
        }
        return viewSize;
    }

    // When scrolling we will 'snap to grid'.
    public void setGridSize(float gridSize) {
        this.gridSize.setValue((int) gridSize);
    }

    @Override
    public void setViewPosition(Point p) {
        if (p != viewPosition) {
            rewindScrollBarVisibilityAnimations();
        }
        Component view = getView();
        if (view != null) {
            float x = Math.max(0, p.x);
            float y = Math.max(0, p.y);

            float widthValue = width.getValue();
            float heightValue = height.getValue();
            if (x + widthValue > viewPrefSize.width) {
                x = Math.max(0, viewPrefSize.width - widthValue);
            }
            if (y + heightValue > viewPrefSize.height) {
                y = Math.max(0, viewPrefSize.height - heightValue);
            }

            viewPosition.set(x, y);
            view.setPosition(-x, -y);
            repaint();
        }
    }

    public Point getViewPosition() {
        return viewPosition;
    }

    @Override
    protected Dimension getViewPrefSize() {
        return viewPrefSize;
    }

    private boolean isVerticalScrollSupported() {
        return vsbPolicy.getValue() != NEVER && height.getValue() < viewPrefSize.height;
    }

    private boolean isHorizontalScrollSupported() {
        return hsbPolicy.getValue() != NEVER && width.getValue() < viewPrefSize.width;
    }

    public void scroll(float velocity) {
        if (isVerticalScrollSupported()) {
            int y = (int) (viewPosition.y + gridSize.getValue() * velocity);
            setViewPosition(new Point(viewPosition.x, y));
        } else if (isHorizontalScrollSupported()) {
            int x = (int) (viewPosition.x + gridSize.getValue() * velocity);
            setViewPosition(new Point(x, viewPosition.y));
        }
    }

    public void scrollVertical(float velocity) {
        if (isVerticalScrollSupported()) {
            int y = (int) (viewPosition.y + gridSize.getValue() * velocity);
            setViewPosition(new Point(viewPosition.x, y));
        }
    }

    public void scrollHorizontal(float velocity) {
        if (isHorizontalScrollSupported()) {
            int x = (int) (viewPosition.x + gridSize.getValue() * velocity);
            setViewPosition(new Point(x, viewPosition.y));
        }
    }

    public void scrollPage(boolean down) {
        scrollPage(down, isVerticalScrollSupported());
    }

    public void scrollPage(boolean down, boolean vertical) {
        int sign = down ? 1 : -1;
        if (vertical && isVerticalScrollSupported()) {
            int velocity = MathUtil.ifloor(height.getValue() / gridSize.getValue()) * sign;
            int y = (int) (viewPosition.y + gridSize.getValue() * velocity);
            setViewPosition(new Point(viewPosition.x, y));
        } else if (!vertical && isHorizontalScrollSupported()) {
            int velocity = MathUtil.ifloor(width.getValue() / gridSize.getValue()) * sign;
            int x = (int) (viewPosition.x + gridSize.getValue() * velocity);
            setViewPosition(new Point(x, viewPosition.y));
        }
    }

    @Override
    public void scrollRectToVisible(Rectangle contentRect) {
        if (isVerticalScrollSupported()) {
            float heightValue = height.getValue();
            if (heightValue < contentRect.height) {
                if (viewPosition.y < contentRect.y) {
                    setViewPosition(new Point(viewPosition.x, contentRect.y));
                } else {
                    if (contentRect.y + contentRect.height < viewPosition.y + heightValue) {
                        setViewPosition(new Point(viewPosition.x, contentRect.y + contentRect.height - heightValue));
                    }
                }
            } else if (contentRect.y < viewPosition.y) {
                setViewPosition(new Point(viewPosition.x, contentRect.y));
            } else if (viewPosition.y + heightValue < contentRect.y + contentRect.height) {
                setViewPosition(new Point(viewPosition.x, contentRect.y + contentRect.height - heightValue));
            }
        }
        if (isHorizontalScrollSupported()) {
            float widthValue = width.getValue();
            if (widthValue < contentRect.width) {
                if (viewPosition.x < contentRect.x) {
                    setViewPosition(new Point(contentRect.x, viewPosition.y));
                } else {
                    if (contentRect.x + contentRect.width < viewPosition.x + widthValue) {
                        setViewPosition(new Point(contentRect.x + contentRect.width - widthValue, viewPosition.y));
                    }
                }
            } else if (contentRect.x < viewPosition.x) {
                setViewPosition(new Point(contentRect.x, viewPosition.y));
            } else if (viewPosition.x + widthValue < contentRect.x + contentRect.width) {
                setViewPosition(new Point(contentRect.x + contentRect.width - widthValue, viewPosition.y));
            }
        }
    }

    public boolean isVsbVisible() {
        return vsbVisible.getValue();
    }

    public boolean isHsbVisible() {
        return hsbVisible.getValue();
    }

    public float vScrollBarHeight(Dimension size) {
        float scrollBarWidth = this.scrollBarWidth.getValue();
        return isHorizontalScrollSupported() ? size.height - scrollBarWidth : size.height;
    }

    public int vSliderPosition(float height) {
        return MathUtil.ifloor(getViewPosition().y * height / viewPrefSize.height);
    }

    public int vSliderHeight(float height) {
        return MathUtil.round(this.height.getValue() * height / viewPrefSize.height);
    }

    public float hScrollBarWidth(Dimension size) {
        float scrollBarWidth = this.scrollBarWidth.getValue();
        return isVerticalScrollSupported() ? size.width - scrollBarWidth : size.width;
    }

    public int hSliderPosition(float width) {
        return MathUtil.ifloor(getViewPosition().x * width / viewPrefSize.width);
    }

    public int hSliderWidth(float width) {
        return MathUtil.ifloor(this.width.getValue() * width / viewPrefSize.width);
    }

    public boolean vSliderHovered() {
        return hoverIndex.getValue() == +1;
    }

    public boolean hSliderHovered() {
        return hoverIndex.getValue() == -1;
    }

    private void rewindScrollBarVisibilityAnimations() {
        if (isVerticalScrollSupported()) {
            if (vsbVisibleAnimation.isFinished() && vsbPolicy.valueEquals(AS_NEEDED)) {
                addBehavior(vsbVisibleAnimation);
            }
            vsbVisibleAnimation.rewind();
        }
        if (isHorizontalScrollSupported()) {
            if (hsbVisibleAnimation.isFinished() && hsbPolicy.valueEquals(AS_NEEDED)) {
                addBehavior(hsbVisibleAnimation);
            }
            hsbVisibleAnimation.rewind();
        }
    }

    // Always visible scrollbars should appear or disappear on layout change as well.
    private void updateVisibilityOfAlwaysVisibleScrollbars() {
        if (vsbPolicy.valueEquals(ALWAYS)) {
            vsbVisible.setValue(height.getValue() < viewPrefSize.height);
        }
        if (hsbPolicy.valueEquals(ALWAYS)) {
            hsbVisible.setValue(width.getValue() < viewPrefSize.width);
        }
    }

    private static class ScrollLayout implements LayoutManager {

        @Override
        public void layout(Component component) {
            ScrollPane scrollPane = (ScrollPane) component;

            Component view = scrollPane.getView();
            if (view != null) {
                scrollPane.viewPrefSize = view.getPreferredSize();
                // Layout should fix the view position (sizes might have changed)!
                scrollPane.setViewPosition(scrollPane.viewPosition.addLocal(-scrollPane.overdriveX.getValue(),
                        -scrollPane.overdriveY.getValue()));

                Point position = scrollPane.viewPosition;
                setComponentPosition(view, -position.x, -position.y);

                Dimension size = scrollPane.getSize();
                setComponentSize(view, size.width, size.height);

                scrollPane.updateVisibilityOfAlwaysVisibleScrollbars();
            }
        }

        @Override
        public Dimension preferredLayoutSize(Component component) {
            Dimension prefSize = new Dimension(0, 0);
            if (component.getParent() != null) {
                prefSize.width = component.getParent().getSize().width;
                prefSize.height = component.getParent().getSize().height;
            }

            ScrollPane scrollPane = (ScrollPane) component;
            if (scrollPane.hsbPolicy.getValue() == NEVER || prefSize.width == 0) {
                prefSize.width = scrollPane.getView().getPreferredSize().width;
            }
            if (scrollPane.vsbPolicy.getValue() == NEVER || prefSize.height == 0) {
                prefSize.height = scrollPane.getView().getPreferredSize().height;
            }

            return prefSize;
        }
    }

    private class MouseHandler implements MouseListener {

        private final Flicker flicker = new Flicker();

        private Point viewPosition;
        private Dimension startSize;
        private Double dragStartTime;
        private Point vScrollStartPoint;
        private Point hScrollStartPoint;
        private Point viewStartPoint;

        public MouseHandler(Property<Font> font) {
            font.addValueChangeListener(f -> flicker.minFlickDelta(f.getValue().size()));
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Dimension size = getSize();
            float scrollBarWidth = ScrollPane.this.scrollBarWidth.getValue();

            viewPosition = ScrollPane.this.viewPosition.clone();
            startSize = size.clone();
            dragStartTime = e.getTime();
            if (isVerticalScrollSupported()) {
                if (size.width - scrollBarWidth <= e.getX()) {
                    float vScrollBarHeight = vScrollBarHeight(size);
                    int vSliderPosition = vSliderPosition(vScrollBarHeight);
                    int vSliderHeight = vSliderHeight(vScrollBarHeight);
                    if (e.getY() < vSliderPosition) {
                        scrollPage(false, true);
                        e.consume();
                    } else if (vSliderPosition + vSliderHeight < e.getY() && e.getY() <= vScrollBarHeight) {
                        scrollPage(true, true);
                        e.consume();
                    } else {
                        vScrollStartPoint = new Point(e.getX(), e.getY());
                    }
                } else {
                    viewStartPoint = new Point(e.getX(), e.getY());
                }
            }

            if (isHorizontalScrollSupported() && (!isVerticalScrollSupported() || viewStartPoint != null)) {
                viewStartPoint = null;
                if (size.height - scrollBarWidth <= e.getY()) {
                    float hScrollBarWidth = hScrollBarWidth(size);
                    int hSliderPosition = hSliderPosition(hScrollBarWidth);
                    int hSliderWidth = hSliderWidth(hScrollBarWidth);
                    if (e.getX() < hSliderPosition) {
                        scrollPage(false, false);
                        e.consume();
                    } else if (hSliderPosition + hSliderWidth < e.getX() && e.getX() < hScrollBarWidth) {
                        scrollPage(true, false);
                        e.consume();
                    } else {
                        hScrollStartPoint = new Point(e.getX(), e.getY());
                    }
                } else {
                    viewStartPoint = new Point(e.getX(), e.getY());
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (viewStartPoint != null) {
                Point diff = viewStartPoint.subtract(e.getX(), e.getY(), new Point());
                float duration = (float) (e.getTime() - dragStartTime);
                float velocityX = diff.x / duration;
                float velocityY = diff.y / duration;
                if (flicker.isFlicked(diff.x, velocityX) || flicker.isFlicked(diff.y, velocityY)) {
                    flicked(diff, duration);
                    e.consume();
                }
            }

            cancelOverdrive();
            viewPosition = null;
            startSize = null;
            dragStartTime = null;
            vScrollStartPoint = null;
            hScrollStartPoint = null;
            viewStartPoint = null;
        }

        private void flicked(Point diff, float duration) {
            float translationX = Math.abs(diff.x);
            float translationY = Math.abs(diff.y);
            if (translationX <= translationY && isVerticalScrollSupported()) {
                float velocity = diff.y / duration;
                final Point originalViewPosition = ScrollPane.this.viewPosition.clone();

                flicker.flick(velocity,
                        event -> setViewPosition(originalViewPosition.add(0, MathUtil.round(event.getValue()))))
                        .start(ScrollPane.this, true);
            } else if (translationY < translationX && isHorizontalScrollSupported()) {
                float velocity = diff.x / duration;
                final Point originalViewPosition = ScrollPane.this.viewPosition.clone();

                flicker.flick(velocity,
                        event -> setViewPosition(originalViewPosition.add(MathUtil.round(event.getValue()), 0)))
                        .start(ScrollPane.this, true);
            }
        }

        @Override
        public void mouseWheelScroll(MouseEvent e) {
            scroll(e.getVelocity());
        }

        @Override
        public void mouseDragged(MouseMotionEvent mouseEvent) {
            if (vScrollStartPoint != null) {// NOTE: small calculation issue when horizontal scrollbar is also visible
                float y = (mouseEvent.getY() - vScrollStartPoint.y) * viewPrefSize.height / getSize().height;
                setViewPosition(new Point(viewPosition.x, viewPosition.y + y));
                mouseEvent.consume();
            } else if (hScrollStartPoint != null) {
                // NOTE: small calculation issue when vertical scrollbar is also visible
                float x = (mouseEvent.getX() - hScrollStartPoint.x) * viewPrefSize.width / getSize().width;
                setViewPosition(new Point(viewPosition.x + x, viewPosition.y));
                mouseEvent.consume();
            } else if (viewStartPoint != null) {
                float x = (mouseEvent.getX() - viewStartPoint.x);
                float y = (mouseEvent.getY() - viewStartPoint.y);
                setViewPosition(new Point(viewPosition.x - x, viewPosition.y - y));
                checkOverdrive(viewPosition, startSize, x, y);
                mouseEvent.consume();
            }
        }

        @Override
        public void mouseMoved(MouseMotionEvent e) {
            rewindScrollBarVisibilityAnimations();
            // set hover index
            if (vsbVisible.getValue()) {
                Dimension size = getSize();
                float scrollBarWidth = ScrollPane.this.scrollBarWidth.getValue();
                if (size.width - scrollBarWidth <= e.getX()) {
                    float vScrollBarHeight = vScrollBarHeight(size);
                    int vSliderPosition = vSliderPosition(vScrollBarHeight);
                    int vSliderHeight = vSliderHeight(vScrollBarHeight);
                    if (vSliderPosition <= e.getY() && e.getY() < vSliderPosition + vSliderHeight) {
                        hoverIndex.setValue(1);
                        return;
                    }
                }
            }
            if (hsbVisible.getValue()) {
                Dimension size = getSize();
                float scrollBarWidth = ScrollPane.this.scrollBarWidth.getValue();
                if (size.height - scrollBarWidth <= e.getY()) {
                    float hScrollBarWidth = hScrollBarWidth(size);
                    int hSliderPosition = hSliderPosition(hScrollBarWidth);
                    int hSliderWidth = hSliderWidth(hScrollBarWidth);
                    if (hSliderPosition <= e.getX() && e.getX() < hSliderPosition + hSliderWidth) {
                        hoverIndex.setValue(-1);
                        return;
                    }
                }
            }
            hoverIndex.setValue(0);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            rewindScrollBarVisibilityAnimations();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            hoverIndex.setValue(0);
        }
    }

    private class KeyHandler implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.isKeyCode(Key.PAGE_DOWN)) {
                scrollPage(true);
            } else if (e.isKeyCode(Key.PAGE_UP)) {
                scrollPage(false);
            } else if (e.isKeyCode(Key.UP)) {
                scroll(-1);
            } else if (e.isKeyCode(Key.DOWN)) {
                scroll(1);
            }
        }
    }
}

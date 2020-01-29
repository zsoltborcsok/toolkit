package org.nting.toolkit.internal;

import static org.nting.toolkit.ToolkitServices.toolkitManager;

import org.nting.toolkit.Component;
import org.nting.toolkit.component.TooltipPopup;
import org.nting.toolkit.event.KeyEvent;
import org.nting.toolkit.event.KeyListener;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.event.MouseMotionEvent;
import org.nting.toolkit.util.ToolkitUtils;

import playn.core.Key;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;

public class TooltipManager {

    private static final int INITIAL_DELAY = 750;
    private static final int DISMISS_DELAY = 4000;

    private final MouseListener mouseHandler = new MouseHandler();
    private final KeyListener keyHandler = new KeyHandler();

    private Component tooltipComponent = null;
    private Point tooltipLocation = null;
    private float initialTime = INITIAL_DELAY;
    private float dismissTime = DISMISS_DELAY;
    private TooltipPopup tooltipPopup;

    public void registerRoot(Root root) {
        root.addKeyListener(keyHandler);
    }

    public void registerComponent(Component component) {
        component.addMouseListener(mouseHandler);
    }

    public void unRegisterComponent(Component component) {
        component.addMouseListener(mouseHandler).remove();
    }

    public void update(float delta) {
        if (tooltipComponent != null) {
            if (0 < initialTime) {
                initialTime -= delta;
                if (initialTime <= 0) {
                    showToolTip();
                }
            } else {
                if (0 < dismissTime) {
                    dismissTime -= delta;
                    if (dismissTime < 0) {
                        closeToolTip();
                    }
                }
            }
        }
    }

    private void showToolTip() {
        // TODO
        // String tooltipText = tooltipComponent.getTooltipText();
        // if (!Strings.isNullOrEmpty(tooltipText)) {
        // showToolTip(tooltipText, tooltipComponent);
        // } else {
        // Pair<String, Rectangle> advancedTooltip = tooltipComponent.getAdvancedTooltip(tooltipLocation);
        // if (advancedTooltip != null) {
        // tooltipText = advancedTooltip.getFirst();
        // Rectangle rectangle = advancedTooltip.getSecond();
        // Point rootPosition = ToolkitUtils.getRootPosition(tooltipComponent);
        // rectangle.translate(rootPosition.x, rootPosition.y);
        //
        // if (tooltipText.contains("\n")) {
        // showToolTip(tooltipText, rectangle, true);
        // } else {
        // float tooltipMaxWidth = getTooltipMaxWidth(rectangle);
        // float prefWidth = new LabelBuilder().text(tooltipText).component.getPreferredSize().width;
        // showToolTip(tooltipText, rectangle, tooltipMaxWidth <= prefWidth);
        // }
        // }
        // }
        //
        // dismissTime = DISMISS_DELAY;
    }

    private void showToolTip(String tooltipText, Component component) {
        Rectangle rectangle = new Rectangle(ToolkitUtils.getRootPosition(component), component.getSize());
        showToolTip(tooltipText, rectangle, tooltipText.contains("\n"));
    }

    private void showToolTip(String tooltipText, Rectangle rectangle, boolean multiLine) {
        // TODO
        // if (!multiLine) {
        // ContainerBuilder<TooltipPopup> tooltipPopupBuilder = new ContainerBuilder<>(new TooltipPopup(
        // tooltipComponent.getTooltipLocation(), new FormLayout("2dlu, pref, 2dlu", "1dlu, pref, 1dlu")));
        // tooltipPopupBuilder.addLabel(xy(1, 1)).text(tooltipText).color(tooltipPopupBuilder.component.color);
        // tooltipPopup = tooltipPopupBuilder.component;
        // tooltipPopup.showRelativeTo(rectangle);
        // } else {
        // int tooltipMaxWidth = MathUtil.ifloor(getTooltipMaxWidth(rectangle))
        // - unitConverter().dialogUnitXAsPixel(4, null) - 1;
        // ContainerBuilder<TooltipPopup> tooltipPopupBuilder = new ContainerBuilder<>(
        // new TooltipPopup(tooltipComponent.getTooltipLocation(),
        // new FormLayout("2dlu, " + tooltipMaxWidth + "px, 2dlu", "2dlu, min(pref;80dlu), 2dlu")));
        // // In order to have proper preferred size for the popup, we need to set the MultiLineLabel width in advance!
        // tooltipPopupBuilder.addMultiLineLabel(xy(1, 1)).text(tooltipText)
        // .color(tooltipPopupBuilder.component.color).component.width.setValue((float) tooltipMaxWidth);
        // tooltipPopup = tooltipPopupBuilder.component;
        // tooltipPopup.showRelativeTo(rectangle);
        // }
    }

    private void closeToolTip() {
        if (tooltipPopup != null) {
            tooltipPopup.close();
            tooltipPopup = null;
        }

        tooltipComponent = null;
    }

    private void start(Component component, Point location) {
        tooltipComponent = component;
        tooltipLocation = location;
        initialTime = INITIAL_DELAY;
    }

    private void restart(Component component, Point location) {
        start(component, location);
        if (tooltipPopup != null) {
            tooltipPopup.close();
            tooltipPopup = null;
        }
    }

    private void stop() {
        closeToolTip();
        tooltipComponent = null;
    }

    private float getTooltipMaxWidth(Rectangle rectangle) {
        float width = toolkitManager().root().width.getValue();
        return Math.max(width - rectangle.x, rectangle.maxX());
    }

    private class KeyHandler implements KeyListener {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.isKeyCode(Key.ESCAPE)) {
                stop();
            }
        }
    }

    private class MouseHandler implements MouseListener {

        @Override
        public void mouseEntered(MouseEvent e) {
            start(e.getSource(), new Point(e.getX(), e.getY()));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            stop();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            stop();
        }

        @Override
        public void mouseMoved(MouseMotionEvent e) {
            restart(e.getSource(), new Point(e.getX(), e.getY()));
        }

        @Override
        public void mouseDragged(MouseMotionEvent e) {
            stop();
        }
    }
}
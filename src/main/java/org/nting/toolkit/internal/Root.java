package org.nting.toolkit.internal;

import static org.nting.toolkit.ToolkitRunnable.createLoopedRunnable;
import static org.nting.toolkit.ToolkitServices.notifications;
import static org.nting.toolkit.ToolkitServices.toolkitManager;

import java.util.List;

import org.nting.toolkit.Component;
import org.nting.toolkit.Notifications;
import org.nting.toolkit.component.Dialog;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.Popup;
import org.nting.toolkit.component.ScrollComponent;
import org.nting.toolkit.component.TooltipPopup;
import org.nting.toolkit.util.ToolkitUtils;

import com.google.common.collect.Lists;

import playn.core.Canvas;
import pythagoras.f.Dimension;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;

public final class Root extends Panel {

    private final List<Popup> popups = Lists.newArrayList();// popups in reverse order of paint sequence
    private boolean duringLayout = false;
    private boolean canvasDirty = false;
    private boolean modalityPainted = true;

    public Root(float width, float height) {
        setSize(width, height);

        attached.setValue(true);
        if (renderingOptimisation()) {
            toolkitManager().schedule(createLoopedRunnable(-1, 100, this::repaint));
        }
        notifications().setParent(this);
    }

    public boolean isCanvasDirty() {
        return canvasDirty;
    }

    @Override
    public List<Component> getComponents() {
        if (duringLayout) {
            return super.getComponents();
        } else {
            List<Component> components = Lists.<Component> newArrayList(popups);
            components.add(notifications());
            components.addAll(super.getComponents());
            return components;
        }
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        // Keep the popups instead; otherwise closePopups();
    }

    public void setPopupVisible(Popup popup, boolean visible) {
        if (visible && popups.contains(popup)) {
            int index = popups.indexOf(popup);
            for (int i = 0; i < index; i++) {
                Popup removedPopup = popups.remove(0);
                removedPopup.closed();
            }
        } else if (visible) {
            popups.add(0, popup);
            popup.setParent(this);
            // If we open a normal popup, then close the previous not dialog popup(s).
            if (!(popup instanceof TooltipPopup)) {
                while (2 <= popups.size()) {
                    Popup aPopup = popups.get(1);
                    if (!(aPopup instanceof Dialog)) {
                        popups.remove(aPopup);
                        aPopup.closed();
                    } else {
                        break;
                    }
                }
            } else { // If we open a tooltip, then close only the previous tooltip
                if (2 <= popups.size()) {
                    Popup aPopup = popups.get(1);
                    if (aPopup instanceof TooltipPopup) {
                        popups.remove(aPopup);
                        aPopup.closed();
                    }
                }
            }
            // If we open a dialog then it is a whole new layer, which 'grabs' the mouse.
            if (popup instanceof Dialog) {
                toolkitManager().mouseDispatcher().onDialogOpen();
            }
        } else {
            int index = popups.indexOf(popup);
            for (int i = 0; i <= index; i++) {
                Popup removedPopup = popups.remove(0);
                removedPopup.closed();
            }
        }
        repaint();
    }

    public void closePopups() {
        boolean needsRepaint = 0 < popups.size();

        while (popups.size() > 0) {
            Popup removedPopup = popups.remove(0);
            removedPopup.closed();
        }

        if (needsRepaint) {
            repaint();
        }
    }

    public void updatePopups(Component component) {
        while (component != null) {
            if (component == this) {
                closePopups();
                break;
            } else if (component instanceof Popup) {
                setPopupVisible((Popup) component, true);
                break;
            }

            component = component.getParent();
        }
    }

    public List<Popup> popups() {
        return popups;
    }

    @Override
    public void doLayout() {
        duringLayout = true;
        super.doLayout();
        duringLayout = false;
        modalityPainted = false;
    }

    @Override
    public void doPaintChildren(Canvas canvas) {
        canvasDirty = false;

        if (renderingOptimisation() && !isDirty()) { // Paint background where repaint will happen.
            List<Component> components = Lists.newLinkedList();
            List<Rectangle> clips = Lists.newLinkedList();
            components.add(this);
            clips.add(null);
            boolean hasPopups = 0 < popups.size() || notifications().isVisible();
            for (int i = 0; i < components.size(); i++) {
                Component component = components.get(i);
                if (!component.isVisible()) {
                    continue;
                }

                if (hasPopups && component.isDirty() && component != this) {
                    // It's needed to repaint the whole root for properly paint the popups by doPaintForeground(...)
                    toolkitManager().invokeAfterRepaint(new Runnable() {
                        @Override
                        public void run() {
                            repaint();
                        }
                    });
                    break;
                } else if (component.isDirty()) {
                    canvas.setFillColor(component.getBackgroundColor());

                    Point position = ToolkitUtils.getRootPosition(component);
                    Dimension size = component.getSize();
                    if (component.getParent() instanceof ScrollComponent) {
                        Point componentPosition = component.getPosition();
                        size.width -= componentPosition.x;
                        size.height -= componentPosition.y;
                    }
                    Rectangle currentClip = clips.get(i);
                    if (currentClip == null) {
                        canvas.fillRect(position.x, position.y, size.width, size.height);
                    } else {
                        Rectangle clip = currentClip.intersection(position.x, position.y, size.width, size.height);
                        if (0 < clip.width && 0 < clip.height) {
                            canvas.fillRect(clip.x, clip.y, clip.width, clip.height);
                        }
                    }
                    canvasDirty = true;
                } else {
                    List<Component> children = component.getComponents();
                    components.addAll(i + 1, children);
                    Rectangle currentClip = clips.get(i);
                    if (component.isClip() || component instanceof ScrollComponent) {
                        Point position = ToolkitUtils.getRootPosition(component);
                        Dimension size = component.getSize();
                        Rectangle newClip = new Rectangle(position.x, position.y, size.width, size.height);
                        if (currentClip == null) {
                            currentClip = newClip;
                        } else {
                            currentClip = currentClip.intersection(newClip);
                        }
                    }
                    for (int j = 1; j <= children.size(); j++) {
                        clips.add(i + j, currentClip);
                    }
                }
            }
        } else {
            canvasDirty = true;
        }

        super.doPaintChildren(canvas);
    }

    public void doPaintForeground(Canvas canvas) {
        Notifications notifications = notifications();
        if (notifications.isVisible()) {
            notifications.doLayout();
            notifications.repaint();
            doPaintChild(notifications, canvas);
        }
        for (int i = popups.size() - 1; i >= 0; i--) {
            Popup popup = popups.get(i);
            popup.repaint();
            if (!modalityPainted) { // paint modality curtain for dialogs
                if (popup instanceof Dialog) {
                    Dialog dialog = (Dialog) popup;
                    // new Background(new RectangleShape().fillColor(dialog.modalityCurtain.getValue())).paint(canvas,
                    // dialog.getSize()); TODO
                }

                int shadowSize = popup.shadowSize.getValue();
                if (0 < shadowSize) {
                    Point position = new Point(popup.x.getValue(), popup.y.getValue());
                    Dimension size = new Dimension(popup.width.getValue(), popup.height.getValue());

                    canvas.translate(position.x, position.y);
                    // TODO MaterialShadows.paintShadow(shadowSize, canvas, size);
                    canvas.translate(-position.x, -position.y);
                }
            }

            doPaintChild(popup, canvas);
        }
        modalityPainted = true;
    }

    public void update(float delta) {
        super.update(delta);

        for (int i = popups.size() - 1; i >= 0; i--) {
            popups.get(i).update(delta);
        }
        notifications().update(delta);
    }
}

package org.nting.toolkit.internal;

import static org.nting.toolkit.ToolkitServices.toolkitManager;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.layout.PivotAnimationLayout.pivotLayout;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.nting.data.Property;
import org.nting.toolkit.Component;
import org.nting.toolkit.Notifications;
import org.nting.toolkit.ToolkitRunnable;
import org.nting.toolkit.animation.Easing;
import org.nting.toolkit.component.AbstractComponent;
import org.nting.toolkit.component.Button;
import org.nting.toolkit.component.FontIcon;
import org.nting.toolkit.component.Icon;
import org.nting.toolkit.component.Label;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.SimpleIconComponent;
import org.nting.toolkit.event.ActionListener;
import org.nting.toolkit.layout.FormLayout;
import org.nting.toolkit.layout.PivotAnimationLayout;
import org.nting.toolkit.ui.shape.RectangleShape;
import org.nting.toolkit.util.MaterialShadows;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;

import playn.core.Canvas;
import playn.core.PlayN;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

public class NotificationsImpl extends AbstractComponent implements Notifications {

    public final Property<Integer> successColor = createProperty("successColor", 0xffffffff);
    public final Property<Integer> successBackground = createProperty("successBackground", 0xff2aaba0);
    public final Property<Integer> successBorderColor = createProperty("successBorderColor", 0xff009788);

    public final Property<Integer> warningColor = createProperty("warningColor", 0xff545454);
    public final Property<Integer> warningBackground = createProperty("warningBackground", 0xfffff1c0);
    public final Property<Integer> warningBorderColor = createProperty("warningBorderColor", 0xfffecd00);

    public final Property<Integer> errorColor = createProperty("errorColor", 0xffffffff);
    public final Property<Integer> errorBackground = createProperty("errorBackground", 0xffff7946);
    public final Property<Integer> errorBorderColor = createProperty("errorBorderColor", 0xffff580f);

    public final Property<Integer> noticeColor = createProperty("noticeColor", 0xffffffff);
    public final Property<Integer> noticeBackground = createProperty("noticeBackground", 0xff47a9f8);
    public final Property<Integer> noticeBorderColor = createProperty("noticeBorderColor", 0xff1694f4);

    public final Property<Integer> infoColor = createProperty("infoColor", 0xffffffff);
    public final Property<Integer> infoBackground = createProperty("infoBackground", 0xff7f97a3);
    public final Property<Integer> infoBorderColor = createProperty("infoBorderColor", 0xff5f7d8c);

    // Delay the creation of the NotificationStyling-s because the current style may overwrite the property values.
    private final LoadingCache<Type, NotificationStyling> notificationStylingCache = CacheBuilder.newBuilder()
            .build(CacheLoader.from(this::createStyling));

    public NotificationsImpl() {
        setFocusable(false);
        setLayoutManager(pivotLayout(new FormLayout("pref", "pref, 1dlu, pref, 1dlu, pref")));
    }

    @Override
    public boolean isVisible() {
        return 0 < getComponents().size();
    }

    @Override
    public void doLayout() {
        Dimension parentSize = getParent().getSize();
        Point position = getPosition();
        Dimension preferredSize = getPreferredSize();
        float width = Math.min(preferredSize.width, parentSize.width - position.x);
        float height = Math.min(preferredSize.height, parentSize.height - position.y);
        setSize(width, height);

        super.doLayout();
    }

    @Override
    public void show(Type type, String message) {
        try {
            show(new Notification(notificationStylingCache.get(type), message));
        } catch (ExecutionException e) {
            PlayN.log(getClass()).warn(e.getMessage());
        }
    }

    @Override
    public void show(Type type, String message, String action, ActionListener actionListener) {
        try {
            show(new Notification(notificationStylingCache.get(type), message, action, actionListener));
        } catch (ExecutionException e) {
            PlayN.log(getClass()).warn(e.getMessage());
        }
    }

    private void show(final Notification notification) {
        List<Component> components = Lists.reverse(getComponents());
        for (int i = 0; i < components.size(); i++) {
            Notification child = (Notification) components.get(i);
            if (i < 2) {
                addComponent(child, xy(0, (i + 1) * 2));
                ((PivotAnimationLayout) getLayoutManager()).moveChildY(child, child.y.getValue(), 200,
                        Easing.REGULAR_IN);
            } else {
                removeComponent(child);
            }
        }

        addComponent(notification, xy(0, 0));
        ((PivotAnimationLayout) getLayoutManager()).translateChildX(notification,
                -notification.getPreferredSize().width, 500, Easing.REGULAR_IN);
        toolkitManager().schedule(new ToolkitRunnable(3500) {
            @Override
            public void run() {
                removeComponent(notification);
            }
        });
    }

    private NotificationStyling createStyling(Type type) {
        switch (type) {
        case SUCCESS:
            return new NotificationStyling(successColor, successBackground, successBorderColor, FontIcon.DONE);
        case WARNING:
            return new NotificationStyling(warningColor, warningBackground, warningBorderColor, FontIcon.WARNING);
        case ERROR:
            return new NotificationStyling(errorColor, errorBackground, errorBorderColor, FontIcon.EXCLAMATION_CIRCLE);
        case NOTICE:
            return new NotificationStyling(noticeColor, noticeBackground, noticeBorderColor, FontIcon.CHAT2);
        case INFO:
            return new NotificationStyling(infoColor, infoBackground, infoBorderColor, FontIcon.INFO_CIRCLE);
        }
        throw new IllegalStateException();
    }

    public static class Notification extends Panel {

        public final Property<Integer> shadowSize = createProperty("shadowSize", 0);

        private final int borderColor;

        public Notification(NotificationStyling notificationStyling, String message) {
            borderColor = notificationStyling.borderColor;

            setLayoutManager(new FormLayout("7dlu, pref, 7dlu, pref, 7dlu", "4dlu, center:pref, 4dlu"));
            backgroundColor.setValue(notificationStyling.backgroundColor);
            addComponent(new SimpleIconComponent(notificationStyling.icon).set("color", notificationStyling.color),
                    xy(1, 1));
            addComponent(new Label().set("text", message).set("color", notificationStyling.color), xy(3, 1));
        }

        public Notification(NotificationStyling notificationStyling, String message, String action,
                ActionListener actionListener) {
            borderColor = notificationStyling.borderColor;

            setLayoutManager(
                    new FormLayout("7dlu, pref, 7dlu, pref:grow, 30dlu, pref, 5dlu", "4dlu, center:pref, 4dlu"));
            backgroundColor.setValue(notificationStyling.backgroundColor);
            addComponent(new SimpleIconComponent(notificationStyling.icon).set("color", notificationStyling.color),
                    xy(1, 1));
            addComponent(new Label().set("text", message).set("color", notificationStyling.color), xy(3, 1));
            Button button = new Button().set("text", action).set("color", notificationStyling.color);
            button.actions().addActionListener(actionListener); // TODO noBorder, noBackground (as styleName)
            addComponent(button, xy(5, 1));
        }

        @Override
        public void doPaintComponent(Canvas canvas) {
            int shadowSizeValue = shadowSize.getValue();
            if (0 < shadowSizeValue) {
                MaterialShadows.paintShadow(shadowSizeValue, canvas, getSize());
            }
            super.doPaintComponent(canvas);
            if (shadowSizeValue == 0) {
                new RectangleShape().strokeColor(borderColor).size(width.getValue(), height.getValue()).paint(canvas);
            }
        }
    }

    private static class NotificationStyling {

        private final int color, backgroundColor, borderColor;
        private final Icon icon;

        private NotificationStyling(Property<Integer> color, Property<Integer> backgroundColor,
                Property<Integer> borderColor, Icon icon) {
            this.color = color.getValue();
            this.backgroundColor = backgroundColor.getValue();
            this.borderColor = borderColor.getValue();
            this.icon = icon;
        }
    }
}

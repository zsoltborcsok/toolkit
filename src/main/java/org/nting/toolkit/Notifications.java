package org.nting.toolkit;

import org.nting.toolkit.event.ActionListener;

/**
 * Notifications is a component showing toasts. Notifications are placed below(Z-axis) the popups. Max 3 notifications.
 */
public interface Notifications extends PaintableComponent {

    enum Type {
        SUCCESS, WARNING, ERROR, NOTICE, INFO
    }

    void show(Type type, String message);

    void show(Type type, String message, String actionText, ActionListener actionListener);
}

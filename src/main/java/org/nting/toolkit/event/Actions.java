package org.nting.toolkit.event;

import java.util.Map;

import org.nting.toolkit.util.SimpleMap;

import com.google.common.base.Preconditions;

/**
 * Support registering action listeners with 'actionId', which is configured to the action when firing it.
 * <p>
 * Makes it possible to register the same action listener to more places, and not the source but the actionId identifies
 * what to do with the action (i.e. how to handle it).
 */
public class Actions {

    private final SimpleMap<ActionListener, Object> actionListeners = new SimpleMap<>();

    public void addActionListener(ActionListener actionListener) {
        addActionListener(actionListener, null);
    }

    public void addActionListener(ActionListener actionListener, Object actionId) {
        Preconditions.checkNotNull(actionListener);

        actionListeners.put(actionListener, actionId);
    }

    public void removeActionListener(ActionListener actionListener) {
        actionListeners.remove(actionListener);
    }

    public void fireActionPerformed(ActionEvent actionEvent) {
        for (Map.Entry<ActionListener, Object> listener : actionListeners.entries()) {
            if (listener.getValue() != null) {
                listener.getKey().actionPerformed(actionEvent.withActionId(listener.getValue()));
            } else {
                listener.getKey().actionPerformed(actionEvent);
            }
        }
    }
}

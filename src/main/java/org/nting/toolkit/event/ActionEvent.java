package org.nting.toolkit.event;

import org.nting.toolkit.Component;

import com.google.common.base.MoreObjects;

public class ActionEvent extends InputEvent {

    private final InputEvent sourceEvent;
    private final Object actionId;

    public ActionEvent(Component source, double time) {
        this(source, time, null, null);
    }

    public ActionEvent(Component source, double time, InputEvent sourceEvent) {
        this(source, time, sourceEvent, null);
    }

    public ActionEvent(Component source, double time, InputEvent sourceEvent, Object actionId) {
        super(source, time);

        this.sourceEvent = sourceEvent;
        this.actionId = actionId;
    }

    public InputEvent getSourceEvent() {
        return sourceEvent;
    }

    public Object getActionId() {
        return actionId;
    }

    public ActionEvent withActionId(Object actionId) {
        return new ActionEvent(getSource(), getTime(), sourceEvent, actionId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("sourceEvent", sourceEvent).add("actionId", actionId)
                .add("source", getSource()).add("time", getTime()).add("consumed", isConsumed()).toString();
    }
}

package org.nting.toolkit.app.pages;

import static org.nting.toolkit.Notifications.Type.ERROR;
import static org.nting.toolkit.Notifications.Type.INFO;
import static org.nting.toolkit.Notifications.Type.NOTICE;
import static org.nting.toolkit.Notifications.Type.SUCCESS;
import static org.nting.toolkit.Notifications.Type.WARNING;
import static org.nting.toolkit.ToolkitServices.notifications;
import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.FormLayout.xy;

import org.nting.toolkit.Component;
import org.nting.toolkit.Notifications;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.event.ActionEvent;
import org.nting.toolkit.event.ActionListener;

public class NotificationsTestPage implements ITestPage, ActionListener {

    @Override
    public PageSize getPageSize() {
        return PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent() {
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder("pref",
                "pref, 4dlu, pref, 4dlu, pref, 4dlu, pref, 4dlu, pref");

        panelBuilder.addButton(xy(0, 0)).text("Show SUCCESS Notification").actionListener(this, SUCCESS);
        panelBuilder.addButton(xy(0, 2)).text("Show WARNING Notification").actionListener(this, WARNING);
        panelBuilder.addButton(xy(0, 4)).text("Show ERROR Notification").actionListener(this, ERROR);
        panelBuilder.addButton(xy(0, 6)).text("Show NOTICE Notification").actionListener(this, NOTICE);
        panelBuilder.addButton(xy(0, 8)).text("Show INFO Notification").actionListener(this, INFO);

        return wrap(panelBuilder.build());
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object actionId = actionEvent.getActionId();
        if (actionId instanceof Notifications.Type) {
            Notifications.Type type = (Notifications.Type) actionId;
            notifications().show(type, type.toString() + " Message", "ACTION", this);
        }
    }
}

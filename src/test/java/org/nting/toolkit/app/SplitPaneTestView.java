package org.nting.toolkit.app;

import org.nting.toolkit.Component;
import org.nting.toolkit.component.Label;
import org.nting.toolkit.component.Orientation;
import org.nting.toolkit.component.SplitPane;

public class SplitPaneTestView {

    public Component createPane() {
        SplitPane splitPane1 = new SplitPane(Orientation.HORIZONTAL);
        splitPane1.type.setValue(SplitPane.Type.FIXED_LEFT);
        splitPane1.resizeWeight.setValue(0.25);
        SplitPane splitPane2 = new SplitPane(Orientation.HORIZONTAL);
        splitPane2.type.setValue(SplitPane.Type.FIXED_RIGHT);
        splitPane2.resizeWeight.setValue(0.66);
        SplitPane splitPane3 = new SplitPane(Orientation.HORIZONTAL);

        splitPane1.setLeftComponent(new Label().set("text", "1"));
        splitPane1.setRightComponent(splitPane2);
        splitPane2.setLeftComponent(splitPane3);
        splitPane2.setRightComponent(new Label().set("text", "4"));
        splitPane3.setLeftComponent(new Label().set("text", "2"));
        splitPane3.setRightComponent(new Label().set("text", "3"));

        return splitPane1;
    }
}

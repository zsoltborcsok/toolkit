package org.nting.toolkit.app;

import static org.nting.toolkit.layout.FormLayout.xy;

import java.awt.Image;
import java.awt.Toolkit;

import org.nting.toolkit.Component;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.layout.AbsoluteLayout;
import org.nting.toolkit.layout.FormLayout;

import playn.core.PlayN;
import playn.swing.JavaGraphics;
import playn.swing.JavaPlatform;

public class ToolkitSampleApp {

    public static void main(String[] args) {
        JavaPlatform platform = JavaPlatform.register();
        platform.setTitle("Toolkit");
        platform.setIcon(getIconImage());
        platform.assets().setPathPrefix("org/nting/assets");
        platform.graphics().registerFont("SourceSansPro-Bold", "fonts/SourceSansPro-Bold.ttf");
        platform.graphics().registerFont("SourceSansPro-BoldItalic", "fonts/SourceSansPro-BoldItalic.ttf");
        platform.graphics().registerFont("SourceSansPro-Italic", "fonts/SourceSansPro-Italic.ttf");
        platform.graphics().registerFont("SourceSansPro-Regular", "fonts/SourceSansPro-Regular.ttf");
        platform.graphics().registerFont("IconFont", "fonts/IconFont.ttf");
        ((JavaGraphics) PlayN.graphics()).setSize(1024, 800);

        ToolkitApp.startApp().then(toolkitManager -> toolkitManager.root().addComponent(createContent(),
                AbsoluteLayout.fillParentConstraint()));
    }

    private static Image getIconImage() {
        return Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("icon.png"));
    }

    private static Component createContent() {
        Panel panel = new Panel(new FormLayout("7dlu, 0px:grow, 7dlu", "4dlu, 0px:grow, 4dlu"));
        // panel.addComponent(new ButtonTestView().createPane(), xy(1, 1));
        // panel.addComponent(new ScrollPaneTestView().createPane(), xy(1, 1));
        // panel.addComponent(new SplitPaneTestView().createPane(), xy(1, 1));
        // panel.addComponent(new PopupTestView().createPane(), xy(1, 1));
        // panel.addComponent(new DialogTestView().createPane(), xy(1, 1));
        panel.addComponent(new TextFieldTestView().createPane(), xy(1, 1));
        return panel;
    }
}

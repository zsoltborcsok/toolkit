package org.nting.toolkit.app;

import static org.nting.toolkit.FontManager.FontSize.LARGE_FONT;
import static org.nting.toolkit.ToolkitServices.fontManager;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.layout.FormLayout.xyw;
import static playn.core.Font.Style.BOLD;

import java.awt.Image;
import java.awt.Toolkit;

import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.Component;
import org.nting.toolkit.component.Button;
import org.nting.toolkit.component.Label;
import org.nting.toolkit.component.Panel;
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

        ToolkitApp.startApp(createContent());
    }

    private static Image getIconImage() {
        return Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemClassLoader().getResource("icon.png"));
    }

    private static Component createContent() {
        FormLayout formLayout = new FormLayout("7dlu, 0px:grow, 7dlu, 0px:grow, 7dlu, 0px:grow, 7dlu",
                "4dlu, pref, 4dlu, pref, 4dlu");
        Panel panel = new Panel(formLayout);
        panel.addComponent(new Label().set("text", "Buttons").set("font", fontManager().getFont(LARGE_FONT, BOLD)),
                xyw(1, 1, 5));
        panel.addComponent(new Button().set("text", "NORMAL / DISABLED").set("enabled", false), xy(1, 3));
        panel.addComponent(new Button().set("text", "NORMAL / ENABLED"), xy(3, 3));
        panel.addComponent(new Button().set("text", "NORMAL / FOCUSED")
                .process(c -> ((ObjectProperty<Boolean>) c.focused).forceValue(true)), xy(5, 3));
        return panel;
    }
}

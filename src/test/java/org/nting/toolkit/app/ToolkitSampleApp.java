package org.nting.toolkit.app;

import java.awt.Image;
import java.awt.Toolkit;

import org.nting.toolkit.Component;
import org.nting.toolkit.app.pages.TestsPage;
import org.nting.toolkit.layout.AbsoluteLayout;

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
        Pages pages = new Pages();
        pages.addPage(new TestsPage(pages).createContent(), Pages.PageSize.SINGLE_COLUMN);
        return pages;
    }
}

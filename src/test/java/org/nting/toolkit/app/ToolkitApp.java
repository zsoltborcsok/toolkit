package org.nting.toolkit.app;

import static org.nting.toolkit.FontManager.FontSize.LARGE_FONT;
import static org.nting.toolkit.ToolkitServices.fontManager;
import static org.nting.toolkit.ToolkitServices.toolkitManager;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.layout.FormLayout.xyw;
import static playn.core.Font.Style.BOLD;

import java.awt.Image;
import java.awt.Toolkit;

import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.Component;
import org.nting.toolkit.ToolkitManager;
import org.nting.toolkit.component.Button;
import org.nting.toolkit.component.Label;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.internal.Root;
import org.nting.toolkit.layout.AbsoluteLayout;
import org.nting.toolkit.layout.FormLayout;
import org.nting.toolkit.ui.style.material.MaterialStyleModule;

import playn.core.Game;
import playn.core.PlayN;
import playn.swing.JavaGraphics;
import playn.swing.JavaPlatform;
import pythagoras.f.Dimension;

public class ToolkitApp {

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
        ((JavaGraphics) PlayN.graphics()).setSize(1024, 800);// 760x460 for simulate a 10" tablet
        PlayN.run(createGame(createContent()));
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

    public static Game createGame(Component content) {
        ToolkitManager toolkitManager = toolkitManager();
        toolkitManager.addDluChangeListener((dluSizeX, dluSizeY) -> {
            Root root = toolkitManager.root();
            Dimension size = root.getSize();
            root.addComponent(content, AbsoluteLayout.xywh(0, 0, size.width, size.height));
        });
        toolkitManager.setStyleModule(new MaterialStyleModule());

        return new Game.Default(33) {
            private int paintCount = 0;
            private int time = 0;

            @Override
            public void init() {
            }

            @Override
            public boolean paint(float alpha) {
                paintCount++;

                try {
                    return toolkitManager.paint();
                } catch (RuntimeException e) {
                    PlayN.log(getClass()).error(e.getMessage(), e);
                    return true;
                }
            }

            @Override
            public void update(int delta) {
                try {
                    toolkitManager.update(delta);
                } catch (RuntimeException e) {
                    PlayN.log(getClass()).error(e.getMessage(), e);
                }

                time += delta;
                if (time > 5000) {
                    if (5 <= paintCount && paintCount < 150) {
                        PlayN.log(getClass()).info("FPS: {}", paintCount / 5);
                    }

                    paintCount = 0;
                    time -= 5000;
                }
            }
        };
    }
}

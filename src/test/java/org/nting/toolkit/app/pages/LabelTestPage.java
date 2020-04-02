package org.nting.toolkit.app.pages;

import static org.nting.toolkit.FontManager.FontSize.LARGE_FONT;
import static org.nting.toolkit.FontManager.FontSize.MEDIUM_FONT;
import static org.nting.toolkit.FontManager.FontSize.SMALL_FONT;
import static org.nting.toolkit.component.TextAlignment.CENTER;
import static org.nting.toolkit.component.TextAlignment.LEFT;
import static org.nting.toolkit.component.TextAlignment.RIGHT;
import static org.nting.toolkit.component.builder.ContainerBuilder.panelBuilder;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.layout.FormLayout.xyw;
import static playn.core.Font.Style.BOLD;
import static playn.core.Font.Style.BOLD_ITALIC;
import static playn.core.Font.Style.ITALIC;
import static playn.core.Font.Style.PLAIN;

import org.nting.toolkit.Component;
import org.nting.toolkit.app.Pages.PageSize;
import org.nting.toolkit.component.Panel;
import org.nting.toolkit.component.ScrollPane;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.layout.FormLayout;

public class LabelTestPage implements ITestPage {

    @Override
    public PageSize getPageSize() {
        return PageSize.DOUBLE_COLUMN;
    }

    @Override
    public Component createContent() {
        FormLayout formLayout = new FormLayout("pref, 7dlu, 0px:grow",
                "pref, 7dlu, pref, 7dlu, pref, 7dlu, pref, 7dlu, pref, 7dlu, pref");
        ContainerBuilder<Panel, ?> panelBuilder = panelBuilder(formLayout);
        panelBuilder.addLabel(xy(0, 0)).text("Label Alignments (large and bold)").pass().font(LARGE_FONT, BOLD);
        panelBuilder.addLabel(xy(0, 2)).text("Left (italic)").alignment(LEFT).pass().font(SMALL_FONT, ITALIC)
                .color(0xFF287ECE);
        panelBuilder.addLabel(xy(0, 4)).text("Center (medium and plain)").alignment(CENTER).pass()
                .font(MEDIUM_FONT, PLAIN).color(0xFFC22F24);
        panelBuilder.addLabel(xy(0, 6)).text("Right (bold+italic)").alignment(RIGHT).pass()
                .font(SMALL_FONT, BOLD_ITALIC).color(0xFFDBC103);
        panelBuilder.addPanel(xy(0, 8)).layoutManager("0px:grow", "pref").addMultiLineLabel(xy(0, 0))
                .text("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut "
                        + "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco "
                        + "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in "
                        + "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat "
                        + "cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .end().backgroundColor(0x10FF0000);
        panelBuilder.addPanel(xyw(0, 10, 3)).layoutManager("0px:grow", "pref").addMultiLineLabel(xy(0, 0))
                .text("Can't get element 0\n" + "java.lang.IndexOutOfBoundsException: Can't get element 0\n"
                        + "\tat Unknown.Exception_0(oncanvas-0.js@3)\n"
                        + "\tat Unknown.RuntimeException_1(oncanvas-0.js@15)\n"
                        + "\tat Unknown.IndexOutOfBoundsException_1(oncanvas-0.js@22)\n"
                        + "\tat Unknown.$get_12(oncanvas-0.js@13)\n"
                        + "\tat Unknown.$getSelectedViewPane(oncanvas-0.js@10)\n"
                        + "\tat Unknown.getSelectedViewPane(oncanvas-0.js@10)\n"
                        + "\tat Unknown.$updateLayout(oncanvas-0.js@25)\n"
                        + "\tat Unknown.$closeNextViews(oncanvas-0.js@5)\n"
                        + "\tat Unknown.valueChange_0(oncanvas-0.js@76)\n"
                        + "\tat Unknown.$fireValueChange(oncanvas-0.js@14)\n"
                        + "\tat Unknown.$setValue_8(oncanvas-0.js@5)\n" + "\tat Unknown.setValue_23(oncanvas-0.js@3)\n"
                        + "\tat Unknown.$valueChange_0(oncanvas-0.js@26)\n" + "\tat Unknown.bind_2(oncanvas-0.js@3)\n"
                        + "\tat Unknown.Bindings$AutoBindingBase_1(oncanvas-0.js@8)\n"
                        + "\tat Unknown.Bindings$AutoBindingRead_1(oncanvas-0.js@30)\n"
                        + "\tat Unknown.bind_1(oncanvas-0.js@24)\n"
                        + "\tat Unknown.$selectedItemId_0(oncanvas-0.js@36)\n"
                        + "\tat Unknown.createMainViewPane_6(oncanvas-0.js@10)\n"
                        + "\tat Unknown.createViewPanes(oncanvas-0.js@167)\n"
                        + "\tat Unknown.reInit(oncanvas-0.js@28)\n"
                        + "\tat Unknown.$updateForDluChange(oncanvas-0.js@12)\n"
                        + "\tat Unknown.$updateDluSize(oncanvas-0.js@5)\n" + "\tat Unknown.$onResize(oncanvas-0.js@3)\n"
                        + "\tat Unknown.$fireResizeEvent(oncanvas-0.js@35)\n"
                        + "\tat Unknown.onResize_2(oncanvas-0.js@3)\n" + "\tat Unknown.dispatch(oncanvas-0.js@11)\n"
                        + "\tat Unknown.$doFire(oncanvas-0.js@17)\n" + "\tat Unknown.$fireEvent(oncanvas-0.js@5)\n"
                        + "\tat Unknown.fireEvent(oncanvas-0.js@28)\n" + "\tat Unknown.fire_0(oncanvas-0.js@12)\n"
                        + "\tat Unknown.$handleResize(oncanvas-0.js@3)\n" + "\tat Unknown.execute_4(oncanvas-0.js@3)\n"
                        + "\tat Unknown.runScheduledTasks(oncanvas-0.js@74)\n"
                        + "\tat Unknown.$flushPostEventPumpCommands(oncanvas-0.js@5)\n"
                        + "\tat Unknown.execute_0(oncanvas-0.js@3)\n" + "\tat Unknown.execute(oncanvas-0.js@14)\n"
                        + "\tat Unknown.apply_5(oncanvas-0.js@21)\n" + "\tat Unknown.entry0(oncanvas-0.js@16)\n"
                        + "\tat Unknown.eval(oncanvas-0.js@16)\n" + "\tat Unknown.eval(oncanvas-0.js@30)\n"
                        + "\tat Unknown.eval(oncanvas-0.js@5)\n")
                .end().backgroundColor(0x10FF0000);

        return new ScrollPane(panelBuilder.build());
    }
}

package org.nting.toolkit.ui.style.material;

import static org.nting.toolkit.FontManager.FontSize.MEDIUM_FONT;
import static org.nting.toolkit.FontManager.FontSize.SMALL_FONT;
import static org.nting.toolkit.ToolkitServices.fontManager;
import static org.nting.toolkit.ui.style.material.CheckBoxPropertyIds.CHECK_BOX_SIZE;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.DISABLED_OPACITY_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.PRIMARY_BACKGROUND_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.PRIMARY_TEXT_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.TOOLTIP_COLOR;
import static playn.core.Font.Style.BOLD;

import org.nting.toolkit.Component;
import org.nting.toolkit.component.AbstractComponent;
import org.nting.toolkit.component.Button;
import org.nting.toolkit.component.CheckBox;
import org.nting.toolkit.component.Dialog;
import org.nting.toolkit.component.Popup;
import org.nting.toolkit.component.RadioButton;
import org.nting.toolkit.component.ScrollPane;
import org.nting.toolkit.component.SplitPane;
import org.nting.toolkit.component.StandardPopup;
import org.nting.toolkit.component.SwitchButton;
import org.nting.toolkit.component.TextField;
import org.nting.toolkit.component.TooltipPopup;
import org.nting.toolkit.component.WindowPopup;
import org.nting.toolkit.internal.NotificationsImpl;
import org.nting.toolkit.internal.Root;
import org.nting.toolkit.ui.ComponentUI;
import org.nting.toolkit.ui.style.AbstractStyleModule;

import playn.core.Canvas;
import pythagoras.f.Dimension;

// See: https://github.com/PolymerElements/paper-styles/blob/master/default-theme.html
// https://www.webcomponents.org/element/PolymerElements/paper-styles
public class MaterialStyleModule extends AbstractStyleModule {

    @Override
    protected void doConfigureUIs() {
        toType(Button.class).bind("componentUI", new MaterialButtonUI());
        toType(TextField.class).bind("componentUI", new MaterialTextFieldUI<>());
        toType(ScrollPane.class).bind("componentUI", new MaterialScrollPaneUI());
        toType(Dialog.class).bind("componentUI", new MaterialDialogUI());
        toType(StandardPopup.class).bind("componentUI", new MaterialStandardPopupUI<>());
        toType(TooltipPopup.class).bind("componentUI", new MaterialTooltipPopupUI());
        toType(SplitPane.class).bind("componentUI", new MaterialSplitPaneUI());
        toType(CheckBox.class).bind("componentUI", new MaterialCheckBoxUI<>());
        toType(RadioButton.class).bind("componentUI", new MaterialRadioButtonUI());
        toType(SwitchButton.class).bind("componentUI", new MaterialSwitchButtonUI());
    }

    @Override
    protected void doConfigureComponents() {
        toType(Root.class).bind("backgroundColor", PRIMARY_BACKGROUND_COLOR);

        toType(AbstractComponent.class).bind("font", font);

        toType(Button.class).bind("color", PRIMARY_TEXT_COLOR);
        toType(Button.class).bind("padding", dluY(3));
        toType(Button.class).bind("font", fontManager().getFont(SMALL_FONT, BOLD));

        toType(ScrollPane.class).bind("scrollBarWidth", dluY(3));
        toType(ScrollPane.class).bind("gridSize", dluY(8));

        toType(SplitPane.class).bind("mouseOverSize", 6);

        toType(Popup.class).bind("shadowSize", 1);

        toType(StandardPopup.class).bind("padding", dluY(3));

        toType(TooltipPopup.class).bind("color", TOOLTIP_COLOR);
        toType(TooltipPopup.class).bind("shadowSize", 0);

        toType(WindowPopup.class).bind("shadowSize", 4);

        toType(NotificationsImpl.Notification.class).bind("shadowSize", 2);

        toType(Dialog.class).bind("modalityCurtain", DISABLED_OPACITY_COLOR);
        toType(Dialog.class).bind("font", fontManager().getFont(MEDIUM_FONT, BOLD));

        toType(TextField.class).bind("hPadding", 0);
        toType(TextField.class).bind("vPadding", dluY(3));

        toType(CheckBox.class).bind("padding", dluY(3));
        toType(CheckBox.class).bind(CHECK_BOX_SIZE, 2 * dluY(4));
        toType(RadioButton.class).bind(CHECK_BOX_SIZE, 2 * dluY(4));

        toType(SwitchButton.class).bind("padding", dluY(4));
    }

    @Override
    protected void doConfigureStyles() {
    }

    private static class EmptyComponentUI implements ComponentUI<Component> {

        @Override
        public void initialize(Component component) {

        }

        @Override
        public void terminate(Component component) {

        }

        @Override
        public void paintComponent(Component component, Canvas canvas) {

        }

        @Override
        public Dimension getPreferredSize(Component component) {
            return new Dimension(200, 80);
        }

        @Override
        public void paintForeground(Component component, Canvas canvas) {

        }

        @Override
        public boolean isComponentSupported(Component c) {
            return true;
        }
    }
}

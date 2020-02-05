package org.nting.toolkit.ui.style.material;

import static org.nting.toolkit.ToolkitServices.fontManager;
import static org.nting.toolkit.ToolkitServices.unitConverter;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.ui.style.material.DialogPropertyIds.TITLE_BACKGROUND_COLOR;
import static org.nting.toolkit.ui.style.material.DialogPropertyIds.TITLE_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.DIALOG_TITLE_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.PRIMARY_BACKGROUND_COLOR;

import org.nting.toolkit.Component;
import org.nting.toolkit.FontManager;
import org.nting.toolkit.component.Dialog;
import org.nting.toolkit.layout.FormLayout;
import org.nting.toolkit.ui.ComponentUI;
import org.nting.toolkit.ui.shape.FlexRectangleShape;
import org.nting.toolkit.ui.shape.RectangleShape;
import org.nting.toolkit.ui.stone.ContentBuilder;
import org.nting.toolkit.ui.stone.TextContentWithEllipsis;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class MaterialDialogUI implements ComponentUI<Dialog> {

    @Override
    public void initialize(Dialog dialog) {
        dialog.setLayoutManager(new FormLayout("pref", "16dlu, pref"));
        dialog.setLayoutConstraint(dialog.getContent(), xy(0, 1));
        dialog.shadowSize.setValue(16);

        dialog.createProperty(TITLE_COLOR, DIALOG_TITLE_COLOR);
        dialog.createProperty(TITLE_BACKGROUND_COLOR, PRIMARY_BACKGROUND_COLOR);
    }

    @Override
    public void terminate(Dialog dialog) {
        dialog.setLayoutManager(null);
        dialog.setLayoutConstraint(dialog.getContent(), null);
        dialog.shadowSize.setValue(0);

        dialog.removeProperty(TITLE_COLOR);
        dialog.removeProperty(TITLE_BACKGROUND_COLOR);
    }

    @Override
    public void paintComponent(Dialog dialog, Canvas canvas) {
        canvas.translate(dialog.x.getValue(), dialog.y.getValue());
        new RectangleShape(0, 0, dialog.width.getValue(), dialog.height.getValue()).fillColor(PRIMARY_BACKGROUND_COLOR)
                .paint(canvas);

        int titleHeight = Math
                .round(16 * unitConverter().dialogUnitYAsPixel(fontManager().getFont(FontManager.FontSize.SMALL_FONT)));
        int titlePadding = Math
                .round(7 * unitConverter().dialogUnitXAsPixel(fontManager().getFont(FontManager.FontSize.SMALL_FONT)));
        Dimension titleSize = new Dimension(dialog.width.getValue(), titleHeight);
        new FlexRectangleShape(4, 4, 0, 0).fillColor(TITLE_BACKGROUND_COLOR.getValueOf(dialog)).size(titleSize)
                .paint(canvas);
        ContentBuilder
                .builderOnContent(new TextContentWithEllipsis(dialog.font.getValue(), TITLE_COLOR.getValueOf(dialog),
                        dialog.title.getValue()))
                .paddedContent(0, titlePadding, 0, titlePadding).paddedContent(1, 0, 0, 0).paint(canvas, titleSize);
        canvas.translate(-dialog.x.getValue(), -dialog.y.getValue());
    }

    @Override
    public Dimension getPreferredSize(Dialog dialog) {
        return dialog.getLayoutManager().preferredLayoutSize(dialog);
    }

    @Override
    public void paintForeground(Dialog component, Canvas canvas) {
    }

    @Override
    public boolean isComponentSupported(Component c) {
        return c instanceof Dialog;
    }
}

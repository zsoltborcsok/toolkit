package org.nting.toolkit.ui.style.material;

import static org.nting.toolkit.ui.style.material.MaterialStyleColors.TOOLTIP_BACKGROUND;

import org.nting.toolkit.Component;
import org.nting.toolkit.component.TooltipPopup;
import org.nting.toolkit.ui.shape.RoundedRectangleShape;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class MaterialTooltipPopupUI extends MaterialStandardPopupUI<TooltipPopup> {

    @Override
    public void paintComponent(TooltipPopup tooltipPopup, Canvas canvas) {
        Dimension size = tooltipPopup.getSize();
        new RoundedRectangleShape(0, 0, size.width, size.height, 4).fillColor(TOOLTIP_BACKGROUND).paint(canvas);
    }

    @Override
    public boolean isComponentSupported(Component c) {
        return c instanceof TooltipPopup;
    }
}

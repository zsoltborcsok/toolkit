package org.nting.toolkit.ui.style.material;

import static org.nting.toolkit.ui.style.material.CheckBoxPropertyIds.CHECK_BOX_CHECKED_COLOR;

import org.nting.toolkit.Component;
import org.nting.toolkit.component.RadioButton;
import org.nting.toolkit.ui.shape.EllipsisShape;
import org.nting.toolkit.ui.stone.Content;
import org.nting.toolkit.ui.stone.ContentBuilder;

import pythagoras.f.Dimension;

public class MaterialRadioButtonUI extends MaterialCheckBoxUI<RadioButton> {

    @Override
    protected Content boxContent(RadioButton radioButton, float boxSize, int checkBoxColor) {
        Boolean selected = radioButton.selected.getValue();
        EllipsisShape boxShape = new EllipsisShape().strokeWidth(2);
        if (selected == Boolean.TRUE) {
            boxShape.strokeColor(CHECK_BOX_CHECKED_COLOR.getValueOf(radioButton));
        } else {
            boxShape.strokeColor(checkBoxColor);
        }

        ContentBuilder contentBuilder = new ContentBuilder().basicShapeContent(boxShape,
                new Dimension(boxSize, boxSize));
        if (radioButton.selected.getValue()) {
            contentBuilder.addScalableContent(CHECK_BOX_CHECKED_COLOR.getValueOf(radioButton), 1).addCircle(0.5f, 0.5f,
                    0.25f);
        }
        return contentBuilder.paddedContentWithoutClipping(1, 0, 1, 1).getContent();
    }

    @Override
    public boolean isComponentSupported(Component c) {
        return c instanceof RadioButton;
    }

}

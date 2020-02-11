package org.nting.toolkit.ui.style.material;

import static org.nting.toolkit.ui.Colors.TRANSPARENT;
import static org.nting.toolkit.ui.style.material.FieldComponentPropertyIds.BACKGROUND_COLOR;
import static org.nting.toolkit.ui.style.material.FieldComponentPropertyIds.DIVIDER_COLOR;
import static org.nting.toolkit.ui.style.material.FieldComponentPropertyIds.ERROR_COLOR;
import static org.nting.toolkit.ui.style.material.FieldComponentPropertyIds.FOCUSED_COLOR;
import static org.nting.toolkit.ui.style.material.FieldComponentPropertyIds.SECONDARY_COLOR;
import static org.nting.toolkit.ui.style.material.FieldComponentPropertyIds.SELECTION_BACKGROUND;
import static org.nting.toolkit.ui.style.material.MaterialContentUtil.paintFocusedLine;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.PRIMARY_COLOR;
import static org.nting.toolkit.ui.style.material.MaterialStyleColors.SECONDARY_TEXT_COLOR;

import java.util.Map;
import java.util.Optional;

import org.nting.data.Property;
import org.nting.data.Registration;
import org.nting.toolkit.animation.Tween;
import org.nting.toolkit.component.FieldComponent;
import org.nting.toolkit.ui.ComponentUI;
import org.nting.toolkit.ui.stone.ContentBuilder;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import playn.core.Canvas;

public abstract class MaterialFieldUI<C extends FieldComponent> implements ComponentUI<C> {

    private static final Map<FieldComponent, Registration> registrations = Maps.newHashMap();
    protected static final String FOCUS_PERCENT = "FOCUS_PERCENT";

    @Override
    public void initialize(C component) {
        component.createProperty(BACKGROUND_COLOR, TRANSPARENT);
        component.createProperty(SECONDARY_COLOR, SECONDARY_TEXT_COLOR); // caption, icon
        component.createProperty(DIVIDER_COLOR, MaterialStyleColors.DIVIDER_COLOR); // line
        component.createProperty(FOCUSED_COLOR, PRIMARY_COLOR); // focused line
        component.createProperty(ERROR_COLOR, MaterialStyleColors.ERROR_COLOR); // line when error, error message
        component.createProperty(SELECTION_BACKGROUND, MaterialStyleColors.SELECTION_BACKGROUND);
        animateFocusPercent(component, component.createProperty(FOCUS_PERCENT, 0f));
    }

    private void animateFocusPercent(FieldComponent fieldComponent, Property<Float> focusPercent) {
        registrations.put(fieldComponent, fieldComponent.focused.addValueChangeListener(event -> {
            if (event.getValue()) {
                fieldComponent.addBehavior(new Tween<>(focusPercent, 0f, 100f, 150));
            } else {
                focusPercent.setValue(0f);
            }
        }));
    }

    @Override
    public void terminate(C component) {
        component.removeProperty(BACKGROUND_COLOR);
        component.removeProperty(SECONDARY_COLOR);
        component.removeProperty(DIVIDER_COLOR);
        component.removeProperty(FOCUSED_COLOR);
        component.removeProperty(ERROR_COLOR);
        component.removeProperty(SELECTION_BACKGROUND);
        component.removeProperty(FOCUS_PERCENT);
        Optional.ofNullable(registrations.remove(component)).ifPresent(Registration::remove);
    }

    // Preferred height: 2px
    protected ContentBuilder paintLine(C component) {
        return paintFocusedLine(component.enabled.getValue(), !Strings.isNullOrEmpty(component.errorMessage.getValue()),
                component.getValue(FOCUS_PERCENT), component.width.getValue(), DIVIDER_COLOR.getValueOf(component),
                ERROR_COLOR.getValueOf(component), FOCUSED_COLOR.getValueOf(component));
    }

    protected int getCaretColor(C component) {
        boolean hasErrorMessage = !Strings.isNullOrEmpty(component.errorMessage.getValue());
        return hasErrorMessage ? ERROR_COLOR.getValueOf(component) : FOCUSED_COLOR.getValueOf(component);
    }

    @Override
    public void paintForeground(FieldComponent component, Canvas canvas) {
    }
}

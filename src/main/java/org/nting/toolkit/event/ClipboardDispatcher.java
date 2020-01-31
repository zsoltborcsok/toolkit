package org.nting.toolkit.event;

import org.nting.data.Property;
import org.nting.data.Property.ValueChangeListenerSupport;
import org.nting.data.ValueChangeEvent;
import org.nting.data.ValueChangeListener;
import org.nting.toolkit.Component;
import org.nting.toolkit.component.AbstractTextComponent;
import org.nting.toolkit.component.FieldComponent;
import org.nting.toolkit.util.ToolkitUtils;

import playn.core.Clipboard;
import playn.core.PlayN;

public class ClipboardDispatcher implements Clipboard.PasteListener {

    private final TextSelectionChangeListener textSelectionChangeListener = new TextSelectionChangeListener();

    public ClipboardDispatcher(Property<Component> focusOwner) {
        focusOwner.addValueChangeListener(new FocusChangeListener());
    }

    @Override
    public void onPasteOrCut(String newText) {
        Component component = ToolkitUtils.getFocusOwner().getValue();
        if (component instanceof FieldComponent) {
            FieldComponent fieldComponent = (FieldComponent) component;
            fieldComponent.paste(newText);
        }
    }

    private class FocusChangeListener implements ValueChangeListener<Component> {

        @SuppressWarnings("unchecked")
        @Override
        public void valueChange(ValueChangeEvent<Component> event) {
            Component prevComponent = event.getPrevValue();
            Component currentComponent = event.getValue();

            if (prevComponent instanceof AbstractTextComponent) {
                AbstractTextComponent abstractTextComponent = (AbstractTextComponent) prevComponent;
                ((ValueChangeListenerSupport<String>) abstractTextComponent.selectedText)
                        .removeValueChangeListener(textSelectionChangeListener);
            }

            if (currentComponent instanceof AbstractTextComponent) {
                AbstractTextComponent abstractTextComponent = (AbstractTextComponent) currentComponent;
                abstractTextComponent.selectedText.addValueChangeListener(textSelectionChangeListener);
                PlayN.clipboard().setSelectedText(abstractTextComponent.selectedText.getValue());
            }
        }
    }

    private static class TextSelectionChangeListener implements ValueChangeListener<String> {

        @Override
        public void valueChange(ValueChangeEvent<String> event) {
            PlayN.clipboard().setSelectedText(event.getValue());
        }
    }
}

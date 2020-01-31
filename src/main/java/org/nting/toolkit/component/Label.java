package org.nting.toolkit.component;

import static org.nting.toolkit.ui.Colors.TRN_BLACK;
import static org.nting.toolkit.ui.stone.ContentBuilder.builderOnContent;
import static org.nting.toolkit.ui.stone.TextContentSingleLine.textContent;

import org.nting.data.Property;
import org.nting.data.condition.Conditions;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.ui.ComponentUI;
import org.nting.toolkit.ui.shape.RectangleShape;
import org.nting.toolkit.ui.stone.ContentBuilder;
import org.nting.toolkit.ui.stone.TextContent;
import org.nting.toolkit.util.ToolkitUtils;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class Label extends AbstractTextComponent {

    public final Property<TextAlignment> alignment = createProperty("alignment", TextAlignment.LEFT);
    public final Property<Integer> selectionBackground = createProperty("selectionBackground", TRN_BLACK(0x40));
    public final Property<Boolean> selectionSupported = createProperty("selectionSupported", true);

    protected final TextContent textContent = createTextContent();

    public Label() {
        this("text");
    }

    protected Label(String... reLayoutPropertyNames) {
        super(reLayoutPropertyNames);

        addMouseListener(createMouseHandler());
        Conditions.textOf(selectedText).isNotBlank().addValueChangeListener(event -> {
            boolean hasSelection = event.getValue();
            setFocusable(hasSelection);
            if (hasSelection) {
                requestFocus();
            }
        });
        focused.addValueChangeListener(event -> {
            if (!event.getValue()) {
                selectionLength.setValue(0);
            }
        });
    }

    protected TextContent createTextContent() {
        return textContent(font, color, text);
    }

    protected MouseListener createMouseHandler() {
        return new MouseHandler();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setComponentUI(ComponentUI componentUI) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension preferredContentSize = textContent.getPreferredSize();
        return new Dimension(preferredContentSize.width + 1, preferredContentSize.height);
    }

    @Override
    public void doPaintComponent(Canvas canvas) {
        if (0 < selectionLength.getValue()) {
            float selectionX1 = Math.round(textContent.getCaretPosition(0, selectionStart.getValue()));
            float selectionX2 = Math
                    .round(textContent.getCaretPosition(0, selectionStart.getValue() + selectionLength.getValue()));
            float paddingFromAlign = getPaddingFromAlign();
            selectionX1 = Math.max(0, selectionX1 + paddingFromAlign);
            selectionX2 = Math.min(getSize().width, selectionX2 + paddingFromAlign);
            new RectangleShape(selectionX1, 0, selectionX2 - selectionX1 + 1, getSize().height)
                    .fillColor(selectionBackground.getValue()).paint(canvas);
        }

        ContentBuilder contentBuilder = builderOnContent(textContent);
        if (alignment.getValue() == TextAlignment.LEFT) {
            contentBuilder.rightPaddedContent(1);
        } else if (alignment.getValue() == TextAlignment.CENTER) {
            contentBuilder.paddedContent(0, 1, 0, 1);
        } else if (alignment.getValue() == TextAlignment.RIGHT) {
            contentBuilder.leftPaddedContent(1);
        }
        contentBuilder.paint(canvas, getSize());
    }

    private float getPaddingFromAlign() {
        if (alignment.getValue() == TextAlignment.LEFT) {
            return 0;
        } else if (alignment.getValue() == TextAlignment.CENTER) {
            return Math.round((getSize().width - textContent.getPreferredSize().width) / 2);
        } else if (alignment.getValue() == TextAlignment.RIGHT) {
            return Math.round(getSize().width - textContent.getPreferredSize().width);
        }
        return 0;
    }

    @Override
    public float getBaselinePosition() {
        return textContent.getPreferredSize().height - font.getValue().size();
    }

    @Override
    public int search(String searchText) {
        return textContent.search(searchText);
    }

    @Override
    public void highlightMatch(int index) {
        textContent.highlightMatch(index);
        repaint();
        ToolkitUtils.scrollComponentToVisible(this);
    }

    private class MouseHandler extends SelectionMouseHandler {

        @Override
        public void mousePressed(MouseEvent e) {
            if (selectionSupported.getValue()) {
                super.mousePressed(e);
            }
        }

        @Override
        public void mouseLongPressed(MouseEvent e) {
            if (selectionSupported.getValue()) {
                super.mouseLongPressed(e);
            }
        }

        @Override
        protected int getCaretIndex(float x, float y) {
            return textContent.getCaretIndex(0, x - getPaddingFromAlign());
        }
    }
}

package org.nting.toolkit.component;

import static org.nting.toolkit.ToolkitServices.toolkitManager;
import static org.nting.toolkit.ToolkitServices.unitConverter;
import static org.nting.toolkit.component.Alignment.BOTTOM_LEFT;
import static org.nting.toolkit.component.Orientation.VERTICAL;
import static org.nting.toolkit.component.ScrollComponent.ScrollBarPolicy.AS_NEEDED;
import static org.nting.toolkit.component.ScrollComponent.ScrollBarPolicy.NEVER;
import static org.nting.toolkit.layout.FormLayout.xy;
import static org.nting.toolkit.ui.Colors.TRN_BLACK;
import static org.nting.toolkit.ui.stone.ContentBuilder.builderOnContent;
import static org.nting.toolkit.ui.stone.TextContentSingleLine.textContent;

import org.nting.data.Property;
import org.nting.data.Registration;
import org.nting.data.binding.Bindings;
import org.nting.data.condition.Conditions;
import org.nting.data.property.ObjectProperty;
import org.nting.toolkit.ToolkitRunnable;
import org.nting.toolkit.component.builder.ContainerBuilder;
import org.nting.toolkit.component.builder.LabelMiddleBuilder;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.layout.FormLayout;
import org.nting.toolkit.ui.ComponentUI;
import org.nting.toolkit.ui.shape.RectangleShape;
import org.nting.toolkit.ui.stone.TextContent;
import org.nting.toolkit.util.ToolkitUtils;

import com.google.common.base.Strings;

import playn.core.Canvas;
import pythagoras.f.Dimension;

public class LabelWithEllipsis extends AbstractTextComponent {

    public final Property<Integer> selectionBackground = createProperty("selectionBackground", TRN_BLACK(0x40));
    public final Property<Boolean> selectionSupported = createProperty("selectionSupported", true);

    private final TextContent textContent = textContent(font, color, text);
    private final Property<TextContent> shownTextContent;
    private final SelectionMouseHandler selectionMouseHandler = new SelectionMouseHandler();
    private final EllipsisMouseHandler ellipsisEllipsisMouseHandler = new EllipsisMouseHandler();

    public LabelWithEllipsis() {
        super("text");

        shownTextContent = Bindings.transformProperties(text, width, (text, width) -> {
            if (textContent.getPreferredSize().width <= width || Strings.isNullOrEmpty(text)) {
                return textContent;
            } else {
                float maxWidth = width - unitConverter().dialogUnitXAsPixel(6, LabelWithEllipsis.this);
                int splitPosition = textContent.getCaretIndex(0, maxWidth);
                return textContent(font, color, new ObjectProperty<>(text.substring(0, splitPosition) + "..."));
            }
        });

        Registration[] mouseListenerRegistration = new Registration[1];
        Bindings.bind(Conditions.valueOf(shownTextContent).is(textContent), event -> {
            if (mouseListenerRegistration[0] != null) {
                mouseListenerRegistration[0].remove();
            }
            if (event.getValue()) {
                mouseListenerRegistration[0] = addMouseListener(selectionMouseHandler);
            } else {
                mouseListenerRegistration[0] = addMouseListener(ellipsisEllipsisMouseHandler);
            }
        });

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

    @SuppressWarnings("rawtypes")
    @Override
    public void setComponentUI(ComponentUI componentUI) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Dimension getPreferredSize() {
        return textContent.getPreferredSize();
    }

    @Override
    public void doPaintComponent(Canvas canvas) {
        if (0 < selectionLength.getValue()) {
            float selectionX1 = Math.round(textContent.getCaretPosition(0, selectionStart.getValue()));
            float selectionX2 = Math
                    .round(textContent.getCaretPosition(0, selectionStart.getValue() + selectionLength.getValue()));
            selectionX1 = Math.max(0, selectionX1);
            selectionX2 = Math.min(getSize().width, selectionX2);
            new RectangleShape(selectionX1, 0, selectionX2 - selectionX1 + 1, getSize().height)
                    .fillColor(selectionBackground.getValue()).paint(canvas);
        }

        builderOnContent(shownTextContent.getValue()).paint(canvas, getSize());
    }

    @Override
    public float getBaselinePosition() {
        return textContent.getPreferredSize().height - font.getValue().size();
    }

    @Override
    public int search(String searchText) {
        return shownTextContent.getValue().search(searchText);
    }

    @Override
    public void highlightMatch(int index) {
        shownTextContent.getValue().highlightMatch(index);
        repaint();
        ToolkitUtils.scrollComponentToVisible(this);
    }

    private class SelectionMouseHandler extends AbstractTextComponent.SelectionMouseHandler {

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
            return textContent.getCaretIndex(0, x);
        }
    }

    private class EllipsisMouseHandler implements MouseListener {

        private ToolkitRunnable tooltipRunnable, tooltipCloseRunnable;
        private TooltipPopup tooltipPopup;

        private MouseListener tooltipMouseHandler = new MouseListener() {

            @Override
            public void mouseEntered(MouseEvent e) {
                tooltipCloseRunnable.cancel();
                tooltipCloseRunnable = null;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                toolkitManager().invokeAfterRepaint(() -> closeTooltipPopup());
            }
        };

        @Override
        public void mouseClicked(MouseEvent e) {
            if (width.getValue() < textContent.getPreferredSize().width) {
                int ellipsisWidth = unitConverter().dialogUnitXAsPixel(6, LabelWithEllipsis.this);
                if (width.getValue() - ellipsisWidth <= e.getX()) {
                    e.consume();
                    showTooltipPopup();
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (textContent.getPreferredSize().width <= width.getValue()) {
                return;
            }

            toolkitManager().schedule(tooltipRunnable = new ToolkitRunnable(500) {
                @Override
                public void run() {
                    showTooltipPopup();
                }
            });
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (tooltipRunnable != null) {
                tooltipRunnable.cancel();
                tooltipRunnable = null;
            }
            toolkitManager().schedule(tooltipCloseRunnable = new ToolkitRunnable(100) {
                @Override
                public void run() {
                    closeTooltipPopup();
                }
            });
        }

        private void showTooltipPopup() {
            float requiredWidth = ToolkitUtils.getRootPosition(LabelWithEllipsis.this).x
                    + textContent.getPreferredSize().width;
            if (requiredWidth < toolkitManager().root().width.getValue() && !text.getValue().contains("\n")) {
                ContainerBuilder<TooltipPopup, ?> tooltipPopupBuilder = new ContainerBuilder<>(
                        tooltipPopup = new TooltipPopup(BOTTOM_LEFT, VERTICAL, new FormLayout("pref", "pref")));
                tooltipPopupBuilder.addLabel(xy(0, 0)).text(text.getValue()).pass().font(font.getValue());
            } else if (!text.getValue().contains("\n")) {
                ContainerBuilder<TooltipPopup, ?> tooltipPopupBuilder = new ContainerBuilder<>(
                        tooltipPopup = new TooltipPopup(BOTTOM_LEFT, VERTICAL,
                                new FormLayout("min(pref;" + (width.getValue() - 2) + "px)", "pref")));
                tooltipPopup.forceInjectStyleProperties();
                tooltipPopupBuilder.addComponent(new ScrollPane(new LabelMiddleBuilder<>().text(text.getValue()).pass()
                        .color(tooltipPopup.color.getValue()).build(), NEVER, AS_NEEDED), xy(0, 0));
            } else {
                float textWidth = new TextContent(font.getValue(), color.getValue(), text.getValue(), Float.MAX_VALUE)
                        .getPreferredSize().width;
                textWidth = Math.min(width.getValue() - 2, textWidth + 10); // ScrollPane.scrollBarWidth: 10
                ContainerBuilder<TooltipPopup, ?> tooltipPopupBuilder = new ContainerBuilder<>(
                        tooltipPopup = new TooltipPopup(BOTTOM_LEFT, VERTICAL,
                                new FormLayout(textWidth + "px", "min(pref;" + 100 + "dlu)")));
                tooltipPopup.forceInjectStyleProperties();
                tooltipPopupBuilder.addComponent(new ScrollPane(new MultiLineLabel().<MultiLineLabel> process(label -> {
                    label.text.setValue(text.getValue());
                    label.color.setValue(tooltipPopup.color.getValue());
                }), AS_NEEDED, AS_NEEDED), xy(0, 0));
            }
            tooltipPopup.showRelativeTo(LabelWithEllipsis.this);
            tooltipPopup.addMouseListener(tooltipMouseHandler);

            if (tooltipRunnable != null) {
                tooltipRunnable.cancel();
                tooltipRunnable = null;
            }
        }

        private void closeTooltipPopup() {
            if (tooltipPopup != null) {
                tooltipPopup.close();
                tooltipPopup = null;
            }
        }
    }
}

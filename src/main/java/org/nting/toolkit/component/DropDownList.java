package org.nting.toolkit.component;

import static org.nting.toolkit.ToolkitServices.toolkitManager;
import static org.nting.toolkit.component.Alignment.BOTTOM_LEFT;
import static org.nting.toolkit.component.Orientation.VERTICAL;
import static org.nting.toolkit.component.ScrollComponent.ScrollBarPolicy.AS_NEEDED;
import static org.nting.toolkit.component.ScrollComponent.ScrollBarPolicy.NEVER;
import static org.nting.toolkit.component.ScrollView.createVerticalScrollView;
import static org.nting.toolkit.event.MouseEvent.MouseButton.BUTTON_LEFT;
import static org.nting.toolkit.ui.Colors.GREY;
import static org.nting.toolkit.ui.Colors.LIGHT_GREY;
import static org.nting.toolkit.ui.Colors.ULTRA_LIGHT_GREY;
import static org.nting.toolkit.ui.Colors.WHITE;
import static org.nting.toolkit.ui.stone.ContentBuilder.builderOnContent;
import static org.nting.toolkit.ui.stone.TextContentSingleLine.textContent;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.nting.data.Property;
import org.nting.data.binding.BindingList;
import org.nting.data.binding.Bindings;
import org.nting.data.condition.Conditions;
import org.nting.data.query.DataProvider;
import org.nting.data.query.Query;
import org.nting.toolkit.Component;
import org.nting.toolkit.event.KeyEvent;
import org.nting.toolkit.event.KeyListener;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.event.MouseMotionEvent;
import org.nting.toolkit.layout.FormLayout;
import org.nting.toolkit.ui.shape.RectangleShape;
import org.nting.toolkit.ui.stone.ContentBuilder;
import org.nting.toolkit.ui.stone.PaddedContent;
import org.nting.toolkit.ui.stone.TextContent;
import org.nting.toolkit.util.ToolkitUtils;

import com.google.common.collect.Lists;

import playn.core.Key;
import playn.core.PlayN;
import pythagoras.d.MathUtil;

/** Compared to ComboBox, its text is not editable. */
public class DropDownList<T> extends AbstractTextComponent {

    @SuppressWarnings("rawtypes")
    private static Query EMPTY_QUERY = new Query(null, null);

    public final Property<Boolean> emptySelectionAllowed = createProperty("emptySelectionAllowed", true);
    public final Property<String> emptySelectionCaption = createProperty("emptySelectionCaption", "");
    public final Property<String> caption = createProperty("caption", null);
    public final Property<Boolean> enabled = createProperty("enabled", true);
    public final Property<Integer> hPadding = createProperty("hPadding", 3);
    public final Property<Integer> vPadding = createProperty("vPadding", 3);
    public final Property<Integer> visibleItemCount = createProperty("visibleItemCount", 6);
    public final Property<T> selectedItem = createProperty("selectedItem", null);

    private final Property<Integer> focusedIndex = createProperty("focusedIndex", 0);

    private final TextContent textContent = textContent(font, color, text);
    private final DropDownListPopup popup = new DropDownListPopup();
    private DataProvider<T> dataProvider;
    private List<T> items;
    private Function<T, String> itemCaptionGenerator = Object::toString;

    public DropDownList() {
        addKeyListener(new KeyHandler());
        addMouseListener(new MouseHandler());

        focused.addValueChangeListener(event -> {
            if (!event.getValue()) {
                selectionLength.setValue(0);
            }
        });

        emptySelectionAllowed.addValueChangeListener(event -> {
            if (items != null) {
                if (event.getValue()) {
                    items = Stream.concat(Stream.of((T) null), items.stream()).collect(Collectors.toList());
                } else {
                    items = items.subList(1, items.size());
                    if (!selectedItem.hasValue() && 0 < items.size()) {
                        selectedItem.setValue(items.get(0));
                    }
                }
            }
        });

        Bindings.bind(selectedItem, text,
                t -> t != null ? itemCaptionGenerator.apply(t) : emptySelectionCaption.getValue());
        Bindings.bind(emptySelectionCaption, text,
                esc -> selectedItem.hasValue() ? itemCaptionGenerator.apply(selectedItem.getValue()) : esc);
    }

    public DataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(DataProvider<T> dataProvider) {
        DataProvider<T> prevDataProvider = this.dataProvider;
        this.dataProvider = dataProvider;

        if (dataProvider == null) {
            items = null;
            return;
        }

        fetchAllItems(items -> {
            this.items = !emptySelectionAllowed.getValue() ? items
                    : Stream.concat(Stream.of((T) null), items.stream()).collect(Collectors.toList());

            if (!selectedItem.hasValue() && 0 < this.items.size()) {
                selectedItem.setValue(this.items.get(0));
            } else if (selectedItem.hasValue()) {
                if (items.size() == 0) {
                    selectedItem.setValue(null);
                } else { // Refresh the selected item from the fetched items
                    T selectedItemValue = selectedItem.getValue();
                    selectedItem.setValue(items.stream().filter(
                            item -> Objects.equals(dataProvider.getId(item), prevDataProvider.getId(selectedItemValue)))
                            .findFirst().orElse(this.items.get(0)));
                }
            }
        });
    }

    public Function<T, String> getItemCaptionGenerator() {
        return itemCaptionGenerator;
    }

    public void setItemCaptionGenerator(Function<T, String> itemCaptionGenerator) {
        this.itemCaptionGenerator = itemCaptionGenerator;
        if (selectedItem.hasValue()) {
            text.setValue(itemCaptionGenerator.apply(selectedItem.getValue()));
        }
    }

    public TextContent getTextContent() {
        return textContent;
    }

    public float getBaselinePosition() {
        return textContent.getPreferredSize().height - font.getValue().size() + vPadding.getValue();
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

    @Override
    public boolean isFocusable() {
        return super.isFocusable() && enabled.getValue();
    }

    @SuppressWarnings("unchecked")
    private void fetchAllItems(Consumer<List<T>> consumer) {
        dataProvider.size(EMPTY_QUERY)
                .then(size -> dataProvider.fetch(new Query<>(0, (int) size, null, null)).then(consumer));
    }

    private void focusByChar(char keyChar) {
        String character = ("" + keyChar).toLowerCase();
        Integer focusedIndexValue = focusedIndex.getValue();

        Stream<T> orderedItems = Stream.concat(items.subList(focusedIndexValue + 1, items.size()).stream(),
                items.subList(0, focusedIndexValue + 1).stream());
        orderedItems.filter(t -> t != null && itemCaptionGenerator.apply(t).toLowerCase().startsWith(character))
                .findFirst().ifPresent(item -> focusedIndex.setValue(items.indexOf(item)));
    }

    private void showPopup() {
        if (items != null && items.contains(selectedItem.getValue())) {
            focusedIndex.setValue(items.indexOf(selectedItem.getValue()));
        } else {
            focusedIndex.setValue(0);
        }

        if (items != null && 0 < items.size()) {
            popup.show();
            repaint();
        }
    }

    private void closePopup() {
        if (isPopupVisible()) {
            popup.close();
            requestFocus();
            repaint();
        }
    }

    public boolean isPopupVisible() {
        return popup.isPopupVisible();
    }

    private class KeyHandler implements KeyListener {

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (items == null) { // noinspection UnnecessaryReturnStatement
                return;
            } else if (keyEvent.isKeyCode(Key.DOWN)) {
                if (!isPopupVisible()) {
                    showPopup();
                } else {
                    focusNext();
                }
            } else if (keyEvent.isKeyCode(Key.UP)) {
                focusPrev();
            } else if (keyEvent.isKeyCode(Key.PAGE_DOWN)) {
                focusNextPage();
            } else if (keyEvent.isKeyCode(Key.PAGE_UP)) {
                focusPrevPage();
            } else if (keyEvent.isKeyCode(Key.ESCAPE)) {
                closePopup();
            } else if (keyEvent.isKeyCode(Key.ENTER)) {
                if (isPopupVisible()) {
                    selectedItem.setValue(items.get(focusedIndex.getValue()));
                    closePopup();
                }
            } else if (keyEvent.isKeyCode(Key.TAB)) {
                closePopup();
                keyEvent.unConsume();
            }
        }

        private void focusNext() {
            focusedIndex.setValue(Math.min(items.size() - 1, focusedIndex.getValue() + 1));
        }

        private void focusPrev() {
            focusedIndex.setValue(Math.max(0, focusedIndex.getValue() - 1));
        }

        private void focusNextPage() {
            if (0 < visibleItemCount.getValue()) {
                focusedIndex.setValue(
                        Math.min(items.size() - 1, focusedIndex.getValue() + visibleItemCount.getValue() - 1));
            }
        }

        private void focusPrevPage() {
            if (0 < visibleItemCount.getValue()) {
                focusedIndex.setValue(Math.max(0, focusedIndex.getValue() - visibleItemCount.getValue() + 1));
            }
        }

        @Override
        public void keyTyped(KeyEvent keyEvent) {
            if (items != null && 0 < items.size()) {
                byte key = (byte) keyEvent.getKeyChar(false);
                if (!(key == 0 || key == 13)) {
                    if (!isPopupVisible()) {
                        showPopup();
                    }
                    focusByChar(keyEvent.getKeyChar(true));
                }
            }
        }
    }

    private class MouseHandler extends SelectionMouseHandler {

        @Override
        public void mousePressed(MouseEvent e) {
            if (enabled.getValue() && items != null && 0 < items.size()) {
                showPopup();
            }
            super.mousePressed(e);
        }

        @Override
        public void mouseDragged(MouseMotionEvent e) {
            if (e.getButton() == BUTTON_LEFT && enabled.getValue()) {
                e.consume(); // Disable the scroll by dragging the DropDownList...
            }
        }

        @Override
        protected int getCaretIndex(float x, float y) {
            return textContent.getCaretIndex(0, x);
        }
    }

    public final class DropDownListPopup extends WindowPopup {

        private final Property<Integer> backgroundColor = createProperty("backgroundColor", WHITE);
        private final Property<Integer> focusedBackgroundColor = createProperty("focusedBackgroundColor",
                ULTRA_LIGHT_GREY);
        private final Property<Integer> selectedBackgroundColor = createProperty("selectedBackgroundColor", LIGHT_GREY);
        private final Property<Integer> focusedSelectedBackgroundColor = createProperty(
                "focusedSelectedBackgroundColor", GREY);

        private final BindingList popupBindings = new BindingList();

        public DropDownListPopup() {
            super(BOTTOM_LEFT, VERTICAL);

            setFocusable(false);
            addCloseListener(() -> {
                removeAllComponents();
                popupBindings.unbind();
            });

            attached.addValueChangeListener(event -> {
                if (!event.getValue()) {
                    toolkitManager().mouseDispatcher().updatePopupsConsumesMousePressedAndClicked();
                }
            });

            forceInjectStyleProperties();
        }

        public void show() {
            List<Component> renderedItems = renderedItems();
            ScrollPane scrollPane = new ScrollPane(createVerticalScrollView(renderedItems), AS_NEEDED, NEVER, false);
            scrollPane.setGridSize(renderedItems.get(0).getPreferredSize().height);

            float minWidth = DropDownList.this.getSize().width - 2;
            int itemCount = Math.min(items.size(), visibleItemCount.getValue());
            setLayoutManager(new FormLayout("max(pref;" + minWidth + "px)",
                    "2dlu, " + (scrollPane.gridSize.getValue() * itemCount) + "px, 2dlu"));
            addComponent(scrollPane, FormLayout.xy(0, 1));

            showRelativeTo(DropDownList.this);
            PlayN.invokeLater(() -> scrollPane.scroll(focusedIndex.getValue())); // Can't scroll until the first layout

            popupBindings.addBinding(Bindings.asBinding(focusedIndex.addValueChangeListener(event -> {
                float scrollViewPosition = scrollPane.getViewPosition().y;
                float scrollGridSize = scrollPane.gridSize.getValue();
                int firstVisibleIndex = MathUtil.round(scrollViewPosition / scrollGridSize);
                int visibleItemCountValue = visibleItemCount.getValue();
                int focusedIndexValue = focusedIndex.getValue();

                if (firstVisibleIndex < focusedIndexValue - visibleItemCountValue + 1) {
                    scrollPane.scroll((focusedIndexValue - visibleItemCountValue + 1) - firstVisibleIndex);
                }
                if (focusedIndexValue < firstVisibleIndex) {
                    scrollPane.scroll(focusedIndexValue - firstVisibleIndex);
                }
            })));
        }

        private List<Component> renderedItems() {
            List<Component> renderedItems = Lists.newLinkedList();

            MouseListener mouseListener = new MouseListener() {
                @SuppressWarnings("unchecked")
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == BUTTON_LEFT) {
                        T item = (T) e.getSource().getId();
                        selectedItem.setValue(item);
                        closePopup();
                    }
                }

                @SuppressWarnings("unchecked")
                @Override
                public void mouseEntered(MouseEvent e) {
                    T item = (T) e.getSource().getId();
                    focusedIndex.setValue(items.indexOf(item));
                }
            };

            for (int i = 0; i < items.size(); i++) {
                T item = items.get(i);
                boolean selected = selectedItem.getValue() == item;

                String itemCaption = item != null ? itemCaptionGenerator.apply(item) : emptySelectionCaption.getValue();
                TextContent content = textContent(DropDownList.this.font.getValue(), color.getValue(), itemCaption);
                PaddedContent paddedContent = new PaddedContent(new PaddedContent(content, vPadding.getValue(),
                        hPadding.getValue(), vPadding.getValue(), hPadding.getValue()), 0, 1, 0, 0);
                ContentBuilder contentBuilder = builderOnContent(paddedContent);
                RectangleShape backgroundShape = new RectangleShape();
                contentBuilder.background(backgroundShape).contentAndBackground();
                popupBindings.addBinding(Bindings.bind(Conditions.valueOf(focusedIndex).is(i), event -> {
                    Property<Integer> background = selected
                            ? (event.getValue() ? focusedSelectedBackgroundColor : selectedBackgroundColor)
                            : (event.getValue() ? focusedBackgroundColor : backgroundColor);
                    backgroundShape.fillColor(background.getValue());
                }));
                renderedItems.add(new StoneComponent(contentBuilder.getContent()).process(s -> s.setId(item))
                        .process(s -> s.addMouseListener(mouseListener)));
            }

            return renderedItems;
        }
    }
}

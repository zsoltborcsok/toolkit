package org.nting.toolkit.component;

import static org.nting.toolkit.component.ScrollComponent.ScrollBarPolicy.AS_NEEDED;
import static org.nting.toolkit.component.Selection.MULTI;
import static org.nting.toolkit.component.Selection.NEEDS_ONE;
import static org.nting.toolkit.component.Selection.SINGLE;
import static org.nting.toolkit.event.MouseEvent.MouseButton.BUTTON_LEFT;
import static org.nting.toolkit.ui.Colors.DARK_GREY;
import static org.nting.toolkit.ui.Colors.GREY;
import static org.nting.toolkit.ui.stone.TextContentSingleLine.textContent;
import static playn.core.util.SimpleMessageFormat.format;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.nting.data.Property;
import org.nting.data.Registration;
import org.nting.data.binding.Bindings;
import org.nting.data.property.ListProperty;
import org.nting.data.query.DataProvider;
import org.nting.data.query.Query;
import org.nting.toolkit.component.renderer.ItemRenderer;
import org.nting.toolkit.event.KeyEvent;
import org.nting.toolkit.event.KeyListener;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.event.MouseMotionEvent;
import org.nting.toolkit.layout.AbsoluteLayout;
import org.nting.toolkit.layout.FormLayout;
import org.nting.toolkit.ui.stone.PaddedContent;
import org.nting.toolkit.ui.stone.TextContent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import playn.core.Canvas;
import playn.core.Key;
import pythagoras.f.Dimension;
import pythagoras.f.MathUtil;
import pythagoras.f.Rectangle;

public class ListComponent<T> extends Panel {

    @SuppressWarnings("rawtypes")
    private static Query EMPTY_QUERY = new Query(null, null);

    public final Property<Selection> selection = createProperty("selection", Selection.SINGLE);
    /** The pageLength defines the preferred height. If height is configured by the layout then it's better to set 0. */
    public final Property<Integer> pageLength = createProperty("pageLength", 8);
    public final Property<String> noItemsInfo = createProperty("noItemsInfo", "");
    public final ListProperty<T> selectedItems = createListProperty("selectedItems");
    public final Property<Boolean> enabled = createProperty("enabled", true);

    // Styleable properties
    public final Property<Integer> color = createProperty("color", DARK_GREY);
    public final Property<Integer> secondaryColor = createProperty("secondaryColor", GREY);
    public final Property<ListCellRenderer> listCellRenderer = createProperty("listCellRenderer", null);

    private final Property<Integer> focusedIndex = createProperty("focusedIndex", -1);
    private final Property<Integer> hoveredIndex = createProperty("hoveredIndex", -1);
    private final Property<Integer> itemCount = createProperty("itemCount", 0);

    private DataProvider<T> dataProvider;
    private List<T> items;
    private ItemRenderer<T> itemRenderer;
    private boolean fetchIsLocked = false;

    private final ScrollPane scrollPane = new ScrollPane(new ListScrollView(), AS_NEEDED, AS_NEEDED, false);

    public ListComponent(ItemRenderer<T> itemRenderer) {
        super(new FormLayout("pref:grow", "0px:grow"));

        addComponent(scrollPane, FormLayout.xy(0, 0));
        setItemRenderer(itemRenderer);

        selection.addValueChangeListener(event -> {
            Selection selection = event.getValue();
            switch (selection) {
            case NEEDS_ONE:
                if (selectedItems.size() == 0 && items != null && 0 < items.size()) {
                    selectedItems.add(items.get(0));
                }
                // Ensure single selection as well, so no break here.
            case SINGLE:
                if (1 < selectedItems.size()) {
                    T firstSelectedItem = selectedItems.get(0);
                    selectedItems.removeIf(item -> item != firstSelectedItem);
                }
                break;
            default:
                break;
            }
        });
        focusedIndex.addValueChangeListener(event -> {
            if (0 <= event.getValue()) {
                scrollToIndex(event.getValue());
            }
        });
        selectedItems.addValueChangeListener(event -> updateLayout());
        pageLength.addValueChangeListener(event -> updateLayout());
        itemCount.addValueChangeListener(event -> updateLayout());
    }

    public void setItemRenderer(ItemRenderer<T> itemRenderer) {
        Preconditions.checkNotNull(itemRenderer);
        this.itemRenderer = itemRenderer;
        updateLayout();
    }

    public ItemRenderer<T> getItemRenderer() {
        return itemRenderer;
    }

    @SuppressWarnings("unchecked")
    public void setDataProvider(DataProvider<T> dataProvider) {
        DataProvider<T> prevDataProvider = this.dataProvider;
        this.dataProvider = dataProvider;
        itemCount.setValue(0);

        if (dataProvider == null) {
            items = null;
            return;
        }

        dataProvider.size(EMPTY_QUERY).then(size -> {
            itemCount.setValue((int) size);
            fetchItems(Query.DEFAULT_LIMIT, prevDataProvider);
        });
    }

    public DataProvider<T> getDataProvider() {
        return dataProvider;
    }

    @Override
    public int search(String searchText) {
        return itemRenderer.search(searchText, items);
    }

    @Override
    public void highlightMatch(int index) {
        scrollToItem(itemRenderer.highlightMatch(index));
        repaint();
    }

    public void selectItem(T item) {
        if (!selectedItems.contains(item)) {
            if (selection.valueEqualsAny(SINGLE, NEEDS_ONE)) {
                selectedItems.clear();
            }
            selectedItems.add(item);
        }
    }

    public boolean invertSelection(T item) {
        if (!selectedItems.contains(item)) {
            if (selection.valueEqualsAny(SINGLE, NEEDS_ONE)) {
                selectedItems.clear();
            }
            selectedItems.add(item);
            return true;
        } else {
            if (selection.valueEqualsAny(MULTI, SINGLE)) {
                selectedItems.remove(item);
            }
            return false;
        }
    }

    public int getColor() {
        return enabled.getValue() ? color.getValue() : secondaryColor.getValue();
    }

    private void fetchItems(int limit, DataProvider<T> prevDataProvider) {
        if (!fetchIsLocked) {
            fetchIsLocked = true;
            dataProvider.fetch(new Query<>(0, limit, null, null)).then(items -> {
                this.items = items;

                if (selectedItems.size() == 0 && 0 < items.size() && selection.valueEquals(NEEDS_ONE)) {
                    selectedItems.add(items.get(0));
                } else if (0 < selectedItems.size()) {
                    List<Object> selectedItemIds = selectedItems.stream().map(prevDataProvider::getId)
                            .collect(Collectors.toList());
                    selectedItems.clear();
                    selectedItems
                            .addAll(items.stream().filter(item -> selectedItemIds.contains(dataProvider.getId(item)))
                                    .collect(Collectors.toList()));
                }
                fetchIsLocked = false;
            });
        }
    }

    @SuppressWarnings("unchecked")
    private void fetchMoreItems() {
        if (items.size() < itemCount.getValue() && !fetchIsLocked) {
            int lastLimit = (int) ((Math.ceil(items.size() / (double) Query.DEFAULT_LIMIT)) * Query.DEFAULT_LIMIT);
            fetchItems(lastLimit + Query.DEFAULT_LIMIT, dataProvider);
            // Update itemCount as well (not just the list of items)
            dataProvider.size(EMPTY_QUERY).then(size -> itemCount.setValue((int) size));
        }
    }

    // It recalculates the preferredSize of the scrollView based on the itemCount and the itemRenderer
    private void updateLayout() {
        Dimension itemPreferredSize = itemRenderer.getPreferredSize(this, false);
        Dimension selectedItemPreferredSize = itemRenderer.getPreferredSize(this, true);
        float maxPreferredWidth = Math.max(itemPreferredSize.width, selectedItemPreferredSize.width);
        float sumHeight = itemPreferredSize.height * itemCount.getValue()
                + selectedItems.size() * (selectedItemPreferredSize.height - itemPreferredSize.height);

        scrollPane.getView().setLayoutManager(new AbsoluteLayout(maxPreferredWidth, sumHeight));
        scrollPane.setGridSize(itemPreferredSize.height);

        int height = MathUtil.iceil(itemPreferredSize.height * pageLength.getValue());
        setLayoutManager(new FormLayout("pref:grow", format("{}px:grow", height)));
        setLayoutConstraint(scrollPane, FormLayout.xy(0, 0));
    }

    private void doPaintCell(Canvas canvas, ListCellRenderer cellRenderer, Dimension cellSize, int index) {
        boolean selected = selectedItems.contains(items.get(index));
        boolean focused = focusedIndex.valueEquals(index);
        boolean hovered = hoveredIndex.valueEquals(index);

        if (cellRenderer != null) {
            cellRenderer.paintCellBackground(canvas, cellSize, focused, selected, hovered, index % 2 == 0, this);
        }
        itemRenderer.paint(this, items.get(index), canvas, cellSize, selected);
        if (cellRenderer != null) {
            cellRenderer.paintCellForeground(canvas, cellSize, focused, selected, hovered, index % 2 == 0, this);
        }
    }

    @SuppressWarnings("unchecked")
    private void scrollToIndex(int index) {
        ListScrollView scrollView = (ListScrollView) scrollPane.getView();
        List<Float> itemPositions = Stream.concat(Stream.of(0.0f), scrollView.itemPositions.stream())
                .collect(Collectors.toList());
        if (index + 1 < itemPositions.size()) {
            float viewPositionX = scrollPane.getViewPosition().x;
            scrollPane.scrollRectToVisible(new Rectangle(viewPositionX, itemPositions.get(index), 0,
                    itemPositions.get(index + 1) - itemPositions.get(index)));
        }
    }

    private void scrollToItem(T item) {
        scrollToIndex(items.indexOf(item));
    }

    public interface ListCellRenderer {

        void paintCellBackground(Canvas canvas, Dimension size, boolean selected, boolean focused, boolean hovered,
                boolean even, ListComponent<?> listComponent);

        void paintCellForeground(Canvas canvas, Dimension size, boolean selected, boolean focused, boolean hovered,
                boolean even, ListComponent<?> listComponent);
    }

    private class ListScrollView extends Panel {

        private final List<Float> itemPositions = Lists.newArrayList();

        public ListScrollView() {
            EventHandler eventHandler = new EventHandler();
            List<Registration> eventHandlerRegistrations = Lists.newLinkedList();
            Bindings.bind(enabled, event -> {
                if (event.getValue()) {
                    eventHandlerRegistrations.add(addMouseListener(eventHandler));
                    eventHandlerRegistrations.add(addKeyListener(eventHandler));
                } else {
                    eventHandlerRegistrations.forEach(Registration::remove);
                    eventHandlerRegistrations.clear();
                }
            });
        }

        @Override
        public boolean isFocusable() {
            return enabled.getValue();
        }

        @Override
        public void doPaintComponent(Canvas canvas) {
            super.doPaintComponent(canvas);

            if (itemCount.getValue() == 0) {
                TextContent textContent = textContent(font.getValue(), getColor(), noItemsInfo.getValue());
                new PaddedContent(textContent, 1, 1, 1, 1).paint(canvas, getSize());
            } else {
                itemPositions.clear();
                ListCellRenderer cellRenderer = listCellRenderer.getValue();
                Dimension itemPreferredSize = itemRenderer.getPreferredSize(ListComponent.this, false);
                Dimension selectedItemPreferredSize = itemRenderer.getPreferredSize(ListComponent.this, true);

                float yPosition = 0;
                for (int i = 0; i < itemCount.getValue(); i++) {
                    if (items.size() <= i) { // Load more items
                        fetchMoreItems();
                        break;
                    }

                    boolean selectedItem = selectedItems.contains(items.get(i));
                    float itemHeight = selectedItem ? selectedItemPreferredSize.height : itemPreferredSize.height;
                    if (-y.getValue() - itemHeight <= yPosition
                            && yPosition <= (-y.getValue() + scrollPane.height.getValue())) {
                        canvas.translate(0, yPosition);
                        try {
                            doPaintCell(canvas, cellRenderer, new Dimension(width.getValue(), itemHeight), i);
                        } finally {
                            canvas.translate(0, -yPosition);
                        }
                    }

                    yPosition += itemHeight;
                    itemPositions.add(yPosition);
                }
            }
        }

        public int getItemIndex(float y) {
            int index = Arrays.binarySearch(itemPositions.toArray(), y);
            if (index < 0) {
                index = -index - 1;
            }
            return index;
        }
    }

    private class EventHandler implements MouseListener, KeyListener {

        private Integer lastSelectedItemIndex = null;

        @SuppressWarnings("unchecked")
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == BUTTON_LEFT) {
                int itemIndex = ((ListScrollView) scrollPane.getView())
                        .getItemIndex(e.getY() - scrollPane.getPosition().y);
                if (itemIndex < items.size()) {
                    focusedIndex.setValue(itemIndex);

                    if (selection.getValue() == MULTI) {
                        if (e.isControlPressed()) {
                            if (invertSelection(items.get(itemIndex))) {
                                lastSelectedItemIndex = itemIndex;
                            }
                        } else if (e.isShiftPressed() && lastSelectedItemIndex != null) {
                            handleSelection(e.isShiftPressed());
                        } else {
                            selectedItems.clear();
                            selectItem(items.get(itemIndex));
                            lastSelectedItemIndex = itemIndex;
                        }
                    } else if (selection.getValue() == SINGLE) {
                        if (e.isControlPressed()) {
                            invertSelection(items.get(itemIndex));
                        } else {
                            selectItem(items.get(itemIndex));
                        }
                    } else if (selection.getValue() == NEEDS_ONE) {
                        selectItem(items.get(itemIndex));
                    }
                }
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void mouseMoved(MouseMotionEvent e) {
            int itemIndex = ((ListScrollView) scrollPane.getView()).getItemIndex(e.getY() - scrollPane.getPosition().y);
            hoveredIndex.setValue(itemIndex);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            hoveredIndex.setValue(-1);
        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (itemCount.getValue() == 0) { // noinspection UnnecessaryReturnStatement
                return;
            } else if (keyEvent.isKeyCode(Key.DOWN)) {
                focusNext();
                handleSelection(keyEvent.isShiftPressed());
            } else if (keyEvent.isKeyCode(Key.UP)) {
                focusPrev();
                handleSelection(keyEvent.isShiftPressed());
            } else if (keyEvent.isKeyCode(Key.PAGE_DOWN)) {
                focusNextPage();
                handleSelection(keyEvent.isShiftPressed());
            } else if (keyEvent.isKeyCode(Key.PAGE_UP)) {
                focusPrevPage();
                handleSelection(keyEvent.isShiftPressed());
            }
        }

        private void handleSelection(boolean isShiftPressed) {
            if (selection.getValue() == MULTI && isShiftPressed) {
                int from = lastSelectedItemIndex;
                int to = focusedIndex.getValue();
                if (to < from) {
                    int temp = from;
                    from = to;
                    to = temp;
                }

                selectedItems.clear();
                for (int i = from; i <= to; i++) {
                    selectItem(items.get(i));
                }
            } else {
                if (selection.getValue() == MULTI) {
                    selectedItems.clear();
                }
                selectItem(items.get(focusedIndex.getValue()));
                lastSelectedItemIndex = focusedIndex.getValue();
            }
        }

        private void focusNext() {
            focusedIndex.setValue(Math.min(items.size() - 1, focusedIndex.getValue() + 1));
        }

        private void focusPrev() {
            focusedIndex.setValue(Math.max(0, focusedIndex.getValue() - 1));
        }

        private void focusNextPage() {
            int currentPageLength = calculateCurrentPageLength();
            if (1 < currentPageLength) {
                focusedIndex.setValue(Math.min(items.size() - 1, focusedIndex.getValue() + currentPageLength - 1));
            }
        }

        private void focusPrevPage() {
            int currentPageLength = calculateCurrentPageLength();
            if (1 < currentPageLength) {
                focusedIndex.setValue(Math.max(0, focusedIndex.getValue() - currentPageLength + 1));
            }
        }

        @SuppressWarnings("unchecked")
        private int calculateCurrentPageLength() {
            ListScrollView listScrollView = (ListScrollView) scrollPane.getView();
            return listScrollView.getItemIndex(height.getValue() + font.getValue().size())
                    - listScrollView.getItemIndex(0);
        }
    }
}

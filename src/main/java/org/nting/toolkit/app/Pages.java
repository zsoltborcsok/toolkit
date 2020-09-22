package org.nting.toolkit.app;

import static org.nting.toolkit.ToolkitServices.toolkitManager;
import static org.nting.toolkit.ToolkitServices.unitConverter;
import static org.nting.toolkit.ui.Colors.DARK_GREY;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.nting.data.Property;
import org.nting.data.util.Pair;
import org.nting.toolkit.Component;
import org.nting.toolkit.component.AbstractComponent;
import org.nting.toolkit.component.Separator;
import org.nting.toolkit.layout.LayoutManager;
import org.nting.toolkit.ui.ComponentUI;
import org.nting.toolkit.ui.shape.LineShape;

import com.google.common.collect.Lists;

import playn.core.Canvas;
import pythagoras.f.Dimension;
import pythagoras.f.MathUtil;

public class Pages extends AbstractComponent {

    public enum PageSize {
        SINGLE_COLUMN, DOUBLE_COLUMN, FULL;

        public int getSize(int max) {
            switch (this) {
            case SINGLE_COLUMN:
                return 1;
            case DOUBLE_COLUMN:
                return 2;
            case FULL:
                return max;
            default:
                return 0;
            }
        }
    }

    public final Property<Integer> color = createProperty("color", DARK_GREY);

    public Pages() {
        this(new PagesLayout());
    }

    public Pages(float preferredWidth, float preferredHeight) {
        this(new PagesLayout(preferredWidth, preferredHeight));
    }

    private Pages(LayoutManager layoutManager) {
        super.setLayoutManager(layoutManager);
        setFocusable(false);

        color.setValue(toolkitManager().getStyleInjector().getPropertyValue(Separator.class.getName(), "color"));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setComponentUI(ComponentUI componentUI) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void doPaintForeground(Canvas canvas) {
        List<Component> components = getComponents();
        for (int i = 1; i < components.size(); i++) {
            if (!components.get(i - 1).isVisible()) {
                continue;
            }

            Component page = components.get(i);
            float x = page.getPosition().x - 1;
            new LineShape(x, 0, x, page.getSize().height).strokeWidth(1).strokeColor(color.getValue()).paint(canvas);
        }
    }

    public Pages addPage(Component page, PageSize pageSize) {
        addComponent(page, pageSize);
        return this;
    }

    public Pages addPage(IPageFactory pageFactory) {
        return addPage(pageFactory.createContent(this), pageFactory.getPageSize());
    }

    public Component getNextPage(Component sourcePage) {
        List<Component> components = getComponents();
        for (int i = 0; i < components.size() - 1; i++) {
            if (sourcePage == components.get(i)) {
                return components.get(i + 1);
            }
        }

        return null;
    }

    public Component getPreviousPage(Component sourcePage) {
        List<Component> components = getComponents();
        for (int i = 1; i < components.size(); i++) {
            if (sourcePage == components.get(i)) {
                return components.get(i - 1);
            }
        }

        return null;
    }

    public void removePage(Component page) {
        removeComponent(page);
    }

    /**
     * Remove the provided page and all the next ones.
     */
    public void removePages(Component page) {
        List<Component> components = getComponents();
        if (components.contains(page)) {
            for (int i = components.size() - 1; 0 <= i; i--) {
                Component iPage = components.get(i);
                removePage(iPage);
                if (page == iPage) {
                    break;
                }
            }
        }
    }

    public void removeNextPages(Component page) {
        List<Component> components = getComponents();
        if (components.contains(page)) {
            for (int i = components.size() - 1; 0 <= i; i--) {
                Component iPage = components.get(i);
                if (page == iPage) {
                    break;
                }

                removePage(iPage);
            }
        }
    }

    public Component getLastPage() {
        List<Component> components = getComponents();
        return components.get(components.size() - 1);
    }

    private static class PagesLayout implements LayoutManager {

        private static final int SMALL_MEDIUM_DELIMITER = 360; // Do we have space for 90 chars?
        private static final int MEDIUM_LARGE_DELIMITER = 720; // Do we have space for 180 chars?

        private final Dimension preferredLayoutSize;

        public PagesLayout() {
            this(-1, -1); // Fill
        }

        public PagesLayout(float preferredWidth, float preferredHeight) {
            this.preferredLayoutSize = new Dimension(preferredWidth, preferredHeight);
        }

        @Override
        public void layout(Component component) {
            int dluSizeX = dluSizeX(component);
            int columnCount = dluSizeX < SMALL_MEDIUM_DELIMITER ? 1 : dluSizeX < MEDIUM_LARGE_DELIMITER ? 3 : 5;
            List<Component> visiblePages = getVisiblePages(component, columnCount);
            Map<Component, Float> pagePercentages = calculatePagePercentages(component, visiblePages, columnCount);

            Dimension size = component.getSize();
            int position = 0;
            for (Component child : component.getComponents()) {
                if (!visiblePages.contains(child)) {
                    child.setVisible(false);
                    continue;
                }

                child.setVisible(true);
                setComponentPosition(child, position, 0);
                float pageWidth = MathUtil.iceil(size.width * pagePercentages.get(child));
                setComponentSize(child, pageWidth - 1, size.height);
                position += pageWidth;
            }
        }

        @Override
        public Dimension preferredLayoutSize(Component component) {
            Dimension prefSize = new Dimension(preferredLayoutSize);
            if (component.getParent() != null) {
                if (prefSize.width < 0) {
                    prefSize.width = component.getParent().getSize().width;
                }
                if (prefSize.height < 0) {
                    prefSize.height = component.getParent().getSize().height;
                }
            }

            return prefSize;
        }

        private int dluSizeX(Component component) {
            float xUnit = unitConverter().dialogUnitXAsPixel(component);
            return Math.round(component.getSize().width / xUnit);
        }

        public List<Component> getVisiblePages(Component component, int columnCount) {
            List<Component> pages = Lists.reverse(component.getComponents());
            List<Component> visiblePages = Lists.newLinkedList();

            int viewColumns = 0;
            for (Component page : pages) {
                int newPageColumns = viewColumns
                        + ((PageSize) component.getLayoutConstraints(page)).getSize(columnCount);

                if (columnCount < newPageColumns) {
                    if (visiblePages.size() == 0) {
                        visiblePages.add(page);
                    }
                    break;
                } else if (columnCount == newPageColumns) {
                    visiblePages.add(page);
                    break;
                } else {
                    viewColumns = newPageColumns;
                    visiblePages.add(page);
                }
            }
            return Lists.reverse(visiblePages);
        }

        private Map<Component, Float> calculatePagePercentages(Component component, List<Component> pages,
                int columnCount) {
            List<Pair<Component, Integer>> pageSizes = pages.stream()
                    .map(page -> Pair.of(page, ((PageSize) component.getLayoutConstraints(page)).getSize(columnCount)))
                    .collect(Collectors.toList());
            int pageColumnCount = pageSizes.stream().map(pageSize -> pageSize.second).reduce(0, Integer::sum);
            return pageSizes.stream().collect(Collectors.toMap(pageSize -> pageSize.first,
                    pageSize -> pageSize.second / (float) pageColumnCount));
        }
    }
}

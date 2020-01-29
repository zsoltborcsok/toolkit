package org.nting.toolkit.layout;

import java.util.List;

import org.nting.toolkit.Component;

import pythagoras.f.Dimension;

public class AbsoluteLayout implements LayoutManager {

    private static final Object CUSTOM_POSITION_CONSTRAINT = new Object();
    private static final Object CUSTOM_POSITION_AND_SIZE_CONSTRAINT = new Object();

    private final Dimension preferredLayoutSize;

    public AbsoluteLayout() {
        this(-1, -1);// Fill
    }

    public AbsoluteLayout(float width, float height) {
        this.preferredLayoutSize = new Dimension(width, height);
    }

    @Override
    public void layout(Component component) {
        List<Component> children = component.getComponents();
        for (Component child : children) {
            PositionConstraints constraints;

            Object childConstraints = component.getLayoutConstraints(child);
            if (childConstraints == CUSTOM_POSITION_AND_SIZE_CONSTRAINT) {
                continue;
            } else if (childConstraints == CUSTOM_POSITION_CONSTRAINT) {
                Dimension preferredSize = child.getPreferredSize();
                setComponentSize(child, preferredSize.width, preferredSize.height);
                continue;
            } else if (childConstraints instanceof PositionConstraints) {
                constraints = (PositionConstraints) childConstraints;
                constraints = new PositionConstraints(constraints.x, constraints.y, constraints.width,
                        constraints.height);
                if (constraints.width < 0) {
                    constraints.width += component.getSize().width;
                }
                if (constraints.height < 0) {
                    constraints.height += component.getSize().height;
                }
                if (constraints.width == 0 || constraints.height == 0) {
                    Dimension preferredSize = child.getPreferredSize();
                    if (constraints.width == 0) {
                        if (preferredSize.width > 0) {
                            constraints.width = preferredSize.width;
                        } else {
                            constraints.width = component.getSize().width;
                        }
                    }
                    if (constraints.height == 0) {
                        if (preferredSize.height > 0) {
                            constraints.height = preferredSize.height;
                        } else {
                            constraints.height = component.getSize().height;
                        }
                    }
                }
            } else {
                Dimension preferredSize = child.getPreferredSize();
                constraints = new PositionConstraints(0, 0, preferredSize.width, preferredSize.height);
            }

            setComponentPosition(child, constraints.x, constraints.y);
            setComponentSize(child, constraints.width, constraints.height);
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

    public static PositionConstraints xy(float x, float y) {
        return new PositionConstraints(x, y, 0, 0);
    }

    public static PositionConstraints xyw(float x, float y, float width) {
        return new PositionConstraints(x, y, width, 0);
    }

    public static PositionConstraints xywh(float x, float y, float width, float height) {
        return new PositionConstraints(x, y, width, height);
    }

    public static Object customPosition() {
        return CUSTOM_POSITION_CONSTRAINT;
    }

    public static Object customPositionAndSize() {
        return CUSTOM_POSITION_AND_SIZE_CONSTRAINT;
    }

    public static class PositionConstraints {
        private float x, y, width, height;

        public PositionConstraints(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o == null || getClass() != o.getClass()) {
                return false;
            }

            PositionConstraints that = (PositionConstraints) o;
            return height == that.height && width == that.width && x == that.x && y == that.y;
        }
    }
}

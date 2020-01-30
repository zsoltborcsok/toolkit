package org.nting.toolkit.component;

import static org.nting.toolkit.ToolkitServices.toolkitManager;

import org.nting.data.Property;
import org.nting.toolkit.Component;
import org.nting.toolkit.layout.AbsoluteLayout;
import org.nting.toolkit.layout.LayoutManager;
import org.nting.toolkit.util.ToolkitUtils;

import pythagoras.f.Dimension;
import pythagoras.f.Point;
import pythagoras.f.Rectangle;

public class StandardPopup extends Popup {

    private final Alignment alignment;
    private final Orientation orientation;

    public final Property<Integer> padding = createProperty("padding", 10);

    public StandardPopup(Alignment alignment, Orientation orientation) {
        this(alignment, orientation, new AbsoluteLayout());
    }

    public StandardPopup(Alignment alignment, Orientation orientation, LayoutManager layoutManager) {
        this.alignment = alignment;
        this.orientation = orientation;

        setLayoutManager(layoutManager);
    }

    public void showRelativeTo(Component component) {
        showRelativeTo(component, padding.getValue());
    }

    public void showRelativeTo(Rectangle rectangle) {
        showRelativeTo(rectangle.location(), rectangle.size(), padding.getValue());
    }

    protected void showRelativeTo(Component component, int padding) {
        showRelativeTo(ToolkitUtils.getRootPosition(component), component.getSize(), padding);
    }

    protected void showRelativeTo(Point rPosition, Dimension rSize, int padding) {
        rPosition.set(Math.round(rPosition.x), Math.round(rPosition.y));

        Dimension preferredSize = getPreferredSize();
        Dimension rootSize = toolkitManager().root().getSize();

        boolean top = alignment.isTop();
        boolean right = !alignment.isLeft();
        if (orientation == Orientation.HORIZONTAL) {
            preferredSize.width += padding;

            if (top && rootSize.height < rPosition.y + preferredSize.height) {
                top = false;
            } else if (!top && rPosition.y + rSize.height - preferredSize.height < 0) {
                top = true;
            }

            if (right && rootSize.width < rPosition.x + rSize.width + preferredSize.width) {
                right = false;
            } else if (!right && rPosition.x - preferredSize.width < 0) {
                right = true;
            }
        } else {
            preferredSize.height += padding;

            if (top && rPosition.y - preferredSize.height < 0) {
                top = false;
            } else if (!top && rootSize.height < rPosition.y + rSize.height + preferredSize.height) {
                top = true;
            }

            if (right && rPosition.x + rSize.width - preferredSize.width < 0) {
                right = false;
            } else if (!right && rootSize.width < rPosition.x + preferredSize.width) {
                right = true;
            }
        }

        if (orientation == Orientation.HORIZONTAL) {
            if (top) {
                y.setValue(rPosition.y + 1);
            } else {
                y.setValue(rPosition.y + rSize.height - preferredSize.height - 1);
            }

            if (right) {
                x.setValue(rPosition.x + rSize.width + padding);
            } else {
                x.setValue(rPosition.x - preferredSize.width);
            }

            preferredSize.width -= padding;
        } else {
            if (top) {
                y.setValue(rPosition.y - preferredSize.height);
            } else {
                y.setValue(rPosition.y + rSize.height + padding);
            }

            if (right) {
                x.setValue(rPosition.x + rSize.width - preferredSize.width - 1);
            } else {
                x.setValue(rPosition.x + 1);
            }

            preferredSize.height -= padding;
        }

        width.setValue(preferredSize.width);
        height.setValue(preferredSize.height);

        toolkitManager().root().setPopupVisible(this, true);
        requestFocus();
    }

    public void showAt(float xPos, float yPos) {
        Dimension preferredSize = getPreferredSize();
        x.setValue(xPos);
        y.setValue(yPos);
        width.setValue(preferredSize.width);
        height.setValue(preferredSize.height);

        toolkitManager().root().setPopupVisible(this, true);
        requestFocus();
    }

    public int getType() {
        if (alignment.isTop() && alignment.isLeft()) {
            return orientation.ordinal();
        } else if (alignment.isTop()) {
            return 3 - orientation.ordinal();
        } else if (!alignment.isLeft()) {
            return 4 + orientation.ordinal();
        } else {
            return 7 - orientation.ordinal();
        }
    }
}

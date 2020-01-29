package org.nting.toolkit.layout;

import static org.nting.toolkit.ToolkitServices.unitConverter;

import java.util.List;

import org.nting.toolkit.Component;

import com.google.common.primitives.Floats;

import pythagoras.f.Dimension;

public class ZLayout implements LayoutManager {

    private final float width;
    private final float gap;
    private final boolean isDLU;

    public ZLayout(float width, float gap) {
        this(width, gap, true);
    }

    public ZLayout(float width, float gap, boolean isDLU) {
        this.width = width;
        this.gap = gap;
        this.isDLU = isDLU;
    }

    @Override
    public void layout(Component component) {
        float width = isDLU ? unitConverter().dialogUnitXAsPixel(this.width, component) : this.width;
        float gap = isDLU ? unitConverter().dialogUnitXAsPixel(this.gap, component) : this.gap;
        int columnCount = (int) Math.max(1, (component.getSize().width + gap) / (width + gap));

        float[] columnPositions = new float[columnCount + 1];
        for (int i = 0; i < columnPositions.length; i++) {
            columnPositions[i] = Math.round((component.getSize().width + gap) / columnCount * i);
        }

        float[] heights = new float[columnCount];
        List<Component> children = component.getComponents();
        for (Component child : children) {
            int column = Floats.indexOf(heights, Floats.min(heights));
            float height = child.getPreferredSize().height;

            setComponentPosition(child, columnPositions[column], heights[column]);
            setComponentSize(child, columnPositions[column + 1] - columnPositions[column] - gap, height);

            heights[column] += height;
        }
    }

    @Override
    public Dimension preferredLayoutSize(Component component) {
        int columnCount = 1;
        float width = isDLU ? unitConverter().dialogUnitXAsPixel(this.width, component) : this.width;
        float gap = isDLU ? unitConverter().dialogUnitXAsPixel(this.gap, component) : this.gap;
        if (width <= component.getSize().width) {
            columnCount = (int) ((component.getSize().width + gap) / (width + gap));
            width = component.getSize().width;
        }

        float[] heights = new float[columnCount];
        for (Component child : component.getComponents()) {
            heights[Floats.indexOf(heights, Floats.min(heights))] += child.getPreferredSize().height;
        }

        return new Dimension(width, Floats.max(heights));
    }
}

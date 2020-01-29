package org.nting.toolkit.layout;

import static org.nting.toolkit.ToolkitServices.unitConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.nting.toolkit.Component;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import pythagoras.f.Dimension;

public class FlowLayout implements LayoutManager {

    private final float hGap, vGap, width;
    private final boolean isDLU;
    private float defaultWidth = -1;

    public FlowLayout(float hGap, float vGap, boolean isDLU) {
        this(hGap, vGap, -1, isDLU);
    }

    public FlowLayout(float hGap, float vGap, float width, boolean isDLU) {
        this.hGap = hGap;
        this.vGap = vGap;
        this.width = width;
        this.isDLU = isDLU;
    }

    public FlowLayout defaultWidth(float defaultWidth) {
        this.defaultWidth = defaultWidth;
        return this;
    }

    @Override
    public void layout(Component component) {
        float hGap = isDLU ? unitConverter().dialogUnitXAsPixel(this.hGap, component) : this.hGap;
        float vGap = isDLU ? unitConverter().dialogUnitYAsPixel(this.vGap, component) : this.vGap;
        float width = getEffectiveWidth(component);

        List<Component> children = component.getComponents();
        ListMultimap<Integer, Float> heightsPerRow = calculateHeightsPerRows(component, hGap, width);

        float y = 0;
        int childIndex = 0;
        for (int i = 0; i < heightsPerRow.keySet().size(); i++) {
            Collection<Float> heights = heightsPerRow.get(i);
            float currentHeight = Collections.max(heights);

            float x = 0;
            for (int j = 0; j < heights.size(); j++) {
                Component child = children.get(childIndex);
                float childWidth = getPreferredChildSize(component, child).width;

                setComponentPosition(child, x, y);
                setComponentSize(child, childWidth, currentHeight);

                childIndex++;
                x += hGap + childWidth;
            }
            y += vGap + currentHeight;
        }
    }

    @Override
    public Dimension preferredLayoutSize(Component component) {
        float hGap = isDLU ? unitConverter().dialogUnitXAsPixel(this.hGap, component) : this.hGap;
        float vGap = isDLU ? unitConverter().dialogUnitYAsPixel(this.vGap, component) : this.vGap;
        float width = getEffectiveWidth(component);

        ListMultimap<Integer, Float> heightsPerRow = calculateHeightsPerRows(component, hGap, width);
        float height = 1 <= heightsPerRow.size() ? -vGap : 0;
        for (Collection<Float> heights : heightsPerRow.asMap().values()) {
            height += vGap + Collections.max(heights);
        }

        return new Dimension(width, height);
    }

    private ListMultimap<Integer, Float> calculateHeightsPerRows(Component component, float hGap, float width) {
        ListMultimap<Integer, Float> heightsPerRow = ArrayListMultimap.create();

        float w = 0;
        int row = 0;
        for (Component child : component.getComponents()) {
            if (!child.isVisible()) {
                continue;
            }

            Dimension preferredChildSize = getPreferredChildSize(component, child);
            if (w == 0 && width < hGap + preferredChildSize.width) {
                heightsPerRow.put(row++, preferredChildSize.height);
            } else if (width < w + hGap + preferredChildSize.width) {
                heightsPerRow.put(++row, preferredChildSize.height);
                w = preferredChildSize.width;
            } else {
                heightsPerRow.put(row, preferredChildSize.height);
                if (0 < w) {
                    w += hGap;
                }
                w += preferredChildSize.width;
            }
        }

        return heightsPerRow;
    }

    private Dimension getPreferredChildSize(Component component, Component child) {
        Object constraints = component.getLayoutConstraints(child);
        if (constraints instanceof Integer) {
            return new Dimension((Integer) constraints, child.getPreferredSize().height);
        } else {
            return child.getPreferredSize();
        }
    }

    private float getEffectiveWidth(Component component) {
        if (this.width < 0) {
            float componentWidth = component.getSize().width;
            if (0 < componentWidth || defaultWidth < 0) {
                return componentWidth;
            } else
                return (isDLU ? unitConverter().dialogUnitXAsPixel(this.defaultWidth, component) : this.defaultWidth);
        } else {
            return (isDLU ? unitConverter().dialogUnitXAsPixel(this.width, component) : this.width);
        }
    }
}

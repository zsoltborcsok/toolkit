package org.nting.toolkit.component;

import static org.nting.toolkit.util.ToolkitUtils.getAllComponents;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.nting.toolkit.Component;
import org.nting.toolkit.layout.LayoutManager;
import org.nting.toolkit.ui.ComponentUI;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import playn.core.Canvas;
import pythagoras.f.Dimension;

/*
 * Use integers as layout constraints in order to define the layer index.
 */
public class LayeredPane extends AbstractComponent {

    public LayeredPane() {
        setLayoutManager(new LayeredLayout());
    }

    @Override
    public void addComponent(Component child, Object constraints) {
        Component componentAtLayer = getComponentAtLayer((Integer) constraints);
        if (componentAtLayer != null) {
            removeComponent(componentAtLayer);
        }

        super.addComponent(child, constraints);
    }

    @Override
    public void setLayoutConstraint(Component child, Object constraints) {
        Preconditions.checkArgument(constraints instanceof Integer);
        super.setLayoutConstraint(child, constraints);
    }

    public <C extends Component> C getComponentAtLayer(int layerIndex) {
        return componentAt(layerIndex);
    }

    @Override
    public boolean isDirty() {
        if (!super.isDirty()) {
            if (getAllComponents(this).stream().filter(c -> c != this).anyMatch(Component::isDirty)) {
                repaint(); // Repaint the whole LayeredPane if any child component is dirty.
            }
        }

        return super.isDirty();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setComponentUI(ComponentUI componentUI) {
        throw new UnsupportedOperationException();
    }

    public void doPaintChildren(Canvas canvas) {
        for (Component child : getChildrenInAscendingLayerOrder()) {
            doPaintChild(child, canvas);
        }
    }

    public List<Component> getComponentsOnLayers() {
        return Lists.reverse(getChildrenInAscendingLayerOrder()).stream()
                .flatMap(child -> child.getComponents().stream()).collect(Collectors.toList());
    }

    private List<Component> getChildrenInAscendingLayerOrder() {
        return getComponents().stream()
                .sorted(Comparator.comparing(child -> (Integer) getLayoutConstraints(child), Integer::compareTo))
                .collect(Collectors.toList());
    }

    private static class LayeredLayout implements LayoutManager {

        @Override
        public void layout(Component component) {
            Dimension size = component.getSize();

            List<Component> children = component.getComponents();
            for (Component child : children) {
                setComponentPosition(child, 0, 0);
                setComponentSize(child, size.width, size.height);
            }
        }

        @Override
        public Dimension preferredLayoutSize(Component component) {
            Dimension preferredSize = new Dimension(0, 0);

            List<Component> children = component.getComponents();
            for (Component child : children) {
                Dimension childPreferredSize = child.getPreferredSize();
                if (preferredSize.width < childPreferredSize.width) {
                    preferredSize.width = childPreferredSize.width;
                }
                if (preferredSize.height < childPreferredSize.height) {
                    preferredSize.height = childPreferredSize.height;
                }
            }

            return preferredSize;
        }
    }
}

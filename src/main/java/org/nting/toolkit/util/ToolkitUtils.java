package org.nting.toolkit.util;

import static org.nting.toolkit.ToolkitServices.toolkitManager;
import static org.nting.toolkit.util.GwtCompatibleUtils.isAssignableFrom;

import java.util.List;

import org.nting.toolkit.Component;

import com.google.common.collect.Lists;

import pythagoras.f.Dimension;
import pythagoras.f.Point;

public class ToolkitUtils {

    public static Component getDeepestComponentInToolkit(float x, float y) {
        Component component = getDeepestComponentAt(toolkitManager().root(), x, y);
        return component != null ? component : toolkitManager().root();
    }

    public static Component getDeepestComponentAt(Component component, float x, float y) {
        return component; // TODO
    }

    public static Component getRoot(Component component) {
        while (component.getParent() != null) {
            component = component.getParent();
        }

        return component;
    }

    public static boolean isChildOfToolkitRoot(Component component) {
        return getRoot(component) == toolkitManager().root();
    }

    public static Point getRootPosition(Component component) {
        Point rootPosition = component.getPosition();
        while (component.getParent() != null) {
            component = component.getParent();
            Point position = component.getPosition();
            rootPosition = rootPosition.add(position.x, position.y);
        }

        return rootPosition;
    }

    public static Point getPosition(Component parent, Component component) {
        Point parentPosition = component.getPosition();
        while (component.getParent() != parent) {
            component = component.getParent();
            Point position = component.getPosition();
            parentPosition = parentPosition.add(position.x, position.y);
        }

        return parentPosition;
    }

    /** Return <code>true</code> if a component <code>a</code> descends from a component <code>b</code> */
    public static boolean isDescendingFrom(Component a, Component b) {
        while (a != null) {
            if (a == b) {
                return true;
            }

            a = a.getParent();
        }

        return false;
    }

    public static Dimension growDimension(Dimension dimension, float dx, float dy) {
        dimension.width += dx;
        dimension.height += dy;

        return dimension;
    }

    public static List<Component> getAllComponents() {
        return getAllComponents(toolkitManager().root());
    }

    public static List<Component> getAllComponents(Component root) {// Depth-first
        List<Component> components = Lists.newLinkedList();
        components.add(root);
        for (int i = 0; i < components.size(); i++) {
            if (!components.get(i).isVisible()) {
                components.remove(i--);
            } else {
                components.addAll(i + 1, components.get(i).getComponents());
            }
        }

        return components;
    }

    public static boolean requestFocus(Component component, Class<?> typeToFocus) {
        List<Component> components = Lists.newLinkedList();
        components.add(component);
        for (int i = 0; i < components.size(); i++) {
            Component currentComponent = components.get(i);
            if (!currentComponent.isVisible()) {
                components.remove(i--);
            } else if (currentComponent.isFocusable() && isAssignableFrom(typeToFocus, currentComponent.getClass())) {
                currentComponent.requestFocus();
                return true;
            } else {
                components.addAll(i + 1, currentComponent.getComponents());
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static <C extends Component> C getComponentById(Component parent, String... ids) {
        if (ids.length == 0) {
            return null;
        }

        List<Component> components = Lists.newLinkedList();
        components.add(parent);
        for (int i = 0; i < components.size(); i++) { // Breadth-first
            Component component = components.get(i);
            if (ids[0].equals(component.getId())) {
                if (ids.length == 1) {
                    return (C) component;
                } else {
                    return getComponentById(component, GwtCompatibleUtils.copyOfRange(ids, 1, ids.length));
                }
            } else {
                components.addAll(component.getComponents());
            }
        }

        return null;
    }

    public static float minOfDimensions(Component component) {
        Dimension size = component.getSize();
        return Math.min(size.width, size.height);
    }

    public static float maxOfDimensions(Component component) {
        Dimension size = component.getSize();
        return Math.max(size.width, size.height);
    }
}

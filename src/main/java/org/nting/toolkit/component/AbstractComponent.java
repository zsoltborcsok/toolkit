package org.nting.toolkit.component;

import java.util.List;
import java.util.Set;

import org.nting.data.Property;
import org.nting.data.bean.RuntimeBean;
import org.nting.data.property.ListProperty;
import org.nting.data.property.MapProperty;
import org.nting.data.property.ObjectProperty;
import org.nting.data.property.SetProperty;
import org.nting.data.util.Pair;
import org.nting.toolkit.Component;
import org.nting.toolkit.data.Properties;
import org.nting.toolkit.layout.LayoutManager;
import org.nting.toolkit.ui.ComponentUI;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import pythagoras.f.Dimension;
import pythagoras.f.Point;

public abstract class AbstractComponent implements Component, RuntimeBean {

    private final Properties properties = new Properties();

    public final Property<Float> x = createProperty("x", 0f);
    public final Property<Float> y = createProperty("y", 0f);
    public final Property<Float> width = createProperty("width", 0f);
    public final Property<Float> height = createProperty("height", 0f);
    public final Property<String> tooltipText = createProperty("tooltipText", null);
    public final Property<Boolean> visible = createProperty("visible", true);
    public final Property<Boolean> attached = createReadOnlyProperty("attached", false);

    private Pair<Alignment, Orientation> tooltipLocation = Pair.of(Alignment.TOP_LEFT, Orientation.VERTICAL);

    private Component parent;
    private final List<Component> components = Lists.newArrayList();
    private LayoutManager layoutManager;
    private final BiMap<Component, Object> layoutConstraintsMap = HashBiMap.create();
    @SuppressWarnings("rawtypes")
    private ComponentUI componentUI;

    private boolean focusable = true;
    private boolean focusNeutral = false;
    private boolean dirty = true;
    private boolean clip = false;
    private Object id;

    @Override
    public void setSize(float width, float height) {
        this.width.setValue(width);
        this.height.setValue(height);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(width.getValue(), height.getValue());
    }

    @Override
    public void setPosition(float x, float y) {
        this.x.setValue(x);
        this.y.setValue(y);
    }

    @Override
    public Point getPosition() {
        return new Point(x.getValue(), y.getValue());
    }

    @Override
    public void setTooltipText(String text) {
        tooltipText.setValue(text);
    }

    @Override
    public String getTooltipText() {
        return tooltipText.getValue();
    }

    @Override
    public void setTooltipLocation(Alignment alignment, Orientation orientation) {
        tooltipLocation = Pair.of(alignment, orientation);
    }

    @Override
    public Pair<Alignment, Orientation> getTooltipLocation() {
        return tooltipLocation;
    }

    @Override
    public void setParent(Component parent) {
        this.parent = parent;

        if (parent == null) {
            setAttached(false);
        } else {
            setAttached(parent.isAttached());
        }
    }

    @Override
    public Component getParent() {
        return parent;
    }

    private void setAttached(boolean attached) {
        if (attached == this.attached.getValue()) {
            return;
        }

        ((ObjectProperty<Boolean>) this.attached).forceValue(attached);
        for (Component component : getComponents()) {
            if (component instanceof AbstractComponent) {
                ((AbstractComponent) component).setAttached(attached);
            }
        }
    }

    @Override
    public boolean isAttached() {
        return attached.getValue();
    }

    @Override
    public void addComponent(Component child) {
        if (child.getParent() == this) {
            return;// Maybe it's just a replace of layout constraints.
        } else if (child.getParent() != null) {
            child.getParent().removeComponent(child);
        }

        components.add(child);
        child.setParent(this);
        reLayout();
    }

    @Override
    public void addComponent(Component child, Object constraints) {
        addComponent(child);
        layoutConstraintsMap.put(child, constraints);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C extends Component> C componentAt(Object constraints) {
        return (C) layoutConstraintsMap.inverse().get(constraints);
    }

    @Override
    public void removeComponent(Component child) {
        components.remove(child);
        layoutConstraintsMap.remove(child);
        child.setParent(null);
        reLayout();
    }

    @Override
    public List<Component> getComponents() {
        return ImmutableList.copyOf(components);
    }

    @Override
    public void setLayoutConstraint(Component child, Object constraints) {
        Preconditions.checkArgument(components.contains(child));

        layoutConstraintsMap.put(child, constraints);
    }

    @Override
    public Object getLayoutConstraints(Component component) {
        return layoutConstraintsMap.get(component);
    }

    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        layoutConstraintsMap.clear();

        reLayout();
    }

    @Override
    public LayoutManager getLayoutManager() {
        return layoutManager;
    }

    @Override
    public void reLayout() {
        Component c = this;
        while (c != null) {
            c.dirty();
            c = c.getParent();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void setComponentUI(ComponentUI componentUI) {
        if (this.componentUI != null) {
            this.componentUI.terminate(this);
        }

        if (componentUI == null) {
            this.componentUI = null;
        } else if (componentUI.isComponentSupported(this)) {
            this.componentUI = componentUI;
            componentUI.initialize(this);
        } else {
            throw new IllegalArgumentException(
                    getClass().getSimpleName() + " is not supported by " + componentUI.getClass().getSimpleName());
        }

        reLayout();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public ComponentUI getComponentUI() {
        return componentUI;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible.setValue(visible);
    }

    @Override
    public boolean isVisible() {
        return visible.getValue();
    }

    @Override
    public void dirty() {
        dirty = true;
    }

    @Override
    public void painted() {
        dirty = false;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    @Override
    public boolean isFocusable() {
        return focusable;
    }

    @Override
    public void setFocusNeutral(boolean focusNeutral) {
        this.focusNeutral = focusNeutral;
    }

    @Override
    public boolean isFocusNeutral() {
        return focusNeutral;
    }

    @Override
    public void setId(Object id) {
        this.id = id;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public void setClip(boolean clip) {
        this.clip = clip;
    }

    @Override
    public boolean isClip() {
        return clip;
    }

    public final <T> Property<T> createProperty(Object propertyId, T initialValue) {
        return properties.addObjectProperty(propertyId, initialValue);
    }

    public final <T> Property<T> createReadOnlyProperty(Object propertyId, T initialValue) {
        ObjectProperty<T> property = properties.addObjectProperty(propertyId, initialValue);
        property.setReadOnly(true);
        return property;
    }

    @SafeVarargs
    public final <T> ListProperty<T> createListProperty(Object propertyId, T... initialElements) {
        return properties.addListProperty(propertyId, Lists.newArrayList(initialElements));
    }

    @SafeVarargs
    public final <T> SetProperty<T> createSetProperty(Object propertyId, T... initialElements) {
        return properties.addSetProperty(propertyId, Lists.newArrayList(initialElements));
    }

    public final <K, V> MapProperty<K, V> addMapProperty(Object propertyId) {
        return properties.addMapProperty(propertyId);
    }

    public final boolean removeProperty(Object propertyId) {
        return properties.removeProperty(propertyId);
    }

    public final boolean hasProperty(Object propertyId) {
        return properties.getPropertyNames().contains(propertyId.toString());
    }

    @Override
    public final Set<String> getPropertyNames() {
        return properties.getPropertyNames();
    }

    @Override
    public final <T> Property<T> getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}

package org.nting.toolkit.component;

import static org.nting.data.binding.Bindings.BindingStrategy.READ;
import static org.nting.toolkit.ToolkitRunnable.createLoopedRunnable;
import static org.nting.toolkit.ToolkitServices.toolkitManager;
import static org.nting.toolkit.ToolkitServices.tooltipManager;
import static org.nting.toolkit.util.GwtCompatibleUtils.collectImplementedTypes;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.nting.data.Property;
import org.nting.data.Registration;
import org.nting.data.ValueChangeListener;
import org.nting.data.bean.RuntimeBean;
import org.nting.data.binding.Bindings;
import org.nting.data.inject.Injector;
import org.nting.data.property.BeanProperty;
import org.nting.data.property.ListProperty;
import org.nting.data.property.MapProperty;
import org.nting.data.property.ObjectProperty;
import org.nting.data.property.SetProperty;
import org.nting.data.util.Pair;
import org.nting.toolkit.Component;
import org.nting.toolkit.PaintableComponent;
import org.nting.toolkit.ToolkitManager;
import org.nting.toolkit.animation.Behavior;
import org.nting.toolkit.data.MouseOverProperty;
import org.nting.toolkit.data.MousePositionProperty;
import org.nting.toolkit.data.Properties;
import org.nting.toolkit.event.KeyEvent;
import org.nting.toolkit.event.KeyListener;
import org.nting.toolkit.event.MouseEvent;
import org.nting.toolkit.event.MouseListener;
import org.nting.toolkit.layout.LayoutManager;
import org.nting.toolkit.ui.ComponentUI;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import playn.core.Font;
import playn.core.PlayN;
import pythagoras.f.Dimension;
import pythagoras.f.Point;

public abstract class AbstractComponent implements PaintableComponent, RuntimeBean {

    private static final List<String> BOUND_PROPERTY_NAMES = Lists.newArrayList("x", "y", "width", "height");

    private final Properties properties = new Properties();

    public final Property<Float> x = createProperty("x", 0f);
    public final Property<Float> y = createProperty("y", 0f);
    public final Property<Float> width = createProperty("width", 0f);
    public final Property<Float> height = createProperty("height", 0f);
    public final Property<String> tooltipText = createProperty("tooltipText", null);
    public final Property<Boolean> visible = createProperty("visible", true);
    public final Property<Boolean> focused = createReadOnlyProperty("focused", false);
    public final Property<Boolean> attached = createReadOnlyProperty("attached", false);
    /** Feature is need to be activated first by {@link #activateMouseOver()} or add a listener to it */
    public final Property<Boolean> mouseOver = new MouseOverProperty(this);
    /** Feature is need to be activated first by {@link #activateMousePosition(boolean)} or add a listener to it */
    public final Property<Point> mousePosition = new MousePositionProperty(this);
    public final Property<Font> font = createProperty("font", null);

    private final Property<Boolean> injectStyleProperties = createProperty("injectStyleProperties", false);
    private Pair<Alignment, Orientation> tooltipLocation = Pair.of(Alignment.TOP_LEFT, Orientation.VERTICAL);

    private Component parent;
    private final List<Component> components = Lists.newLinkedList();
    private LayoutManager layoutManager;
    private final BiMap<Component, Object> layoutConstraintsMap = HashBiMap.create();
    @SuppressWarnings("rawtypes")
    private ComponentUI componentUI;

    private final List<MouseListener> mouseListeners = Lists.newLinkedList();
    private final List<KeyListener> keyListeners = Lists.newLinkedList();

    private boolean focusable = true;
    private boolean focusNeutral = false;
    private boolean dirty = true;
    private boolean clip = false;
    private Object id;

    private final List<Behavior> behaviors = Lists.newLinkedList();
    private final List<Behavior> behaviorsToRemove = Lists.newLinkedList();

    public AbstractComponent(String... reLayoutPropertyNames) {
        properties.addValueChangeListener(event -> {
            logPropertyChange(event, id);
            String propertyName = event.getPropertyName();
            if ("visible".equals(propertyName)) {
                reLayout();
                return;
            }
            for (String reLayoutPropertyName : reLayoutPropertyNames) {
                if (propertyName.equals(reLayoutPropertyName)) {
                    reLayout();
                    return;
                }
            }
            if (getParent() != null && BOUND_PROPERTY_NAMES.contains(propertyName)) {
                getParent().repaint();
            } else {
                repaint();
            }
        });
        tooltipText.addValueChangeListener(event -> {
            if (Strings.isNullOrEmpty(event.getValue())) {
                tooltipManager().unRegisterComponent(AbstractComponent.this);
            } else {
                tooltipManager().registerComponent(AbstractComponent.this);
            }
        });

        injectStyleProperties();
    }

    /** Inject style properties at the latest time; toolkitManager() is properly initialized by then. */
    private void injectStyleProperties() {
        createBeanProperty("componentUI", AbstractComponent::getComponentUI, AbstractComponent::setComponentUI);

        Registration[] registration = new Registration[1];
        registration[0] = properties.addValueChangeListener(event -> {
            if (event.getPrevValue() != null) {
                if (loggingPropertyChanges()) {
                    PlayN.log(getClass()).info("InjectStyleProperties@{}",
                            Optional.ofNullable(id).orElseGet(this::hashCode));
                }

                ToolkitManager toolkitManager = toolkitManager();
                if (toolkitManager == null) {
                    IllegalStateException exception = new IllegalStateException("ToolkitManager is not yet created!");
                    PlayN.log(getClass()).warn(exception.getMessage(), exception);
                    return;
                }
                Injector styleInjector = toolkitManager.getStyleInjector();
                if (styleInjector == null) {
                    IllegalStateException exception = new IllegalStateException("Style is not yet configured!");
                    PlayN.log(getClass()).warn(exception.getMessage(), exception);
                    return;
                }

                registration[0].remove();
                collectImplementedTypes(this).forEach(type -> styleInjector.injectProperties(this, type.getName()));
            }
        });
    }

    /** We should call it after each constructor is called on the type hierarchy. */
    public void forceInjectStyleProperties() {
        injectStyleProperties.setValue(true);
    }

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
        if (constraints == null) {
            layoutConstraintsMap.remove(child);
        } else {
            layoutConstraintsMap.put(child, constraints);
        }
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
            c.repaint();
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
    public void repaint() {
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
    public void update(float delta) {
        PaintableComponent.super.update(delta);

        for (int i = 0; i < behaviors.size(); i++) {
            Behavior behavior = behaviors.get(i);
            if (behavior.isFinished()) {
                behaviors.remove(i--);
            } else {
                try {
                    behavior.update(delta);
                } catch (RuntimeException e) {
                    PlayN.log(AbstractComponent.class).error(e.getMessage(), e);
                    behavior.fastForward();
                }
            }
        }
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
    public void requestFocus() {
        toolkitManager().keyDispatcher().requestFocus(this);
    }

    @Override
    public Registration addKeyListener(KeyListener keyListener) {
        if (keyListener != null && !keyListeners.contains(keyListener)) {
            keyListeners.add(keyListener);
        }

        return () -> keyListeners.remove(keyListener);
    }

    public void fireKeyEvent(BiConsumer<KeyListener, KeyEvent> keyListenerMethod, KeyEvent keyEvent) {
        for (KeyListener listener : keyListeners) {
            keyListenerMethod.accept(listener, keyEvent);
            if (keyEvent.isConsumed()) {
                handleBehaviorsToRemove();
                break;
            }
        }
    }

    @Override
    public Registration addMouseListener(MouseListener mouseListener) {
        if (mouseListener != null && !mouseListeners.contains(mouseListener)) {
            mouseListeners.add(mouseListener);
        }

        return () -> mouseListeners.remove(mouseListener);
    }

    public <E extends MouseEvent> void fireMouseEvent(BiConsumer<MouseListener, E> mouseListenerMethod, E mouseEvent) {
        mouseListeners.forEach(listener -> mouseListenerMethod.accept(listener, mouseEvent));
        if (mouseEvent.isConsumed()) {
            handleBehaviorsToRemove();
        }
    }

    public void activateMouseOver() {
        properties.addCustomProperty("mouseOver", mouseOver);
    }

    public void activateMousePosition(boolean updateOnMouseDragged) {
        ((MousePositionProperty) mousePosition).setUpdateOnMouseDragged(updateOnMouseDragged);
        properties.addCustomProperty("mousePosition", mousePosition);
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

    @Override
    public void addBehavior(Behavior behavior) {
        if (!behaviors.contains(behavior)) {
            behaviors.add(behavior);
        }
    }

    protected List<Behavior> getBehaviors() {
        return behaviors;
    }

    @Override
    public void removeBehaviorOnAnyConsumedEvent(Behavior behavior) {
        toolkitManager().schedule(createLoopedRunnable(1, 0, () -> behaviorsToRemove.add(behavior)));
    }

    /** Call it when an event (key or mouse) is consumed. */
    private void handleBehaviorsToRemove() {
        if (0 < behaviorsToRemove.size()) {
            behaviors.removeAll(behaviorsToRemove);
            behaviorsToRemove.clear();
        }
    }

    public final <T> Property<T> createProperty(Object propertyId, T initialValue) {
        return properties.addObjectProperty(propertyId, initialValue);
    }

    public final <T> Property<T> createReadOnlyProperty(Object propertyId, T initialValue) {
        ObjectProperty<T> property = properties.addObjectProperty(propertyId, initialValue);
        property.setReadOnly(true);
        return property;
    }

    public final <T> Property<T> createBeanProperty(Object propertyId, Function<AbstractComponent, T> getter,
            BiConsumer<AbstractComponent, T> setter) {
        return properties.addCustomProperty(propertyId, new BeanProperty<>(this, getter, setter));
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

    @SuppressWarnings("unchecked")
    public <COMPONENT extends AbstractComponent> COMPONENT set(String propertyName, Object value) {
        getProperty(propertyName).setValue(value);
        return (COMPONENT) this;
    }

    @SuppressWarnings("unchecked")
    public <COMPONENT extends AbstractComponent, T> COMPONENT set(String propertyName, Property<T> source) {
        Bindings.bind(READ, source, getProperty(propertyName));
        return (COMPONENT) this;
    }

    @SuppressWarnings("unchecked")
    public <COMPONENT extends AbstractComponent> COMPONENT process(Consumer<COMPONENT> consumer) {
        COMPONENT component = (COMPONENT) this;
        consumer.accept(component);
        return component;
    }

    public Registration addValueChangeListener(ValueChangeListener<Object> listener) {
        return properties.addValueChangeListener(listener);
    }
}

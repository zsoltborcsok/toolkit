package org.nting.toolkit;

import java.util.Map;
import java.util.Optional;

import org.nting.toolkit.internal.ToolkitManagerImpl;
import org.nting.toolkit.internal.UnitConverterImpl;

import com.google.common.collect.Maps;

import playn.core.PlayN;

public class ToolkitServices {

    private static final Map<Class<?>, Object> services = Maps.newHashMap();

    static {
        setService(ToolkitManager.class, new ToolkitManagerImpl());
        setService(Notifications.class, null); // TODO
        setService(UnitConverter.class, new UnitConverterImpl());
        setService(FontManager.class, null); // TODO
    }

    @SuppressWarnings("unchecked")
    public static <T> T getService(Class<T> serviceType) {
        return (T) services.get(serviceType);
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> findService(Class<T> serviceType) {
        return Optional.ofNullable((T) services.get(serviceType));
    }

    public static <T> void setService(Class<T> serviceType, T service) {
        if (services.containsKey(serviceType) && service != null) {
            PlayN.log(ToolkitServices.class).warn(
                    "Service of type {} has already been registered. Previous service instance will be discarded.",
                    serviceType.getName());
        }

        if (service != null) {
            services.put(serviceType, service);
        } else {
            services.remove(serviceType);
        }
    }

    public static ToolkitManager toolkitManager() {
        return getService(ToolkitManager.class);
    }

    public static Notifications notifications() {
        return getService(Notifications.class);
    }

    public static UnitConverter unitConverter() {
        return getService(UnitConverter.class);
    }

    public static FontManager fontManager() {
        return getService(FontManager.class);
    }
}

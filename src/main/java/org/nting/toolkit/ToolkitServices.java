package org.nting.toolkit;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import playn.core.PlayN;

public class ToolkitServices {

    private static final Map<Class<?>, Object> services = Maps.newHashMap();

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
}

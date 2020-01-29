package org.nting.toolkit.util;

import org.nting.data.Property;
import org.nting.data.property.ObjectProperty;

import playn.core.PlayN;

public class StorageUtils {

    public static Property<Boolean> property(String key, boolean defaultValue) {
        Property<Boolean> property = new ObjectProperty<>(getItem(key, defaultValue));
        property.addValueChangeListener(event -> setItem(key, event.getValue()));
        return property;
    }

    public static Property<String> property(String key, String defaultValue) {
        Property<String> property = new ObjectProperty<>(getItem(key, defaultValue));
        property.addValueChangeListener(event -> setItem(key, event.getValue()));
        return property;
    }

    public static Property<String> property(String key) {
        Property<String> property = new ObjectProperty<>(null);
        property.setValue(getItem(key, null));
        property.addValueChangeListener(event -> setItem(key, event.getValue()));
        return property;
    }

    public static Property<Integer> property(String key, Integer defaultValue) {
        Property<Integer> property = new ObjectProperty<>(getItem(key, defaultValue));
        property.addValueChangeListener(event -> setItem(key, event.getValue()));
        return property;
    }

    public static Property<Double> property(String key, Double defaultValue) {
        Property<Double> property = new ObjectProperty<>(getItem(key, defaultValue));
        property.addValueChangeListener(event -> setItem(key, event.getValue()));
        return property;
    }

    public static String getItem(String key, String defaultValue) {
        String item = PlayN.storage().getItem(key);
        if (item != null) {
            return item;
        } else {
            return defaultValue;
        }
    }

    public static void setItem(String key, String data) {
        try {
            PlayN.storage().setItem(key, data);
        } catch (RuntimeException e) {
            // NOTHING To Do
        }
    }

    public static boolean getItem(String key, boolean defaultValue) {
        String item = PlayN.storage().getItem(key);
        if (item != null) {
            return Boolean.parseBoolean(item);
        } else {
            return defaultValue;
        }
    }

    public static void setItem(String key, boolean data) {
        try {
            PlayN.storage().setItem(key, String.valueOf(data));
        } catch (RuntimeException e) {
            // NOTHING To Do
        }
    }

    public static int getItem(String key, int defaultValue) {
        String item = PlayN.storage().getItem(key);
        if (item != null) {
            try {
                return Integer.parseInt(item);
            } catch (NumberFormatException e) {
                // NOTHING To Do
            }
        }

        return defaultValue;
    }

    public static int getItem(String key, int defaultValue, int failoverValue) {
        if (PlayN.storage().isPersisted()) {
            return getItem(key, defaultValue);
        } else {
            return failoverValue;
        }
    }

    public static void setItem(String key, int data) {
        try {
            PlayN.storage().setItem(key, String.valueOf(data));
        } catch (RuntimeException e) {
            // NOTHING To Do
        }
    }

    public static double getItem(String key, double defaultValue) {
        String item = PlayN.storage().getItem(key);
        if (item != null) {
            try {
                return Double.parseDouble(item);
            } catch (NumberFormatException e) {
                // NOTHING To Do
            }
        }

        return defaultValue;
    }

    public static double getItem(String key, double defaultValue, double failoverValue) {
        if (PlayN.storage().isPersisted()) {
            return getItem(key, defaultValue);
        } else {
            return failoverValue;
        }
    }

    public static void setItem(String key, double data) {
        try {
            PlayN.storage().setItem(key, String.valueOf(data));
        } catch (RuntimeException e) {
            // NOTHING To Do
        }
    }

    public static void removeItem(String key) {
        PlayN.storage().removeItem(key);
    }
}

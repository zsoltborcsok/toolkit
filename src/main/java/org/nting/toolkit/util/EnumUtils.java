package org.nting.toolkit.util;

import org.nting.data.Property;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

public class EnumUtils {

    private static final Splitter NAME_SPLITTER = Splitter.on(CharMatcher.anyOf(" _"));

    public static String getHumanReadableName(Enum anEnum) {
        if (anEnum == null) {
            return "";
        }
        StringBuilder humanReadableName = new StringBuilder();

        for (String split : NAME_SPLITTER.split(anEnum.toString().toLowerCase())) {
            humanReadableName.append(toUpperCamel(split));
            humanReadableName.append(" ");
        }
        humanReadableName.setLength(humanReadableName.length() - 1);

        return humanReadableName.toString();
    }

    public static <T extends Enum> Property<String> asHumanReadableName(Property<T> enumProperty) {
        return enumProperty.transform(EnumUtils::getHumanReadableName);
    }

    public static <T extends Enum<T>> T getEnumValue(Class<T> enumClass, String value) {
        if (value == null) {
            return null;
        }

        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // Based on WordUtils.toUpperCamel(...)
    private static String toUpperCamel(String word) {
        return word.isEmpty() ? word : word.substring(0, 1).toUpperCase() + word.substring(1);
    }
}

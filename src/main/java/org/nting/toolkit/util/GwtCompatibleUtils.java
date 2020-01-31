package org.nting.toolkit.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;

import playn.core.PlayN;

// http://www.gwtproject.org/doc/latest/RefJreEmulation.html
public class GwtCompatibleUtils {

    /**
     * Works only on Classes and not on Interfaces. Checks that type is super type or not of checkedType. In other words
     * instance of checkedType can be casted to type.
     */
    public static boolean isAssignableFrom(Class<?> type, Class<?> checkedType) {
        if (checkedType == null) {
            return false;
        }

        if (checkedType.equals(type)) {
            return true;
        }

        // Do not use -XdisableClassMetadata compiler flag!
        // Or set <disableClassMetadata> to false in maven plugin configuration.
        Class<?> currentSuperClass = checkedType.getSuperclass();
        while (currentSuperClass != null) {
            if (currentSuperClass.equals(type)) {
                return true;
            }
            currentSuperClass = currentSuperClass.getSuperclass();
        }
        return false;
    }

    /** Returns all the implemented classes, in the order as they extend each other. */
    public static List<Class<?>> collectImplementedTypes(Object object) {
        List<Class<?>> types = Lists.newLinkedList();

        Class<?> type = object.getClass();
        while (type != null) {
            types.add(0, type);
            type = type.getSuperclass();
        }

        return types;
    }

    public static String getSimpleName(Class<?> clazz) {
        String name = clazz.getName();
        return name.substring(name.lastIndexOf(".") + 1);
    }

    public static <T> T[] copyOfRange(T[] original, int from, int to) {
        T[] copy = ObjectArrays.newArray(original, to - from);
        System.arraycopy(original, from, copy, 0, copy.length);
        return copy;
    }

    public static String abbreviate(String str, int maxWidth) {
        if (str == null) {
            return null;
        }

        if (str.length() <= maxWidth) {
            return str;
        } else {
            return str.substring(0, maxWidth - 3) + "...";
        }
    }

    public static String abbreviateFileName(String fileName, int maxLength) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return fileName.substring(0, Math.min(fileName.length(), maxLength));
        } else {
            String extension = fileName.substring(dotIndex);
            if (extension.length() < maxLength) {
                return fileName.substring(0, Math.min(dotIndex, maxLength - extension.length())) + extension;
            } else {
                return fileName.substring(0, Math.min(fileName.length(), maxLength));
            }
        }
    }

    public static String replaceAll(String input, char c, String replacement) {
        int charIndex = input.indexOf(c);
        if (0 <= charIndex) {
            input = input.substring(0, charIndex) + replacement + input.substring(charIndex + 1);
            return replaceAll(input, c, replacement);
        } else {
            return input;
        }
    }

    public static float min(float... numbers) {
        float min = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            min = Math.min(min, numbers[i]);
        }
        return min;
    }

    public static int countMatches(String string, String subString) {
        int count = 0;
        int from = 0;
        while ((from = string.indexOf(subString, from)) != -1) {
            count++;
            from += subString.length();
        }
        return count;
    }

    public static List<Integer> findMatches(String string, String subString) {
        List<Integer> indexes = Lists.newLinkedList();
        int from = 0;
        while ((from = string.indexOf(subString, from)) != -1) {
            indexes.add(from);
            from += subString.length();
        }
        return indexes;
    }

    public static String convertStreamToString(InputStream inputStream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            byte[] data = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
            }
            buffer.flush();
        } catch (IOException e) {
            PlayN.log(GwtCompatibleUtils.class).error(e.getMessage(), e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                PlayN.log(GwtCompatibleUtils.class).error(e.getMessage(), e);
            }
        }

        return new String(buffer.toByteArray(), StandardCharsets.UTF_8).replace("\r\n", "\n").replace('\r', '\n');
    }

    @SuppressWarnings("unchecked")
    public static <N extends Number> N addNumbers(N a, N b) {
        if (a instanceof Double) {
            return (N) (Double) (a.doubleValue() + b.doubleValue());
        } else if (a instanceof Float) {
            return (N) (Float) (a.floatValue() + b.floatValue());
        } else if (a instanceof Long) {
            return (N) (Long) (a.longValue() + b.longValue());
        } else if (a instanceof Integer) {
            return (N) (Integer) (a.intValue() + b.intValue());
        } else if (a instanceof Short) {
            return (N) (Short) (short) (a.shortValue() + b.shortValue());
        } else if (a instanceof Byte) {
            return (N) (Byte) (byte) (a.byteValue() + b.byteValue());
        } else {
            throw new IllegalStateException();
        }
    }
}

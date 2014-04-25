package util;

import com.sun.istack.internal.NotNull;

import java.lang.reflect.Field;

/**
 * Created by Andrew Govorovsky on 21.04.14
 */
public class ReflectionHelper {
    public static Object createInstance(@NotNull String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setFieldValue(Object obj, String name, String value) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            if (field.getType().equals(String.class)) {
                field.set(obj, value);
            } else if (field.getType().equals(int.class)) {
                field.set(obj, Integer.decode(value));
            } else if (field.getType().equals(char.class)) {
                field.set(obj, value.charAt(0));
            }
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

package basemod.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {
    public static class CallableMethod {
        public Method method;

        public CallableMethod(Method method) {
            this.method = method;
        }

        public Object invoke(Object obj, Object... params) {
            try {
                return method.invoke(obj, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static <T> CallableMethod getMethod(Class<T> type, String name, Class... signature) {
        try {
            Method method = type.getDeclaredMethod(name, signature);
            method.setAccessible(true);
            return new CallableMethod(method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, TContainer> T readField(Class<T> type, Class<TContainer> container, TContainer obj, String name) {
        try {
            Field field = container.getDeclaredField(name);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
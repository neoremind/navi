package com.baidu.beidou.navi.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName: ReflectionUtil <br/>
 * Function: 有关 <code>Reflection</code> 处理的工具类
 * 
 * @author Zhang Xu
 */
public class ReflectionUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 获取某个类上面某种类型的所有注解
     * 
     * @param clazz
     *            类
     * @param annoClazz
     *            注解类型
     * @return 注解
     */
    public static <A extends Annotation> A getAnnotation(final Class<?> clazz, Class<A> annoClazz) {
        if (clazz == null || annoClazz == null) {
            return null;
        }

        return clazz.getAnnotation(annoClazz);
    }

    /**
     * 获取某个类的所有实例方法，包含所有非静态的公有方法
     * 
     * @param clazz
     * @return
     */
    public static Method[] getAllInstanceMethods(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        List<Method> methods = new ArrayList<Method>();
        for (Method method : clazz.getMethods()) {
            if (!Modifier.isStatic(method.getModifiers())
                    && Modifier.isPublic(method.getModifiers())) {
                methods.add(method);
            }
        }

        return methods.toArray(new Method[methods.size()]);
    }

    public static Constructor<?>[] getAllConstructorsOfClass(final Class<?> clazz,
            boolean accessible) {
        if (clazz == null) {
            return null;
        }

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (!ArrayUtil.isEmpty(constructors)) {
            AccessibleObject.setAccessible(constructors, accessible);
        }

        return constructors;

    }

    public static <T> T newInstance(final Class<T> clazz) {
        Constructor<?>[] constructors = getAllConstructorsOfClass(clazz, true);
        // impossible ?
        if (ArrayUtil.isEmpty(constructors)) {
            return null;
        }

        Object[] initParameters = getInitParameters(constructors[0].getParameterTypes());

        try {
            @SuppressWarnings("unchecked")
            T instance = (T) constructors[0].newInstance(initParameters);
            return instance;
        } catch (Exception e) {
            LOG.error("newInstance", e);
            return null;
        }

    }

    private static Object[] getInitParameters(Class<?>[] parameterTypes) {
        int length = parameterTypes.length;

        Object[] result = new Object[length];
        for (int i = 0; i < length; i++) {
            if (parameterTypes[i].isPrimitive()) {
                Object init = ClassUtil.getPrimitiveDefaultValue(parameterTypes[i]);
                result[i] = init;
                continue;
            }

            result[i] = null;
        }

        return result;
    }

}

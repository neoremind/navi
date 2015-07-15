package com.baidu.beidou.navi.util;

import java.util.Map;

/**
 * ClassName: ClassUtil <br/>
 * Function: 有关 <code>Class</code> 处理的工具类。
 * 
 * @author Zhang Xu
 */
public class ClassUtil {

    private static final Map<String, PrimitiveInfo<?>> PRIMITIVES = CollectionUtil.createHashMap();

    static {
        addPrimitive(boolean.class, "Z", Boolean.class, "booleanValue", false);
        addPrimitive(short.class, "S", Short.class, "shortValue", (short) 0);
        addPrimitive(int.class, "I", Integer.class, "intValue", 0);
        addPrimitive(long.class, "J", Long.class, "longValue", 0L);
        addPrimitive(float.class, "F", Float.class, "floatValue", 0F);
        addPrimitive(double.class, "D", Double.class, "doubleValue", 0D);
        addPrimitive(char.class, "C", Character.class, "charValue", '\0');
        addPrimitive(byte.class, "B", Byte.class, "byteValue", (byte) 0);
        addPrimitive(void.class, "V", Void.class, null, null);
    }

    private static <T> void addPrimitive(Class<T> type, String typeCode, Class<T> wrapperType,
            String unwrapMethod, T defaultValue) {
        PrimitiveInfo<T> info = new PrimitiveInfo<T>(type, typeCode, wrapperType, unwrapMethod,
                defaultValue);

        PRIMITIVES.put(type.getName(), info);
        PRIMITIVES.put(wrapperType.getName(), info);
    }

    /** 代表一个primitive类型的信息。 */
    @SuppressWarnings("unused")
    private static class PrimitiveInfo<T> {
        final Class<T> type;
        final String typeCode;
        final Class<T> wrapperType;
        final String unwrapMethod;
        final T defaultValue;

        public PrimitiveInfo(Class<T> type, String typeCode, Class<T> wrapperType,
                String unwrapMethod, T defaultValue) {
            this.type = type;
            this.typeCode = typeCode;
            this.wrapperType = wrapperType;
            this.unwrapMethod = unwrapMethod;
            this.defaultValue = defaultValue;
        }
    }

    /**
     * 取得primitive类型的默认值。如果不是primitive，则返回<code>null</code>。
     * <p>
     * 如果<code>clazz</code>为<code>null</code>，则返还 <code>null</code>
     * <p>
     * 例如：
     * <p/>
     * 
     * <pre>
     * ClassUtil.getPrimitiveDefaultValue(int.class) = 0;
     * ClassUtil.getPrimitiveDefaultValue(boolean.class) = false;
     * ClassUtil.getPrimitiveDefaultValue(char.class) = '\0';
     * </pre>
     * <p/>
     * </p>
     */
    public static <T> T getPrimitiveDefaultValue(Class<T> type) {
        if (type == null) {
            return null;
        }

        @SuppressWarnings("unchecked")
        PrimitiveInfo<T> info = (PrimitiveInfo<T>) PRIMITIVES.get(type.getName());

        if (info != null) {
            return info.defaultValue;
        }

        return null;
    }

}

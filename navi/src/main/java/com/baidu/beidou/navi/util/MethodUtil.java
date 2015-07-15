package com.baidu.beidou.navi.util;

import java.lang.reflect.Method;

/**
 * ClassName: MethodUtil <br/>
 * Function: 方法工具类
 * 
 * @author zhangxu04
 */
public class MethodUtil {

    /**
     * Get parameter type name string from a method
     * 
     * @param method
     * @return
     */
    public static String getArgsTypeName(Method method) {
        return getArgsTypeName(getArgsTypeNameArray(method));
    }

    /**
     * Get parameter type name array from a method
     * 
     * @param method
     * @return
     */
    public static String[] getArgsTypeNameArray(Method method) {
        return getArgsTypeNameArray(method.getParameterTypes());
    }

    /**
     * Get parameter type name array from a method
     * 
     * @param argsTypes
     * @return
     */
    public static String[] getArgsTypeNameArray(Class<?>[] argsTypes) {
        String[] argsTypeArray = null;
        if (argsTypes != null) {
            argsTypeArray = new String[argsTypes.length];
            for (int i = 0; i < argsTypes.length; i++) {
                argsTypeArray[i] = argsTypes[i].getName();
            }
        }
        return argsTypeArray;
    }

    /**
     * Get parameter type name string from a arg types string array
     * 
     * @param argTypes
     * @return
     */
    public static String getArgsTypeName(String[] argTypes) {
        if (argTypes != null) {
            return StringUtil.join(argTypes, ',');
        }
        return StringUtil.EMPTY;
    }

}

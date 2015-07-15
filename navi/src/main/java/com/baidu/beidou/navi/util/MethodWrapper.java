package com.baidu.beidou.navi.util;

/**
 * ClassName: MethodWrapper <br/>
 * Function: 包含方法及其参数类型的封装类
 * 
 * @author zhangxu04
 */
public class MethodWrapper {

    /**
     * 类名称或者接口class名称
     */
    private String clazzName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数类型名称字符串
     */
    private String argTypesStr;

    /**
     * 转化为字符串
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(clazzName);
        sb.append("-");
        sb.append(methodName);
        sb.append("-");
        if (argTypesStr != null) {
            sb.append(argTypesStr);
        }
        return sb.toString();
    }

    /**
     * Creates a new instance of MethodWrapper.
     * 
     * @param clazzName
     * @param methodName
     * @param argTypesStr
     */
    public MethodWrapper(String clazzName, String methodName, String argTypesStr) {
        super();
        this.clazzName = clazzName;
        this.methodName = methodName;
        this.argTypesStr = argTypesStr;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getArgTypesStr() {
        return argTypesStr;
    }

    public void setArgTypesStr(String argTypesStr) {
        this.argTypesStr = argTypesStr;
    }

}

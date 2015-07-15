package com.baidu.beidou.navi.server.locator;

import java.lang.reflect.Method;
import java.util.List;

import com.baidu.beidou.navi.server.vo.AppIdToken;
import com.baidu.beidou.navi.util.CollectionUtil;
import com.baidu.beidou.navi.util.StringUtil;

/**
 * ClassName: MethodDescriptor <br/>
 * Function: 一个暴露的rpc服务的方法描述
 * <p>
 * 泛型&lt;KEY&gt;标示服务的唯一标示，例如可以是一个<tt>int</tt>的数字id，可以是一个<tt>String</tt>字符串
 * 
 * @author Zhang Xu
 */
public class MethodDescriptor<KEY> {

    /**
     * 服务的id，唯一标示
     */
    private KEY serviceId;

    /**
     * 服务缓存的method
     */
    private Method method;

    /**
     * 接口定义
     */
    private Class<?> interfClass;

    /**
     * 服务具体的实现bean对象
     */
    private Object target;

    /**
     * 服务输入参数对象
     */
    private Class<?>[] argumentClass;

    /**
     * 服务输出参数对象
     */
    private Class<?> returnClass;

    /**
     * 方法上的支持的appId和token
     */
    private List<AppIdToken> appIdTokens;

    /**
     * toString
     * 
     * @return 可显示信息
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ServiceId=[");
        sb.append(serviceId);
        sb.append("], Signature=[");
        sb.append(returnClass.getSimpleName());
        sb.append(" ");
        sb.append(target.getClass().getSimpleName());
        sb.append(".");
        sb.append(method.getName());
        sb.append("(");
        sb.append(StringUtil.toString4Array(argumentClass));
        sb.append(")]");
        if (CollectionUtil.isNotEmpty(appIdTokens)) {
            sb.append(", appIdTokens=[");
            for (AppIdToken appIdToken : appIdTokens) {
                sb.append(appIdToken);
                sb.append("|");
            }
            sb.append("]");
        }
        return sb.toString();
    }

    public KEY getServiceId() {
        return serviceId;
    }

    public MethodDescriptor<KEY> setServiceId(KEY serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public Method getMethod() {
        return method;
    }

    public MethodDescriptor<KEY> setMethod(Method method) {
        this.method = method;
        return this;
    }

    public Object getTarget() {
        return target;
    }

    public MethodDescriptor<KEY> setTarget(Object target) {
        this.target = target;
        return this;
    }

    public Class<?>[] getArgumentClass() {
        return argumentClass;
    }

    public MethodDescriptor<KEY> setArgumentClass(Class<?>[] argumentClass) {
        this.argumentClass = argumentClass;
        return this;
    }

    public Class<?> getReturnClass() {
        return returnClass;
    }

    public MethodDescriptor<KEY> setReturnClass(Class<?> returnClass) {
        this.returnClass = returnClass;
        return this;
    }

    public Class<?> getInterfClass() {
        return interfClass;
    }

    public MethodDescriptor<KEY> setInterfClass(Class<?> interfClass) {
        this.interfClass = interfClass;
        return this;
    }

    public List<AppIdToken> getAppIdTokens() {
        return appIdTokens;
    }

    public MethodDescriptor<KEY> setAppIdTokens(List<AppIdToken> appIdTokens) {
        this.appIdTokens = appIdTokens;
        return this;
    }

}

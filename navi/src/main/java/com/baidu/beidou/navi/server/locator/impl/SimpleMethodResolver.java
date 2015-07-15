package com.baidu.beidou.navi.server.locator.impl;

import java.lang.reflect.Method;

import com.baidu.beidou.navi.server.annotation.IgnoreNaviRpcMethod;
import com.baidu.beidou.navi.server.locator.MethodResolver;

/**
 * ClassName: SimpleMethodResolver <br/>
 * Function: 默认的简单服务暴露判断类，用于判断某个类下面的方法是否有资格暴露为服务的方法
 * 
 * @author Zhang Xu
 */
public class SimpleMethodResolver implements MethodResolver {

    /**
     * 判断某个方法是否可以暴露为服务，这里的判断条件是满足以下
     * <ul>
     * <li>1)方法上没有{@link com.baidu.beidou.navi.server.annotation.IgnoreNaviRpcMethod IgnoreNaviRpcMethod}注解</li>
     * </ul>
     * 
     * @param m
     *            方法
     * @return 是否可以暴露为服务，true为可以，false为不行
     * @see com.baidu.beidou.navi.server.locator.MethodResolver#isSupport(java.lang.reflect.Method)
     */
    @Override
    public boolean isSupport(Method m) {
        IgnoreNaviRpcMethod anno = m.getAnnotation(IgnoreNaviRpcMethod.class);
        if (anno != null) {
            return false;
        }
        return true;
    }

}

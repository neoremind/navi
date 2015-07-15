package com.baidu.beidou.navi.server.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: NaviRpcService <br/>
 * Function: Navi服务暴露的注解
 * 
 * @author Zhang Xu
 */
@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NaviRpcService {

    /**
     * 实现的接口
     * 
     * @return
     */
    public abstract Class<?> serviceInterface();

}

package com.baidu.beidou.navi.server.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: IgnoreNaviRpcMethod <br/>
 * Function: Navi服务细粒度到方法级别，不暴露指定的方法出来的注解
 * 
 * @author Zhang Xu
 */
@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreNaviRpcMethod {

}

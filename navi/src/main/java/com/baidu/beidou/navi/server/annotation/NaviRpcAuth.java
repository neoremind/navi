package com.baidu.beidou.navi.server.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: NaviRpcAuth <br/>
 * Function: Navi服务权限控制
 * 
 * @author Zhang Xu
 */
@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface NaviRpcAuth {

    /**
     * Authority配置
     * 
     * @return 多个Authority
     */
    Authority[] value();

}

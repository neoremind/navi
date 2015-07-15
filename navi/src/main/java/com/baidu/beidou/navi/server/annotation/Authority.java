package com.baidu.beidou.navi.server.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: Authority <br/>
 * Function: Navi服务权限控制内部，可重复使用，配合{@link NaviRpcAuth}使用
 * 
 * @author Zhang Xu
 */
@Target({ java.lang.annotation.ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Authority {

    /**
     * appId
     * 
     * @return appId
     */
    String appId();

    /**
     * token
     * 
     * @return token
     */
    String token();

}

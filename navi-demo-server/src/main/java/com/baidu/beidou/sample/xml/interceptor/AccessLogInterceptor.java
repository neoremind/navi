package com.baidu.beidou.sample.xml.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName: AccessLogInterceptor <br/>
 * Function: 记录日志拦截器
 * 
 * @author zhangxu04
 */
public class AccessLogInterceptor implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AccessLogInterceptor.class);

    public Object invoke(MethodInvocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        Object obj = invocation.proceed();
        long end = System.currentTimeMillis();
        log.info("cost " + (end - start) + "ms");
        return obj;
    }

}

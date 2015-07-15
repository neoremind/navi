package com.baidu.beidou.navi.it.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ClassName: ProxyServiceInterceptor <br/>
 * Function: 实验拦截器
 * 
 * @author Zhang Xu
 */
@Aspect
@Component
public class ProxyServiceInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(ProxyServiceInterceptor.class);

    @Around("execution(* com.baidu.beidou.navi.it.service.impl.ProxyServiceImpl.*(..))")
    public Object validate4Query(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if (args != null && args.length == 1 && (args[0] instanceof String)) {
            LOG.info("Intercept argument input - " + (String) args[0]);
        }
        return pjp.proceed();
    }

}

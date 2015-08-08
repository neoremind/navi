package com.baidu.beidou.navi.server.locator.impl;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;

import com.baidu.beidou.navi.server.NaviRpcExporter;
import com.baidu.beidou.navi.server.annotation.Authority;
import com.baidu.beidou.navi.server.annotation.NaviRpcAuth;
import com.baidu.beidou.navi.server.locator.MethodDescriptor;
import com.baidu.beidou.navi.server.locator.MethodResolver;
import com.baidu.beidou.navi.server.locator.PublishHandler;
import com.baidu.beidou.navi.server.locator.ServiceLocator;
import com.baidu.beidou.navi.server.locator.ServiceRegistry;
import com.baidu.beidou.navi.server.vo.AppIdToken;
import com.baidu.beidou.navi.util.ArrayUtil;
import com.baidu.beidou.navi.util.CollectionUtil;
import com.baidu.beidou.navi.util.MethodUtil;
import com.baidu.beidou.navi.util.MethodWrapper;
import com.baidu.beidou.navi.util.Preconditions;
import com.baidu.beidou.navi.util.ReflectionUtil;
import com.baidu.beidou.navi.util.StringUtil;

/**
 * ClassName: MethodSignatureKeyServiceLocator <br/>
 * Function: 利用方法签名来做服务标示的定位器
 * 
 * @author Zhang Xu
 */
public class MethodSignatureKeyServiceLocator implements ServiceLocator<String, NaviRpcExporter> {

    private static final Logger LOG = LoggerFactory
            .getLogger(MethodSignatureKeyServiceLocator.class);

    /**
     * 缓存服务的接口签名到实际targe bean的映射关系
     */
    protected Map<String, NaviRpcExporter> exporters = new HashMap<String, NaviRpcExporter>();

    /**
     * 服务描述的缓存，内部可以按照服务标示查找
     */
    private ServiceRegistry<String> serviceRegistry;

    /**
     * 判断方法是否为可暴露为服务的解析工具类
     */
    private MethodResolver methodResolver;

    /**
     * Creates a new instance of MethodSignatureKeyServiceLocator.
     */
    public MethodSignatureKeyServiceLocator() {
        serviceRegistry = ServiceRegistry.getInstance();
        methodResolver = new SimpleMethodResolver();
    }

    /**
     * 根据服务标示获取服务方法的描述
     * 
     * @param key
     * @return 服务方法的描述
     * @see com.baidu.beidou.navi.server.locator.ServiceLocator#getServiceDescriptor(java.lang.Object)
     */
    @Override
    public MethodDescriptor<String> getServiceDescriptor(String key) {
        Preconditions.checkNotNull(key, "Key cannot be null");
        return serviceRegistry.getServiceDescriptorByKey(key);
    }

    /**
     * 注入服务
     * 
     * @param exporter
     *            服务的实例或者封装类
     * @return 是否注册成功
     * @see com.baidu.beidou.navi.server.locator.ServiceLocator#regiserService(java.lang.Object)
     */
    @Override
    public boolean regiserService(NaviRpcExporter exporter) {
        Class<?> interf = exporter.getServiceInterface();
        Object bean = exporter.getServiceBean();
        List<AppIdToken> appIdTokens = getAuthIfPossible(bean);
        String interfSimpleName = interf.getSimpleName();
        try {
            exporters.put(exporter.getName(), exporter);
            if (interf.isAssignableFrom(bean.getClass())) {
                Method[] ms = ReflectionUtil.getAllInstanceMethods(bean.getClass());
                for (Method m : ms) {
                    if (methodResolver.isSupport(m)) {
                        MethodWrapper methodWrapper = new MethodWrapper(interf.getName(),
                                m.getName(), MethodUtil.getArgsTypeName(m));
                        MethodWrapper defaultMethodWrapper = new MethodWrapper(interf.getName(),
                                m.getName(), StringUtil.EMPTY);
                        String key = methodWrapper.toString();
                        MethodDescriptor<String> desc = new MethodDescriptor<String>();
                        desc.setServiceId(key).setMethod(m).setTarget(bean)
                                .setArgumentClass(m.getParameterTypes())
                                .setReturnClass(m.getReturnType()).setInterfClass(interf).setAppIdTokens(appIdTokens);
                        serviceRegistry.addServiceDescriptor(key, desc);
                        serviceRegistry.addServiceDescriptor(defaultMethodWrapper.toString(), desc);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(String.format(
                                    "Register service key=[%s], %s %s#%s(%s) successfully", key, m
                                            .getReturnType().getSimpleName(), interf.getName(), m
                                            .getName(), StringUtil.toString4Array(m
                                            .getParameterTypes())));
                        }
                    }
                }
                LOG.info("Find rpc service configured, interface is "
                        + interfSimpleName + " , implementation is "
                        + AopUtils.getTargetClass(bean).getName());
                return true;
            } else {
                LOG.error("The interface " + interfSimpleName + " is not compatible with the bean "
                        + bean.getClass().getName());
            }
        } catch (Exception e) {
            LOG.error(
                    "The interface " + interfSimpleName + " is configured wrongly."
                            + e.getMessage(), e);
        }
        return false;
    }

    /**
     * 整体发布服务
     * 
     * @param handler
     *            发布处理handler
     * @return 是否发布成功
     * @see com.baidu.beidou.navi.server.locator.ServiceLocator#publishService()
     */
    @Override
    public boolean publishService(PublishHandler handler) {
        return handler.publish(serviceRegistry.getAllServiceDescriptors());
    }

    /**
     * 根据服务名称（通常是接口名称）获取服务
     * 
     * @param 服务名称
     * @return 服务的一个封装
     * @see com.baidu.beidou.navi.server.locator.ServiceLocator#getService(java.lang.String)
     */
    @Override
    public NaviRpcExporter getService(String serviceName) {
        return exporters.get(serviceName);
    }

    /**
     * 根据配置的实现类，获取该类对象上配置的{@link NaviRpcAuth}进行进行权限控制，以便确认哪些认证过的appId和token可以访问
     * 
     * @return
     */
    private List<AppIdToken> getAuthIfPossible(Object bean) {
        NaviRpcAuth auth = ReflectionUtil.getAnnotation(bean.getClass(), NaviRpcAuth.class);
        if (auth != null) {
            Authority[] authorities = auth.value();
            if (!ArrayUtil.isEmpty(authorities)) {
                List<AppIdToken> ret = CollectionUtil.createArrayList();
                for (Authority a : authorities) {
                    if (StringUtil.isNotEmpty(a.appId()) && StringUtil.isNotEmpty(a.token())) {
                        ret.add(new AppIdToken(a.appId(), a.token()));
                    }
                }
                return ret;
            }
        }
        return Collections.emptyList();
    }

    /**
     * 获取所有服务（通常是接口名称）
     * 
     * @return 服务集合
     * @see com.baidu.beidou.navi.server.locator.ServiceLocator#getAllServices()
     */
    @Override
    public Collection<NaviRpcExporter> getAllServices() {
        return exporters.values();
    }

}

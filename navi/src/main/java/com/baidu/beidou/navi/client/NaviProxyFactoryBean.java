package com.baidu.beidou.navi.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import com.baidu.beidou.navi.client.attachment.Attachment;
import com.baidu.beidou.navi.client.cache.ServiceLocalCache;
import com.baidu.beidou.navi.client.ha.LbFactory;
import com.baidu.beidou.navi.client.selector.NaviFailStrategy;
import com.baidu.beidou.navi.client.selector.NaviSelectorStrategy;
import com.baidu.beidou.navi.conf.RpcClientConf;
import com.baidu.beidou.navi.constant.NaviCommonConstant;
import com.baidu.beidou.navi.exception.rpc.ServiceAddressEmptyException;
import com.baidu.beidou.navi.protocol.NaviProtocol;
import com.baidu.beidou.navi.protocol.SerializeHandler;
import com.baidu.beidou.navi.protocol.SerializeHandlerFactory;
import com.baidu.beidou.navi.server.vo.AppIdToken;
import com.baidu.beidou.navi.util.CollectionUtil;
import com.baidu.beidou.navi.util.StringUtil;
import com.baidu.beidou.navi.util.ZkPathUtil;

/**
 * ClassName: NaviProxyFactoryBean <br/>
 * Function: Navi rpc动态代理类，用于客户端以本地方法调用的方式，发起远程RPC调用，并且返回结果。
 * 
 * @author Zhang Xu
 */
@SuppressWarnings("rawtypes")
public class NaviProxyFactoryBean implements FactoryBean, InitializingBean {

    private static final Logger LOG = LoggerFactory.getLogger(NaviProxyFactoryBean.class);

    /**
     * zookeeper中注册的服务端path
     */
    private String zkNodePath;

    /**
     * 当本地local cache为空时，直接访问服务端的地址，可以为域名+端口或者IP+端口
     */
    private String directCallServers;

    /**
     * 调用接口
     */
    private Class<?> serviceInterface;

    /**
     * 序列化协议
     * 
     * @see NaviProtocol
     */
    private NaviProtocol protocol = NaviProtocol.PROTOSTUFF;

    /**
     * 失效处理策略
     * 
     * @see NaviFailStrategy
     */
    private NaviFailStrategy failStrategy = NaviFailStrategy.FAILOVER;

    /**
     * 负载均衡策略
     * 
     * @see NaviSelectorStrategy
     */
    private NaviSelectorStrategy selectorStrategy = NaviSelectorStrategy.RANDOM;

    /**
     * appId
     */
    private String appId;

    /**
     * token
     */
    private String token;

    /**
     * Creates a new instance of NaviProxyFactoryBean.
     */
    public NaviProxyFactoryBean() {

    }

    /**
     * Creates a new instance of NaviProxyFactoryBean.
     * 
     * @param serviceInterface
     */
    public NaviProxyFactoryBean(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    /**
     * Creates a new instance of NaviProxyFactoryBean.
     * 
     * @param zkNodePath
     * @param serviceInterface
     */
    public NaviProxyFactoryBean(String zkNodePath, Class<?> serviceInterface) {
        this.zkNodePath = zkNodePath;
        this.serviceInterface = serviceInterface;
    }

    /**
     * Creates a new instance of NaviProxyFactoryBean.
     * 
     * @param serviceInterface
     * @param protocol
     * @param failStrategy
     * @param selectorStrategy
     */
    public NaviProxyFactoryBean(Class<?> serviceInterface, NaviProtocol protocol,
            NaviFailStrategy failStrategy, NaviSelectorStrategy selectorStrategy) {
        this.serviceInterface = serviceInterface;
        if (protocol != null) {
            this.protocol = protocol;
        }
        if (failStrategy != null) {
            this.failStrategy = failStrategy;
        }
        if (selectorStrategy != null) {
            this.selectorStrategy = selectorStrategy;
        }
    }

    /**
     * Creates a new instance of NaviProxyFactoryBean.
     * 
     * @param zkNodePath
     * @param serviceInterface
     * @param protocol
     * @param failStrategy
     * @param selectorStrategy
     */
    public NaviProxyFactoryBean(String zkNodePath, Class<?> serviceInterface,
            NaviProtocol protocol, NaviFailStrategy failStrategy,
            NaviSelectorStrategy selectorStrategy) {
        this.zkNodePath = zkNodePath;
        this.serviceInterface = serviceInterface;
        if (protocol != null) {
            this.protocol = protocol;
        }
        if (failStrategy != null) {
            this.failStrategy = failStrategy;
        }
        if (selectorStrategy != null) {
            this.selectorStrategy = selectorStrategy;
        }
    }

    /**
     * @see org.springframework.beans.factory.FactoryBean#getObject()
     */
    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[] { serviceInterface }, new NaviInvocationProxy());
    }

    /**
     * ClassName: NaviInvocationProxy <br/>
     * Function: 客户端动态代理
     */
    class NaviInvocationProxy implements InvocationHandler {

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            final Class<?> targetClass;
            Method targetMethod;
            Object[] targetArgs;

            targetClass = method.getDeclaringClass();
            targetMethod = method;
            targetArgs = args;
            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format(
                        "Proxy bean start to invoke rpc class=%s, method=%s, args=%s",
                        targetClass.getName(), targetMethod, Arrays.toString(targetArgs)));
            }

            List<String> serverList = getServerList();

            SerializeHandler serializeHandler = SerializeHandlerFactory
                    .getHandlerByProtocal(protocol.getName());

            List<NaviRpcClient> clientList = CollectionUtil.createArrayList(serverList.size());
            for (String server : serverList) {
                final String url = NaviCommonConstant.TRANSPORT_PROTOCOL + server
                        + NaviCommonConstant.TRANSPORT_URL_BASE_PATH
                        + serviceInterface.getSimpleName();
                NaviRpcClient client = new SimpleNaviRpcClient(url,
                        RpcClientConf.RPC_CONNECTION_TIMEOUT, RpcClientConf.RPC_READ_TIMEOUT,
                        serializeHandler);
                clientList.add(client);
            }

            NaviRpcClient client = new HANaviRpcClient(clientList, LbFactory.build(
                    selectorStrategy, failStrategy));
            if (StringUtil.isNotEmpty(appId) && StringUtil.isNotEmpty(token)) {
                return client.transport(targetMethod, targetArgs,
                        new Attachment().setAppIdToken(new AppIdToken(appId, token)));
            }
            return client.transport(targetMethod, targetArgs);
        }
    }

    /**
     * 从local cache中获取最新的服务地址列表
     * <p/>
     * 如果启用了zookeeper，local cache由异步守护线程作为zookeeper客户端实时更新，否则直接从配置中取
     * 
     * @return 服务地址列表
     */
    protected List<String> getServerList() {
        if (StringUtil.isNotEmpty(zkNodePath)) {
            String key = ZkPathUtil.buildPath(NaviCommonConstant.ZOOKEEPER_BASE_PATH, zkNodePath,
                    serviceInterface.getSimpleName());
            if (LOG.isDebugEnabled()) {
                LOG.debug("Try to get server list from local cache for " + key);
            }
            List<String> serverList = ServiceLocalCache.get(key);
            if (CollectionUtils.isEmpty(serverList)) {
                LOG.warn("Rpc service instance list empty in local service cache for "
                        + key
                        + ", if registry at Zookeeper enabled, check path carefully. Workaround solution enabled now to use direct call.");
                if (StringUtil.isEmpty(directCallServers)) {
                    throw new ServiceAddressEmptyException(
                            "No server available in local service cache! Please check 1)At least one server endpoint is alive, "
                                    + "2)Zookeeper registry path and digest auth code is correct, 3)The backup solution - directCallServers are accessible. "
                                    + "If zookeeper registry enabled, also make sure that NaviRpcServerListListener is started as well.");
                }
                String[] directCallServerArr = StringUtil
                        .split(directCallServers, StringUtil.COMMA);
                return Arrays.asList(directCallServerArr);
            }
            return serverList;
        } else {
            if (StringUtil.isEmpty(directCallServers)) {
                throw new ServiceAddressEmptyException(
                        "Rpc client service list empty, please configure the correct direct server list");
            }
            String[] directCallServerArr = StringUtil.split(directCallServers, StringUtil.COMMA);
            return Arrays.asList(directCallServerArr);
        }
    }

    /**
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        LOG.info("Create proxy rpc bean " + getClass().getSimpleName() + " for interface "
                + serviceInterface);
    }

    /**
     * @see org.springframework.beans.factory.FactoryBean#getObjectType()
     */
    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    /**
     * @see org.springframework.beans.factory.FactoryBean#isSingleton()
     */
    @Override
    public boolean isSingleton() {
        return true;
    }

    public String getZkNodePath() {
        return zkNodePath;
    }

    public void setZkNodePath(String zkNodePath) {
        this.zkNodePath = zkNodePath;
    }

    public String getDirectCallServers() {
        return directCallServers;
    }

    public void setDirectCallServers(String directCallServers) {
        this.directCallServers = directCallServers;
    }

    public Class<?> getServiceInterface() {
        return serviceInterface;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public NaviProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(NaviProtocol protocol) {
        this.protocol = protocol;
    }

    public NaviFailStrategy getFailStrategy() {
        return failStrategy;
    }

    public void setFailStrategy(NaviFailStrategy failStrategy) {
        this.failStrategy = failStrategy;
    }

    public NaviSelectorStrategy getSelectorStrategy() {
        return selectorStrategy;
    }

    public void setSelectorStrategy(NaviSelectorStrategy selectorStrategy) {
        this.selectorStrategy = selectorStrategy;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}

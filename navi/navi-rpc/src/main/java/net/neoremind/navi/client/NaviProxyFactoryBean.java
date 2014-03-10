package net.neoremind.navi.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import net.neoremind.navi.client.cache.ServiceLocalCache;
import net.neoremind.navi.client.invoker.InvokerFactory;
import net.neoremind.navi.client.invoker.NaviServiceInvoker;
import net.neoremind.navi.client.selector.NaviFailStrategy;
import net.neoremind.navi.client.selector.NaviSelectorStrategy;
import net.neoremind.navi.client.selector.NaviServiceSelector;
import net.neoremind.navi.client.selector.SelectorFactory;
import net.neoremind.navi.conf.RpcClientConf;
import net.neoremind.navi.constant.NaviCommonConstant;
import net.neoremind.navi.server.protocol.NaviProtocol;
import net.neoremind.navi.util.ZkPathUtil;

/**
 * Navi proxy factory bean <br>
 * When initializing a Rpc bean, this is used to wrap the procedure of remote invocation.
 * </br>
 * 
 * Use contructor <code>NaviProxyFactoryBean(Class<?> serviceInterface, NaviProtocol protocol)</code>
 * to invoke service directly.
 * </br>
 * 
 * Use contructor <code>NaviProxyFactoryBean(String serviceRootPath, Class<?> serviceInterface, NaviProtocol protocol)</code>
 * to get remote server info from local cache which will be updated by zookeeper watcher
 * 
 * @author Zhang Xu
 */
@SuppressWarnings("rawtypes")
public class NaviProxyFactoryBean implements FactoryBean, InitializingBean {

	private final static Logger log = LoggerFactory.getLogger(NaviProxyFactoryBean.class);

	private String serviceRootPath;
	private Class<?> serviceInterface;
	private NaviProtocol protocol = NaviProtocol.PROTOSTUFF;  //by default use protostuff protocol
	private NaviFailStrategy failStrategy = NaviFailStrategy.FAILOVER;  //by default use failover strategy
	private NaviSelectorStrategy selectorStrategy = NaviSelectorStrategy.RANDOM;  //by default use random strategy
	
	/**
	 * Init 
	 * 
	 * @param serviceInterface 
	 */
	public NaviProxyFactoryBean(Class<?> serviceInterface) {
		this.serviceInterface = serviceInterface;
	}
	
	/**
	 * Init 
	 * 
	 * @param serviceRootPath Namespace of the services, if exist then find server list from zookeeper znode
	 * @param serviceInterface 
	 */
	public NaviProxyFactoryBean(String serviceRootPath, Class<?> serviceInterface) {
		this.serviceRootPath = serviceRootPath;
		this.serviceInterface = serviceInterface;
	}
	
	/**
	 * Init 
	 * 
	 * @param serviceInterface 
	 * @param protocol Ser/Deser protocol
	 * @param failStrategy  Fail handle stragegy, failover or failfast
	 * @param selectorStrategy Selector strategy, random or round robin
	 */
	public NaviProxyFactoryBean(Class<?> serviceInterface, NaviProtocol protocol, NaviFailStrategy failStrategy, NaviSelectorStrategy selectorStrategy) {
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
	 * Init
	 * 
	 * @param serviceRootPath Namespace of the services, if exist then find server list from zookeeper znode
	 * @param serviceInterface 
	 * @param protocol Ser/Deser protocol
	 * @param failStrategy  Fail handle stragegy, failover or failfast
	 * @param selectorStrategy Selector strategy, random or round robin
	 */
	public NaviProxyFactoryBean(String serviceRootPath, Class<?> serviceInterface, NaviProtocol protocol, NaviFailStrategy failStrategy, NaviSelectorStrategy selectorStrategy) {
		this.serviceRootPath = serviceRootPath;
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

	public Object getObject() throws Exception {
		return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { serviceInterface }, new NaviInvocationProxy());
	}

	class NaviInvocationProxy implements InvocationHandler {

		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			final Class<?> targetClass; 
			Method targetMethod; 
			Object[] targetArgs;
			//Class<?>[] targetArgClasses;

			targetClass = method.getDeclaringClass();
			targetMethod = method;
			targetArgs = args;
			if (log.isDebugEnabled()) {
				log.debug("Trying to invoke rpc class=" + targetClass + ", method=" + targetMethod);
				log.debug("Invocation class: " + targetClass);
				log.debug("Invocation method: " + targetMethod);
				log.debug("Invocation args:" + Arrays.toString(targetArgs));
			}
			
			String[] serverList = getServerList();
			
			List<NaviServiceInvoker> serviceInvokers = new ArrayList<NaviServiceInvoker>(serverList.length);
			for (String server : serverList) {
				final String serviceUrl = NaviCommonConstant.TRANSPORT_PROTOCOL + server + NaviCommonConstant.TRANSPORT_URL_BASE_PATH + serviceInterface.getSimpleName();
				//log.debug("service url is " + serviceUrl);
				NaviServiceInvoker service = InvokerFactory.getInvokerByProtocal(protocol, targetClass, serviceUrl, RpcClientConf.RPC_CONNECTION_TIMEOUT, RpcClientConf.RPC_READ_TIMEOUT);
				serviceInvokers.add(service);
			}

			NaviServiceSelector serviceSelector = SelectorFactory.getSelector(selectorStrategy, serviceInvokers, RpcClientConf.RPC_RETRY_TIMES, targetMethod, targetArgs);
			return serviceSelector.execute(failStrategy);
		}
	}

	/**
	 * Get server list from local cache(refreshed by zookeeper watcher) or property defined in configuration file
	 * 
	 * @param serviceRootPath
	 * @return String[] server array
	 * @throws RuntimeException if got null
	 */
	protected String[] getServerList() throws RuntimeException {
		String[] serverList = null;
		if (StringUtils.isNotEmpty(serviceRootPath)) {
			String key = ZkPathUtil.buildPath(NaviCommonConstant.ZOOKEEPER_BASE_PATH, serviceRootPath, serviceInterface.getSimpleName());
			log.debug("Try to get server list from local cache for " + key);
			Set<String> serverSet = ServiceLocalCache.get(key);
			if (serverSet == null) {
				throw new RuntimeException("Rpc service instance list is empty in cache for " + key + ", if service registry in Zookeeper enabled please configure the correct zookeeper root path");
			}
			serverList = serverSet.toArray(new String[] {});
		} else {
			serverList = RpcClientConf.SERVER_LIST;
			if (serverList == null) {
				throw new RuntimeException("Rpc client service list empty, please configure the correct server list in conf file");
			}
		}
		return serverList;
	}

	public void afterPropertiesSet() throws Exception {
		log.info("Create proxy Rpc bean " + getClass() + " for interface " + serviceInterface);
	}

	public Class<?> getObjectType() {
		return serviceInterface;
	}

	public boolean isSingleton() {
		return true;
	}

	public String getServiceRootPath() {
		return serviceRootPath;
	}

	public void setServiceRootPath(String serviceRootPath) {
		this.serviceRootPath = serviceRootPath;
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

}

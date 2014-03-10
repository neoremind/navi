package net.neoremind.navi.client.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyFactory {

	@SuppressWarnings("unchecked")
	public static <T> T createProxy(Class<T> type, InvocationHandler handler) {
		Class<?>[] clazz = new Class[] { type };
		return (T) Proxy.newProxyInstance(ProxyFactory.class.getClassLoader(),
				clazz, handler);
	}

}

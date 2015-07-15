package com.baidu.beidou.navi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * ClassName: ExtensionLocator <br/>
 * Function: 利用SPI技术从META-INF/services动态加下类配置
 *
 * @author Zhang Xu
 */
public class ExtensionLocator<T> {

	private static final Logger LOG = LoggerFactory.getLogger(ExtensionLocator.class);

	private static ConcurrentMap<Class<?>, List<?>> EXTENSION_LOCATOR = new ConcurrentHashMap<Class<?>, List<?>>();

	private static Lock lock = new ReentrantLock();

	/**
	 * Get instances dynamically for a class type
	 *
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getInstanceList(Class<T> type) {
		if (type == null) {
			throw new IllegalArgumentException("Type should not be empty");
		}

		List<T> loader = (List<T>) EXTENSION_LOCATOR.get(type);
		if (loader == null) {
			try {
				lock.lock();
				List<T> instanceList = new ArrayList<T>();
				ServiceLoader<T> serviceLoader = ServiceLoader.load(type);
				for (T service : serviceLoader) {
					instanceList.add(service);
				}
				loader = instanceList;
				if (EXTENSION_LOCATOR.putIfAbsent(type, instanceList) != instanceList) {
					loader = (List<T>) EXTENSION_LOCATOR.get(type);
				}
			} catch (Exception e) {
				LOG.error("Error to load SPI instances from META-INF/services/" + type, e);
			} finally {
				lock.unlock();
			}
		}
		return loader;
	}

}

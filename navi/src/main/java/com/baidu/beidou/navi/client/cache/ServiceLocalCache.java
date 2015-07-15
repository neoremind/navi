package com.baidu.beidou.navi.client.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * Client store remote server ip and port in this cache.
 * 
 * There are two caches working here. At one time only one cache is assigned to 
 * cacheReference which provide interaction with outside.
 * 
 * NaviRpcServerListListener will update unused cache and switch between two caches
 * 
 * @see com.baidu.beidou.navi.client.NaviRpcServerListListener
 * 
 * @author Zhang Xu
 */
public class ServiceLocalCache {

	private static Semaphore semaphore = new Semaphore(1);

	private static Map<String, List<String>> masterCache = new ConcurrentHashMap<String, List<String>>();
	private static Map<String, List<String>> slaveCache = new ConcurrentHashMap<String, List<String>>();

	private static Map<String, List<String>> cacheReference = masterCache;

	/**
	 * Prepare to build cache
	 * 
	 * @throws InterruptedException
	 */
	public static void prepare() throws InterruptedException {
		semaphore.acquire();
		if (cacheReference == masterCache) {
			slaveCache.clear();
		} else {
			masterCache.clear();
		}
	}
	
	/**
	 * Switch online and standby cache
	 * 
	 * @throws InterruptedException
	 */
	public static void switchCache() throws InterruptedException {
		//swith between two cache
		if (cacheReference == masterCache) {
			cacheReference = slaveCache;
			masterCache.clear();
		} else {
			cacheReference = masterCache;
			slaveCache.clear();
		}
	}
	
	/**
	 * Unlock cache update
	 * 
	 * @throws InterruptedException
	 */
	public static void done() throws InterruptedException {
		semaphore.release();
	}

	public static List<String> get(String key) {
		return cacheReference.get(key);
	}

	public static void set(String key, List<String> value) {
		if (semaphore.availablePermits() == 0) {
			if (cacheReference == masterCache) {
				slaveCache.put(key, value);
			} else {
				masterCache.put(key, value);
			}
		}
	}

}

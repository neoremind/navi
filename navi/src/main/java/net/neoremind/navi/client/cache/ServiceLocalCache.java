package net.neoremind.navi.client.cache;

import java.util.Map;
import java.util.Set;
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
 * @see net.neoremind.navi.client.NaviRpcServerListListener
 * 
 * @author Zhang Xu
 */
public class ServiceLocalCache {

	private static Semaphore semaphore = new Semaphore(1);

	private static Map<String, Set<String>> cache1 = new ConcurrentHashMap<String, Set<String>>();
	private static Map<String, Set<String>> cache2 = new ConcurrentHashMap<String, Set<String>>();

	private static Map<String, Set<String>> cacheReference = cache1;

	public static void lock() throws InterruptedException {
		semaphore.acquire();
		if (cacheReference == cache1) {
			cache2.clear();
		} else {
			cache1.clear();
		}
	}
	
	public static void switchCache() throws InterruptedException {
		unlock();
		//swith between two cache
		if (cacheReference == cache1) {
			cacheReference = cache2;
		} else {
			cacheReference = cache1;
		}
	}
	
	public static void unlock() throws InterruptedException {
		semaphore.release();
	}

	public static Set<String> get(String key) {
		return cacheReference.get(key);
	}

	public static void set(String key, Set<String> value) {
		if (semaphore.availablePermits() == 0) {
			if (cacheReference == cache1) {
				cache2.put(key, value);
			} else {
				cache1.put(key, value);
			}
		}
	}

}

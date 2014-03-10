package net.neoremind.navi.client;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.neoremind.navi.client.cache.ServiceLocalCache;
import net.neoremind.navi.conf.RpcClientConf;
import net.neoremind.navi.constant.NaviCommonConstant;
import net.neoremind.navi.util.ZkPathUtil;
import net.neoremind.navi.zk.SimpleZooKeeperClient;

/**
 * Watch the change event of the services' info(ip, port, hostname, etc) registered in Zookeeper.
 * 
 * Whenever service status changed, update will automatically notify client.
 * 
 * When one server start, it will register its server info in Zookeeper znode, while this listener 
 * watches specific path to detect the change and process the update event to rebuild local server 
 * cache. When one server shutdown, the process is alike the above but un-register itself.
 * 
 * Client should configure this class in spring bean life cycle by using XML or annotation way.
 * 
 * @see net.neoremind.navi.conf.RpcClientConf
 * @see net.neoremind.navi.client.cache.ServiceLocalCache
 * 
 * @author Zhang Xu
 */
public class NaviRpcServerListListener {

	private final static Logger log = LoggerFactory.getLogger(NaviRpcServerListListener.class);

	/**
	 * Store lastest update time
	 */
	protected FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
	private Date lastUpdateTime;

	/**
	 * Zookeeper client
	 */
	protected SimpleZooKeeperClient zkClient;

	public NaviRpcServerListListener() {
		try {
			log.info("Connecting to zookeeper server - " + RpcClientConf.ZK_SERVER_LIST);
			zkClient = new SimpleZooKeeperClient(RpcClientConf.ZK_SERVER_LIST, new ServiceWatcher());
			updateServiceLocalCache();
		} catch (Exception e) {
			log.error("Zookeeper client initialization failed, " + e.getMessage(), e);
		}
	}

	/**
	 * Refresh all service info into local cache
	 */
	private void updateServiceLocalCache() {
		//children list changed
		try {
			if (lastUpdateTime == null) {
				log.info("First update local service cache since server started");
			} else {
				log.info("Last update time:" + dateFormat.format(lastUpdateTime));
			}
			zkClient.getChildren(NaviCommonConstant.ZOOKEEPER_BASE_PATH);
			log.info("Zookeeper global root path is " + NaviCommonConstant.ZOOKEEPER_BASE_PATH);
			ServiceLocalCache.lock();
			if (ArrayUtils.isEmpty(RpcClientConf.ZK_WATCH_NAMESPACE_PATHS)) {
				log.info("Zookeeper watched name space is empty");
				return;
			}
			log.info("Zookeeper watched name spaces are " + Arrays.toString(RpcClientConf.ZK_WATCH_NAMESPACE_PATHS));
			for (String watchedNameSpacePath : RpcClientConf.ZK_WATCH_NAMESPACE_PATHS) {
				try {
					List<String> serviceChildren = zkClient.getChildren(ZkPathUtil.buildPath(NaviCommonConstant.ZOOKEEPER_BASE_PATH, watchedNameSpacePath));
					log.info("Find " + serviceChildren.size() + " services under path - " + ZkPathUtil.buildPath(NaviCommonConstant.ZOOKEEPER_BASE_PATH, watchedNameSpacePath));
					if (CollectionUtils.isNotEmpty(serviceChildren)) {
						for (String service : serviceChildren) {
							String servicePath = ZkPathUtil.buildPath(NaviCommonConstant.ZOOKEEPER_BASE_PATH, watchedNameSpacePath, service);
							List<String> serverList = zkClient.getChildren(servicePath);
							log.info("Find " + serverList.size() + " servers available of service - " + servicePath + ", server list = " + Arrays.toString(serverList.toArray(new String[] {})));
							if (CollectionUtils.isNotEmpty(serverList)) {
								ServiceLocalCache.set(servicePath, new HashSet<String>(serverList));
							}
						}
					}
				} catch (NoNodeException e) {
					log.error("Node not found, " + e.getMessage());
				}
			}
			ServiceLocalCache.switchCache();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			try {
				ServiceLocalCache.unlock();
			} catch (Exception e2) {
				log.error(e.getMessage(), e);
			}
		} finally {
			lastUpdateTime = new Date();
		}
	}

	class ServiceWatcher implements Watcher {

		/**
		 * Process when receive watched event
		 */
		public void process(WatchedEvent event) {
			log.info("Receive watched event：" + event);
			if (KeeperState.SyncConnected == event.getState()) {
				if (EventType.None == event.getType() && null == event.getPath()) {
					log.info("Connect to zookeeper server successfully");
					log.info("Start to init service registration from path - " + NaviCommonConstant.ZOOKEEPER_BASE_PATH);
				} else {
					if (event.getType() == EventType.NodeChildrenChanged) {
						log.info("********************************************************");
						log.info("*     Zookeeper service registry changes detected!     *");
						log.info("********************************************************");
						updateServiceLocalCache();
					}
				}
			}
		}
	}

}

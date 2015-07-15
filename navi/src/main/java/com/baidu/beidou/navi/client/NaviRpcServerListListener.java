package com.baidu.beidou.navi.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.zookeeper.KeeperException.NoAuthException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navi.client.cache.ServiceLocalCache;
import com.baidu.beidou.navi.conf.RpcClientConf;
import com.baidu.beidou.navi.constant.NaviCommonConstant;
import com.baidu.beidou.navi.util.ArrayUtil;
import com.baidu.beidou.navi.util.CollectionUtil;
import com.baidu.beidou.navi.util.StringUtil;
import com.baidu.beidou.navi.util.ZkPathUtil;
import com.baidu.beidou.navi.zk.SimpleZooKeeperClient;

/**
 * Watch the change events of the services' info(ip, port, hostname, etc) registered in Zookeeper. Whenever a service
 * status changes, an update will be notified to the client automatically. When a physical server starts, it will create
 * an ephemeral Zookeeper's znode and put server into into the data node, while this listener watches some specific
 * paths(one or more) to detect the changes and process the update events to update the local cache which stores all
 * server info. When a server shuts down, the process behaves just like the above except it will kick off the dead
 * server from the local cache.
 * <p/>
 * Client should run the listener as daemon thread in the back or just left this listener to spring which will create
 * and manage the life cycle of the bean automatically thru either XML or annotation way.
 * 
 * @see com.baidu.beidou.navi.conf.RpcClientConf
 * @see com.baidu.beidou.navi.client.cache.ServiceLocalCache
 * @author Zhang Xu
 */
public class NaviRpcServerListListener {

    private static final Logger LOG = LoggerFactory.getLogger(NaviRpcServerListListener.class);

    /**
     * Zookeeper client
     */
    protected volatile SimpleZooKeeperClient zkClient;

    /**
     * Creates a new instance of NaviRpcServerListListener.
     * <p/>
     * It will try to connect to zookeeper server cluster
     */
    public NaviRpcServerListListener() {
        doConnect();
    }

    /**
     * Refresh all service info in local cache
     */
    private void updateServiceLocalCache() {
        try {
            LOG.info("Update local cache now...");
            zkClient.getChildren(NaviCommonConstant.ZOOKEEPER_BASE_PATH);
            LOG.info("Zookeeper global root path is " + NaviCommonConstant.ZOOKEEPER_BASE_PATH);
            ServiceLocalCache.prepare();
            if (ArrayUtil.isEmpty(RpcClientConf.ZK_WATCH_NAMESPACE_PATHS)) {
                LOG.info("Zookeeper watched name space is empty");
                return;
            }
            LOG.info("Zookeeper watched name spaces are "
                    + Arrays.toString(RpcClientConf.ZK_WATCH_NAMESPACE_PATHS));
            for (String watchedNameSpacePath : RpcClientConf.ZK_WATCH_NAMESPACE_PATHS) {
                try {
                    List<String> serviceChildren = zkClient.getChildren(ZkPathUtil.buildPath(
                            NaviCommonConstant.ZOOKEEPER_BASE_PATH, watchedNameSpacePath));
                    LOG.info("======>Find "
                            + serviceChildren.size()
                            + " interfaces under service path - "
                            + ZkPathUtil.buildPath(NaviCommonConstant.ZOOKEEPER_BASE_PATH,
                                    watchedNameSpacePath));
                    if (CollectionUtil.isNotEmpty(serviceChildren)) {
                        for (String service : serviceChildren) {
                            String servicePath = ZkPathUtil.buildPath(
                                    NaviCommonConstant.ZOOKEEPER_BASE_PATH, watchedNameSpacePath,
                                    service);
                            List<String> serverList = zkClient.getChildren(servicePath);
                            LOG.info(serverList.size() + " servers available for " + servicePath
                                    + ", server list = "
                                    + Arrays.toString(serverList.toArray(new String[] {})));
                            if (CollectionUtil.isNotEmpty(serverList)) {
                                Collections.sort(serverList); // order by natural
                                ServiceLocalCache.set(servicePath, serverList);
                            }
                        }
                    } else {
                        LOG.warn("No services registered");
                    }
                } catch (NoAuthException e) {
                    LOG.error(
                            "[FATAL ERROR]No auth error! Please check zookeeper digest auth code!!! "
                                    + e.getMessage(), e);
                } catch (NoNodeException e) {
                    LOG.error("Node not found, " + e.getMessage());
                }
            }
            ServiceLocalCache.switchCache();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            try {
                ServiceLocalCache.done();
            } catch (Exception e2) {
                LOG.error(e2.getMessage(), e2);
            }
        }
    }

    /**
     * Connect to zookeeper server
     */
    private void doConnect() {
        try {
            LOG.info("Connecting to zookeeper server - " + RpcClientConf.ZK_SERVER_LIST);
            zkClient = new SimpleZooKeeperClient(RpcClientConf.ZK_SERVER_LIST,
                    RpcClientConf.ZK_DIGEST_AUTH, new ServiceWatcher());
            updateServiceLocalCache();
        } catch (Exception e) {
            LOG.error("Zookeeper client initialization failed, " + e.getMessage(), e);
        }
    }

    /**
     * Check if the changed path is in the watched paths that the client should care
     * 
     * @param path
     * @return
     */
    private boolean isInZkWathcedNamespacePaths(String path) {
        if (StringUtil.isEmpty(path)) {
            return false;
        }
        for (String watchNamespacePath : RpcClientConf.ZK_WATCH_NAMESPACE_PATHS) {
            if (StringUtil.isEmpty(watchNamespacePath)) {
                continue;
            }
            if (path.contains(watchNamespacePath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Inner ServiceWatcher
     */
    class ServiceWatcher implements Watcher {

        /**
         * Process when receive watched event
         */
        @Override
        public void process(WatchedEvent event) {
            LOG.info("Receive watched event：" + event);
            if (KeeperState.SyncConnected == event.getState()) {
                if (EventType.None == event.getType() && null == event.getPath()) {
                    LOG.info("Connect to zookeeper server successfully!");
                    LOG.info("Start to init service registration from path - "
                            + NaviCommonConstant.ZOOKEEPER_BASE_PATH);
                } else {
                    if (event.getType() == EventType.NodeChildrenChanged) {
                        LOG.info("********************************************************");
                        LOG.info("*     Zookeeper service registry changes detected!     *");
                        LOG.info("********************************************************");
                        LOG.info("Changed path is " + event.getPath());
                        if (!isInZkWathcedNamespacePaths(event.getPath())) {
                            LOG.info("Changed ignore.. because path - " + event.getPath()
                                    + " is not in wathced namespace paths - "
                                    + RpcClientConf.ZK_WATCH_NAMESPACE_PATHS);
                            return;
                        }
                        updateServiceLocalCache();
                    }
                }
            } else if (KeeperState.Disconnected == event.getState()) {
                LOG.warn("Disconnect from server, probably due to network failure... and "
                        + this.getClass().getSimpleName() + " will alert only when in that case.");
            } else if (KeeperState.Expired == event.getState()) {
                try {
                    if (zkClient != null) {
                        zkClient.close();
                        LOG.info("Explictly closing ok");
                    }
                } catch (Exception e) {
                    LOG.error("zk client connection is expired, but failed to close, so fail over it");
                }
                LOG.info("Receive expired event, as zookeeper client is kick off by server due to sessionTimeout reason, try re-establishing connection with zookeeper server cluster...");
                doConnect(); // re-establish connection with zookeeper server cluster
            } else {
                LOG.warn("This event " + event.getType() + " is not handle by watcher");
            }
        }
    }

}

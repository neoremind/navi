package com.baidu.beidou.navi.server.locator.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navi.conf.RpcServerConf;
import com.baidu.beidou.navi.constant.NaviCommonConstant;
import com.baidu.beidou.navi.server.locator.MethodDescriptor;
import com.baidu.beidou.navi.server.locator.PublishHandler;
import com.baidu.beidou.navi.server.locator.ServiceLocator;
import com.baidu.beidou.navi.server.locator.ServiceRegistry;
import com.baidu.beidou.navi.server.locator.ZooAware;
import com.baidu.beidou.navi.util.CollectionUtil;
import com.baidu.beidou.navi.util.Function;
import com.baidu.beidou.navi.util.ZkPathUtil;
import com.baidu.beidou.navi.util.ZkRegisterInfoUtil;
import com.baidu.beidou.navi.zk.SimpleZooKeeperClient;

/**
 * ClassName: ZookeeperPublishHandler <br/>
 * Function: 在zookeeper上发布注册服务，供其他调用方订阅
 * <p>
 * 作为{@link PublishHandler}的一个实现，配合{@link ServiceLocator}来对加载定位到的所有服务进行发布工作
 * 
 * @author Zhang Xu
 */
public class ZookeeperPublishHandler implements PublishHandler, ZooAware {

    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperPublishHandler.class);

    /**
     * Zookeeper客户端
     */
    protected SimpleZooKeeperClient zkClient;

    /**
     * 如果zookeeper断连，重试发布次数记录
     */
    private AtomicInteger republishCount = new AtomicInteger(0);

    /**
     * 如果启用了zookeeper注册机制，那么尝试连接zookeeper并且创建服务的根node节点
     * 
     * @return 如果连接并且创建节点成功返回true，否则false
     */
    protected boolean doConnectAndCreateZkPath() {
        if (RpcServerConf.ENABLE_ZK_REGISTRY) {
            String path = ZkPathUtil.buildPath(NaviCommonConstant.ZOOKEEPER_BASE_PATH,
                    RpcServerConf.ZK_REGISTRY_NAMESPACE);
            try {
                LOG.info("Connecting to zookeeper server - " + RpcServerConf.ZK_SERVER_LIST);
                zkClient = new SimpleZooKeeperClient(RpcServerConf.ZK_SERVER_LIST,
                        RpcServerConf.ZK_DIGEST_AUTH, new ServerServiceWatcher());
                LOG.info("Connect to zookeeper server successfully!");
                LOG.info("Trying create service registry path - " + path);
                zkClient.createNodeForRecursive(path, "".getBytes());
                LOG.info("Create service registry path successfully!");
            } catch (NodeExistsException e) {
                LOG.warn("Zookeeper path is already exists - " + path);
            } catch (Exception e) {
                LOG.error("Zookeeper client initialization failed, " + e.getMessage(), e);
                return false;
            }
        }
        return true;
    }

    /**
     * 发布服务
     * 
     * @param descs
     *            所有的服务描述
     * @return 发布成功与否
     * @see com.baidu.beidou.navi.server.locator.PublishHandler#publish(java.util.Collection)
     */
    @Override
    public <KEY> boolean publish(Collection<MethodDescriptor<KEY>> descs) {
        if (CollectionUtil.isEmpty(descs)) {
            LOG.warn("No service to publish");
            return false;
        }

        List<String> methodNames = CollectionUtil.transform(descs,
                new Function<MethodDescriptor<KEY>, String>() {
                    @Override
                    public String apply(MethodDescriptor<KEY> input) {
                        return String.format("%s.%s(..)", input.getInterfClass().getSimpleName(),
                                input.getMethod().getName());
                    }
                });

        List<String> serviceNames = CollectionUtil.transform(descs,
                new Function<MethodDescriptor<KEY>, String>() {

                    @Override
                    public String apply(MethodDescriptor<KEY> input) {
                        return input.getInterfClass().getSimpleName();
                    }

                });

        Set<String> distinctServices = new HashSet<String>(serviceNames);

        Collections.sort(methodNames);
        Collections.sort(serviceNames);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Export rpc service methods which may include unneccessary methods: "
                    + Arrays.toString(methodNames.toArray(new String[] {})));
            LOG.debug("Export total " + methodNames.size() + " rpc service methods");
        }
        LOG.info("Export rpc services: "
                + Arrays.toString(distinctServices.toArray(new String[] {})));
        LOG.info("Export total " + distinctServices.size() + " rpc services");

        if (!RpcServerConf.ENABLE_ZK_REGISTRY) {
            LOG.info("Export services soley at localhost NOT register at zookeeper");
            return false;
        }

        if (!doConnectAndCreateZkPath()) {
            LOG.info("Export services failed at zookeeper and exit publishing");
            return false;
        }

        String localIp = ZkRegisterInfoUtil.getLocalHostIp();
        String localHostname = ZkRegisterInfoUtil.getLocalHostName();

        // override configuration files' port
        RpcServerConf.SERVER_PORT = Integer.valueOf(ZkRegisterInfoUtil.getLocalHostPort());

        List<String> failedRegistryServiceList = CollectionUtil.createArrayList();
        for (String service : distinctServices) {
            String zkPath = ZkPathUtil.buildPath(NaviCommonConstant.ZOOKEEPER_BASE_PATH,
                    RpcServerConf.ZK_REGISTRY_NAMESPACE, service, localIp + ":"
                            + RpcServerConf.SERVER_PORT);
            try {
                if (zkClient.exists(zkPath) != null) {
                    zkClient.delete(zkPath);
                }
                zkClient.createSessionNodeForRecursive(zkPath, localHostname.getBytes());
                LOG.info("Service registers at Zookeeper successfully - " + zkPath);
            } catch (NoNodeException e) {
                LOG.warn("Zookeeper path cannot found - " + zkPath);
            } catch (NodeExistsException e) {
                LOG.warn("Zookeeper path is already exists - " + zkPath);
            } catch (Exception e) {
                LOG.error("Zookeeper create path failed for " + zkPath, e);
                failedRegistryServiceList.add(zkPath);
            }
        }

        if (CollectionUtil.isEmpty(failedRegistryServiceList)) {
            LOG.info("Registry all " + (distinctServices.size() - failedRegistryServiceList.size())
                    + " services to zookeeper successfully");
        } else {
            LOG.info("Registry services to zookeeper encounter some problems, the failure registry paths are "
                    + Arrays.toString(failedRegistryServiceList.toArray(new String[] {})));
        }

        LOG.info("Publish for the " + republishCount.incrementAndGet() + " times");
        return true;
    }

    /**
     * ClassName: ServerServiceWatcher <br/>
     * Function: 服务端带有重连功能的Watcher
     */
    class ServerServiceWatcher implements Watcher {

        /**
         * 当发生事件时候的监听处理逻辑
         * 
         * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
         */
        @Override
        public void process(WatchedEvent event) {
            LOG.info("Receive watched event：" + event);
            if (KeeperState.SyncConnected == event.getState()) {
                if (EventType.None == event.getType() && null == event.getPath()) {
                    LOG.info("Connect to zookeeper server successfully!");
                } else {
                    if (event.getType() == EventType.NodeChildrenChanged) {
                        LOG.info("Changed path is " + event.getPath());
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
                LOG.info("Receive expired event, as zookeeper client is kick off by server due to "
                        + " sessionTimeout reason, try re-establishing connection with zookeeper server cluster...");
                publish(ServiceRegistry.getInstance().getAllServiceDescriptors());
            } else {
                LOG.warn("This event " + event.getType() + " is not handle by watcher");
            }
        }
    }

    /**
     * 返回zookeeper client
     * 
     * @return zookeeper客户端引用
     * @see com.baidu.beidou.navi.server.locator.ZooAware#getZkClient()
     */
    @Override
    public SimpleZooKeeperClient getZkClient() {
        return zkClient;
    }

}

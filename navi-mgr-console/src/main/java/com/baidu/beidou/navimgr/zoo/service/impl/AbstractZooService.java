package com.baidu.beidou.navimgr.zoo.service.impl;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper.States;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navimgr.zoo.SimpleZooKeeperClient;
import com.baidu.beidou.navimgr.zoo.ZkPathUtil;
import com.baidu.beidou.navimgr.zoo.ZooConf;
import com.baidu.beidou.navimgr.zoo.ZooNode;
import com.baidu.beidou.navimgr.zoo.service.ZooService;

public abstract class AbstractZooService implements ZooService {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractZooService.class);

    /**
     * Zookeeper客户端
     */
    protected SimpleZooKeeperClient zkClient;

    /**
     * Zookeeper配置
     */
    @Resource
    protected ZooConf zooConf;

    /**
     * 连接zookeeper
     * 
     * @return SimpleZooKeeperClient
     * @throws IOException
     * @throws InterruptedException
     */
    public abstract SimpleZooKeeperClient doConnect() throws IOException, InterruptedException;

    /**
     * 连接zookeeper
     * 
     * @return 是否连接成功
     */
    @PostConstruct
    protected boolean connect() {
        String path = ZkPathUtil.buildPath(zooConf.getNodePathPrefix());
        try {
            zkClient = doConnect();
            LOG.info("Connect to zookeeper server successfully!");
            List<String> children = zkClient.getChildren(path);
            LOG.info("Path children get: " + children);
        } catch (Exception e) {
            LOG.error("Zookeeper client initialization failed, " + e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * 递归地获取一颗节点树
     * 
     * @return 节点树root节点
     * @see com.baidu.beidou.navimgr.zoo.service.ZooService#getNodeRecrusively()
     */
    @Override
    public ZooNode getNodeRecrusively() {
        ZooNode root = new ZooNode(zooConf.getNodePathPrefix(), zooConf.getNodePathPrefix());
        root.setOpen(true);
        if (zkClient == null || zkClient.getState() != States.CONNECTED) {
            LOG.warn("Zookeeper not connected!");
            return root;
        }
        LOG.info("Start to get zoo tree recrusively...");
        long start = System.currentTimeMillis();
        setNodeRecrusively(root, 0);
        LOG.info("Getting zoo tree costs " + (System.currentTimeMillis() - start) + "ms");
        return root;
    }

    /**
     * 递归的填充节点
     * 
     * @param zooNode
     *            节点
     * @param depth
     *            树深度
     */
    private void setNodeRecrusively(ZooNode zooNode, int depth) {
        try {
            int currDepth = depth + 1;
            List<String> children = zkClient.getChildren(zooNode.getPath());
            if (CollectionUtils.isEmpty(children)) {
                zooNode.setLeaf(true);
                return;
            }
            for (String name : children) {
                ZooNode node = new ZooNode(ZkPathUtil.buildPath(zooNode.getPath(), name), name);
                if (currDepth < 2) {
                    node.setOpen(true);
                }
                zooNode.getChildren().add(node);
                setNodeRecrusively(node, currDepth);
            }
        } catch (NoNodeException e) {
            LOG.warn("Zookeeper path not found" + e.getMessage(), e);
        } catch (Exception e) {
            LOG.error("Zookeeper get children error" + e.getMessage(), e);
        }
    }

    /**
     * ClassName: ServerServiceWatcher <br/>
     * Function: 服务端带有重连功能的Watcher
     */
    class ServerServiceWatcher implements Watcher {

        /**
         * 当发生事件时候的监听处理逻辑
         * 
         * @param event
         *            事件
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
                LOG.info("Receive expired event, as zookeeper client is kick off by server "
                        + "due to sessionTimeout reason, "
                        + "try re-establishing connection with zookeeper server cluster...");
                connect();
            } else {
                LOG.warn("This event " + event.getType() + " is not handle by watcher");
            }
        }
    }

}

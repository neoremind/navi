package com.baidu.beidou.sample;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import com.baidu.beidou.navi.conf.RpcClientConf;
import com.baidu.beidou.navi.zk.SimpleZooKeeperClient;

public class SimpleZookeeperTest {

    private static final String connectString = "10.48.56.33:8701,10.48.52.17:8701,10.48.52.31:8701,10.94.37.23:8701,10.94.37.24:8701";

    SimpleZooKeeperClient zkClient;

    public void test() throws Exception {
        zkClient = new SimpleZooKeeperClient(connectString, null, new ServiceWatcher());
    }

    public static void main(String[] args) throws Exception {
        new SimpleZookeeperTest().test();
        Thread.sleep(1200000);
    }

    private void doConnect() {
        try {
            System.out.println("Connecting to zookeeper server - " + RpcClientConf.ZK_SERVER_LIST);
            zkClient = new SimpleZooKeeperClient(connectString, null, new ServiceWatcher());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            zkClient.getChildren("/navi-demo-server/main");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ServiceWatcher implements Watcher {

        /**
         * Process when receive watched event
         */
        @Override
        public void process(WatchedEvent event) {
            System.out.println("Receive watched event：" + event);
            if (KeeperState.SyncConnected == event.getState()) {
                if (EventType.None == event.getType() && null == event.getPath()) {
                    System.out.println("Connect to zookeeper server successfully!");
                } else {
                    if (event.getType() == EventType.NodeChildrenChanged) {
                        System.out.println("*     Zookeeper service registry changes detected!     *");
                    }
                }
            } else if (KeeperState.Disconnected == event.getState() || KeeperState.Expired == event.getState()) {
                try {
                    if (zkClient != null) {
                        zkClient.close();
                    }
                } catch (Exception e) {
                    System.out.println("zk client is disconnected or expired, but fail to close, so fail over it");
                }
                System.out
                        .println("Receive Disconnected or Expired event, try re-establishing connection with zookeeper server cluster...");
                doConnect(); // re-establish connection with zookeeper server cluster
            }
        }
    }
}

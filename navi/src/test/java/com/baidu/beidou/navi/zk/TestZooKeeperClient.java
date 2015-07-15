package com.baidu.beidou.navi.zk;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class TestZooKeeperClient {

    private static CountDownLatch semaphore = new CountDownLatch(1);

    public static void main(String[] args) {
        new TestZooKeeperClient().simpleClient();
    }

    private final static String auth = "beidouRd123";

    public void simpleClient() {
        String connectString = "10.48.56.33:8701,10.48.52.17:8701,10.48.52.31:8701,10.94.37.23:8701,10.94.37.24:8701";
        try {
            // TestWatcher watcher = new TestWatcher();
            ServiceWatcher watcher = new ServiceWatcher();
            SimpleZooKeeperClient zkClient = new SimpleZooKeeperClient(connectString, watcher, auth, 20000);
            // String path = "/navi";
            // System.out.println("before: " + new String(zkClient.getData(path)));
            // zkClient.setData(path, String.valueOf(System.currentTimeMillis()).getBytes());
            // System.out.println("after: " + new String(zkClient.getData(path)));
            //
            // String path = "/test";
            // zkClient.createNode(path, "123".getBytes());
            // zkClient.createNode(path, "123".getBytes());
            // String path2 = "/aaa1/bbb1";
            // zkClient.createSessionNode(path2, "".getBytes());
            // String path3 = "/aaa1/bbb1/ccc1";
            // zkClient.createSessionNode(path3, "".getBytes());
            // String data = "123";
            // zkClient.setData(path3, data.getBytes());
            // System.out.println("now: " + new String(zkClient.getData(path3)));

            String path = "/navi_rpc/navi-demo-server";
            zkClient.delete(path);
            //zkClient.createSessionNode(path, "".getBytes());
            //zkClient.createSessionNode(path + "/uuu", "".getBytes());
//            zkClient.delete(path);
//            List<String> childs = zkClient.getChildren(path);
//            for (String child : childs) {
//                System.out.println(child);
//            }
//            if (zkClient.exists(path) == null) {
//                zkClient.createSessionNodeForRecursive(path + "/1.1.1.1", "".getBytes());
//                zkClient.createSessionNodeForRecursive(path + "/2.2.2.2", "".getBytes());
//                zkClient.createSessionNodeForRecursive(path + "/3.3.3.3", "".getBytes());
//                zkClient.createSessionNodeForRecursive(path + "/4.4.4.4", "".getBytes());
//            }

            // String pathIP1 = path + "/1.1.1.1";
            // String pathIP2 = path + "/2.2.2.2";
            // zkClient.createSessionNode(pathIP1, NetUtils.getLocalAddress().getAddress());
            // zkClient.createSessionNode(pathIP2, NetUtils.getLocalAddress().getAddress());
            // System.out.println("children: " + Arrays.toString(zkClient.getChildren(path).toArray()));
            // System.out.println("pathIP2: " + new String(zkClient.getData(pathIP2)));

            semaphore.await();
            // path = "/aaa/bbb3";
            // Stat stat = zkClient.exists(path);
            Thread.sleep(100000);
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
                // if( EventType.None == event.getType() && null == event.getPath() ){
                // System.out.println("12333333333333333333");
                // }else
                if (event.getType() == EventType.NodeDataChanged) {
                    // children list changed
                    try {
                        // System.out.println( this.getChildren( event.getPath() ) );
                        // _semaphore.countDown();
                        System.out.println("changed!");
                        semaphore.countDown();
                    } catch (Exception e) {
                    }
                }
            }
        }

    }

}

class TestWatcher implements Watcher {

    @Override
    public void process(WatchedEvent arg0) {
        System.out.println("TestWatcher - " + arg0.getPath());
    }

}

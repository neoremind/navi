package com.baidu.beidou.navi.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * ClassName: SimpleServerWatcher <br/>
 * 
 * @author Zhang Xu
 */
public class SimpleServerWatcher implements Watcher {

    /**
     * @see org.apache.zookeeper.Watcher#process(org.apache.zookeeper.WatchedEvent)
     */
    @Override
    public void process(WatchedEvent arg0) {
        // System.out.println("TestWatcher - " + arg0.getPath());
    }

}

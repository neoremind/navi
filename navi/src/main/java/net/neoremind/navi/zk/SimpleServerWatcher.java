package net.neoremind.navi.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class SimpleServerWatcher implements Watcher {

	public void process(WatchedEvent arg0) {
		//System.out.println("TestWatcher - " + arg0.getPath());
	}

}

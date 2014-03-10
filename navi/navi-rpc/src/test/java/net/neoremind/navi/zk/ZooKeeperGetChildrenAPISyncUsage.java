package net.neoremind.navi.zk;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * ZooKeeper API 获取子节点列表，使用同步(sync)接口。
 * 
 * @author Zhang Xu
 */
public class ZooKeeperGetChildrenAPISyncUsage implements Watcher {

	private CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static CountDownLatch _semaphore = new CountDownLatch(1);
	private ZooKeeper zk;

	ZooKeeper createSession(String connectString, int sessionTimeout, Watcher watcher) throws IOException {
		ZooKeeper zookeeper = new ZooKeeper(connectString, sessionTimeout, watcher);
		try {
			connectedSemaphore.await();
		} catch (InterruptedException e) {
		}
		return zookeeper;
	}

	/** create path by sync */
	void createPath_sync(String path, String data, CreateMode createMode) throws IOException, KeeperException, InterruptedException {

		if (zk == null) {
			zk = this.createSession("10.48.56.33:8701,10.48.52.17:8701,10.48.52.31:8701", 5000, this);
		}
		zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, createMode);
	}

	/** Get children znodes of path and set watches */
	@SuppressWarnings("rawtypes")
	List getChildren(String path) throws KeeperException, InterruptedException, IOException {

		System.out.println("===Start to get children znodes.===");
		if (zk == null) {
			zk = this.createSession("10.48.56.33:8701,10.48.52.17:8701,10.48.52.31:8701", 5000, this);
		}
		return zk.getChildren(path, true);
	}

	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws IOException, InterruptedException {

		ZooKeeperGetChildrenAPISyncUsage sample = new ZooKeeperGetChildrenAPISyncUsage();
		String path = "/get_children_test";

		try {

			//sample.createPath_sync(path, "", CreateMode.PERSISTENT);
			//sample.createPath_sync(path + "/c1", "", CreateMode.PERSISTENT);
			List childrenList = sample.getChildren(path);
			System.out.println(childrenList);
			//Add a new child znode to test watches event notify.
			sample.createPath_sync(path + "/c2", "", CreateMode.EPHEMERAL);
			_semaphore.await();
		} catch (KeeperException e) {
			System.err.println("error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Process when receive watched event
	 */
	public void process( WatchedEvent event ) {
		System.out.println( "Receive watched event：" + event );
		if ( KeeperState.SyncConnected == event.getState() ) {

			if( EventType.None == event.getType() && null == event.getPath() ){
				connectedSemaphore.countDown();
			}else if( event.getType() == EventType.NodeChildrenChanged ){
				//children list changed
				try {
					System.out.println( this.getChildren( event.getPath() ) );
					//_semaphore.countDown();
				} catch ( Exception e ) {}
			}

		}
	}
	
}

package com.baidu.beidou.navi.server.locator;

import com.baidu.beidou.navi.zk.SimpleZooKeeperClient;

/**
 * ClassName: ZooAware <br/>
 * Function: Zookeeper感应上下文，实现该接口的类需要对外公布zookeeper客户端{@link SimpleZooKeeperClient}引用
 * 
 * @author Zhang Xu
 */
public interface ZooAware {

    /**
     * 返回zookeeper client
     * 
     * @return zookeeper客户端引用
     */
    SimpleZooKeeperClient getZkClient();

}

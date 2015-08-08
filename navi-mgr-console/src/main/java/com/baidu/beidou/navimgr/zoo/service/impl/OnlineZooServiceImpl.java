package com.baidu.beidou.navimgr.zoo.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baidu.beidou.navimgr.zoo.SimpleZooKeeperClient;
import com.baidu.beidou.navimgr.zoo.service.ZooService;

/**
 * ClassName: OnlineZooServiceImpl <br/>
 * Function: 线上zookeeper服务实现
 * 
 * @author Zhang Xu
 */
@Service
public class OnlineZooServiceImpl extends AbstractZooService implements ZooService {

    private static final Logger LOG = LoggerFactory.getLogger(OnlineZooServiceImpl.class);

    /**
     * 连接zookeeper
     * 
     * @see com.baidu.beidou.navimgr.zoo.service.impl.AbstractZooService#doConnect()
     */
    @Override
    public SimpleZooKeeperClient doConnect() throws IOException, InterruptedException {
        LOG.info("Connecting to zookeeper server - " + zooConf.getOnlineServerList());
        return new SimpleZooKeeperClient(zooConf.getOnlineServerList(),
                zooConf.getOnlineDigestAuth(), new ServerServiceWatcher(),
                zooConf.getSessionTimeout(), zooConf.getConnTimeout());
    }

}

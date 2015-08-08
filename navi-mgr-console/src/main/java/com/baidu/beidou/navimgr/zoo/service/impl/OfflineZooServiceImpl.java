package com.baidu.beidou.navimgr.zoo.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.baidu.beidou.navimgr.zoo.SimpleZooKeeperClient;
import com.baidu.beidou.navimgr.zoo.service.ZooService;

/**
 * ClassName: OfflineZooServiceImpl <br/>
 * Function: 线下zookeeper服务实现
 * 
 * @author Zhang Xu
 */
@Service
public class OfflineZooServiceImpl extends AbstractZooService implements ZooService {

    private static final Logger LOG = LoggerFactory.getLogger(OfflineZooServiceImpl.class);

    /**
     * 连接zookeeper
     * 
     * @see com.baidu.beidou.navimgr.zoo.service.impl.AbstractZooService#doConnect()
     */
    @Override
    public SimpleZooKeeperClient doConnect() throws IOException, InterruptedException {
        LOG.info("Connecting to zookeeper server - " + zooConf.getOfflineServerList());
        return new SimpleZooKeeperClient(zooConf.getOfflineServerList(),
                zooConf.getOfflineDigestAuth(), new ServerServiceWatcher(),
                zooConf.getSessionTimeout(), zooConf.getConnTimeout());
    }

}

package com.baidu.beidou.navimgr.zoo.service;

import com.baidu.beidou.navimgr.zoo.ZooNode;

/**
 * ClassName: ZooService <br/>
 * Function: zookeeper服务
 * 
 * @author Zhang Xu
 */
public interface ZooService {

    /**
     * 递归地获取一颗节点树
     * 
     * @return 节点树root节点
     */
    ZooNode getNodeRecrusively();

}

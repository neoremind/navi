package com.baidu.beidou.navi.client.ha;

import java.lang.reflect.Method;
import java.util.List;

import com.baidu.beidou.navi.client.NaviRpcClient;
import com.baidu.beidou.navi.client.attachment.Attachment;

/**
 * ClassName: LoadBalanceStrategy <br/>
 * Function: 负载均衡器
 * 
 * @author Zhang Xu
 */
public interface LoadBalanceStrategy {

    /**
     * 根据客户端的连接采用负载均衡策略调用发起远程通信
     * 
     * @param clientList
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    Object transport(List<NaviRpcClient> clientList, Method method, Object[] args) throws Throwable;

    /**
     * 根据客户端的连接采用负载均衡策略调用发起远程通信
     * 
     * @param clientList
     * @param method
     * @param args
     * @param attachment
     * @return
     * @throws Throwable
     */
    Object transport(List<NaviRpcClient> clientList, Method method, Object[] args,
            Attachment attachment) throws Throwable;

}

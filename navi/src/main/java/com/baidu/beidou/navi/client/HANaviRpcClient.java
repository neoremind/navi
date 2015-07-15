package com.baidu.beidou.navi.client;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navi.client.attachment.Attachment;
import com.baidu.beidou.navi.client.ha.LoadBalanceStrategy;
import com.baidu.beidou.navi.exception.rpc.RpcException;
import com.baidu.beidou.navi.util.CollectionUtil;

/**
 * ClassName: HANaviRpcClient <br/>
 * Function: 带有复杂均衡以及容错处理策略的客户端
 * 
 * @author Zhang Xu
 */
public class HANaviRpcClient implements NaviRpcClient {

    private static final Logger LOG = LoggerFactory.getLogger(HANaviRpcClient.class);

    /**
     * 负载均衡策略
     */
    private LoadBalanceStrategy loadBalanceStrategy;

    /**
     * 客户端list
     */
    private List<NaviRpcClient> clientList;

    /**
     * Creates a new instance of HANaviRpcClient.
     * 
     * @param clientList
     * @param loadBalanceStrategy
     */
    public HANaviRpcClient(List<NaviRpcClient> clientList, LoadBalanceStrategy loadBalanceStrategy) {
        this.clientList = clientList;
        this.loadBalanceStrategy = loadBalanceStrategy;
    }

    /**
     * @see com.baidu.beidou.navi.client.NaviRpcClient#transport(java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Object transport(Method method, Object[] args) throws Throwable {
        if (LOG.isDebugEnabled()) {
            LOG.debug(getInfo());
        }
        return loadBalanceStrategy.transport(clientList, method, args);
    }

    /**
     * @see com.baidu.beidou.navi.client.NaviRpcClient#transport(java.lang.String, java.lang.Object[])
     */
    @Override
    public <T> T transport(String methodName, Object[] args) throws Throwable {
        throw new RpcException("Operation not supported");
    }

    /**
     * @see com.baidu.beidou.navi.client.NaviRpcClient#transport(java.lang.reflect.Method, java.lang.Object[],
     *      com.baidu.beidou.navi.client.attachment.Attachment)
     */
    @Override
    public Object transport(Method method, Object[] args, Attachment attachment) throws Throwable {
        if (LOG.isDebugEnabled()) {
            LOG.debug(getInfo());
        }
        return loadBalanceStrategy.transport(clientList, method, args, attachment);
    }

    /**
     * @see com.baidu.beidou.navi.client.NaviRpcClient#transport(java.lang.String, java.lang.Object[],
     *      com.baidu.beidou.navi.client.attachment.Attachment)
     */
    @Override
    public <T> T transport(String methodName, Object[] args, Attachment attachment)
            throws Throwable {
        throw new RpcException("Operation not supported");
    }

    /**
     * @see com.baidu.beidou.navi.client.NaviRpcClient#getInfo()
     */
    @Override
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("HA rpc clients=> ");
        if (CollectionUtil.isNotEmpty(clientList)) {
            for (NaviRpcClient client : clientList) {
                sb.append(client.getInfo());
            }
        } else {
            sb.append("empty");
        }
        return sb.toString();
    }

}

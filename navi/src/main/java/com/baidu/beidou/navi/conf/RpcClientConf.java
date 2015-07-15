package com.baidu.beidou.navi.conf;

import com.baidu.beidou.navi.client.selector.NaviFailStrategy;

/**
 * ClassName: RpcClientConf <br/>
 * Function: 客户端配置
 * 
 * @author Zhang Xu
 */
public class RpcClientConf extends RpcBaseConf {

    /**
     * 订阅的服务端地址
     */
    public static String[] ZK_WATCH_NAMESPACE_PATHS = {};

    /**
     * 调用连接建立超时时间
     */
    public static int RPC_CONNECTION_TIMEOUT = 3000;

    /**
     * 调用读超时时间
     */
    public static int RPC_READ_TIMEOUT = 90000;

    /**
     * 启用{@link NaviFailStrategy#FAILOVER}时的失败重试次数
     */
    public static int RPC_RETRY_TIMES = 2;

    public String[] getZK_WATCH_NAMESPACE_PATHS() {
        return ZK_WATCH_NAMESPACE_PATHS;
    }

    public void setZK_WATCH_NAMESPACE_PATHS(String[] zK_WATCH_NAMESPACE_PATHS) {
        ZK_WATCH_NAMESPACE_PATHS = zK_WATCH_NAMESPACE_PATHS;
    }

    public int getRPC_CONNECTION_TIMEOUT() {
        return RPC_CONNECTION_TIMEOUT;
    }

    public void setRPC_CONNECTION_TIMEOUT(int rPC_CONNECTION_TIMEOUT) {
        RPC_CONNECTION_TIMEOUT = rPC_CONNECTION_TIMEOUT;
    }

    public int getRPC_READ_TIMEOUT() {
        return RPC_READ_TIMEOUT;
    }

    public void setRPC_READ_TIMEOUT(int rPC_READ_TIMEOUT) {
        RPC_READ_TIMEOUT = rPC_READ_TIMEOUT;
    }

    public int getRPC_RETRY_TIMES() {
        return RPC_RETRY_TIMES;
    }

    public void setRPC_RETRY_TIMES(int rPC_RETRY_TIMES) {
        RPC_RETRY_TIMES = rPC_RETRY_TIMES;
    }

}

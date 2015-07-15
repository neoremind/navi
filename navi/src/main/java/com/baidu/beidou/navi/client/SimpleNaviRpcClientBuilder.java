package com.baidu.beidou.navi.client;

import com.baidu.beidou.navi.client.constant.NaviRpcClientConstant;
import com.baidu.beidou.navi.constant.NaviCommonConstant;
import com.baidu.beidou.navi.protocol.SerializeHandlerFactory;

/**
 * ClassName: SimpleNaviRpcClientBuilder <br/>
 * Function: 简单客户端client构造器
 * 
 * @author zhangxu04
 */
public class SimpleNaviRpcClientBuilder {

    /**
     * ip port
     */
    private String ipPort = "127.0.0.1:8080";

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 使用序列化协议
     */
    private String protocol = NaviCommonConstant.DEFAULT_PROTOCAL_CONTENT_TYPE;

    /**
     * 连接超时时间
     */
    private int connectTimeout = NaviRpcClientConstant.DEFAULT_CLIENT_CONN_TIMEOUT;

    /**
     * 读超时时间
     */
    private int readTimeout = NaviRpcClientConstant.DEFAULT_CLIENT_READ_TIMEOUT;

    /**
     * 构造客户端
     * 
     * @return
     */
    public SimpleNaviRpcClient build() {
        SimpleNaviRpcClient client = new SimpleNaviRpcClient(NaviCommonConstant.TRANSPORT_PROTOCOL
                + ipPort + NaviCommonConstant.TRANSPORT_URL_BASE_PATH + serviceName,
                connectTimeout, readTimeout, SerializeHandlerFactory.getHandlerByProtocal(protocol));
        return client;
    }

    public SimpleNaviRpcClientBuilder setIpPort(String ipPort) {
        this.ipPort = ipPort;
        return this;
    }

    public SimpleNaviRpcClientBuilder setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public SimpleNaviRpcClientBuilder setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public SimpleNaviRpcClientBuilder setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public SimpleNaviRpcClientBuilder setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

}

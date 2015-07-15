package com.baidu.beidou.navi.client.constant;

/**
 * ClassName: NaviRpcClientConstant <br/>
 * Function: 客户端配置常量
 * 
 * @author Zhang Xu
 */
public class NaviRpcClientConstant {

    public static final String ENDODING = "UTF-8";

    /**
     * 默认客户端连接超时时间，单位毫秒
     */
    public static final int DEFAULT_CLIENT_CONN_TIMEOUT = 2000;

    /**
     * 默认客户端调用读超时时间，单位毫秒
     */
    public static final int DEFAULT_CLIENT_READ_TIMEOUT = 90000;

    /**
     * 是否启用HTTP Persistent Connections复用tcp连接
     */
    public static final String DEFAULT_HTTP_KEEPALIVE = "true";

    /**
     * 用HTTP Persistent Connections复用tcp连接最大数量
     */
    public static final String DEFAULT_MAX_CONNECTIONS = "8";

}

package com.baidu.beidou.navi.constant;

import com.baidu.beidou.navi.protocol.NaviProtocol;

/**
 * ClassName: NaviCommonConstant <br/>
 * Function: 一些公共的常量定义
 * 
 * @author Zhang Xu
 */
public class NaviCommonConstant {

    /**
     * http前缀
     */
    public static final String TRANSPORT_PROTOCOL = "http://";

    /**
     * 默认服务的url前缀
     */
    public static final String TRANSPORT_URL_BASE_PATH = "/service_api/";

    /**
     * zookeeper页面的path
     */
    public static final String ZOOKEEPER_URL_PATH = "zookeeper";

    /**
     * zookeeper所有注册订阅node的根路径前缀
     */
    public static final String ZOOKEEPER_BASE_PATH = "/navi_rpc";

    /**
     * zookeeper path的分隔符
     */
    public static final String ZK_PATH_SEPARATOR = "/";

    /**
     * 默认序列化方式
     */
    public static final String DEFAULT_PROTOCAL_CONTENT_TYPE = NaviProtocol.PROTOSTUFF.getName();

    /**
     * System properties of server port for JPaaS1.0. Usually defined in shell.
     */
    public static final String SYSTEM_PROPERTY_SERVER_PORT_FROM_ENV_4JPAAS1 = "VCAP_APP_PORT";

    /**
     * System properties of server port for JPaaS1.0. Get from -D property
     */
    public static final String SYSTEM_PROPERTY_SERVER_PORT_FROM_SLASH_D_4JPAAS1 = "port.http.nonssl";

    /**
     * System properties of server host for JPaaS2.0. Usually defined in shell.
     */
    public static final String SYSTEM_PROPERTY_SERVER_HOST_FROM_ENV_4JPAAS2 = "JPAAS_HOST";

    /**
     * System properties of server port for JPaaS2.0. Usually defined in shell.
     */
    public static final String SYSTEM_PROPERTY_SERVER_PORT_FROM_ENV_4JPAAS2 = "JPAAS_HTTP_PORT";

}

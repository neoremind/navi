package com.baidu.beidou.navi.util;

import com.baidu.beidou.navi.conf.RpcServerConf;
import com.baidu.beidou.navi.constant.NaviCommonConstant;

/**
 * ClassName: ZkRegisterInfoUtil <br/>
 * Function: 注册到zookeeper的IP/Host/端口信息工具类
 * 
 * @author Zhang Xu
 */
public class ZkRegisterInfoUtil {

    /**
     * 获取本地ip
     * 
     * @return 本地ip
     */
    public static String getLocalHostIp() {
        String localIp = NetUtils.getLocalHostIP();
        return SystemPropertiesUtils.getSystemProperty(
                NaviCommonConstant.SYSTEM_PROPERTY_SERVER_HOST_FROM_ENV_4JPAAS2, localIp);
    }

    /**
     * 获取本地hostname
     * 
     * @return 本地host
     */
    public static String getLocalHostName() {
        String localHostName = NetUtils.getHostName();
        return SystemPropertiesUtils.getSystemProperty(
                NaviCommonConstant.SYSTEM_PROPERTY_SERVER_HOST_FROM_ENV_4JPAAS2, localHostName);
    }

    /**
     * 获取服务端口
     * 
     * @return 本地ipport
     */
    public static String getLocalHostPort() {
        String defaultPort = SystemPropertiesUtils.getSystemProperty(
                NaviCommonConstant.SYSTEM_PROPERTY_SERVER_PORT_FROM_SLASH_D_4JPAAS1,
                NaviCommonConstant.SYSTEM_PROPERTY_SERVER_PORT_FROM_ENV_4JPAAS1, ""
                        + RpcServerConf.SERVER_PORT);
        return SystemPropertiesUtils.getSystemProperty(
                NaviCommonConstant.SYSTEM_PROPERTY_SERVER_PORT_FROM_ENV_4JPAAS2, defaultPort);
    }

}

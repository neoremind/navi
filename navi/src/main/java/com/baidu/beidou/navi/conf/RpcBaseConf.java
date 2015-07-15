package com.baidu.beidou.navi.conf;

/**
 * ClassName: RpcBaseConf <br/>
 * Function: Navi RPC服务端和客户端共有的配置
 * <p>
 * 属性配置为静态的方便直接使用，暂时不作为bean传入客户端。
 * 
 * @author Zhang Xu
 */
public class RpcBaseConf {

    /**
     * 是否启用zookeeper服务注册
     */
    public static boolean ENABLE_ZK_REGISTRY = false;

    /**
     * zookeeper服务端地址列表，按照逗号分隔
     */
    public static String ZK_SERVER_LIST = "";

    /**
     * zookeeper digest auth认证的密码，ACL粒度为path
     */
    public static String ZK_DIGEST_AUTH = "";

    /**
     * zookeeper默认的session超时时间
     */
    public static int ZK_DEFAULT_SESSION_TIMEOUT_MILLS = 30000;

    /**
     * zookeeper重连期间，countdown latch的wait时间
     */
    public static int ZK_CONNECTION_TIMEOUT_MILLS = 30000;

    public boolean isENABLE_ZK_REGISTRY() {
        return ENABLE_ZK_REGISTRY;
    }

    public void setENABLE_ZK_REGISTRY(boolean eNABLE_ZK_REGISTRY) {
        ENABLE_ZK_REGISTRY = eNABLE_ZK_REGISTRY;
    }

    public String getZK_SERVER_LIST() {
        return ZK_SERVER_LIST;
    }

    public void setZK_SERVER_LIST(String zK_SERVER_LIST) {
        ZK_SERVER_LIST = zK_SERVER_LIST;
    }

    public int getZK_DEFAULT_SESSION_TIMEOUT_MILLS() {
        return ZK_DEFAULT_SESSION_TIMEOUT_MILLS;
    }

    public void setZK_DEFAULT_SESSION_TIMEOUT_MILLS(int zK_DEFAULT_SESSION_TIMEOUT_MILLS) {
        ZK_DEFAULT_SESSION_TIMEOUT_MILLS = zK_DEFAULT_SESSION_TIMEOUT_MILLS;
    }

    public int getZK_CONNECTION_TIMEOUT_MILLS() {
        return ZK_CONNECTION_TIMEOUT_MILLS;
    }

    public void setZK_CONNECTION_TIMEOUT_MILLS(int zK_CONNECTION_TIMEOUT_MILLS) {
        ZK_CONNECTION_TIMEOUT_MILLS = zK_CONNECTION_TIMEOUT_MILLS;
    }

    public String getZK_DIGEST_AUTH() {
        return ZK_DIGEST_AUTH;
    }

    public void setZK_DIGEST_AUTH(String zK_DIGEST_AUTH) {
        ZK_DIGEST_AUTH = zK_DIGEST_AUTH;
    }

}

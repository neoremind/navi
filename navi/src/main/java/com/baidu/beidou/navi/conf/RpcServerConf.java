package com.baidu.beidou.navi.conf;

/**
 * ClassName: RpcServerConf <br/>
 * Function: 服务端配置
 * 
 * @author Zhang Xu
 */
public class RpcServerConf extends RpcBaseConf {

    /**
     * 服务注册到zookeeper的path
     */
    public static String ZK_REGISTRY_NAMESPACE = "";

    /**
     * 如果服务没有在tomcat或者环境变量中定义端口，那么这个端口就是注册到zookeeper上的服务端口
     */
    public static int SERVER_PORT = 8080;

    public String getZK_REGISTRY_NAMESPACE() {
        return ZK_REGISTRY_NAMESPACE;
    }

    public void setZK_REGISTRY_NAMESPACE(String zK_REGISTRY_NAMESPACE) {
        ZK_REGISTRY_NAMESPACE = zK_REGISTRY_NAMESPACE;
    }

    public int getSERVER_PORT() {
        return SERVER_PORT;
    }

    public void setSERVER_PORT(int sERVER_PORT) {
        SERVER_PORT = sERVER_PORT;
    }

}

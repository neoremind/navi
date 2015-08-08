package com.baidu.beidou.navimgr.zoo;

/**
 * ClassName: ZooConf <br/>
 * Function: zookeeper配置
 * 
 * @author Zhang Xu
 */
public class ZooConf {

    /**
     * 线上地址
     */
    private String onlineServerList;

    /**
     * 线上digest验证密码
     */
    private String onlineDigestAuth;

    /**
     * 线下地址
     */
    private String offlineServerList;

    /**
     * 线下digest验证密码
     */
    private String offlineDigestAuth;

    /**
     * 节点根前缀
     */
    private String nodePathPrefix;

    /**
     * session超时
     */
    private int sessionTimeout;

    /**
     * 连接超时
     */
    private int connTimeout;

    public String getOnlineServerList() {
        return onlineServerList;
    }

    public void setOnlineServerList(String onlineServerList) {
        this.onlineServerList = onlineServerList;
    }

    public String getOnlineDigestAuth() {
        return onlineDigestAuth;
    }

    public void setOnlineDigestAuth(String onlineDigestAuth) {
        this.onlineDigestAuth = onlineDigestAuth;
    }

    public String getOfflineServerList() {
        return offlineServerList;
    }

    public void setOfflineServerList(String offlineServerList) {
        this.offlineServerList = offlineServerList;
    }

    public String getOfflineDigestAuth() {
        return offlineDigestAuth;
    }

    public void setOfflineDigestAuth(String offlineDigestAuth) {
        this.offlineDigestAuth = offlineDigestAuth;
    }

    public String getNodePathPrefix() {
        return nodePathPrefix;
    }

    public void setNodePathPrefix(String nodePathPrefix) {
        this.nodePathPrefix = nodePathPrefix;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

}

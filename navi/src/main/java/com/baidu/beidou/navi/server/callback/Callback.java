package com.baidu.beidou.navi.server.callback;

/**
 * ClassName: Callback <br/>
 * Function: 在服务端的调用链中扮演响应holder角色的回调
 * 
 * @author Zhang Xu
 */
public interface Callback<T> {

    /**
     * 处理返回报文
     * 
     * @param result
     */
    void handleResult(T result);

}
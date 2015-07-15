package com.baidu.beidou.navi.server.processor;

import com.baidu.beidou.navi.server.callback.Callback;
import com.baidu.beidou.navi.server.vo.NaviRpcRequest;
import com.baidu.beidou.navi.server.vo.NaviRpcResponse;

/**
 * ClassName: NaviRpcProcessor <br/>
 * Function: Navi Rpc处理单元接口
 * 
 * @author Zhang Xu
 */
public interface NaviRpcProcessor {

    /**
     * 处理请求并且响应结果
     * 
     * @param request
     *            请求
     * @param callback
     *            结果Callback
     */
    public void service(NaviRpcRequest request, Callback<NaviRpcResponse> callback);

}

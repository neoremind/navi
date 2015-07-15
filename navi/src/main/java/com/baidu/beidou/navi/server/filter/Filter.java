package com.baidu.beidou.navi.server.filter;

import com.baidu.beidou.navi.exception.rpc.RpcException;
import com.baidu.beidou.navi.server.callback.Callback;
import com.baidu.beidou.navi.server.processor.NaviRpcProcessor;
import com.baidu.beidou.navi.server.vo.NaviRpcRequest;
import com.baidu.beidou.navi.server.vo.NaviRpcResponse;

/**
 * ClassName: Filter <br/>
 * Function: 请求调用过滤器，可以看做同<tt>pipeline</tt>或者<tt>chain</tt>模式
 * 
 * @author Zhang Xu
 */
public interface Filter {

    /**
     * 在调用链中处理。<br/>
     * 一些业务逻辑可以在下一个调用filter前执行，使用方式如下：
     * 
     * <pre>
     * // Do biz logic
     * processor.service(request, callback);
     * </pre>
     * 
     * @param processor
     * @param request
     * @param callback
     * @throws RpcException
     */
    void doChain(NaviRpcProcessor processor, NaviRpcRequest request,
            Callback<NaviRpcResponse> callback) throws RpcException;

    /**
     * When callback result is handled, then this will be performed in chain filter
     * <p>
     * Note that there is an <code>callback</code> argument which will be wrapped by
     * {@link com.baidu.beidou.navi.server.filter.FilterCallback} defined in the above method
     * <p>
     * <code>
     * void doChain(NaviRpcProcessor processor, NaviRpcRequest request, Callback<NaviRpcResponse> callback)
     * 		throws RpcException;
     * </code>
     * <p>
     * when callback is invoked in {@link com.baidu.beidou.navi.server.processor.CoreNaviRpcProcessor}
     * <p>
     * the cascade invocation will be performed right after that, so filter will be notified of the finish of the result
     * being handled properly, and do whatever what it wants to update the result.
     * 
     * @param result
     *            the result returned in the callback.
     */
    // TODO
    void doOnCallbackHandleResultTrigger(NaviRpcResponse result);

}

package com.baidu.beidou.navi.server.filter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navi.exception.rpc.RpcException;
import com.baidu.beidou.navi.server.callback.Callback;
import com.baidu.beidou.navi.server.context.LocalContext;
import com.baidu.beidou.navi.server.filter.Filter;
import com.baidu.beidou.navi.server.processor.NaviRpcProcessor;
import com.baidu.beidou.navi.server.vo.NaviRpcRequest;
import com.baidu.beidou.navi.server.vo.NaviRpcResponse;
import com.baidu.beidou.navi.util.AccessLogSupport;
import com.baidu.beidou.navi.util.vo.AccessLog;

/**
 * ClassName: AccessLogFilter <br/>
 * Function: 访问日志filter
 * 
 * @author Zhang Xu
 */
public class AccessLogFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(AccessLogFilter.class);

    /**
     * 异步记录日志辅助类
     */
    protected AccessLogSupport accessLogSupport = new AccessLogSupport();

    /**
     * @see com.baidu.beidou.navi.server.filter.Filter#doChain(com.baidu.beidou.navi.server.processor.NaviRpcProcessor,
     *      com.baidu.beidou.navi.server.vo.NaviRpcRequest, com.baidu.beidou.navi.server.callback.Callback)
     */
    @Override
    public void doChain(NaviRpcProcessor processor, NaviRpcRequest request,
            Callback<NaviRpcResponse> callback) throws RpcException {
        AccessLog accessLogInfo = new AccessLog();
        accessLogInfo.setStartTime(LocalContext.getContext().getAccessStartTime());
        accessLogInfo.setFromIp(LocalContext.getContext().getFromIp());
        accessLogInfo.setServiceIntfName(LocalContext.getContext().getServiceName());
        accessLogInfo.setProtocol(LocalContext.getContext().getProtocol());
        accessLogInfo.setRequestByteSize(LocalContext.getContext().getReqByteSize());

        processor.service(request, callback);

        try {
            accessLogInfo.setRequest(request.getRequestDTO());
            accessLogInfo.setResponse(LocalContext.getContext().getResponse());
            accessLogInfo.setEndTime(System.currentTimeMillis());
            accessLogSupport.log(accessLogInfo);
        } catch (Exception e) {
            LOG.error("Error occurred when logging, " + e.getMessage(), e);
        }
    }

    /**
     * @see com.baidu.beidou.navi.server.filter.Filter#doOnCallbackHandleResultTrigger(com.baidu.beidou.navi.server.vo.NaviRpcResponse)
     */
    @Override
    public void doOnCallbackHandleResultTrigger(NaviRpcResponse result) {
        LocalContext.getContext().setResponse(result.getResponseDTO());
    }

}

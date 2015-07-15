package com.baidu.beidou.navi.server.filter.impl;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navi.exception.rpc.RpcException;
import com.baidu.beidou.navi.protocol.SerializeHandler;
import com.baidu.beidou.navi.protocol.SerializeHandlerFactory;
import com.baidu.beidou.navi.server.callback.Callback;
import com.baidu.beidou.navi.server.context.LocalContext;
import com.baidu.beidou.navi.server.filter.Filter;
import com.baidu.beidou.navi.server.processor.NaviRpcProcessor;
import com.baidu.beidou.navi.server.vo.NaviRpcRequest;
import com.baidu.beidou.navi.server.vo.NaviRpcResponse;
import com.baidu.beidou.navi.server.vo.RequestDTO;
import com.baidu.beidou.navi.server.vo.ResponseDTO;
import com.baidu.beidou.navi.util.Preconditions;

/**
 * ClassName: SerializeFilter <br/>
 * Function: 序列化反序列化filter
 * 
 * @author Zhang Xu
 */
public class SerializeFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(SerializeFilter.class);

    /**
     * @see com.baidu.beidou.navi.server.filter.Filter#doChain(com.baidu.beidou.navi.server.processor.NaviRpcProcessor,
     *      com.baidu.beidou.navi.server.vo.NaviRpcRequest, com.baidu.beidou.navi.server.callback.Callback)
     */
    @Override
    public void doChain(NaviRpcProcessor processor, NaviRpcRequest request,
            Callback<NaviRpcResponse> callback) throws RpcException {
        /** get serialize handler */
        String protocol = LocalContext.getContext().getProtocol();
        Preconditions.checkNotNull(protocol, "Please ensure protocol is set to context!");
        SerializeHandler handler = SerializeHandlerFactory.getHandlerByProtocal(protocol);

        /** serialize byte array to DTO */
        RequestDTO requestDTO = handler.deserialize(request.getRequest(), RequestDTO.class, request
                .getExporter().getServiceInterface(), null);
        Preconditions.checkDeserializeNotNull(requestDTO);

        /** inject DTO to request */
        request.setRequestDTO(requestDTO);

        /** log before bean invocation */
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deserialize " + request.getRequest().length + " done");
            LOG.debug(String.format("Invoke start... traceId=%d, service=%s, request=[%s(%s)]",
                    requestDTO.getTraceId(), request.getExporter().getName(),
                    requestDTO.getMethod(), Arrays.toString(requestDTO.getParameters())));
        }

        processor.service(request, callback);
    }

    /**
     * @see com.baidu.beidou.navi.server.filter.Filter#doOnCallbackHandleResultTrigger(com.baidu.beidou.navi.server.vo.NaviRpcResponse)
     */
    @Override
    public void doOnCallbackHandleResultTrigger(NaviRpcResponse result) {
        /** get serialize handler */
        String protocol = LocalContext.getContext().getProtocol();
        Preconditions.checkNotNull(protocol, "Please ensure protocol is set to context!");
        SerializeHandler handler = SerializeHandlerFactory.getHandlerByProtocal(protocol);

        /** serialize DTO to byte array and inject to response */
        result.setResponse(handler.serialize(result.getResponseDTO(), ResponseDTO.class));
    }

}

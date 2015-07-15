package com.baidu.beidou.navi.server.context;

import com.baidu.beidou.navi.server.vo.ResponseDTO;
import com.baidu.beidou.navi.util.ThreadHolder;

/**
 * ClassName: LocalContext <br/>
 * Function: 服务端内部的上下文
 * 
 * @author Zhang Xu
 */
public class LocalContext {

    private static final ThreadLocal<LocalContext> LOCAL = new ThreadLocal<LocalContext>() {
        @Override
        protected LocalContext initialValue() {
            return new LocalContext();
        }
    };

    public static LocalContext getContext() {
        return LOCAL.get();
    }

    public static void removeContext() {
        LOCAL.remove();
        ThreadHolder.clean();
    }

    private static final String ACCESS_START_TIME = "startTime";
    private static final String PROTOCOL = "protocol";
    private static final String REQ_BYTE_SIZE = "reqByteSize";
    private static final String SERVICE_NAME = "serviceName";
    private static final String FROM_IP = "fromIp";
    private static final String RESPONSE = "response";

    public LocalContext setStartTime() {
        ThreadHolder.putContext(ACCESS_START_TIME, System.currentTimeMillis());
        return this;
    }

    public LocalContext setProtocol(String protocol) {
        ThreadHolder.putContext(PROTOCOL, protocol);
        return this;
    }

    public LocalContext setFromIp(String fromIp) {
        ThreadHolder.putContext(FROM_IP, fromIp);
        return this;
    }

    public LocalContext setServiceName(String serviceName) {
        ThreadHolder.putContext(SERVICE_NAME, serviceName);
        return this;
    }

    public LocalContext setReqByteSize(int reqByteSize) {
        ThreadHolder.putContext(REQ_BYTE_SIZE, reqByteSize);
        return this;
    }

    public LocalContext setResponse(ResponseDTO response) {
        ThreadHolder.putContext(RESPONSE, response);
        return this;
    }

    public String getProtocol() {
        return ThreadHolder.getContext(PROTOCOL);
    }

    public Long getAccessStartTime() {
        Long result = ThreadHolder.getContext(ACCESS_START_TIME);
        if (result == null) {
            return new Long(0L);
        }
        return result;
    }

    public String getServiceName() {
        return ThreadHolder.getContext(SERVICE_NAME);
    }

    public String getFromIp() {
        return ThreadHolder.getContext(FROM_IP);
    }

    public Integer getReqByteSize() {
        Integer result = ThreadHolder.getContext(REQ_BYTE_SIZE);
        if (result == null) {
            return new Integer(-1);
        }
        return result;
    }

    public ResponseDTO getResponse() {
        return ThreadHolder.getContext(RESPONSE);
    }

}
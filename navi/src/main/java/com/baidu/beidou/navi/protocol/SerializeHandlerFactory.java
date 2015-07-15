package com.baidu.beidou.navi.protocol;

import java.util.HashMap;
import java.util.Map;

import com.baidu.beidou.navi.exception.InvalidProtocolException;

/**
 * ClassName: SerializeHandlerFactory <br/>
 * Function: 序列化、反序列化工厂
 * 
 * @author Zhang Xu
 */
public class SerializeHandlerFactory {

    /**
     * handler缓存map
     */
    private static final Map<String, SerializeHandler> HANDLER_MAP = new HashMap<String, SerializeHandler>();

    /**
     * 初始化
     */
    static {
        HANDLER_MAP.put(NaviProtocol.PROTOSTUFF.getName(), new ProtostuffSerializeHandler());
        HANDLER_MAP.put(NaviProtocol.PROTOBUF.getName(), new ProtobufSerializeHandler());
        HANDLER_MAP.put(NaviProtocol.JSON.getName(), new JsonSerializeHandler());
    }

    /**
     * 根据http请求中的content-type尝试寻找序列化handler
     * 
     * @param protocol
     * @return
     * @throws InvalidProtocolException
     */
    public static SerializeHandler getHandlerByProtocal(String protocol)
            throws InvalidProtocolException {
        SerializeHandler handler = HANDLER_MAP.get(protocol);
        if (handler != null) {
            return handler;
        }
        throw new InvalidProtocolException("rpc protocol not supported for " + protocol);
    }

}

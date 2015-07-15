package com.baidu.beidou.navi.protocol;

import java.lang.reflect.Type;

import com.baidu.beidou.navi.exception.rpc.CodecException;

/**
 * ClassName: SerializeHandler <br/>
 * Function: 序列化反序列化handler
 * 
 * @author Zhang Xu
 */
public interface SerializeHandler {

    /**
     * 获取序列化协议类型
     * 
     * @return
     */
    NaviProtocol getProtocol();

    /**
     * 反序列化解码成RequestDTO
     * 
     * @param req
     * @param clazz
     * @param interf
     * @param genericType
     * @return T
     * @throws CodecException
     */
    <T> T deserialize(byte[] req, Class<T> clazz, Class<?> interf, Type genericType)
            throws CodecException;

    /**
     * 编码ResponseDTO为字节码
     * 
     * @param res
     * @param clazz
     * @return 字节码
     * @throws CodecException
     */
    <T> byte[] serialize(T res, Class<T> clazz) throws CodecException;

}

package com.baidu.beidou.navi.codec;

/**
 * ClassName: Codec <br/>
 * Function: 编码接口，用作报文序列化以及反序列化
 * 
 * @author Zhang Xu
 */
public interface Codec {

    /**
     * 编码序列化
     * 
     * @param clazz
     * @param bytes
     * @return
     * @throws Exception
     */
    <T> T decode(Class<T> clazz, byte[] bytes) throws Exception;

    /**
     * 解码反序列化
     * 
     * @param clazz
     * @param object
     * @return
     * @throws Exception
     */
    <T> byte[] encode(Class<T> clazz, T object) throws Exception;

}

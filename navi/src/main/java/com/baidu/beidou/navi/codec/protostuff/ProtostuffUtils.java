package com.baidu.beidou.navi.codec.protostuff;

import java.io.IOException;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;

/**
 * ClassName: ProtostuffUtils <br/>
 * Function: Protostuff编码工具类
 * 
 * @author Zhang Xu
 */
public class ProtostuffUtils {

    /**
     * 将<tt>message</tt>根据<tt>schema</tt>转换为字节码
     * 
     * @param message
     * @param schema
     * @param buffer
     * @return
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema, LinkedBuffer buffer) {
        return ProtostuffIOUtil.toByteArray(message, schema, buffer);
    }

    /**
     * 将字节码根据<tt>schema</tt>转换为<tt>message</tt>
     * 
     * @param in
     * @param message
     * @param schema
     * @throws IOException
     */
    public static <T> void mergeFrom(byte[] in, T message, Schema<T> schema) throws IOException {
        ProtostuffIOUtil.mergeFrom(in, message, schema);
    }

}

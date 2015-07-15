package com.baidu.beidou.navi.codec.protobuf;

import java.io.IOException;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;

/**
 * ClassName: ProtobufUtils <br/>
 * Function: 运行时Protobuf编码工具类
 * 
 * @author Zhang Xu
 */
public class ProtobufUtils {

    /**
     * 将<tt>message</tt>根据<tt>schema</tt>转换为字节码
     * 
     * @param message
     * @param schema
     * @param buffer
     * @return
     */
    public static <T> byte[] toByteArray(T message, Schema<T> schema, LinkedBuffer buffer) {
        return ProtobufIOUtil.toByteArray(message, schema, buffer);
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
        ProtobufIOUtil.mergeFrom(in, message, schema);
    }

}

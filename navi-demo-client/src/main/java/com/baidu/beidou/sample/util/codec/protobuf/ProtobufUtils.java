package com.baidu.beidou.sample.util.codec.protobuf;

import java.io.IOException;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;

public class ProtobufUtils {

    public static <T> byte[] toByteArray(T message, Schema<T> schema, LinkedBuffer buffer) {
        return ProtobufIOUtil.toByteArray(message, schema, buffer);
    }

    public static <T> void mergeFrom(byte[] in, T message, Schema<T> schema) throws IOException {
        ProtobufIOUtil.mergeFrom(in, message, schema);
    }
}

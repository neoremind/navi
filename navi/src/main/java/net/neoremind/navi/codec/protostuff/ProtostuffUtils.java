package net.neoremind.navi.codec.protostuff;

import java.io.IOException;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;

public class ProtostuffUtils {

    public static <T> byte[] toByteArray(T message, Schema<T> schema, LinkedBuffer buffer) {
        return ProtostuffIOUtil.toByteArray(message, schema, buffer);
    }

    public static <T> void mergeFrom(byte[] in, T message, Schema<T> schema) throws IOException {
        ProtostuffIOUtil.mergeFrom(in, message, schema);
    }
}

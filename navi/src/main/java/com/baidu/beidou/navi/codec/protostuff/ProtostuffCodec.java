package com.baidu.beidou.navi.codec.protostuff;

import com.baidu.beidou.navi.codec.Codec;
import com.baidu.beidou.navi.util.ReflectionUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * ClassName: ProtostuffCodec <br/>
 * Function: Protostuff编码器
 * 
 * @author Zhang Xu
 */
public class ProtostuffCodec implements Codec {

    /**
     * 初始化一些参数
     */
    static {
        System.setProperty("protostuff.runtime.collection_schema_on_repeated_fields", "true");
        System.setProperty("protostuff.runtime.morph_collection_interfaces", "true");
        System.setProperty("protostuff.runtime.morph_map_interfaces", "true");
    }

    /**
     * buffer
     */
    private ThreadLocal<LinkedBuffer> linkedBuffer = new ThreadLocal<LinkedBuffer>() {
        @Override
        protected LinkedBuffer initialValue() {
            return LinkedBuffer.allocate(500);
        }
    };

    /**
     * @see com.baidu.beidou.navi.codec.Codec#decode(java.lang.Class, byte[])
     */
    @Override
    public <T> T decode(Class<T> clazz, byte[] bytes) throws Exception {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T content = ReflectionUtil.newInstance(clazz);
        ProtostuffUtils.mergeFrom(bytes, content, schema);
        return content;
    }

    /**
     * @see com.baidu.beidou.navi.codec.Codec#encode(java.lang.Class, java.lang.Object)
     */
    @Override
    public <T> byte[] encode(Class<T> clazz, T object) throws Exception {
        try {
            Schema<T> schema = RuntimeSchema.getSchema(clazz);
            byte[] protostuff = ProtostuffUtils.toByteArray(object, schema, linkedBuffer.get());
            return protostuff;
        } finally {
            linkedBuffer.get().clear();
        }
    }

}

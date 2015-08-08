package com.baidu.beidou.sample.util.codec.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import com.baidu.beidou.navi.codec.Codec;

public class ProtostuffCodec implements Codec {
	
    public static final String NAME = "protostuff";
    
    static {
        System.setProperty("protostuff.runtime.collection_schema_on_repeated_fields", "true");
        System.setProperty("protostuff.runtime.morph_collection_interfaces", "true");
        System.setProperty("protostuff.runtime.morph_map_interfaces", "true");
    }
    
    private ThreadLocal<LinkedBuffer> linkedBuffer = new ThreadLocal<LinkedBuffer>() {
        @Override
        protected LinkedBuffer initialValue() {
            return LinkedBuffer.allocate(500);
        }
    };

    public <T> T decode(Class<T> clazz, byte[] bytes) throws Exception {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T content = clazz.newInstance();
        ProtostuffUtils.mergeFrom(bytes, content, schema);
        return content;
    }

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

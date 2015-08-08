package com.baidu.beidou.sample.util.codec.json;

import com.baidu.beidou.navi.codec.Codec;

public class JsonCodec implements Codec {
	
    private JsonMapper mapper = JsonMapper.buildNormalMapper();

    public <T> T decode(Class<T> clazz, byte[] bytes) throws Exception {
        return mapper.fromJsonBytes(bytes, clazz);
    }

    public <T> byte[] encode(Class<T> clazz, T object) throws Exception {
        return mapper.toJsonBytes(object);
    }

}

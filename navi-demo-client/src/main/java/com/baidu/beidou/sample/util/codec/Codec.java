package com.baidu.beidou.sample.util.codec;

public interface Codec {

    public <T> T decode(Class<T> clazz, byte[] bytes) throws Exception;

    public <T> byte[] encode(Class<T> clazz, T object) throws Exception;

}

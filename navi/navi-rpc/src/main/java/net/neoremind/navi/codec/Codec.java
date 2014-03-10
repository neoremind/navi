package net.neoremind.navi.codec;

public interface Codec {

    public <T> T decode(Class<T> clazz, byte[] bytes) throws Exception;

    public <T> byte[] encode(Class<T> clazz, T object) throws Exception;

}

package com.baidu.beidou.navi.codec.json;

import java.lang.reflect.Type;

import org.codehaus.jackson.JsonNode;

import com.baidu.beidou.navi.codec.Codec;

/**
 * ClassName: JsonCodec <br/>
 * Function: Json编码器
 * 
 * @author Zhang Xu
 */
public class JsonCodec implements Codec {

    /**
     * <tt>Jackson json</tt>的Mapper
     */
    private JsonMapper mapper = JsonMapper.buildNormalMapper();

    /**
     * @see com.baidu.beidou.navi.codec.Codec#decode(java.lang.Class, byte[])
     */
    @Override
    public <T> T decode(Class<T> clazz, byte[] bytes) throws Exception {
        return mapper.fromJsonBytes(bytes, clazz);
    }

    /**
     * @see com.baidu.beidou.navi.codec.Codec#encode(java.lang.Class, java.lang.Object)
     */
    @Override
    public <T> byte[] encode(Class<T> clazz, T object) throws Exception {
        return mapper.toJsonBytes(object);
    }

    /**
     * 解码成<code>JsonNode</code>
     * 
     * @param bytes
     * @return
     * @throws Exception
     */
    public JsonNode decode(byte[] bytes) throws Exception {
        JsonNode jsonNode = mapper.fromJson(bytes);
        return jsonNode;
    }

    /**
     * 根据<code>JsonNode</code>以及<code>Type</code>还原对象
     * 
     * @param type
     * @param jsonNode
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T decode(Type type, JsonNode jsonNode) throws Exception {
        return (T) (mapper.fromJson(jsonNode, type));
    }

}

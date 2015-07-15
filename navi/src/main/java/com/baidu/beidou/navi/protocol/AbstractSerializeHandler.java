package com.baidu.beidou.navi.protocol;

import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navi.codec.Codec;
import com.baidu.beidou.navi.exception.rpc.CodecException;
import com.baidu.beidou.navi.util.Preconditions;

/**
 * ClassName: AbstractSerializeHandler <br/>
 * Function: 序列化反序列化handler
 * 
 * @author Zhang Xu
 */
public abstract class AbstractSerializeHandler implements SerializeHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSerializeHandler.class);

    /**
     * 编解码codec
     */
    protected Codec codec;

    /**
     * 设置编解码codec
     * 
     * @return
     */
    public abstract void setCodec();

    /**
     * Creates a new instance of AbstractSerializeHandler.
     */
    public AbstractSerializeHandler() {
        setCodec();
    }

    /**
     * @see com.baidu.beidou.navi.protocol.SerializeHandler#deserialize(byte[], java.lang.Class, java.lang.Class,
     *      java.lang.reflect.Type)
     */
    @Override
    public <T> T deserialize(byte[] req, Class<T> clazz, Class<?> interf, Type genericType)
            throws CodecException {
        Preconditions.checkNotNull(codec, "Codec not init");
        try {
            return (T) codec.decode(clazz, req);
        } catch (Exception e) {
            LOG.error("Deserialize byte failed", e);
            throw new CodecException("Deserialize byte error");
        }
    }

    /**
     * @see com.baidu.beidou.navi.protocol.SerializeHandler#serialize(java.lang.Object, java.lang.Class)
     */
    @Override
    public <T> byte[] serialize(T res, Class<T> clazz) throws CodecException {
        Preconditions.checkNotNull(codec, "Codec not init");
        try {
            return codec.encode(clazz, res);
        } catch (Exception e) {
            LOG.error("Serialize object failed", e);
            throw new CodecException("Serialize object error");
        }
    }

}

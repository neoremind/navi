package com.baidu.beidou.navi.protocol;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baidu.beidou.navi.codec.json.JsonCodec;
import com.baidu.beidou.navi.exception.rpc.CodecException;
import com.baidu.beidou.navi.exception.rpc.InvalidParamException;
import com.baidu.beidou.navi.exception.rpc.MethodNotFoundException;
import com.baidu.beidou.navi.server.vo.RequestDTO;
import com.baidu.beidou.navi.server.vo.ResponseDTO;

/**
 * 
 * ClassName: JsonSerializeHandler <br/>
 * Function: json编码handler
 *
 * @author Zhang Xu
 */
public class JsonSerializeHandler extends AbstractSerializeHandler implements SerializeHandler {

    private static final Logger LOG = LoggerFactory.getLogger(JsonSerializeHandler.class);

    /**
     * @see com.baidu.beidou.navi.protocol.SerializeHandler#getProtocol()
     */
    @Override
    public NaviProtocol getProtocol() {
        return NaviProtocol.JSON;
    }

    /**
     * @see com.baidu.beidou.navi.protocol.AbstractSerializeHandler#setCodec()
     */
    @Override
    public void setCodec() {
        this.codec = new JsonCodec();
    }

    /**
     * @see com.baidu.beidou.navi.protocol.AbstractSerializeHandler#deserialize(byte[], java.lang.Class,
     *      java.lang.Class, java.lang.reflect.Type)
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(byte[] req, Class<T> clazz, Class<?> interf, Type genericType)
            throws CodecException {
        if (genericType != null && clazz.isAssignableFrom(ResponseDTO.class)) {
            try {
                JsonCodec jsonCodec = (JsonCodec) codec;
                JsonNode responseNode = jsonCodec.decode(req);
                JsonNode resultNode = jsonCodec.decode(req).get("result");
                ResponseDTO responseDTO = jsonCodec.decode(ResponseDTO.class, responseNode);
                responseDTO.setResult(jsonCodec.decode(genericType, resultNode));
                return (T) responseDTO;
            } catch (Exception e) {
                LOG.error("Deserialize byte to json failed", e);
                throw new CodecException("Deserialize byte to json error");
            }
        } else if (clazz.isAssignableFrom(RequestDTO.class)) {
            try {
                JsonCodec jsonCodec = (JsonCodec) codec;

                JsonNode requestNode = jsonCodec.decode(req);
                RequestDTO requestDTO = jsonCodec.decode(RequestDTO.class, requestNode);

                JsonNode methodNode = jsonCodec.decode(req).get("method");
                if (methodNode == null) {
                    throw new MethodNotFoundException();
                }
                String methodName = jsonCodec.decode(String.class, methodNode);

                for (Method ms : interf.getMethods()) {
                    if (ms.getName().equals(methodName)) {
                        Type[] argTypes = ms.getGenericParameterTypes();
                        JsonNode paramNode = jsonCodec.decode(req).get("parameters");
                        if (argTypes.length != 0) {
                            if (paramNode == null) {
                                throw new InvalidParamException("Invalid params");
                            }
                            if (argTypes.length != paramNode.size()) {
                                throw new InvalidParamException("Invalid params");
                            }
                            if (paramNode != null) {
                                Object[] argObj = new Object[argTypes.length];
                                Iterator<JsonNode> iter = paramNode.iterator();
                                for (int i = 0; i < argTypes.length; i++) {
                                    argObj[i] = jsonCodec.decode(argTypes[i], iter.next());
                                }
                                requestDTO.setParameters(argObj);
                            }
                        }
                        return (T) requestDTO;
                    }
                }

                throw new MethodNotFoundException("Service method not found");
            } catch (Exception e) {
                LOG.error("Deserialize byte to json failed", e);
                throw new CodecException("Deserialize byte to json error");
            }
        }
        throw new CodecException("Deserialize byte to json error");
    }
}

package com.baidu.beidou.navi.protocol;

import com.baidu.beidou.navi.codec.protobuf.ProtobufCodec;

/**
 * ClassName: ProtobufSerializeHandler <br/>
 * Function: protobuf序列化handler
 * 
 * @author Zhang Xu
 */
public class ProtobufSerializeHandler extends AbstractSerializeHandler implements SerializeHandler {

    /**
     * @see com.baidu.beidou.navi.protocol.SerializeHandler#getProtocol()
     */
    @Override
    public NaviProtocol getProtocol() {
        return NaviProtocol.PROTOBUF;
    }

    /**
     * @see com.baidu.beidou.navi.protocol.AbstractSerializeHandler#setCodec()
     */
    @Override
    public void setCodec() {
        this.codec = new ProtobufCodec();
    }

}

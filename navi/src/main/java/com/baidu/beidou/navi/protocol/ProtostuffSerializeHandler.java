package com.baidu.beidou.navi.protocol;

import com.baidu.beidou.navi.codec.protostuff.ProtostuffCodec;

/**
 * ClassName: ProtostuffSerializeHandler <br/>
 * Function: protostuff序列化handler
 * 
 * @author Zhang Xu
 */
public class ProtostuffSerializeHandler extends AbstractSerializeHandler implements
        SerializeHandler {

    /**
     * @see com.baidu.beidou.navi.protocol.SerializeHandler#getProtocol()
     */
    @Override
    public NaviProtocol getProtocol() {
        return NaviProtocol.PROTOSTUFF;
    }

    /**
     * @see com.baidu.beidou.navi.protocol.AbstractSerializeHandler#setCodec()
     */
    @Override
    public void setCodec() {
        this.codec = new ProtostuffCodec();
    }

}

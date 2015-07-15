package com.baidu.beidou.navi.codec;

import org.junit.Test;

import com.baidu.beidou.navi.codec.protobuf.ProtobufCodec;

public class ProtobufCodecTest extends BaseCodecTest {

    @Test
    public void testCodec() throws Exception {
        encodeAndDecode();
    }

    @Override
    public void initCodec() {
        codec = new ProtobufCodec();
    }

}

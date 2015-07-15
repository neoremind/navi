package com.baidu.beidou.navi.codec;

import org.junit.Test;

import com.baidu.beidou.navi.codec.protostuff.ProtostuffCodec;

public class ProtostuffCodecTest extends BaseCodecTest {

    @Test
    public void testCodec() throws Exception {
        encodeAndDecode();
    }

    @Override
    public void initCodec() {
        codec = new ProtostuffCodec();
    }

}

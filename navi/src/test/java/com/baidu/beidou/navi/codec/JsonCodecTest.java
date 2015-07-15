package com.baidu.beidou.navi.codec;

import org.junit.Test;

import com.baidu.beidou.navi.codec.json.JsonCodec;

public class JsonCodecTest extends BaseCodecTest {

    @Test
    public void testCodec() throws Exception {
        encodeAndDecode();
    }

    @Override
    public void initCodec() {
        codec = new JsonCodec();
    }

}

package com.baidu.beidou.navi.codec;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.baidu.beidou.navi.codec.protostuff.ProtostuffCodec;
import com.baidu.beidou.navi.vo.Book;
import com.baidu.beidou.navi.vo.Book2;

public class ProtostuffCodecAddNewFieldTest extends BaseCodecTest {

    @Test
    public void testCodec() throws Exception {
        initCodec();
        Book b = new Book(1, "java");
        byte[] bytes = codec.encode(Book.class, b);
        System.out.println("bytes.length=" + bytes.length);
        Book2 b2 = codec.decode(Book2.class, bytes);
        System.out.println(b2);
        assertThat(b2.getId(), is(1));
        assertThat(b2.getName(), is("java"));
    }

    @Override
    public void initCodec() {
        codec = new ProtostuffCodec();
    }

}

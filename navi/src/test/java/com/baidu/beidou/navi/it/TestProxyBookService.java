package com.baidu.beidou.navi.it;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;

import com.baidu.beidou.navi.it.service.BookService;
import com.baidu.beidou.navi.vo.Book;

public class TestProxyBookService extends BaseTest {

    @Resource
    private BookService bookServiceDirectCall;

    @Test
    public void testGet() {
        Book book = bookServiceDirectCall.get(1);
        validateBook(book);
    }

    @Test
    public void testGetOverrideMethod() {
        Book book = bookServiceDirectCall.get("天方夜谭");
        validateBook(book);
    }

    @Test
    public void testGetOverrideMethod2() {
        Book book = bookServiceDirectCall.get(1, "天方夜谭");
        validateBook(book);
    }

    private void validateBook(Book b) {
        assertThat(b.getId(), is(1));
        assertThat(b.getName(), is("天方夜谭"));
    }

}

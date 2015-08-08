package com.baidu.beidou.sample.xml;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.baidu.beidou.sample.xml.service.BookMgr;
import com.baidu.beidou.sample.xml.vo.Book;

@ContextConfiguration(locations = { "classpath:applicationContext-test-xml.xml" })
public class BookMgrTest extends AbstractJUnit4SpringContextTests {

    @Resource
    private BookMgr bookMgr;

    @Test
    public void testGetAll() {
        List<Book> result = bookMgr.getAll();
        for (Book book : result) {
            System.out.println(book);
        }
        assertThat(result.size(), is(3));
    }

    @Test
    public void testGetById() {
        try {
            Book result = bookMgr.get(999, "123");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Book result = bookMgr.get("123");
        System.out.println(result);

        result = bookMgr.get(999);
        System.out.println(result);
    }

}

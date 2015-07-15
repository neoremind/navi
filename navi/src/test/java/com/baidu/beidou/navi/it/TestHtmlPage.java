package com.baidu.beidou.navi.it;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TestHtmlPage extends BaseTest {

    @Test
    public void testIndexHtml() throws Throwable {
        String content = getContent(("http://127.0.0.1:" + Config.PORT + "/service_api"));
        assertThat(content.contains("CompanyService"), is(true));
    }

    @Test
    public void testServiceHtml() throws Throwable {
        String content = getContent(("http://127.0.0.1:" + Config.PORT + "/service_api/CompanyService"));
        assertThat(content.contains("get"), is(true));
    }

    @Test
    public void testClassHtml() throws Throwable {
        String content = getContent(("http://127.0.0.1:" + Config.PORT
                + "/service_api/get_class_defination?" + "class=com.baidu.beidou.navi.vo.Company"));
        assertThat(content.contains("name"), is(true));
    }

    @Test
    public void testZooHtml() throws Throwable {
        getContent(("http://127.0.0.1:" + Config.PORT + "/service_api/zookeeper"));
    }

}

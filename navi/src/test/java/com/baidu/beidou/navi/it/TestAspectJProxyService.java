package com.baidu.beidou.navi.it;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.annotation.Resource;

import org.junit.Test;

import com.baidu.beidou.navi.it.service.ProxyService;

public class TestAspectJProxyService extends BaseTest {

    @Resource
    private ProxyService proxyServiceDirectCall;

    @Test
    public void testGet() {
        String back = proxyServiceDirectCall.hellworld("123");
        assertThat(back, is("123"));
    }

}

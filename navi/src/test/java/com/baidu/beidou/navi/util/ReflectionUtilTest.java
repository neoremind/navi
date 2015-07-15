package com.baidu.beidou.navi.util;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.baidu.beidou.navi.server.annotation.Authority;
import com.baidu.beidou.navi.server.annotation.NaviRpcAuth;

public class ReflectionUtilTest {

    @Test
    public void testGetAllAnnotationsOfClass() {
        TestBean t = new TestBean();
        NaviRpcAuth auth = ReflectionUtil.getAnnotation(t.getClass(), NaviRpcAuth.class);
        Authority[] authorities = auth.value();
        assertThat(authorities.length, is(2));
        for (Authority authority : authorities) {
            System.out.println(authority.appId() + "|" + authority.token());
        }
    }

}

@NaviRpcAuth({ @Authority(appId = "beidou", token = "123456"),
        @Authority(appId = "ssp", token = "111111") })
class TestBean {

}

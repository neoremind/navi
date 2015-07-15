package com.baidu.beidou.navi.it;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.junit.Test;

import com.baidu.beidou.navi.exception.rpc.AuthAccessDeniedException;
import com.baidu.beidou.navi.it.service.AuthService;

public class TestAuthService extends BaseTest {

    @Resource
    private AuthService authServiceDirectCall;

    @Resource
    private AuthService authServiceDirectCallNoAuth;

    @Resource
    private AuthService authServiceDirectCallTokenWrong;

    @Test
    public void testSayHelloNegative() {
        String result = null;
        try {
            result = authServiceDirectCallNoAuth.sayHello();
            System.out.println(result);
        } catch (AuthAccessDeniedException e) {
            assertThat(e.getClass().getName(), is(AuthAccessDeniedException.class.getName()));
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSayHelloNegative2() {
        String result = null;
        try {
            result = authServiceDirectCallTokenWrong.sayHello();
            System.out.println(result);
        } catch (AuthAccessDeniedException e) {
            assertThat(e.getClass().getName(), is(AuthAccessDeniedException.class.getName()));
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testSayHello() {
        String result = null;
        result = authServiceDirectCall.sayHello();
        System.out.println(result);
        assertThat(result, is("hi!"));
    }

}

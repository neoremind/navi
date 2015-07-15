package com.baidu.beidou.navi.it;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.net.SocketTimeoutException;

import javax.annotation.Resource;

import org.junit.Test;

import com.baidu.beidou.navi.client.SimpleNaviRpcClient;
import com.baidu.beidou.navi.client.SimpleNaviRpcClientBuilder;
import com.baidu.beidou.navi.exception.rpc.MethodNotFoundException;
import com.baidu.beidou.navi.exception.rpc.RpcException;
import com.baidu.beidou.navi.it.service.CompanyService;
import com.baidu.beidou.navi.vo.Company;

public class TestNegativeCompanyService extends AbstractCompanyServiceTest {

    @Resource
    private CompanyService companyServiceDirectCallNegative;

    @Test
    public void testGet() {
        try {
            companyServiceDirectCallNegative.get(88);
        } catch (RpcException e) {
            assertThat(e.getCause().getClass().getName(),
                    is(SocketTimeoutException.class.getName()));
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testServiceNotFound() {
        try {
            SimpleNaviRpcClient client = new SimpleNaviRpcClientBuilder()
                    .setIpPort("127.0.0.1:" + Config.PORT).setServiceName("CompanyServiceXXXXX")
                    .build();
            Company c = (Company) client.transport("get", new Object[] { 88 },
                    new Class<?>[] { int.class }, Company.class, null);
            System.out.println(c);
        } catch (Throwable t) {
            assertThat(t.getCause().getClass().getName(), is(FileNotFoundException.class.getName()));
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testMethodNotFound() {
        try {
            SimpleNaviRpcClient client = new SimpleNaviRpcClientBuilder()
                    .setIpPort("127.0.0.1:" + Config.PORT).setServiceName("CompanyService").build();
            Company c = (Company) client.transport("get123", new Object[] { 88 },
                    new Class<?>[] { int.class }, Company.class, null);
            System.out.println(c);
        } catch (Throwable t) {
            assertThat(t.getClass().getName(), is(MethodNotFoundException.class.getName()));
            return;
        }
        fail("should not get here");
    }

    @Test
    public void testInvalidRequest() {
        try {
            SimpleNaviRpcClient client = new SimpleNaviRpcClientBuilder()
                    .setIpPort("127.0.0.1:" + Config.PORT).setServiceName("XXXX").build();
            Company c = (Company) client.transport("get", new Object[] { 88 },
                    new Class<?>[] { int.class }, Company.class, null);
            System.out.println(c);
        } catch (Throwable t) {
            System.out.println(t.getMessage());
            assertThat(t.getClass().getName(), is(RpcException.class.getName()));
            return;
        }
        fail("should not get here");
    }

}

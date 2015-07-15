package com.baidu.beidou.navi.it;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.baidu.beidou.navi.client.SimpleNaviRpcClient;
import com.baidu.beidou.navi.client.SimpleNaviRpcClientBuilder;
import com.baidu.beidou.navi.exception.rpc.CodecException;
import com.baidu.beidou.navi.protocol.NaviProtocol;
import com.baidu.beidou.navi.vo.Company;

public class TestSimpleNaviRpcClient extends BaseTest {

    private static final String IPPORT = "127.0.0.1:" + Config.PORT;

    @Test
    public void testSimpleGet() throws Throwable {
        Object[] args = new Object[] { 88 };
        SimpleNaviRpcClient client = new SimpleNaviRpcClientBuilder().setIpPort(IPPORT)
                .setServiceName("CompanyService").setConnectTimeout(5000).setReadTimeout(5000)
                .build();
        Company result = client.transport("get", args);
        System.out.println(result);
    }

    @Test
    public void testSimpleGetNegative() throws Throwable {
        try {
            Object[] args = new Object[] { 88 };
            SimpleNaviRpcClient client = new SimpleNaviRpcClientBuilder().setIpPort(IPPORT)
                    .setServiceName("CompanyService").setConnectTimeout(5000).setReadTimeout(5000)
                    .setProtocol(NaviProtocol.JSON.getName()).build();
            Company result = client.transport("get", args);
            System.out.println(result);
        } catch (CodecException e) {
            assertThat(e.getClass().getName(), is(CodecException.class.getName()));
            return;
        }
    }

}

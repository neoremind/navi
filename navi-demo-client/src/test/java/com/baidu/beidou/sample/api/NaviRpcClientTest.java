package com.baidu.beidou.sample.api;

import org.junit.Test;

import com.baidu.beidou.navi.client.SimpleNaviRpcClient;
import com.baidu.beidou.navi.client.SimpleNaviRpcClientBuilder;
import com.baidu.beidou.sample.annotation.vo.Company;

public class NaviRpcClientTest {

    @Test
    public void testGetBySiteUrl() throws Throwable {
        Object[] args = new Object[] { 88 };
        SimpleNaviRpcClient client = new SimpleNaviRpcClientBuilder()
                .setIpPort("127.0.0.1:8080").setServiceName("CompanyMgr")
                .setConnectTimeout(5000).setReadTimeout(5000).build();
        Company result = client.transport("get", args);
        System.out.println(result);
    }

}

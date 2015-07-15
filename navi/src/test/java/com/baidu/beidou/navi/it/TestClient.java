package com.baidu.beidou.navi.it;

import java.lang.reflect.Method;

import com.baidu.beidou.navi.client.SimpleNaviRpcClient;
import com.baidu.beidou.navi.it.service.CompanyService;
import com.baidu.beidou.navi.it.service.impl.CompanyServiceImpl;
import com.baidu.beidou.navi.protocol.ProtostuffSerializeHandler;
import com.baidu.beidou.navi.protocol.SerializeHandler;
import com.baidu.beidou.navi.vo.Company;

public class TestClient {

    public void testClient() throws Throwable {
        // SerializeHandler h = new JsonSerializeHandler();
        SerializeHandler h = new ProtostuffSerializeHandler();
        SimpleNaviRpcClient c = new SimpleNaviRpcClient(
                "http://127.0.0.1:8088/service_api/CompanyService", h);
        CompanyService serv = new CompanyServiceImpl();
        Method m = serv.getClass().getDeclaredMethods()[1];
        Company company = (Company) (c.transport(m, new Object[] { 88 }));
        System.out.println(company);
    }

}

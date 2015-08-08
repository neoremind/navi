package com.baidu.beidou.sample.simpletest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.junit.Test;

import com.baidu.beidou.navi.codec.Codec;
import com.baidu.beidou.navi.codec.json.JsonCodec;
import com.baidu.beidou.navi.codec.json.JsonMapper;

public class SimpleHTTPInvocationTest {

    @Test
    public void testHttpRpc() throws Exception {
        PostMethod postMethod = null;
        try {
            // 1. 构造请求体
            Codec codec = new JsonCodec();
            RequestDTO request = new RequestDTO();
            request.setTraceId(123);
            request.setMethod("getByIds");
            List<Integer> ids = new ArrayList<Integer>();
            ids.add(5276);
            request.setParameters(new Object[] { ids });
            // request.setParamterTypes(MethodUtil.getArgsTypeNameArray(new Class<?>[] { List.class }));

            // 2. 设置调用的HTTP客户端以及填充请求体
            // String serviceUrl = "http://127.0.0.1:8080/service_api/CompanyMgr";
            String serviceUrl = "http://cache.beidou.baidu.com/service_api/UnionSiteService";
            postMethod = new PostMethod(serviceUrl);
            postMethod.setRequestHeader("content-type", "application/baidu.json");
            System.out.println(JsonMapper.buildNormalMapper().toJson(request));
            postMethod.setRequestEntity(new ByteArrayRequestEntity(codec.encode(RequestDTO.class,
                    request)));

            // 3. 发送请求
            HttpConnectionManagerParams connectionParams = new HttpConnectionManagerParams();
            connectionParams.setConnectionTimeout(5 * 1000);
            connectionParams.setSoTimeout(5 * 1000);
            HttpConnectionManager manager = new SimpleHttpConnectionManager();
            manager.setParams(connectionParams);
            HttpClient client = new HttpClient(manager);
            int state = client.executeMethod(postMethod);

            // 4. 解析请求返回
            if (state == HttpStatus.SC_OK) {
                ResponseDTO response = codec
                        .decode(ResponseDTO.class, postMethod.getResponseBody());
                System.out.println(response.getId() + "\t" + response.getStatus() + "\t"
                        + response.getResult());
            } else {
                System.err.println("Invocation failed " + serviceUrl + ", HTTP status=" + state);
            }

        } catch (Exception e) {
            System.err.println("Failed to invoke rpc. " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (postMethod != null) {
                try {
                    postMethod.releaseConnection();
                } catch (Exception e) {
                    System.err.println("Failed to release connection. " + e.getMessage());
                }
            }
        }
    }

}

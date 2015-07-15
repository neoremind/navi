package com.baidu.beidou.navi.it;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.baidu.beidou.navi.client.constant.NaviRpcClientConstant;
import com.baidu.beidou.navi.util.ByteUtil;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class BaseTest extends AbstractJUnit4SpringContextTests {

    protected static StandaloneServer server;

    @BeforeClass
    public static void setUp() {
        server = new StandaloneServer();
        server.start(Config.PORT);
    }

    @Test
    public void test() {

    }

    @AfterClass
    public static void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    public String getContent(String path) throws Exception {
        HttpURLConnection httpconnection = (HttpURLConnection) new URL(path).openConnection();
        httpconnection.setRequestMethod("GET");
        httpconnection.setDoInput(true);
        httpconnection.setDoOutput(true);
        httpconnection.setRequestProperty("Content-Type", "text/html;charset="
                + NaviRpcClientConstant.ENDODING);
        httpconnection.connect();
        InputStream in = httpconnection.getInputStream();
        int len = httpconnection.getContentLength();
        byte[] resBytes = ByteUtil.readStream(in, len);
        String content = new String(resBytes, NaviRpcClientConstant.ENDODING);
        in.close();
        httpconnection.disconnect();
        return content;
    }

}

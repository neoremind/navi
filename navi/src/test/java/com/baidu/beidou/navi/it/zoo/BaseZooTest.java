package com.baidu.beidou.navi.it.zoo;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.baidu.beidou.navi.client.NaviRpcServerListListener;
import com.baidu.beidou.navi.client.constant.NaviRpcClientConstant;
import com.baidu.beidou.navi.conf.RpcClientConf;
import com.baidu.beidou.navi.conf.RpcServerConf;
import com.baidu.beidou.navi.it.Config;
import com.baidu.beidou.navi.it.StandaloneServer;
import com.baidu.beidou.navi.util.ByteUtil;
import com.baidu.beidou.navi.util.StringUtil;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class BaseZooTest extends AbstractJUnit4SpringContextTests {

    protected static StandaloneServer server;

    protected static NaviRpcServerListListener listener;

    @BeforeClass
    public static void setUp() {
        // init server-side zookeeper config
        RpcServerConf.ENABLE_ZK_REGISTRY = true;
        RpcServerConf.ZK_SERVER_LIST = Config.ZOO_LIST;
        RpcServerConf.ZK_REGISTRY_NAMESPACE = Config.ZOO_REGISTRY_NAMESPACE;
        RpcServerConf.SERVER_PORT = Config.ZOO_SERVER_PORT;

        server = new StandaloneServer();
        server.start(RpcServerConf.SERVER_PORT);

        // init client-side zookeeper config
        RpcClientConf.ENABLE_ZK_REGISTRY = true;
        RpcClientConf.ZK_SERVER_LIST = Config.ZOO_LIST;
        RpcClientConf.ZK_WATCH_NAMESPACE_PATHS = StringUtil.split(Config.ZOO_REGISTRY_NAMESPACE,
                StringUtil.COMMA);

        listener = new NaviRpcServerListListener();
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

package com.baidu.beidou.navi.it.zoo;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.baidu.beidou.navi.it.Config;

public class TestZooHtmlPage extends BaseZooTest {

    @Test
    public void testZooHtml() throws Throwable {
        String content = getContent(("http://127.0.0.1:" + Config.ZOO_SERVER_PORT + "/service_api/zookeeper"));
        assertThat(content.contains(Config.ZOO_REGISTRY_NAMESPACE), is(true));
    }

}

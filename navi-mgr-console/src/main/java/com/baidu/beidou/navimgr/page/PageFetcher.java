package com.baidu.beidou.navimgr.page;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ClassName: PageFetcher <br/>
 * Function: 抓取页面工具
 * 
 * @author Zhang Xu
 */
@Component
public class PageFetcher {

    private static final Logger LOG = LoggerFactory.getLogger(PageFetcher.class);

    /**
     * 根据url抓取页面
     * 
     * @param url
     *            url
     * @return 页面html片段
     */
    public String getPageHtml(String url) {
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(2000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(3000);
        GetMethod getMethod = new GetMethod(url);
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != HttpStatus.SC_OK) {
                LOG.warn("HttpStatus not equal to 200, some problems may occurred:"
                        + getMethod.getStatusLine());
            }
            InputStream inputStream = getMethod.getResponseBodyAsStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String html = "";
            while ((html = br.readLine()) != null) {
                stringBuffer.append(html);
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            LOG.error("Error to get html of url=" + url + " due to " + e.getMessage());
            throw new RuntimeException("Get html failed, " + e.getMessage(), e);
        } finally {
            try {
                getMethod.releaseConnection();
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }
    }

    /**
     * 测试
     * 
     * @param args
     *            参数
     */
    public static void main(String[] args) {
        PageFetcher p = new PageFetcher();
        System.out.println(p.getPageHtml("http://sync-report.beidou.baidu.com/service_api"));
    }

}

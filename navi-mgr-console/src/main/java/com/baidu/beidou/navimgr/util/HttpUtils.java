package com.baidu.beidou.navimgr.util;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName: HttpUtils <br/>
 * Function: http工具类
 * 
 * @author Zhang Xu
 */
public class HttpUtils {

    /**
     * 获取浏览器信息
     * 
     * @param request
     *            请求request
     * @return ua
     */
    public static String getHttpBrowser(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        return request.getHeader("User-Agent");
    }

    /**
     * 获得整个url,如http://www.weamea.com/music/index.jsp?id=4342
     * 
     * @param request
     *            请求request
     * @return url
     */
    public static String getFullURL(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String url = request.getRequestURL().toString();
        String QueryString = request.getQueryString();
        if ((QueryString != null) && (QueryString.length() > 0)) {
            url = url + "?" + QueryString;
        }
        return url;
    }

}

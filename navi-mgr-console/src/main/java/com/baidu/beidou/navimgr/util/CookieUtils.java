package com.baidu.beidou.navimgr.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName: CookieUtils <br/>
 * Function: cookie工具
 * 
 * @author Zhang Xu
 */
public class CookieUtils {

    /**
     * 设置cookie值
     * 
     * @param response
     *            返回response
     * @param cookieName
     *            cookie名
     * @param cookieValue
     *            cookie值
     * @param expireInHour
     *            过期时间
     */
    public static void setCookieValue(HttpServletResponse response, String cookieName,
            String cookieValue, int expireInHour) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        if (expireInHour > 0) {
            cookie.setMaxAge(60 * 60 * 24 * expireInHour);
        } else {
            cookie.setMaxAge(60 * 60 * 24);
        }
        response.addCookie(cookie);
    }

    /**
     * 清除cookie
     * 
     * @param response
     *            返回response
     * @param cookieName
     *            cookie名
     */
    public static void resetCookieValue(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    /**
     * 获取cookie值
     * 
     * @param request
     *            请求request
     * @param cookieName
     *            cookie名
     * @return cookie值
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        if (cookieName == null || request == null) {
            return null;
        }
        Cookie[] cks = request.getCookies();
        if (cks == null) {
            return null;
        }
        for (Cookie cookie : cks) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 获取cookie
     * 
     * @param request
     *            请求request
     * @param cookieName
     *            cookie名
     * @return cookie
     */
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        if (cookieName == null || request == null) {
            return null;
        }
        Cookie[] cks = request.getCookies();
        if (cks == null) {
            return null;
        }
        for (Cookie cookie : cks) {
            if (cookieName.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }
}

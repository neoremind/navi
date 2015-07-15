package com.baidu.beidou.navi.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * ClassName: IPUtils <br/>
 * Function: IP地址工具类
 *
 * @author Zhang Xu
 */
public class IPUtils {

    private static final String IP_UNKNOWN = "unknown";

    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || IP_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 取X-Forwarded-For中第一个非unknown的有效IP字符串。
        if (ip.indexOf(",") != -1) {
            String[] ipList = ip.split(",");
            String tmp;
            for (int i = 0; i < ipList.length; i++) {
                tmp = ipList[i];
                if (tmp != null && !IP_UNKNOWN.equalsIgnoreCase(tmp.trim())) {
                    return tmp.trim();
                }
            }
        }
        return ip;
    }

    public static String getLocalHostAddress() {
        try {
            InetAddress inet = InetAddress.getLocalHost();
            String hostAddress = inet.getHostAddress();
            return hostAddress;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

}

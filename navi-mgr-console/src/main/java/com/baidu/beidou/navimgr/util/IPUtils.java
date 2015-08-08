package com.baidu.beidou.navimgr.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;

/**
 * #func Java获取客户端IP<br>
 * #desc 考虑(代理,包括UTR)转发等情形 不再只调用getRemoteAddr()
 * 
 * @author luochao
 * @version 3.13.2
 * 
 */
public class IPUtils {

	private static final String IP_UNKNOWN = "unknown";

	/**
	 * #func 获取IP地址<br>
	 * #desc 不再简单getRemoteAddr
	 * 
	 * @author luochao
	 * @version 3.13.2
	 */
	public static String getIpAddr(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0
				|| IP_UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0
				|| IP_UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0
				|| IP_UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		// 取X-Forwarded-For中第一个非unknown的有效IP字符串。
		if (ip.indexOf(",") != -1) {
			String[] ipList = ip.split(",");
			String tmp;
			for (int i = 0; i < ipList.length; i++) {
				tmp = ipList[i];
				if (tmp != null
						&& !IP_UNKNOWN.equalsIgnoreCase(tmp.trim())) {
					return tmp.trim();
				}
			}
		}
		return ip;
	}

	/**
	 * @func 获得本机的机器名称，用来从配置文件中排除本机
	 * @desc 在此添加实现相关说明
	 * 
	 * @author zhangfeng
	 * @version 4.0.0.0
	 */
	public static String getLocalHostName() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return addr.getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * #func 返回主机名的全限定域名<br>
	 * 
	 * @author v_dongguoshuang
	 * @version 4.0.0
	 */
	public static String getFullyLocalHostName() {
		String hostName = null;
		try {
			InetAddress inet = InetAddress.getLocalHost();
			hostName = inet.getCanonicalHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return hostName;
	}

	/**
	 * #func 返回本机IP<br>
	 * 
	 * @author v_dongguoshuang
	 * @version 4.0.0
	 */
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

	/**
	 * 根据服务器的IP地址，生成一个唯一的ID
	 */
	public static long getServerId() {
		String ipAddress = getLocalHostAddress();
		try {
			long result = 0;
			String[] array = ipAddress.split("\\.");
			for(String s : array){
				long i = Long.parseLong(s);
				result = 256 * result + i;
			}
			return result;
		} catch (Exception e) {
			return ipAddress.hashCode();
		}
	}

	/**
	 * #func 判断本机是否和传入的域名一致<br>
	 * 
	 * @author v_dongguoshuang
	 * @version 4.0.0
	 */
	public static boolean isDomainEqualsLocal(String domainName) {
		if (StringUtils.isBlank(domainName)) {
			return false;
		}
		try {
			InetAddress host = InetAddress.getByName(domainName);
			String domainAddress = host.getHostAddress();
			if (domainAddress.equals(getLocalHostAddress())) {
				return true;
			} else {
				return false;
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取服务器本机的IP地址
	 * @author zhangpingan
	 * @return
	 */
	public static String getIPAddr() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface interfaceN = (NetworkInterface) interfaces.nextElement();
				Enumeration<InetAddress> ienum = interfaceN.getInetAddresses();
				while (ienum.hasMoreElements()) {
					InetAddress ipaddr = (InetAddress) ienum.nextElement();
					if (ipaddr instanceof Inet4Address) {
						if (ipaddr.getHostAddress().toString().startsWith("127") || ipaddr.getHostAddress().toString().startsWith("192")) {
							continue;
						} else {
							return ipaddr.getHostAddress();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(getServerId());
	}
}

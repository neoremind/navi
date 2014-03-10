package net.neoremind.navi.util;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtil {
	
	private final static Logger log = LoggerFactory.getLogger(PropertiesUtil.class);

	private Properties props;

	public PropertiesUtil(String fileName) {
		readProperties(fileName);
	}

	private void readProperties(String fileName) {
		try {
			props = new Properties();
			InputStream fis = getClass().getResourceAsStream(fileName);
			props.load(fis);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public String getProperty(String key) {
		return props.getProperty(key);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getAllProperty() {
		Map map = new HashMap();
		Enumeration enu = props.propertyNames();
		while (enu.hasMoreElements()) {
			String key = (String) enu.nextElement();
			String value = props.getProperty(key);
			map.put(key, value);
		}
		return map;
	}

	public void printProperties() {
		props.list(System.out);
	}

	public static void main(String[] args) {
		PropertiesUtil util = new PropertiesUtil("rpc.conf");
		String serverList = util.getProperty("server_list");
		System.out.println(serverList);
	}
	
}
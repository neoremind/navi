package net.neoremind.navi.constant;

import net.neoremind.navi.server.protocol.NaviProtocol;

public class NaviCommonConstant {
	
	/**
	 * HTTP doGet page needed 
	 */
	public static final String TRANSPORT_PROTOCOL = "http://";
	public static final String TRANSPORT_URL_BASE_PATH = "/service_api/";
	public static final String ZOOKEEPER_URL_PATH = "zookeeper";
	
	/**
	 * Zookeeper base path
	 * Services are registered under the path
	 */
	public static final String ZOOKEEPER_BASE_PATH = "/my_service";

	public static final String ZK_PATH_SEPARATOR = "/";

	
	
	/**
	 * Protocol defination
	 */
	public static final String CONTENT_TYPE_PROTOBUF = NaviProtocol.PROTOBUF.getName();
	public static final String CONTENT_TYPE_PROTOSTUFF = NaviProtocol.PROTOSTUFF.getName();
	public static final String DEFAULT_PROTOCAL_CONTENT_TYPE = CONTENT_TYPE_PROTOSTUFF;
	
	/**
	 * System properties of server port.
	 * Usually defined in shell.
	 */
	public static final String SYSTEM_PROPERTY_SERVER_PORT_FROM_ENV = "VCAP_APP_PORT";
	
	/**
	 * System properties of server port.
	 * Get from -D property
	 */
	public static final String SYSTEM_PROPERTY_SERVER_PORT_FROM_SLASH_D = "port.http.nonssl";
	
}

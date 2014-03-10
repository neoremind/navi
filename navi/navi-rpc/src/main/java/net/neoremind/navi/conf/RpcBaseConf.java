package net.neoremind.navi.conf;

public class RpcBaseConf {
	
	public static boolean ENABLE_ZK_REGISTRY = false;
	
	public static String ZK_SERVER_LIST = "";
	
	public static int ZK_DEFAULT_SESSION_TIMEOUT_MILLS = 10000;
	
	public static int ZK_CONNECTION_TIMEOUT_MILLS = 30000;

	public boolean isENABLE_ZK_REGISTRY() {
		return ENABLE_ZK_REGISTRY;
	}

	public void setENABLE_ZK_REGISTRY(boolean eNABLE_ZK_REGISTRY) {
		ENABLE_ZK_REGISTRY = eNABLE_ZK_REGISTRY;
	}

	public String getZK_SERVER_LIST() {
		return ZK_SERVER_LIST;
	}

	public void setZK_SERVER_LIST(String zK_SERVER_LIST) {
		ZK_SERVER_LIST = zK_SERVER_LIST;
	}

	public int getZK_DEFAULT_SESSION_TIMEOUT_MILLS() {
		return ZK_DEFAULT_SESSION_TIMEOUT_MILLS;
	}

	public void setZK_DEFAULT_SESSION_TIMEOUT_MILLS(int zK_DEFAULT_SESSION_TIMEOUT_MILLS) {
		ZK_DEFAULT_SESSION_TIMEOUT_MILLS = zK_DEFAULT_SESSION_TIMEOUT_MILLS;
	}

	public int getZK_CONNECTION_TIMEOUT_MILLS() {
		return ZK_CONNECTION_TIMEOUT_MILLS;
	}

	public void setZK_CONNECTION_TIMEOUT_MILLS(int zK_CONNECTION_TIMEOUT_MILLS) {
		ZK_CONNECTION_TIMEOUT_MILLS = zK_CONNECTION_TIMEOUT_MILLS;
	}

}

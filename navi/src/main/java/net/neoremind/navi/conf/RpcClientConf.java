package net.neoremind.navi.conf;


/**
 * Inject Rpc configuration into Spring container
 * 
 * @author Zhang Xu
 */
public class RpcClientConf extends RpcBaseConf {
	
	public static String[] ZK_WATCH_NAMESPACE_PATHS = {};
	
	public static String[] SERVER_LIST = {};
	
	public static int RPC_CONNECTION_TIMEOUT = -1;

	public static int RPC_READ_TIMEOUT = -1;

	public static int RPC_RETRY_TIMES = 2;

	public String[] getZK_WATCH_NAMESPACE_PATHS() {
		return ZK_WATCH_NAMESPACE_PATHS;
	}

	public void setZK_WATCH_NAMESPACE_PATHS(String[] zK_WATCH_NAMESPACE_PATHS) {
		ZK_WATCH_NAMESPACE_PATHS = zK_WATCH_NAMESPACE_PATHS;
	}

	public String[] getSERVER_LIST() {
		return SERVER_LIST;
	}

	public void setSERVER_LIST(String[] sERVER_LIST) {
		SERVER_LIST = sERVER_LIST;
	}

	public int getRPC_CONNECTION_TIMEOUT() {
		return RPC_CONNECTION_TIMEOUT;
	}

	public void setRPC_CONNECTION_TIMEOUT(int rPC_CONNECTION_TIMEOUT) {
		RPC_CONNECTION_TIMEOUT = rPC_CONNECTION_TIMEOUT;
	}

	public int getRPC_READ_TIMEOUT() {
		return RPC_READ_TIMEOUT;
	}

	public void setRPC_READ_TIMEOUT(int rPC_READ_TIMEOUT) {
		RPC_READ_TIMEOUT = rPC_READ_TIMEOUT;
	}

	public int getRPC_RETRY_TIMES() {
		return RPC_RETRY_TIMES;
	}

	public void setRPC_RETRY_TIMES(int rPC_RETRY_TIMES) {
		RPC_RETRY_TIMES = rPC_RETRY_TIMES;
	}

}

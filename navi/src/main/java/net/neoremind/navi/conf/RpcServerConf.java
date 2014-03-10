package net.neoremind.navi.conf;

/**
 * Inject Rpc configuration into Spring container
 * 
 * @author Zhang Xu
 */
public class RpcServerConf extends RpcBaseConf {

	public static String ZK_REGISTRY_NAMESPACE = "";

	public static int SERVER_PORT = 8080;

	public String getZK_REGISTRY_NAMESPACE() {
		return ZK_REGISTRY_NAMESPACE;
	}

	public void setZK_REGISTRY_NAMESPACE(String zK_REGISTRY_NAMESPACE) {
		ZK_REGISTRY_NAMESPACE = zK_REGISTRY_NAMESPACE;
	}

	public int getSERVER_PORT() {
		return SERVER_PORT;
	}

	public void setSERVER_PORT(int sERVER_PORT) {
		SERVER_PORT = sERVER_PORT;
	}

}

package net.neoremind.navi.server;

/**
 * RPC Exporter bean wrapping interface and actual implementation bean
 * 
 * @author Zhang Xu
 */
public class NaviRpcExporter {

	private String serviceInterface;

	private Object serviceBean;

	public NaviRpcExporter() {
		super();
	}

	public NaviRpcExporter(String serviceInterface, Object serviceBean) {
		this.serviceInterface = serviceInterface;
		this.serviceBean = serviceBean;
	}

	public Class<?> getServiceInterface() throws ClassNotFoundException {
		return Class.forName(serviceInterface);
	}

	public String getServiceInterfaceName() {
		return serviceInterface;
	}

	public void setServiceInterfaceName(String serviceInterface) {
		this.serviceInterface = serviceInterface;
	}

	public Object getServiceBean() {
		return serviceBean;
	}

	public void setServiceBean(Object serviceBean) {
		this.serviceBean = serviceBean;
	}

}

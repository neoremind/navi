package net.neoremind.navi.server.vo;


/**
 * Rpc request bean
 * 
 * @author Zhang Xu
 */
public class NaviRpcRequest {

	/**
	 * 所调用的服务接口
	 */
	private Class<?> service;

	/**
	 * 完成请求的bean
	 */
	private Object actor;

	/**
	 * 传入的协议数据
	 */
	private byte[] request;

	public NaviRpcRequest() {
		
	}
	
	/**
	 * @param service
	 *            所调用的服务接口
	 * @param actor
	 *            完成请求的bean
	 * @param reqs
	 *            传入的协议数据
	 */
	public NaviRpcRequest(Class<?> service, Object actor, byte[] request) {
		this.service = service;
		this.actor = actor;
		this.request = request;
	}

	public Class<?> getService() {
		return service;
	}

	public void setService(Class<?> service) {
		this.service = service;
	}

	public Object getActor() {
		return actor;
	}

	public void setActor(Object actor) {
		this.actor = actor;
	}

	public byte[] getRequest() {
		return request;
	}

	public void setRequest(byte[] request) {
		this.request = request;
	}

}
package net.neoremind.navi.server.vo;

/**
 * Rpc request bean
 * 
 * @author Zhang Xu
 */
public class NaviRpcResponse {

	private byte[] response;

	public NaviRpcResponse() {
		super();
	}

	public NaviRpcResponse(byte[] response) {
		super();
		this.response = response;
	}

	public byte[] getResponse() {
		return response;
	}

	public void setResponse(byte[] response) {
		this.response = response;
	}

}
package net.neoremind.navi.server.protocol;

/**
 * Protocol supported
 * 
 * @author Zhang Xu
 */
public enum NaviProtocol {

	PROTOSTUFF("application/baidu.protostuff"), 
	PROTOBUF("application/baidu.protobuf");

	private String name;

	private NaviProtocol(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

package net.neoremind.navi.server.vo;

/**
 * Request
 * 
 * @author Zhang Xu
 */
public class RequestDTO {

	private long id;

	private String method;

	private Object[] parameters;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

}
